package com.example.pic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            // Usuário já está logado, redireciona para a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val etEmail = findViewById<TextView>(R.id.etEmail)
        val etPassword = findViewById<TextView>(R.id.etPassword)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        btnEntrar?.setOnClickListener {
            val email = etEmail?.text.toString().trim()
            val password = etPassword?.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task -> if (task.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao logar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
                }
        }
    }
}