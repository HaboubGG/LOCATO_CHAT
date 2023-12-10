package com.example.easychat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.easychat.model.UserModel
import com.example.easychat.utils.FirebaseUtil.currentUserDetails
import com.example.easychat.utils.FirebaseUtil.currentUserId
import com.google.firebase.Timestamp

class LoginUsernameActivity : AppCompatActivity() {
    var usernameInput: EditText? = null
    lateinit  var letMeInBtn: Button
    var progressBar: ProgressBar? = null
    var phoneNumber: String? = null
    var userModel: UserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_username)
        usernameInput = findViewById(R.id.login_username)
        letMeInBtn = findViewById(R.id.login_let_me_in_btn)
        progressBar = findViewById(R.id.login_progress_bar)
        phoneNumber = intent.extras!!.getString("phone")
        username
        letMeInBtn.setOnClickListener(View.OnClickListener { v: View? -> setUsername() })
    }

    fun setUsername() {
        val username = usernameInput!!.text.toString()
        if (username.isEmpty() || username.length < 3) {
            usernameInput!!.error = "Username length should be at least 3 chars"
            return
        }
        setInProgress(true)
        if (userModel != null) {
            userModel!!.username = username
        } else {
            userModel = UserModel(phoneNumber, username, Timestamp.now(), currentUserId())
        }
        currentUserDetails().set(userModel!!).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this@LoginUsernameActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    val username: Unit
        get() {
            setInProgress(true)
            currentUserDetails().get().addOnCompleteListener { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    userModel = task.result.toObject(UserModel::class.java)
                    if (userModel != null) {
                        usernameInput!!.setText(userModel!!.username)
                    }
                }
            }
        }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar!!.visibility = View.VISIBLE
            letMeInBtn!!.visibility = View.GONE
        } else {
            progressBar!!.visibility = View.GONE
            letMeInBtn!!.visibility = View.VISIBLE
        }
    }
}