package com.kissybnts.udemykotlinandroid5.service

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.kissybnts.udemykotlinandroid5.controller.App
import com.kissybnts.udemykotlinandroid5.utils.*
import org.json.JSONException
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
                return CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQue.add(registerRequest)
    }

    fun login(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val url = URL_LOGIN

        val requestBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }.toString()

        val loginRequest = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { response ->
            try {
                App.prefs.userEmail = response.getString("user")
                App.prefs.authToken = response.getString("token")
                App.prefs.isLoggedIn = true
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
                return CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQue.add(loginRequest)
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
                return CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${App.prefs.authToken}")
            }
        }
        
        App.prefs.requestQue.add(createUserRequest)
    }


    fun findByUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val url = URL_FIND_USER_BY_EMIL(App.prefs.userEmail)
        val findUserRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            try {
                it.run {
                    UserDataService.id = getString("_id")
                    UserDataService.name = getString("name")
                    UserDataService.email = getString("email")
                    UserDataService.avatarName = getString("avatarName")
                    UserDataService.avatarColor = getString("avatarColor")
                }

                // Broadcast what user data has been changed
                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)

                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener {
            Log.d("ERROR", "Could not log in: $it")
            complete(false)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${App.prefs.authToken}")
            }

            override fun getBodyContentType(): String {
                return CONTENT_TYPE
            }
        }

        App.prefs.requestQue.add(findUserRequest)
    }
}