package com.example.pic

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class IndicatorsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indicators)

        val homeButton = findViewById<View>(R.id.homeButton)
        val indicatorsButton = findViewById<View>(R.id.indicatorsButton)
        val fabCenter = findViewById<View>(R.id.fabCenter)

        homeButton?.setOnClickListener { showHomeActivity() }
        fabCenter?.setOnClickListener { showCurrentExpenseBottomSheet() }

        indicatorsButton?.setOnClickListener {
            // Como o usuário já está nessa activity, rola a página suavemente de volta para o topo
            val nestedScrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.nestedScrollView)
            nestedScrollView?.smoothScrollTo(0, 0)
        }
    }

    private fun showHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showCurrentExpenseBottomSheet() {
        val bottomSheet = CurrentExpenseBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "CurrentExpenseBottomSheet")
    }
}