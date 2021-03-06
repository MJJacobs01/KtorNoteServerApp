package com.jacobs.mj.ktornoteapp.ui.auth

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.databinding.FragmentAuthBinding
import com.jacobs.mj.ktornoteapp.other.Resource
import com.jacobs.mj.ktornoteapp.other.Status
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by mj on 2021/03/06 at 10:03 AM
 */
@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.fragment_auth), View.OnClickListener {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    //  Get the AuthViewModel
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
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

    private fun subscribeToObservers(){
        //  Subscribe to liveData objects in the viewModel
        viewModel.registerStatus.observe(viewLifecycleOwner, { result->
            result?.let {
                //  Check status
                when(result.status){
                    Status.SUCCESS->{
                        binding.registerProgressBar.visibility = View.GONE
                        showSnackBar(result.data?: "Successfully registered and account")
                    }
                    Status.ERROR->{
                        binding.registerProgressBar.visibility = View.GONE
                        showSnackBar(result.message ?: "An unknown error occurred")
                    }
                    Status.LOADING->{
                        binding.registerProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}