package com.practicum.playlistmaker.search.presentation.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.presentation.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.SearchScreenState
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.R

class SearchFragment: Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModel()

    private var searchText: String? = ""

    private val historyAdapter = TrackAdapter(ArrayList()).apply {
        clickListener = TrackAdapter.TrackClickListener {
            viewModel.showPlayer(it)
        }
    }

    private val items = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(items).apply {
        clickListener = TrackAdapter.TrackClickListener {
            viewModel.addTrackToSearchHistory(it)
            viewModel.showPlayer(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getShowPlayerTrigger().observe(viewLifecycleOwner) {
            showPlayerActivity(it)
        }

        binding.btClearSearch.setOnClickListener {
            binding.edSearch.setText("")
            val imm =
                binding.edSearch.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.edSearch.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btClearSearch.isVisible = !s.isNullOrEmpty()
                viewModel.onEditTextChanged(binding.edSearch.hasFocus(), binding.edSearch.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = binding.edSearch.text.toString()
            }
        }

        binding.edSearch.addTextChangedListener(searchTextWatcher)
        binding.edSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEditorAction()
            }
            false
        }
        binding.edSearch.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onEditFocusChange(hasFocus)
        }

        binding.trackError.btRetry.setOnClickListener {
            viewModel.onRetryButtonClick()
        }

        binding.searchHistory.btClearSearchHistory.setOnClickListener {
            viewModel.onClearSearchHistoryButtonClick()
        }

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = trackAdapter

        val mLayoutManager = LinearLayoutManager(requireContext())
        mLayoutManager.reverseLayout = true
        binding.searchHistory.SearchList.layoutManager = mLayoutManager
        binding.searchHistory.SearchList.adapter = historyAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.edSearch.setText(savedInstanceState?.getString(SEARCH_TEXT))
        binding.edSearch.setSelection(binding.edSearch.text.length)
    }

    private fun showPlayerActivity(track: Track) {
        findNavController().navigate(R.id.action_searchFragment_to_playerActivity,
            PlayerActivity.createArgs(track))
    }

    private fun render(state: SearchScreenState) {
        binding.trackList.isVisible = state is SearchScreenState.List
        binding.trackEmpty.root.isVisible = state is SearchScreenState.Empty
        binding.trackError.root.isVisible = state is SearchScreenState.Error
        binding.searchHistory.root.isVisible = state is SearchScreenState.History
        binding.progressBar.isVisible = state is SearchScreenState.Progress
        when (state) {
            is SearchScreenState.List -> trackAdapter.addItems(state.tracks)
            is SearchScreenState.Empty -> Unit
            is SearchScreenState.Error -> Unit
            is SearchScreenState.History -> historyAdapter.addItems(state.tracks)
            is SearchScreenState.Progress -> Unit
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}