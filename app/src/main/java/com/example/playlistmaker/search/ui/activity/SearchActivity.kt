package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.model.NetworkError
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.TrackAdapter
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.util.App.Companion.TRACK

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private var searchInputQuery = ""

    private val trackAdapter = TrackAdapter { showPlayer(it) }
    private val historyAdapter = TrackAdapter { showPlayer(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        initAdapters()

        binding.inputEditText.requestFocus()
        binding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.inputEditText.doOnTextChanged { text, _, _, _ ->
            binding.clearImageView.visibility = clearButtonVisibility(text)
            text?.let { viewModel.searchDebounce(it.toString()) }
        }

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(this))[SearchViewModel::class.java]
        viewModel.stateLiveData.observe(this) {
            render(it)
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.SearchHistory -> {
                showHistoryList(state.tracks)
            }

            is SearchState.Loading -> {
                showLoading()
            }

            is SearchState.SearchedTracks -> {
                showSearchResult(state.tracks)
            }

            is SearchState.SearchError -> {
                showErrorMessage(state.error)
            }
        }
    }

    private fun initAdapters() {
        binding.searchRecycler.adapter = trackAdapter
        binding.searchHistoryRecycler.adapter = historyAdapter
    }

    private fun initListeners() {
        binding.refreshButton.setOnClickListener {
            viewModel.search(searchInputQuery)
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
        binding.clearImageView.setOnClickListener {
            viewModel.clearSearchText()
            binding.inputEditText.text?.clear()
            clearContent()

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun showPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            viewModel.addTrackToHistory(track)
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(
                TRACK, track)
            viewModel.clickDebounce()
            startActivity(intent)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showHistoryList(tracks: List<Track>) {
        clearContent()
        historyAdapter.tracks = tracks as ArrayList<Track>
        if (tracks.isNotEmpty()) {
            binding.searchHistoryLayout.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        clearContent()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showSearchResult(tracks: List<Track>) {
        clearContent()
        trackAdapter.clearTracks()
        trackAdapter.tracks.addAll(tracks)
        binding.searchRecycler.visibility = View.VISIBLE
    }

    private fun showErrorMessage(error: NetworkError) {
        clearContent()
        when(error) {
            NetworkError.EMPTY_RESULT -> {
                binding.searchRecycler.visibility = View.GONE
                binding.internetProblem.visibility = View.GONE
                binding.searchHistoryLayout.visibility = View.GONE
                binding.nothingFound.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
            NetworkError.CONNECTION_ERROR -> {
                binding.searchRecycler.visibility = View.GONE
                binding.nothingFound.visibility = View.GONE
                binding.searchHistoryLayout.visibility = View.GONE
                binding.internetProblem.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun clearContent() {
        binding.nothingFound.visibility = View.GONE
        binding.internetProblem.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputQuery = savedInstanceState.getString(SEARCH_QUERY, "")
        if (searchInputQuery.isNotEmpty()) {
            binding.inputEditText.setText(searchInputQuery)
            viewModel.search(searchInputQuery)
        }
    }

    companion object {
        const val SEARCH_QUERY = "search_query"
    }
}