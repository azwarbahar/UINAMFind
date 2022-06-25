package com.azwar.uinamfind.utils.ui

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View


open class MySpannable(isUnderline : Boolean) : ClickableSpan() {

    private var isUnderline = isUnderline

//    fun MySpannable(isUnderline: Boolean) {
//        this.isUnderline = isUnderline
//    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = isUnderline
        ds.color = Color.parseColor("#1b76d3")
    }

    override fun onClick(view: View) {}
}