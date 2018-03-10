package com.kissybnts.udemykotlinandroid5.utils

private const val BASE_URL = "https://udemykotlinandroid.herokuapp.com/v1"

const val URL_REGISTER = "$BASE_URL/account/register"
const val URL_LOGIN = "$BASE_URL/account/login"
const val URL_CREATE_USER = "$BASE_URL/user/add"
fun URL_FIND_USER_BY_EMIL(email: String): String =  "$BASE_URL/user/byEmail/$email"
const val URL_GET_CHANNELS = "$BASE_URL/channel"
fun URL_GET_MESSAGES(channelId: String) = "$BASE_URL/message/byChannel/$channelId"
const val URL_SOCKET = "https://udemykotlinandroid.herokuapp.com"

const val BROADCAST_USER_DATA_CHANGE = "broadcast/userDataChange"

const val EMIT_ADD_CHANNEL = "newChannel"
const val EMIT_ADD_MESSAGE = "newMessage"
const val EMIT_CHANNEL_CREATED = "channelCreated"
const val EMIT_MESSAGE_CREATED = "messageCreated"

const val CONTENT_TYPE = "application/json; charset=utf-8"