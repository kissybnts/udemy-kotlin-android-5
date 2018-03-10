package com.kissybnts.udemykotlinandroid5.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kissybnts.udemykotlinandroid5.R
import com.kissybnts.udemykotlinandroid5.service.AuthService
import com.kissybnts.udemykotlinandroid5.utils.hideKeyboard
import com.kissybnts.udemykotlinandroid5.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        spinnerLogin.visibility = View.INVISIBLE
    }

    fun onLoginButtonClicked(view: View) {
        enableSpinner(true)
        val email = emailTextLogin.text.toString()
        val password = passwordTextLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            errorToast("Please fill in both email and password")
            return
        }

        hideKeyboard()

        AuthService.login(email, password) { loginSuccess ->
            if (loginSuccess) {
                AuthService.findByUserByEmail(this) { userFound ->
                    if (userFound) {
                        finish()
                        enableSpinner(false)
                    } else {
                        errorToast("User Not Found")
                    }
                }
            } else {
                errorToast("Failed to login")
            }
        }
    }

    fun onCreateUserButtonClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    private fun errorToast(message: String) {
        showErrorToast(message, Toast.LENGTH_LONG)
        enableSpinner(false)
    }

    private fun enableSpinner(enable: Boolean) {
        if (enable) {
            spinnerLogin.visibility = View.VISIBLE
        } else {
            spinnerLogin.visibility = View.INVISIBLE
        }
        loginButtonLogin.isEnabled = !enable
        createUserButtonLogin.isEnabled = !enable
    }
}
