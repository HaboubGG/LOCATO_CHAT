package com.example.easychat.model

import com.google.firebase.Timestamp

class UserModel {
    @JvmField
    var phone: String? = null
    @JvmField
    var username: String? = null
    var createdTimestamp: Timestamp? = null
    @JvmField
    var userId: String? = null
    @JvmField
    var fcmToken: String? = null

    constructor()
    constructor(phone: String?, username: String?, createdTimestamp: Timestamp?, userId: String?) {
        this.phone = phone
        this.username = username
        this.createdTimestamp = createdTimestamp
        this.userId = userId
    }
}