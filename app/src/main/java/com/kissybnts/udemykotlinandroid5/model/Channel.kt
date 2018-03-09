package com.kissybnts.udemykotlinandroid5.model

class Channel(val id: String, val name: String, val description: String) {
    constructor(args: Array<out Any>): this(args[2] as String, args[0] as String, args[1] as String)
    override fun toString(): String = "# $name"
}