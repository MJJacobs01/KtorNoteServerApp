package com.jacobs.mj.ktornoteapp.ui.notedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import com.jacobs.mj.ktornoteapp.databinding.FragmentNoteDetailBinding
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

/**
 * Created by mj on 2021/03/06 at 10:04 AM
 */
@AndroidEntryPoint
class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail), View.OnClickListener {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private val args: NoteDetailFragmentArgs by navArgs()
    private val viewModel: NoteDetailViewModel by viewModels()

    private var currentNote: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoteDetailBinding.bind(view)

        binding.apply {
            fabEditNote.setOnClickListener(this@NoteDetailFragment)
        }

        subscribeToObservers()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fabEditNote) {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }
    }

    private fun setMarkdownText(text: String) {
        val markwon = Markwon.create(requireContext())
        val markdown = markwon.toMarkdown(text)
        markwon.setParsedMarkdown(binding.tvNoteContent, markdown)
    }

    private fun subscribeToObservers() {
        viewModel.observerNoteById(args.id).observe(viewLifecycleOwner, {
            it?.let { note ->
                binding.tvNoteTitle.text = note.title
                setMarkdownText(note.content)
                currentNote = note
            } ?: showSnackBar("Note not found")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}