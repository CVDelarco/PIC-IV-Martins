package com.example.pic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Evento de clique no botão "Entrar" para navegar para a HomeActivity
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        btnEntrar?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Fecha a MainActivity para não voltar à tela de login ao pressionar "Voltar"
        }
    }
}