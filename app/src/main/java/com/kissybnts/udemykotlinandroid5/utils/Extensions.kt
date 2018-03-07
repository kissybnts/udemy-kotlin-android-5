package com.kissybnts.udemykotlinandroid5.utils

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

internal fun Context.showErrorToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

internal fun AppCompatActivity.hideKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}