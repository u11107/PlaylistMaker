package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.App.Companion.TRACK
import com.example.playlistmaker.SearchActivity.Companion.SEARCH_EDIT_TEXT
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private var text: String = ""
    private val baseUrl = "http://itunes.apple.com"
    private val trackList = ArrayList<Track>()
    private var historyList = ArrayList<Track>()
    private val interceptor = HttpLoggingInterceptor()


    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl).client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val trackApi = retrofit.create(TrackApi::class.java)
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { search() }
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter = TrackAdapter {
            if(clickDebounce()) {
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra(TRACK, it)
                startActivity(intent)
            }
        }

        binding.recView.adapter = trackAdapter
        trackAdapter.trackList = trackList
        historyList.clear()
        historyList = SearchHistory.fillInList()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            focusVisibility(hasFocus)
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        binding.searchBtBack.setOnClickListener { finish() }

        binding.clearBt.setOnClickListener {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            binding.searchEditText.setText("")
            trackList.clear()
            binding.placeholderMessage.visibility = View.GONE
            showHistory()
            trackAdapter.notifyDataSetChanged()
        }

        binding.buttonUpdate.setOnClickListener {
            search()
        }

        binding.btClearHistory.setOnClickListener {
            SearchHistory.clear()
            historyList.clear()
            hideButtons()
            trackAdapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            this@SearchActivity.text = text.toString()
            if (!text.isNullOrEmpty()) {
                binding.clearBt.visibility = View.VISIBLE
                searchDebounce()
                history()
            } else {
                binding.clearBt.visibility = View.GONE
            }
        }
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

    }


    private fun hideButtons() {
        binding.tittleHistory.visibility = View.GONE
        binding.btClearHistory.visibility = View.GONE
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun history() {
        binding.tittleHistory.visibility = View.GONE
        binding.btClearHistory.visibility = View.GONE
        trackAdapter.trackList = trackList
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun focusVisibility(hasFocus: Boolean) {
        if (hasFocus && binding.searchEditText.text.isEmpty() && historyList.isNotEmpty()) {
            binding.tittleHistory.visibility = View.VISIBLE
            binding.btClearHistory.visibility = View.VISIBLE
        } else {
            binding.tittleHistory.visibility = View.GONE
            binding.btClearHistory.visibility = View.GONE
        }
        trackAdapter.trackList = historyList
        trackAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDIT_TEXT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(SEARCH_EDIT_TEXT).toString()
        searchEditText.setText(text)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, button: Boolean) {
        if (text.isNotEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.noConnection.visibility = View.GONE
            binding.nothingFoundImage.visibility = View.VISIBLE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            binding.tvError.text = text
            if (button) {
                binding.nothingFoundImage.visibility = View.GONE
                binding.noConnection.visibility = View.VISIBLE
                binding.buttonUpdate.visibility = View.VISIBLE
            } else {
                binding.buttonUpdate.visibility = View.GONE
            }
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        binding.tittleHistory.visibility = View.VISIBLE
        binding.btClearHistory.visibility = View.VISIBLE
        historyList = SearchHistory.fillInList()
        trackAdapter.trackList = historyList
        trackAdapter.notifyDataSetChanged()

    }


    private fun search() {
        if (binding.searchEditText.text.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recView.visibility = View.GONE
            binding.nothingFoundImage.visibility = View.GONE
            trackApi.search(binding.searchEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        Log.d("TRACK", "onResponse $response")
                        if (response.code() == 200) {
                            binding.progressBar.visibility = View.GONE
                            binding.recView.visibility = View.VISIBLE
                            trackList.clear()
                        }
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            showMessage(
                                getString(R.string.nothing_found),
                                false
                            )
                            binding.progressBar.visibility = View.GONE
                            binding.nothingFoundImage.visibility = View.VISIBLE

                        } else {
                            showMessage("", false)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(
                            getString(R.string.problem_internet),
                            true
                        )
                        binding.progressBar.visibility = View.GONE
                    }
                })
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}








