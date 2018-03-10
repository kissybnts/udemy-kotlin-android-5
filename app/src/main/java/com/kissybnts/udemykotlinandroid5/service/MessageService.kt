package com.kissybnts.udemykotlinandroid5.service

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.kissybnts.udemykotlinandroid5.controller.App
import com.kissybnts.udemykotlinandroid5.model.Channel
import com.kissybnts.udemykotlinandroid5.model.Message
import com.kissybnts.udemykotlinandroid5.utils.CONTENT_TYPE
import com.kissybnts.udemykotlinandroid5.utils.URL_GET_CHANNELS
import com.kissybnts.udemykotlinandroid5.utils.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener {
            try {
                for (x in 0 until it.length()) {
                    val channelJson = it.getJSONObject(x)
                    channels.add(Channel(channelJson))
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not retrieve channels")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return CONTENT_TYPE
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${App.prefs.authToken}")
            }
        }

        App.prefs.requestQue.add(channelRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        clearMessages()

        val url = URL_GET_MESSAGES(channelId)

        val messageRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener {
            try {
                for (x in 0 until it.length()) {
                    val message = Message(it.getJSONObject(x))
                    messages.add(message)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener {
            Log.d("ERROR", "Could not retrieve messages")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return CONTENT_TYPE
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${App.prefs.authToken}")
            }
        }

        App.prefs.requestQue.add(messageRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels() {
        channels.clear()
    }
}