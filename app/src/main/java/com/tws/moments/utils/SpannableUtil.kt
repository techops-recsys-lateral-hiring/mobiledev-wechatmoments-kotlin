package com.tws.moments.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.tws.moments.views.span.NoUnderLineSpan

fun String.clickableSpan(onClick: ((View) -> Unit)? = null): SpannableString {
    return SpannableString(this).also {
        it.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick?.invoke(widget)
            }

        }, 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        it.setSpan(NoUnderLineSpan(), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        it.setSpan(ForegroundColorSpan(Color.parseColor("#4152c9")), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}
