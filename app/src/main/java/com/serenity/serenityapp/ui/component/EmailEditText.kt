package com.serenity.serenityapp.ui.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.MutableLiveData

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs), View.OnTouchListener {
    val liveError = MutableLiveData<String?>()

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(chars: CharSequence, start: Int, before: Int, count: Int) {
                error = if (!isValidEmail(chars.toString())) "Email tidak valid" else null
                liveError.value = error?.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return super.onTouchEvent(p1)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}