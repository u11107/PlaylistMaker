package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
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
    private val interceptor = HttpLoggingInterceptor()


    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl).client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val trackApi = retrofit.create(TrackApi::class.java)

    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter = TrackAdapter(trackList)
        binding.recView.adapter = trackAdapter

        interceptor.level = HttpLoggingInterceptor.Level.BODY


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
            trackAdapter.notifyDataSetChanged()
        }

        binding.buttonUpdate.setOnClickListener {
            search()
        }

        val simpleTextWatcher = binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            this@SearchActivity.text = text.toString()
            if (!text.isNullOrEmpty()) {
                binding.clearBt.visibility = View.VISIBLE
            } else {
                binding.clearBt.visibility = View.GONE
            }
        }
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)
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

    private fun showMessage(text: String,button: Boolean) {
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

    private fun search() {
        if (binding.searchEditText.text.isNotEmpty()) {
            trackApi.search(binding.searchEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        Log.d("TRACK", "onResponse $response")
                        if (response.code() == 200) {
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
                        } else {
                            showMessage("",  false)
                        }
                    }
                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(
                            getString(R.string.problem_internet),
                             true
                        )
                    }
                })
        }
    }
}






