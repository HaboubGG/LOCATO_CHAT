package com.example.easychat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker

class LoginPhoneNumberActivity : AppCompatActivity() {
    lateinit var countryCodePicker: CountryCodePicker
    lateinit var phoneInput: EditText
    lateinit var sendOtpBtn: Button
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone_number)
        countryCodePicker = findViewById(R.id.login_countrycode)
        phoneInput = findViewById(R.id.login_mobile_number)
        sendOtpBtn = findViewById(R.id.send_otp_btn)
        progressBar = findViewById(R.id.login_progress_bar)
        progressBar.setVisibility(View.GONE)
        countryCodePicker.registerCarrierNumberEditText(phoneInput)
        sendOtpBtn.setOnClickListener(View.OnClickListener { v: View? ->
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid")
            }
            val intent = Intent(this@LoginPhoneNumberActivity, LoginOtpActivity::class.java)
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus())
            startActivity(intent)
        })
    }
}