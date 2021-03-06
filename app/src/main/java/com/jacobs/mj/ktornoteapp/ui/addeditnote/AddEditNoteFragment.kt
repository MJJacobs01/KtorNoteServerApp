package com.jacobs.mj.ktornoteapp.ui.addeditnote

import android.os.Bundle
import android.view.View
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.databinding.FragmentAddEditNoteBinding
import com.jacobs.mj.ktornoteapp.ui.BaseFragment

/**
 * Created by mj on 2021/03/06 at 10:00 AM
 */
class AddEditNoteFragment : BaseFragment(R.layout.fragment_add_edit_note), View.OnClickListener {
    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditNoteBinding.bind(view)

        binding.apply {

        }
    }

    override fun onClick(v: View?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}