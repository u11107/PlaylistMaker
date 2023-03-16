package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
    }

    var text:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val butClear = findViewById<ImageView>(R.id.clear_bt)
        val searchEditText = findViewById<EditText>(R.id.search_editText)
        val searchBackBt = findViewById<Button>(R.id.search_bt_back)

        butClear.setOnClickListener {
            searchEditText.setText("")
        }


        searchBackBt.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    butClear.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        val searchEditText = findViewById<EditText>(R.id.search_editText).text.toString()
//        outState.putString(SEARCH_EDIT_TEXT, searchEditText)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        val searchEditText = findViewById<EditText>(R.id.search_editText)
//        text = savedInstanceState.getString(SEARCH_EDIT_TEXT).toString()
//        searchEditText.setText(text)
//    }

}
