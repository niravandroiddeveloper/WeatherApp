package com.wheatherapp

import android.widget.ImageView



fun ImageView.changeColor(color: Int) =
    setColorFilter(
        color,
        android.graphics.PorterDuff.Mode.MULTIPLY)
