package com.kissybnts.udemykotlinandroid5.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import com.kissybnts.udemykotlinandroid5.R
import com.kissybnts.udemykotlinandroid5.model.Channel
import com.kissybnts.udemykotlinandroid5.model.Message
import com.kissybnts.udemykotlinandroid5.service.AuthService
import com.kissybnts.udemykotlinandroid5.service.MessageService
import com.kissybnts.udemykotlinandroid5.service.UserDataService
import com.kissybnts.udemykotlinandroid5.utils.*
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    private val socket = IO.socket(URL_SOCKET)
    private val onNewChannel = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                val channel = Channel(args)
                MessageService.channels.add(channel)
                channelAdapter.notifyDataSetChanged()
            }
        }
    }
    private val onNewMessage = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                // args[2] means a channel id
                if (selectedChannel?.id == args[2] as String) {
                    val message = Message(args)
                    MessageService.messages.add(message)
                    println(message)
                }
            }
        }
    }
    private lateinit var channelAdapter: ArrayAdapter<Channel>
    private var selectedChannel: Channel? = null

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.prefs.isLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.getAvatarColor(UserDataService.avatarColor))
                loginButtonNavHeader.text = "Logout"

                MessageService.getChannels { complete ->
                    if (complete) {
                        MessageService.channels.firstOrNull()?.let {
                            selectedChannel = it
                            channelAdapter.notifyDataSetChanged()
                            updateWithChannel()
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setupAdapters()

        socket.connect()
        socket.on(EMIT_CHANNEL_CREATED, onNewChannel)
        socket.on(EMIT_MESSAGE_CREATED, onNewMessage)

        if (App.prefs.isLoggedIn) {
            AuthService.findByUserByEmail(this) {}
        }

        channel_list.setOnItemClickListener { _, _, i, _ ->
            selectedChannel = MessageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHANGE))
        super.onResume()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        socket.disconnect()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupAdapters() {
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter
    }

    fun onLoginButtonClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            UserDataService.logout()
            loginButtonNavHeader.setText(R.string.login)
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            mainChannelName.setText(R.string.please_login)
            MessageService.clearMessages()
            MessageService.clearChannels()
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun onAddChannelButtonClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            builder.setView(dialogView).setPositiveButton("Add") { _, _ ->
                // perform some logic to add channel
                val channelName = dialogView.findViewById<EditText>(R.id.channelNameTextAddChannel).text.toString()
                val channelDescription = dialogView.findViewById<EditText>(R.id.channelDescriptionTextAddChannel).text.toString()

                socket.emit(EMIT_ADD_CHANNEL, channelName, channelDescription)
            }.setNegativeButton("Cancel") { _, i ->
                // TODO do something when adding channel canceled
            }.show()
        }
    }

    fun onSendMessageButtonClicked(view: View) {
        val channel = selectedChannel
        if (App.prefs.isLoggedIn && messageTextField.text.isNotEmpty() && channel != null) {
            val userId = UserDataService.id
            val channelId = channel.id
            socket.emit(EMIT_ADD_MESSAGE, messageTextField.text.toString(), userId, channelId, UserDataService.name, UserDataService.avatarName, UserDataService.avatarColor)
            messageTextField.text.clear()
            hideKeyboard()
        }
    }

    fun updateWithChannel() {
        selectedChannel?.let {
            mainChannelName.text = it.toString()
            MessageService.getMessages(it.id) { complete ->
                if (complete) {
                    MessageService.messages.forEach { m ->
                        println(m)
                    }
                }
            }
        }
    }
}
