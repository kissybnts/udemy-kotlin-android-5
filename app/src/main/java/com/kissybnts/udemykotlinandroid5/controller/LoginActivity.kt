package com.kissybnts.udemykotlinandroid5.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kissybnts.udemykotlinandroid5.R
import com.kissybnts.udemykotlinandroid5.service.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLoginButtonClicked(view: View) {
        val email = emailTextLogin.text.toString()
        val password = passwordTextLogin.text.toString()
        AuthService.login(this, email, password) { loginSuccess ->
            if (loginSuccess) {
                AuthService.findByUserByEmail(this) { userFound ->
                    if (userFound) {
                        finish()
                    } else {
                        Toast.makeText(this, "User Not Found", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Failed to login", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onCreateUserButtonClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }
}
