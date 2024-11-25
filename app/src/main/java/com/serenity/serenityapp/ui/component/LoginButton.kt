package com.serenity.serenityapp.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.serenity.serenityapp.R

class LoginButton: AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var enabledTextColor: Int = 0
    private var disabledTextColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    init {
        enabledTextColor = ContextCompat.getColor(context, R.color.white)
        disabledTextColor = ContextCompat.getColor(context, R.color.black)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        gravity = Gravity.CENTER
        textSize = 12f
        text = if (isEnabled) "Login" else "Fill all input"
    }
}