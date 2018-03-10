package com.kissybnts.udemykotlinandroid5.service

import android.graphics.Color
import com.kissybnts.udemykotlinandroid5.controller.App
import java.util.*

object UserDataService {
    var id = ""
    var name = ""
    var email = ""
    var avatarName = ""
    var avatarColor = ""

    fun getAvatarColor(component: String): Int {
        val strippedColor = component.replace("[", "").replace(",", "").replace("]", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r, g, b)
    }

    fun logout() {
        id = ""
        name = ""
        email = ""
        avatarName = ""
        avatarColor = ""
        App.prefs.authToken = ""
        App.prefs.isLoggedIn = false
        App.prefs.userEmail = ""
    }

    override fun toString(): String {
        return "id: $id, name: $name, email: $email, avatarName: $avatarName, avatarColor: $avatarColor"
    }
}