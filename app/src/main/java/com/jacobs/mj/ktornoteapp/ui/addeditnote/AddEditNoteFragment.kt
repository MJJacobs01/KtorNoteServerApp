package com.jacobs.mj.ktornoteapp.ui.addeditnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.security.crypto.EncryptedSharedPreferences
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import com.jacobs.mj.ktornoteapp.databinding.FragmentAddEditNoteBinding
import com.jacobs.mj.ktornoteapp.other.Constants
import com.jacobs.mj.ktornoteapp.other.Status
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import com.jacobs.mj.ktornoteapp.ui.dialogs.ColorPickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by mj on 2021/03/06 at 10:00 AM
 */

const val FRAGMENT_TAG = "AddEditNoteFragment"

@AndroidEntryPoint
class AddEditNoteFragment : BaseFragment(R.layout.fragment_add_edit_note), View.OnClickListener {
    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddEditViewModel by viewModels()
    private val args: AddEditNoteFragmentArgs by navArgs()

    private var currentNote: Note? = null
    private var currentNoteColor = Constants.DEFAULT_NOTE_COLOR

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditNoteBinding.bind(view)

        if (savedInstanceState != null) {
            //  Attaching the listener to the dialog fragment after screen rotation
            val colorPickerDialog = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG) as ColorPickerDialogFragment?
            colorPickerDialog?.setPositiveListener {
                changeViewNoteColor(it)
            }
        }

        if (args.id.isNotEmpty()) {
            viewModel.getNoteById(args.id)
            subscribeToObservers()
        }

        binding.apply {
            viewNoteColor.setOnClickListener(this@AddEditNoteFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.viewNoteColor -> {
                ColorPickerDialogFragment().apply {
                    setPositiveListener {
                        changeViewNoteColor(it)
                    }
                }.show(parentFragmentManager, FRAGMENT_TAG)
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.note.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val note = result.data!!
                        currentNote = note
                        binding.apply {
                            etNoteTitle.setText(note.title)
                            etNoteContent.setText(note.content)
                            changeViewNoteColor(note.color)
                        }
                    }
                    Status.ERROR -> {
                        showSnackBar(result.message ?: "Note not found")
                    }
                    Status.LOADING -> {
                        /* NO-OP */
                    }
                }
            }
        })
    }

    private fun saveNote() {
        val authEmail = sharedPreferences.getString(Constants.KEY_LOGGED_IN_EMAIL, Constants.NO_EMAIL)

        val title = binding.etNoteTitle.text.toString()
        val content = binding.etNoteContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            return
        }
        val date = System.currentTimeMillis()
        val color = currentNoteColor
        val id = currentNote?.id ?: UUID.randomUUID().toString()
        val owners: List<String> = (currentNote?.owners ?: listOf(authEmail)) as List<String>
        val note = Note(title, content, date, owners, color, id)
        viewModel.insertNote(note)
    }

    private fun changeViewNoteColor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#${colorString}")
            DrawableCompat.setTint(wrappedDrawable, color)
            binding.viewNoteColor.background = wrappedDrawable
            currentNoteColor = colorString
        }
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        currentNote = null
    }
}