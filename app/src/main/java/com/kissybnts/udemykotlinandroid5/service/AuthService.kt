package com.kissybnts.udemykotlinandroid5.service

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kissybnts.udemykotlinandroid5.utils.URL_CREATE_USER
import com.kissybnts.udemykotlinandroid5.utils.URL_LOGIN
import com.kissybnts.udemykotlinandroid5.utils.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    var token = ""
    var isAuthed = false
    var userEmail = ""

    private const val contentType = "application/json; charset=utf-8"

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
                return contentType
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun login(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val url = URL_LOGIN

        val requestBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }.toString()

        val loginRequest = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { response ->
            try {
                userEmail = response.getString("user")
                token = response.getString("token")
                isAuthed = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not log in: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return contentType
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }
    
    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val url = URL_CREATE_USER

        val requestBody = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("avatarName", avatarName)
            put("avatarColor", avatarColor)
        }.toString()
        
        val createUserRequest = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { 
            try {
                it.run {
                    UserDataService.id = getString("_id")
                    UserDataService.name = getString("name")
                    UserDataService.email = getString("email")
                    UserDataService.avatarName = getString("avatarName")
                    UserDataService.avatarColor = getString("avatarColor")
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { 
            Log.d("ERROR", "Could not log in: $it")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return contentType
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${token}")
            }
        }
        
        Volley.newRequestQueue(context).add(createUserRequest)
    }
}