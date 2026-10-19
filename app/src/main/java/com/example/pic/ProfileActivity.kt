package com.example.pic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnBack = findViewById<View>(R.id.btnBack)

        btnBack?.setOnClickListener { this.finish() }
    }
}