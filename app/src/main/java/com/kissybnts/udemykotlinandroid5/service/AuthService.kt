package com.kissybnts.udemykotlinandroid5.service

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kissybnts.udemykotlinandroid5.utils.URL_REGISTER
import org.json.JSONObject

object AuthService {
    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val url: String = URL_REGISTER

        val requestBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }.toString()

        val registerRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { _ ->
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }
}