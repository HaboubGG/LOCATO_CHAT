package com.example.easychat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.easychat.model.UserModel
import com.example.easychat.utils.AndroidUtil.setProfilePic
import com.example.easychat.utils.AndroidUtil.showToast
import com.example.easychat.utils.FirebaseUtil.currentProfilePicStorageRef
import com.example.easychat.utils.FirebaseUtil.currentUserDetails
import com.example.easychat.utils.FirebaseUtil.logout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.UploadTask

class ProfileFragment : Fragment() {
    lateinit var profilePic: ImageView
    var usernameInput: EditText? = null
    var phoneInput: EditText? = null
    lateinit var updateProfileBtn: Button
    var progressBar: ProgressBar? = null
    lateinit var logoutBtn: TextView
    var currentUserModel: UserModel? = null
    var imagePickLauncher: ActivityResultLauncher<Intent>? = null
    var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.getResultCode() == Activity.RESULT_OK) {
                val data: Intent? = result.getData()
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData()
                    setProfilePic(getContext(), selectedImageUri, profilePic)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profilePic = view.findViewById(R.id.profile_image_view)
        usernameInput = view.findViewById(R.id.profile_username)
        phoneInput = view.findViewById(R.id.profile_phone)
        updateProfileBtn = view.findViewById(R.id.profle_update_btn)
        progressBar = view.findViewById(R.id.profile_progress_bar)
        logoutBtn = view.findViewById(R.id.logout_btn)
        userData
        updateProfileBtn.setOnClickListener(View.OnClickListener { v: View? -> updateBtnClick() })
        logoutBtn.setOnClickListener(View.OnClickListener { v: View? ->
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        logout()
                        val intent = Intent(context, SplashActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                })
        })
        profilePic.setOnClickListener(View.OnClickListener { v: View? ->
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent { intent: Intent ->
                    imagePickLauncher!!.launch(intent)
                    null
                }
        })
        return view
    }

    fun updateBtnClick() {
        val newUsername = usernameInput!!.text.toString()
        if (newUsername.isEmpty() || newUsername.length < 3) {
            usernameInput!!.error = "Username length should be at least 3 chars"
            return
        }
        currentUserModel!!.username = newUsername
        setInProgress(true)
        if (selectedImageUri != null) {
            currentProfilePicStorageRef.putFile(selectedImageUri!!)
                .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?>? -> updateToFirestore() }
        } else {
            updateToFirestore()
        }
    }

    fun updateToFirestore() {
        currentUserDetails().set(currentUserModel!!)
            .addOnCompleteListener { task: Task<Void?> ->
                setInProgress(false)
                if (task.isSuccessful()) {
                    showToast(getContext(), "Updated successfully")
                } else {
                    showToast(getContext(), "Updated failed")
                }
            }
    }

    val userData: Unit
        get() {
            setInProgress(true)
            currentProfilePicStorageRef.downloadUrl
                .addOnCompleteListener { task: Task<Uri?> ->
                    if (task.isSuccessful()) {
                        val uri: Uri? = task.getResult()
                        setProfilePic(getContext(), uri, profilePic)
                    }
                }
            currentUserDetails().get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
                setInProgress(false)
                currentUserModel = task.getResult().toObject(UserModel::class.java)
                usernameInput!!.setText(currentUserModel!!.username)
                phoneInput!!.setText(currentUserModel!!.phone)
            }
        }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar!!.visibility = View.VISIBLE
            updateProfileBtn!!.visibility = View.GONE
        } else {
            progressBar!!.visibility = View.GONE
            updateProfileBtn!!.visibility = View.VISIBLE
        }
    }
}