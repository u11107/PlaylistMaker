package com.example.playlistmaker

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.SearchActivity.Companion.SEARCH_EDIT_TEXT
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding

    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
    }

    private var text: String = ""
    val track: Track? = null

    private val dataList = mutableListOf(
        Track(
            "Smells Like Teen Spiritsdfsadfadsfsafdasdfasdf",
            "Nirvanazdfgdsgsdgsdgfgsdgfdsfgsdfgxfgsdrrgsegxfcghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            "Billie Jean", "Michael Jackson", "4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),
        Track(
            "Stayin' Alive", "Bee Gees", "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            "Whole Lotta Love", "Led Zeppelin", "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),
        Track(
            "Sweet Child O'Mine", "Guns N' Roses", "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "
        ),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val trackAdapter = TrackAdapter(dataList)

        binding.recView.adapter = trackAdapter

        binding.searchBtBack.setOnClickListener {
            finish()
        }

        binding.clearBt.setOnClickListener {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            binding.searchEditText.setText("")
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
}

