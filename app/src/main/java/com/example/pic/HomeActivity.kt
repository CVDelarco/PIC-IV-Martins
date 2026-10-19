package com.example.pic

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import android.content.Intent

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Vincula o arquivo XML da tela Home
        setContentView(R.layout.activity_home)

        val cardAvatar = findViewById<View>(R.id.cardAvatar)
        val fabCenter = findViewById<View>(R.id.fabCenter)
        val currentExpenseButton = findViewById<View>(R.id.currentExpenseButton)
        val newExpenseButton = findViewById<View>(R.id.newExpenseButton)
        val newIncomeButton = findViewById<View>(R.id.newIncomeButton)
        //val historico
        val indicatorsText = findViewById<View>(R.id.indicatorsText)
        val homeButton = findViewById<View>(R.id.homeButton)
        val indicatorsButton = findViewById<View>(R.id.indicatorsButton)

        cardAvatar?.setOnClickListener { showProfile() }
        currentExpenseButton?.setOnClickListener { showCurrentExpenseBottomSheet() }
        newExpenseButton?.setOnClickListener { showNewExpenseBottomSheet() }
        newIncomeButton?.setOnClickListener { showNewIncomeBottomSheet() }
        //new historico
        indicatorsButton?.setOnClickListener { showIndicatorsActivity() }
        fabCenter?.setOnClickListener { showCurrentExpenseBottomSheet() }
        indicatorsText?.setOnClickListener { showIndicatorsActivity() }

        homeButton?.setOnClickListener {
            // Como o usuário já está na Home, rola a página suavemente de volta para o topo
            val nestedScrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.nestedScrollView)
            nestedScrollView?.smoothScrollTo(0, 0)
        }
    }

    private fun showCurrentExpenseBottomSheet() {
        val bottomSheet = CurrentExpenseBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "CurrentExpenseBottomSheet")
    }

    private fun showNewExpenseBottomSheet() {
        val bottomSheet = NewExpenseBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "NewExpenseBottomSheet")
    }

    private fun showNewIncomeBottomSheet() {
        val bottomSheet = NewIncomeBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "NewIncomeBottomSheet")
    }

    private fun showProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
    private fun showIndicatorsActivity() {
        val intent = Intent(this, IndicatorsActivity::class.java)
        startActivity(intent)
    }
}