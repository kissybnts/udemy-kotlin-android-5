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
import android.widget.EditText
import com.kissybnts.udemykotlinandroid5.R
import com.kissybnts.udemykotlinandroid5.service.AuthService
import com.kissybnts.udemykotlinandroid5.service.UserDataService
import com.kissybnts.udemykotlinandroid5.utils.BROADCAST_USER_DATA_CHANGE
import com.kissybnts.udemykotlinandroid5.utils.EMIT_ADD_CHANNEL
import com.kissybnts.udemykotlinandroid5.utils.URL_SOCKET
import com.kissybnts.udemykotlinandroid5.utils.hideKeyboard
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    private val socket = IO.socket(URL_SOCKET)

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isAuthed) {
                println(UserDataService.toString())
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.getAvatarColor(UserDataService.avatarColor))
                loginButtonNavHeader.text = "Logout"
                println("done")
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
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHANGE))
        socket.connect()
        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onLoginButtonClicked(view: View) {
        if (AuthService.isAuthed) {
            UserDataService.logout()
            loginButtonNavHeader.setText(R.string.login)
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun onAddChannelButtonClicked(view: View) {
        if (AuthService.isAuthed) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            builder.setView(dialogView).setPositiveButton("Add") { dialogInterface, i ->
                // perform some logic to add channel
                val channelName = dialogView.findViewById<EditText>(R.id.channelNameTextAddChannel).text.toString()
                val channelDescription = dialogView.findViewById<EditText>(R.id.channelDescriptionTextAddChannel).text.toString()

                socket.emit(EMIT_ADD_CHANNEL, channelName, channelDescription)
            }.setNegativeButton("Cancel") { dialogInterface, i ->
                // TODO do something when adding channel canceled
            }.show()
        }
    }

    fun onSendMessageButtonClicked(view: View) {
        hideKeyboard()
    }
}
