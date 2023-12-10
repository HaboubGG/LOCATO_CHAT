package com.example.easychat

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easychat.adapter.SearchUserRecyclerAdapter
import com.example.easychat.model.UserModel
import com.example.easychat.utils.FirebaseUtil.allUserCollectionReference
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchUserActivity : AppCompatActivity() {
    lateinit var searchInput: EditText
    lateinit var searchButton: ImageButton
    lateinit var backButton: ImageButton
    var recyclerView: RecyclerView? = null
    var adapter: SearchUserRecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
        searchInput = findViewById(R.id.seach_username_input)
        searchButton = findViewById(R.id.search_user_btn)
        backButton = findViewById(R.id.back_btn)
        recyclerView = findViewById(R.id.search_user_recycler_view)
        searchInput.requestFocus()
        backButton.setOnClickListener(View.OnClickListener { v: View? -> onBackPressed() })
        searchButton.setOnClickListener(View.OnClickListener { v: View? ->
            val searchTerm = searchInput.getText().toString()
            if (searchTerm.isEmpty() || searchTerm.length < 3) {
                searchInput.setError("Invalid Username")
            }
            setupSearchRecyclerView(searchTerm)
        })


    }

    fun setupSearchRecyclerView(searchTerm: String) {
        val query = allUserCollectionReference()
            .whereGreaterThanOrEqualTo("username", searchTerm)
            .whereLessThanOrEqualTo("username", searchTerm + '\uf8ff')
        val options = FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query, UserModel::class.java).build()
        adapter = SearchUserRecyclerAdapter(options, applicationContext)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = adapter
        adapter!!.startListening()
    }

    override fun onStart() {
        super.onStart()
        if (adapter != null) adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) adapter!!.stopListening()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) adapter!!.startListening()
    }
}