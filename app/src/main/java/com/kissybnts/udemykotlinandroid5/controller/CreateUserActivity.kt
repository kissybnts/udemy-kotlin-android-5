package com.kissybnts.udemykotlinandroid5.controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kissybnts.udemykotlinandroid5.R
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun onUserAvatarClicked(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        // image number is between 0 and 27
        val avatar = random.nextInt(28)

        userAvatar = if (color == 0) {
            "light$avatar"
        } else {
            "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        userAvatarImageCreateUser.setImageResource(resourceId)
    }

    fun onGenerateBackgroundColorButtonClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        userAvatarImageCreateUser.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255
        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun onCreateUserButtonClicked(view: View) {

    }
}