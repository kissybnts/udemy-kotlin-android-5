package com.kissybnts.udemykotlinandroid5.model

import org.json.JSONObject

data class Message(val message: String,
                   val userName: String,
                   val channelId: String,
                   val userAvatar: String,
                   val userAvatarColor: String,
                   val id: String,
                   val timestamp: String) {
    constructor(args: Array<out Any>): this(
            args[0] as String,
            args[3] as String,
            args[2] as String,
            args[4] as String,
            args[5] as String,
            args[6] as String,
            args[7] as String
    )
    constructor(json: JSONObject): this(
            json.getString("messageBody"),
            json.getString("userName"),
            json.getString("channelId"),
            json.getString("userAvatar"),
            json.getString("userAvatarColor"),
            json.getString("_id"),
            json.getString("timeStamp")
    )
}