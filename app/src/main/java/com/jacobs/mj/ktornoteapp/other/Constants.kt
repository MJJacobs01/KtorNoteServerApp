package com.jacobs.mj.ktornoteapp.other

/**
 * Created by mj on 2021/03/06 at 8:59 AM
 */
object Constants {

    const val notesDatabaseName = "notesDB"

    const val BASE_URL = "http://10.0.2.2:8001"   //  10.0.2.2 (Running emulator) 10.0.0.106 (Running on device

    //  When using emulator on WiFi, use the IPv4 address for the computer where the server is running

    val IGNORE_AUTH_URLS = listOf("/login", "/register")        //  To be used to identify outgoing requests

    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
    const val KEY_LOGGED_IN_EMAIL = "KEY_LOGGED_IN_EMAIL"
    const val KEY_PASSWORD = "KEY_PASSWORD"
}