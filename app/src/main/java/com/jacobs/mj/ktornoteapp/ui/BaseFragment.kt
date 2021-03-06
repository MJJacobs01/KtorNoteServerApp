package com.jacobs.mj.ktornoteapp.ui

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.jacobs.mj.ktornoteapp.R

/**
 * Created by mj on 2021/03/06 at 9:54 AM
 */
open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    fun showSnackBar(message: String) {
        Snackbar.make(requireActivity().findViewById(R.id.rootLayout), message, Snackbar.LENGTH_LONG).show()
    }
}