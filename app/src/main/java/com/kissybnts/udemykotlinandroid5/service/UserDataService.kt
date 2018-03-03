package com.kissybnts.udemykotlinandroid5.service

object UserDataService {
    var id = ""
    var name = ""
    var email = ""
    var avatarName = ""
    var avatarColor = ""

    override fun toString(): String {
        return "id: $id, name: $name, email: $email, avatarName: $avatarName, avatarColor: $avatarColor"
    }
}