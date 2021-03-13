package com.jacobs.mj.ktornoteapp.ui.notes

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.graphics.Canvas
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.adapters.NoteAdapter
import com.jacobs.mj.ktornoteapp.databinding.FragmentNotesBinding
import com.jacobs.mj.ktornoteapp.other.Constants
import com.jacobs.mj.ktornoteapp.other.Status
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notes.*
import java.math.BigInteger
import javax.inject.Inject

/**
 * Created by mj on 2021/03/06 at 10:05 AM
 */
@AndroidEntryPoint
class NotesFragment : BaseFragment(R.layout.fragment_notes), View.OnClickListener {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var noteAdapter: NoteAdapter

    private val viewModel: NotesViewModel by viewModels()

    private val swipingItem = MutableLiveData(false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //  Letting the fragment know that there is an overFlowMenu
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        requireActivity().requestedOrientation = SCREEN_ORIENTATION_USER

        setupRecyclerView()

        subscribeToService()

        setupSwipeToRefreshLayout()

        noteAdapter.setOnItemClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToNoteDetailFragment(it.id))
        }

        binding.apply {
            fabAddNote.setOnClickListener(this@NotesFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionLogout) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        //  Clearing the saved variables in the shared preferences for the user that is logging out
        sharedPreferences.edit().putString(Constants.KEY_LOGGED_IN_EMAIL, Constants.NO_EMAIL).apply()
        sharedPreferences.edit().putString(Constants.KEY_PASSWORD, Constants.NO_PASSWORD).apply()
        //  Clearing the back stack to go back to AuthFragment
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.notesFragment, true).build()
        findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAuthFragment(), navOptions)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fabAddNote) {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(""))
        }
    }

    private fun setupRecyclerView() {
        binding.rvNotes.apply {
            noteAdapter = NoteAdapter()
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val note = noteAdapter.notes[position]
            viewModel.deleteNote(note.id)
            Snackbar.make(requireView(), "Note was successfully deleted", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.insertNote(note)
                    viewModel.deleteLocallyDeletedNoteId(note.id)
                }
                show()
            }
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float,
            dY: Float, actionState: Int, isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                swipingItem.postValue(isCurrentlyActive)
            }
        }
    }

    private fun setupSwipeToRefreshLayout(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.syncAllNotes()
        }
    }

    private fun subscribeToService() {
        viewModel.allNotes.observe(viewLifecycleOwner, {
            it?.let { event ->
                val result = event.peekContent()
                when (result.status) {
                    Status.SUCCESS -> {
                        noteAdapter.notes = result.data!!
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    Status.LOADING -> {
                        result.data?.let { notes ->
                            noteAdapter.notes = notes
                        }
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    Status.ERROR -> {
                        event.getContentIfNotHandled()?.let { errorResource ->
                            errorResource.message?.let { message ->
                                showSnackBar(message)
                            }
                        }
                        result.data?.let { notes ->
                            noteAdapter.notes = notes
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        })

        swipingItem.observe(viewLifecycleOwner, {
            binding.swipeRefreshLayout.isEnabled = !it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}