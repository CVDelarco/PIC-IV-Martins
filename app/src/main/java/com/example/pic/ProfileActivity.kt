package com.example.pic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnBack = findViewById<View>(R.id.btnBack)
        val btnSave = findViewById<View>(R.id.btnSave)
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val cardUserName = findViewById<TextView>(R.id.cardUserName)

        updateCardInitials(tvUserName,cardUserName)

        btnBack?.setOnClickListener { this.finish() }
        btnSave?.setOnClickListener { this.finish() }
    }

    private fun updateCardInitials(tvUserName: TextView?, cardUserName: TextView?) {
        val fullName = tvUserName?.text?.toString()?.trim() ?: return

        if (fullName.isEmpty()) {
            cardUserName?.text = ""
            return
        }

        val initials = fullName
            .split("\\s+".toRegex()) // Split by single or multiple spaces
            .take(2)                 // Take up to the first 2 words
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")

        cardUserName?.text = initials
    }
}