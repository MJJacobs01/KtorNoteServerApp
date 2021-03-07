package com.jacobs.mj.ktornoteapp.ui.notes

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.databinding.FragmentNotesBinding
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger

/**
 * Created by mj on 2021/03/06 at 10:05 AM
 */
@AndroidEntryPoint
class NotesFragment : BaseFragment(R.layout.fragment_notes), View.OnClickListener {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        binding.apply {
            fabAddNote.setOnClickListener(this@NotesFragment)
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fabAddNote) {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(""))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}