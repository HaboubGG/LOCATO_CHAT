package com.example.easychat.utils

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat

object FirebaseUtil {
    @JvmStatic
    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    @JvmStatic
    val isLoggedIn: Boolean
        get() = if (currentUserId() != null) {
            true
        } else false

    @JvmStatic
    fun currentUserDetails(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId()!!)
    }

    @JvmStatic
    fun allUserCollectionReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("users")
    }

    @JvmStatic
    fun getChatroomReference(chatroomId: String?): DocumentReference {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId!!)
    }

    @JvmStatic
    fun getChatroomMessageReference(chatroomId: String?): CollectionReference {
        return getChatroomReference(chatroomId).collection("chats")
    }

    @JvmStatic
    fun getChatroomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) {
            userId1 + "_" + userId2
        } else {
            userId2 + "_" + userId1
        }
    }

    @JvmStatic
    fun allChatroomCollectionReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("chatrooms")
    }

    fun getOtherUserFromChatroom(userIds: MutableList<String?>): DocumentReference {
        return if (userIds[0] == currentUserId()) {
            allUserCollectionReference().document(userIds[1].toString())
        } else {
            allUserCollectionReference().document(userIds[0].toString())
        }
    }

    fun timestampToString(timestamp: Timestamp): String {
        return SimpleDateFormat("HH:MM").format(timestamp.toDate())
    }
    @JvmStatic
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
    @JvmStatic
    val currentProfilePicStorageRef: StorageReference
        get() = FirebaseStorage.getInstance().reference.child("profile_pic")
            .child(currentUserId()!!)

    @JvmStatic
    fun getOtherProfilePicStorageRef(otherUserId: String?): StorageReference {
        return FirebaseStorage.getInstance().reference.child("profile_pic")
            .child(otherUserId!!)
    }
}