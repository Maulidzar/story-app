package com.example.storyapp.UI.Authentication.Text

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.MutableLiveData

class NameText : AppCompatEditText {
    private val msg = MutableLiveData<String>()
    private val hideError = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Boolean>()


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val name = s.toString()

                when {
                    name.isEmpty() -> {
                        setMsg("Name is needed !")
                        setError(true)
                    }
                    else -> {
                        hideErrorMessage()
                        setError(false)
                    }
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun hideErrorMessage() {
        hideError.value = true
    }

    private fun setMsg(msg: String) {
        this.msg.value = msg
    }

    private fun setError(error: Boolean) {
        this.error.value = error
    }
}
