package com.jacobs.mj.ktornoteapp.ui.notedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import com.jacobs.mj.ktornoteapp.databinding.FragmentNoteDetailBinding
import com.jacobs.mj.ktornoteapp.other.Status
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import com.jacobs.mj.ktornoteapp.ui.dialogs.AddOwnerDialog
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

/**
 * Created by mj on 2021/03/06 at 10:04 AM
 */

const val ADD_OWNER_DIALOG_TAG = "ADD_OWNER_DIALOG_TAG"

@AndroidEntryPoint
class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail), View.OnClickListener {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private val args: NoteDetailFragmentArgs by navArgs()
    private val viewModel: NoteDetailViewModel by viewModels()

    private var currentNote: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoteDetailBinding.bind(view)

        binding.apply {
            fabEditNote.setOnClickListener(this@NoteDetailFragment)
        }

        subscribeToObservers()

        if (savedInstanceState != null) {
            val addOwnerDialog = parentFragmentManager.findFragmentByTag(ADD_OWNER_DIALOG_TAG) as AddOwnerDialog?
            addOwnerDialog?.setPositiveListener {
                addOwnerToCurrentNote(it)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fabEditNote) {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id))
        }
    }

    private fun addOwnerToCurrentNote(email: String) {
        currentNote?.let { note ->
            viewModel.addOwnerToNote(email, note.id)
        }
    }

    private fun showAddOwnerDialog() {
        AddOwnerDialog().apply {
            setPositiveListener {
                addOwnerToCurrentNote(it)
            }
        }.show(parentFragmentManager, ADD_OWNER_DIALOG_TAG)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAddOwner->{
                showAddOwnerDialog()
            }
        }
        return super.onOptionsItemSelected(item)
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

        viewModel.addOwnerStatus.observe(viewLifecycleOwner,{event->
            event?.getContentIfNotHandled()?.let { result->
                when(result.status){
                    Status.SUCCESS->{
                        binding.apply {
                            addOwnerProgressBar.visibility = View.GONE
                        }
                        showSnackBar(result.data?:"Successfully added owner to note")
                    }
                    Status.LOADING->{
                        binding.apply {
                            addOwnerProgressBar.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR->{
                        binding.apply {
                            addOwnerProgressBar.visibility = View.GONE
                        }
                        showSnackBar(result.message?:"An unknown error occurred")
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