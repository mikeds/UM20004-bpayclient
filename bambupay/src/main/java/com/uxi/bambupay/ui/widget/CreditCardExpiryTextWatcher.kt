package com.uxi.bambupay.ui.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CreditCardExpiryTextWatcher(private val editText: EditText) : TextWatcher {
    private var isDelete = false
    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        isDelete = if (before == 0) false else true
    }

    override fun afterTextChanged(s: Editable) {
        val source = s.toString()
        val length = source.length
        val stringBuilder = StringBuilder()
        stringBuilder.append(source)
        if (length > 0 && length == 3) {
            if (isDelete) stringBuilder.deleteCharAt(length - 1) else stringBuilder.insert(
                length - 1,
                "/"
            )
            editText.setText(stringBuilder)
            editText.setSelection(editText.text.length)
        }
    }

}