package com.kissybnts.udemykotlinandroid5.controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.kissybnts.udemykotlinandroid5.R
import com.kissybnts.udemykotlinandroid5.service.AuthService
import com.kissybnts.udemykotlinandroid5.service.UserDataService
import com.kissybnts.udemykotlinandroid5.utils.BROADCAST_USER_DATA_CHANGE
import com.kissybnts.udemykotlinandroid5.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        spinnerCreateUser.visibility = View.INVISIBLE
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
        enableSpinner(true)

        val userName = userNameTextCreateUser.text.toString()
        val email = emailTextCreateUser.text.toString()
        val password = passwordTestCreateUser.text.toString()

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorToast("Make sure user name, email and password are filled in")
            return
        }

        AuthService.registerUser(this, email, password) { registerSuccess ->
            if (registerSuccess) {
                AuthService.login(this, email, password) { loginSuccess ->
                    if (loginSuccess) {
                        AuthService.createUser(this, userName, email, userAvatar, avatarColor) { createSuccess ->
                            if (createSuccess) {
                                // Notify that the user data has been changed to another activity using LocalBroadcast
                                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                enableSpinner(false)
                                finish()
                            } else {
                                errorToast("Failed to createUser")
                            }
                        }
                    } else {
                        errorToast("Failed to login")
                    }
                }
            } else {
                errorToast("Failed to registerUser")
            }
        }
    }

    private fun errorToast(message: String) {
        showErrorToast(message, Toast.LENGTH_LONG)
        enableSpinner(false)
    }

    private fun enableSpinner(enable: Boolean) {
        if (enable) {
            spinnerCreateUser.visibility = View.VISIBLE
        } else {
            spinnerCreateUser.visibility = View.INVISIBLE
        }
        userNameTextCreateUser.isEnabled = !enable
        emailTextCreateUser.isEnabled = !enable
        passwordTestCreateUser.isEnabled = !enable
        userAvatarImageCreateUser.isEnabled = !enable
        generateBackgroundColorButtonCreateUser.isEnabled = !enable
    }
}
