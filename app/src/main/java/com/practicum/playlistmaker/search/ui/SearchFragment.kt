package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.view_model.ClearTextState
import com.practicum.playlistmaker.search.view_model.SearchState
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import com.practicum.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class

SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    private val searchResultAdapter = TrackAdapter {
        onCLickDebounce(it)
    }

    private val searchHistoryAdapter = TrackAdapter {
        onCLickDebounce(it)
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var onCLickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        viewModel.observeSearchText().observe(viewLifecycleOwner) {
            binding.searchEditText.setText(it)
        }

        onCLickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.onTrackPressed(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(track.toString())
            )
        }

        viewModel.observeClearTextState().observe(viewLifecycleOwner) { clearTextState ->
            if (clearTextState is ClearTextState.ClearText) {
                clearSearchText()
                hideKeyboard()
                viewModel.textCleared()
            }
        }

        binding.searchHistoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchHistoryAdapter
        }

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchResultAdapter
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(hasFocus, binding.searchEditText.text.toString())
        }

        binding.clearTextImage.setOnClickListener {
            viewModel.onClearTextPressed()
        }

        binding.refreshSearchButton.setOnClickListener {
            viewModel.onRefreshSearchButtonPressed()
        }

        binding.clearSearchHistoryButton.setOnClickListener {
            viewModel.onClearSearchHistoryPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.searchEditText.removeTextChangedListener(searchTextWatcher)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showProgressBar()
            is SearchState.SearchContent -> showSearchResult(state.tracks)
            is SearchState.HistoryContent -> showSearchHistoryLayout(state.tracks)
            is SearchState.EmptySearch -> showEmptySearch(state.emptySearchMessage)
            is SearchState.Error -> showSearchError(state.errorMessage)
            is SearchState.EmptyScreen -> showEmptyScreen()
        }
    }

    private fun clearSearchText() {
        binding.searchEditText.text.clear()
        binding.clearTextImage.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.onTextChanged(p0.toString() ?: "")
        }

        override fun afterTextChanged(editable: Editable?) {
            if (editable?.isNotEmpty() == true) {
                binding.clearTextImage.visibility = View.VISIBLE
            } else {
                binding.clearTextImage.visibility = View.GONE
            }
        }
    }

    private fun showEmptyScreen() {
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchErrorLayout.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.GONE
    }

    private fun showSearchResult(tracks: List<Track>) {
        binding.searchRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.searchErrorLayout.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.GONE
        searchResultAdapter.trackList.clear()
        searchResultAdapter.trackList.addAll(tracks)
        searchResultAdapter.notifyDataSetChanged()
    }

    private fun showSearchHistoryLayout(searchHistory: List<Track>) {
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchErrorLayout.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.VISIBLE
        searchHistoryAdapter.trackList.clear()
        searchHistoryAdapter.trackList.addAll(searchHistory)
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun showEmptySearch(emptySearchMessage: String) {
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchErrorLayout.visibility = View.VISIBLE
        binding.searchHistoryLayout.visibility = View.GONE
        binding.refreshSearchButton.visibility = View.GONE
        searchResultAdapter.trackList.clear()
        searchResultAdapter.notifyDataSetChanged()
        binding.searchErrorText.text = emptySearchMessage
        binding.searchErrorImage.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.nothing_is_found
            )
        )
    }

    private fun showSearchError(errorMessage: String) {
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchErrorLayout.visibility = View.VISIBLE
        binding.searchHistoryLayout.visibility = View.GONE
        binding.refreshSearchButton.visibility = View.VISIBLE
        searchResultAdapter.trackList.clear()
        searchResultAdapter.notifyDataSetChanged()
        binding.searchErrorText.text = errorMessage
        binding.searchErrorImage.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.no_internet_connection
            )
        )
    }

    private fun showProgressBar() {
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.searchErrorLayout.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.GONE
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}