package com.kissybnts.udemykotlinandroid5.utils

import android.content.Context
import android.widget.Toast

internal fun Context.showErrorToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}