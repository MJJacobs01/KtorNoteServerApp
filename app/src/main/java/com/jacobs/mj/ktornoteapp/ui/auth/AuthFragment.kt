package com.jacobs.mj.ktornoteapp.ui.auth

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.data.remote.BasicAuthInterceptor
import com.jacobs.mj.ktornoteapp.databinding.FragmentAuthBinding
import com.jacobs.mj.ktornoteapp.other.Constants.KEY_LOGGED_IN_EMAIL
import com.jacobs.mj.ktornoteapp.other.Constants.KEY_PASSWORD
import com.jacobs.mj.ktornoteapp.other.Constants.NO_EMAIL
import com.jacobs.mj.ktornoteapp.other.Constants.NO_PASSWORD
import com.jacobs.mj.ktornoteapp.other.Resource
import com.jacobs.mj.ktornoteapp.other.Status
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.jacobs.mj.ktornoteapp.other.Constants as Constants

/**
 * Created by mj on 2021/03/06 at 10:03 AM
 */
@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.fragment_auth), View.OnClickListener {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    //  Get the AuthViewModel
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var curEmail: String? = null
    private var curPassword: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLoggedIn()) {
            authenticateApi(curEmail ?: "", curPassword ?: "")
            redirectLogin()
        }
        _binding = FragmentAuthBinding.bind(view)

        //  Setting the screen orientation to Portrait
        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT

        binding.apply {
            btnRegister.setOnClickListener(this@AuthFragment)
            btnLogin.setOnClickListener(this@AuthFragment)
        }

        subscribeToObservers()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                binding.apply {
                    val email = etLoginEmail.text.toString()
                    val password = etLoginPassword.text.toString()
                    curEmail = email
                    curPassword = password
                    viewModel.login(email, password)
                }
            }
            R.id.btnRegister -> {
                binding.apply {
                    val email = etRegisterEmail.text.toString()
                    val password = etRegisterPassword.text.toString()
                    val repeatedPassword = etRegisterPasswordConfirm.text.toString()
                    viewModel.register(email, password, repeatedPassword)
                }
            }
        }
    }

    private fun subscribeToObservers() {
        //  Subscribe to liveData objects in the viewModel

        //  Register status
        viewModel.registerStatus.observe(viewLifecycleOwner, { result ->
            result?.let {
                //  Check status
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.registerProgressBar.visibility = View.GONE
                        showSnackBar(result.data ?: "Successfully registered and account")
                    }
                    Status.ERROR -> {
                        binding.registerProgressBar.visibility = View.GONE
                        showSnackBar(result.message ?: "An unknown error occurred")
                    }
                    Status.LOADING -> {
                        binding.registerProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

        //  Login status
        viewModel.loginStatus.observe(viewLifecycleOwner, { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.loginProgressBar.visibility = View.GONE
                        showSnackBar(result.data ?: "Successfully logged in")
                        //  Once login is successful we have to save email and password of the user
                        sharedPreferences.edit().putString(KEY_LOGGED_IN_EMAIL, curEmail).apply()
                        sharedPreferences.edit().putString(KEY_PASSWORD, curPassword).apply()
                        authenticateApi(curEmail ?: "", curPassword ?: "")
                        redirectLogin()
                    }
                    Status.LOADING -> {
                        binding.loginProgressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        binding.loginProgressBar.visibility = View.GONE
                        showSnackBar(result.message ?: "An unknown error occurred")
                    }
                }
            }
        })
    }

    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment, true)
            .build()
        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment(), navOptions)
    }

    private fun isLoggedIn(): Boolean {
        curEmail = sharedPreferences.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        curPassword = sharedPreferences.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        return curEmail != NO_EMAIL && curPassword != NO_PASSWORD
    }

    private fun authenticateApi(email: String, password: String) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}