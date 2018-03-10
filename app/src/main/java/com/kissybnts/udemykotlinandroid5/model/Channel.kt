package com.kissybnts.udemykotlinandroid5.model

import org.json.JSONObject

class Channel(val id: String, val name: String, val description: String) {
    constructor(args: Array<out Any>): this(args[2] as String, args[0] as String, args[1] as String)
    constructor(json: JSONObject): this(json.getString("_id"), json.getString("name"), json.getString("description"))
    override fun toString(): String = "#$name"
}