package com.tws.moments.views.span

import android.text.TextPaint
import android.text.style.UnderlineSpan

class NoUnderLineSpan : UnderlineSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.color = ds.linkColor
        ds.isUnderlineText = false
    }
}
