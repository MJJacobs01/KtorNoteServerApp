package com.jacobs.mj.ktornoteapp.ui.notedetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.databinding.FragmentNoteDetailBinding
import com.jacobs.mj.ktornoteapp.ui.BaseFragment

/**
 * Created by mj on 2021/03/06 at 10:04 AM
 */
class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail), View.OnClickListener {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoteDetailBinding.bind(view)

        binding.apply {
            fabEditNote.setOnClickListener(this@NoteDetailFragment)
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fabEditNote) {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(""))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}