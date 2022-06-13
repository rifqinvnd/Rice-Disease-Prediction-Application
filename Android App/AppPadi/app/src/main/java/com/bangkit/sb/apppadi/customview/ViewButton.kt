package com.bangkit.sb.apppadi.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bangkit.sb.apppadi.R

class ViewButton:AppCompatButton {
    private lateinit var btnColor: Drawable
    private var color: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        setTextColor(color)
        textSize = 20f
        gravity = Gravity.CENTER
    }

    private fun init(){
        color = ContextCompat.getColor(context, android.R.color.background_light)
        btnColor = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
    }

}