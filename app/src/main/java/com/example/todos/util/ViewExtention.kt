package com.example.todos.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.todos.R
import com.skydoves.powerspinner.PowerSpinnerView


fun PowerSpinnerView.setCustomBackground(context: Context) {
    background = if (this.isShowing) {
        ContextCompat.getDrawable(
            context, R.drawable.custom_spinner_active
        )
    } else {
        ContextCompat.getDrawable(
            context, R.drawable.custom_spinner_shape
        )
    }
}
