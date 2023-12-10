package com.example.easychat.model

import com.google.firebase.Timestamp

class ChatroomModel {
    var chatroomId: String? = null
    var userIds: MutableList<String?>? = null
    @JvmField
    var lastMessageTimestamp: Timestamp? = null
    @JvmField
    var lastMessageSenderId: String? = null
    @JvmField
    var lastMessage: String? = null

    constructor()
    constructor(
        chatroomId: String?,
        userIds: MutableList<String?>,
        lastMessageTimestamp: Timestamp?,
        lastMessageSenderId: String?
    ) {
        this.chatroomId = chatroomId
        this.userIds = userIds
        this.lastMessageTimestamp = lastMessageTimestamp
        this.lastMessageSenderId = lastMessageSenderId
    }
}