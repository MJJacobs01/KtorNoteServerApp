package com.jacobs.mj.ktornoteapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.databinding.FragmentAuthBinding
import com.jacobs.mj.ktornoteapp.ui.BaseFragment

/**
 * Created by mj on 2021/03/06 at 10:03 AM
 */
class AuthFragment : BaseFragment(R.layout.fragment_auth), View.OnClickListener {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAuthBinding.bind(view)

        binding.apply {
            btnRegister.setOnClickListener(this@AuthFragment)
            btnLogin.setOnClickListener(this@AuthFragment)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin->{
                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
            }
            R.id.btnRegister->{
                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}