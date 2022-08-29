package com.tr4n.puzzle.extension

import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

fun EditText.setOnSearchSubmitListener(onSubmit: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onSubmit()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

fun TextView.setHtml(html: String) {
    text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
}

fun EditText.addTextWatcher(
    onTextChanged: (s: String) -> Unit = { _ -> },
    onAfterChanged: (s: String) -> Unit = { _ -> },
    onBeforeChanged: (s: String) -> Unit = { _ -> }
) =
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            editable?.let {
                onAfterChanged(editable.toString())
            }
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            text?.let {
                onBeforeChanged(text.toString())
            }
        }

        override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged(sequence.toString())
        }
    })
