package catrin.dev.postgettestapp.helpers

import android.view.View

fun View.click(click: () -> Unit) = setOnClickListener { click() }