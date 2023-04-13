package com.example.storyapp.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText

@SuppressLint("AppCompatCustomView")
class PasswordEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    init {
        // setup here if needed
    }

    fun setError() {
        if (text.length < 8) {
            error = "Password harus memiliki setidaknya 8 karakter"
        } else {
            setBackgroundColor(Color.TRANSPARENT)
            error = null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setError()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })
    }

}