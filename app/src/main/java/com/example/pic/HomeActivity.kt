package com.example.pic

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    val localeBR = Locale("pt", "BR")
    val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM", localeBR)
    val monthFormat = SimpleDateFormat("MMMM", localeBR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Vincula o arquivo XML da tela Home
        setContentView(R.layout.activity_home)

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvBalanceLabel = findViewById<TextView>(R.id.tvBalanceLabel)
        val cardAvatar = findViewById<View>(R.id.cardAvatar)
        val fabCenter = findViewById<View>(R.id.fabCenter)
        val currentExpenseButton = findViewById<View>(R.id.currentExpenseButton)
        val newExpenseButton = findViewById<View>(R.id.newExpenseButton)
        val newIncomeButton = findViewById<View>(R.id.newIncomeButton)
        //val historico
        val indicatorsText = findViewById<View>(R.id.indicatorsText)
        val homeButton = findViewById<View>(R.id.homeButton)
        val indicatorsButton = findViewById<View>(R.id.indicatorsButton)

        val formattedDate = dateFormat.format(Date())
            .replace("-feira", "") // Optional: removes "-feira" if present (e.g. "sexta-feira" -> "sexta")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeBR) else it.toString() }
        val formattedMonth = "Saldo de " +  monthFormat.format(Date()).replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeBR) else it.toString() }

        tvDate?.text = formattedDate
        tvBalanceLabel?.text = formattedMonth

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