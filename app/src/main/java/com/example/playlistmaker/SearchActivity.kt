package com.example.playlistmaker

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
    }

    private var text: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val butClear = findViewById<ImageView>(R.id.clear_bt)
        val searchEditText = findViewById<EditText>(R.id.search_editText)
        val searchBackBt = findViewById<Button>(R.id.search_bt_back)


        searchBackBt.setOnClickListener {
            finish()
        }

        butClear.setOnClickListener {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = searchEditText.text.toString()
                visibilityItem(s)
            }

            private fun visibilityItem(s: CharSequence?) {
                if (!s.isNullOrEmpty()) {
                    butClear.visibility = View.VISIBLE
                } else {
                    butClear.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDIT_TEXT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val searchEditText = findViewById<EditText>(R.id.search_editText)
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(SEARCH_EDIT_TEXT).toString()
        searchEditText.setText(text)
    }

}

