package com.jacobs.mj.ktornoteapp.ui.notes

import android.content.SharedPreferences
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.*
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jacobs.mj.ktornoteapp.R
import com.jacobs.mj.ktornoteapp.databinding.FragmentNotesBinding
import com.jacobs.mj.ktornoteapp.other.Constants
import com.jacobs.mj.ktornoteapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //  Letting the fragment know that there is an overFlowMenu
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}