package com.example.pic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        val fabCenter = findViewById<View>(R.id.fabCenter)
        val homeButton = findViewById<View>(R.id.btnInicio)
        val indicatorsButton = findViewById<View>(R.id.btnIndicadores)
        val mapButton = findViewById<View>(R.id.btnMapa)
        val historyButton = findViewById<View>(R.id.btnHistorico)

        homeButton?.setOnClickListener { showHome() }
        indicatorsButton?.setOnClickListener { showIndicators() }
        fabCenter?.setOnClickListener { showCurrentExpenseBottomSheet() }
        historyButton?.setOnClickListener { showHistory() }

        mapButton?.setOnClickListener {
            // Como o usuário já está na Home, rola a página suavemente de volta para o topo
            val nestedScrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.nestedScrollView)
            nestedScrollView?.smoothScrollTo(0, 0)
        }
    }

    private fun showHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun showIndicators() {
        val intent = Intent(this, IndicatorsActivity::class.java)
        startActivity(intent)
    }

    private fun showCurrentExpenseBottomSheet() {
        val bottomSheet = CurrentExpenseBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "CurrentExpenseBottomSheet")
    }
}