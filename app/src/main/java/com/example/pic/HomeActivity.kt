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

        // =========================================================
        // 1. INICIALIZAÇÃO DAS VIEWS (findViewById)
        // =========================================================

        // Header / Topo
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvBalanceLabel = findViewById<TextView>(R.id.tvBalanceLabel)
        val cardAvatar = findViewById<View>(R.id.cardAvatar)

        // Ações Rápidas / Bottom Sheets
        val fabCenter = findViewById<View>(R.id.fabCenter)
        val currentExpenseButton = findViewById<View>(R.id.currentExpenseButton)
        val newExpenseButton = findViewById<View>(R.id.newExpenseButton)
        val newIncomeButton = findViewById<View>(R.id.newIncomeButton)

        // Seção de Histórico
        val historicoLayout = findViewById<View>(R.id.HistoricoLayout)
        val btnSeeAll = findViewById<View>(R.id.btnSeeAll)
        val newHistoryButton = findViewById<View>(R.id.newHistoryButton)

        // Seção de Indicadores
        val indicatorsText = findViewById<View>(R.id.indicatorsText)
        val indicatorsButton = findViewById<View>(R.id.indicatorsButton)

        // Navegação (Menu Inferior)
        val homeButton = findViewById<View>(R.id.homeButton)
        val mapButton = findViewById<View>(R.id.mapButton)
        // 2. CONFIGURAÇÃO DE DADOS E TEXTOS


        val formattedDate = dateFormat.format(Date())
            .replace("-feira", "") // Optional: removes "-feira" if present (e.g. "sexta-feira" -> "sexta")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeBR) else it.toString() }

        val formattedMonth = "Saldo de " +  monthFormat.format(Date())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeBR) else it.toString() }

        tvDate?.text = formattedDate
        tvBalanceLabel?.text = formattedMonth


        // 3. LISTENERS DE CLIQUE

        // Perfil
        cardAvatar?.setOnClickListener { showProfile() }

        // Bottom Sheets (Ações de Novo Registro)
        fabCenter?.setOnClickListener { showCurrentExpenseBottomSheet() }
        currentExpenseButton?.setOnClickListener { showCurrentExpenseBottomSheet() }
        newExpenseButton?.setOnClickListener { showNewExpenseBottomSheet() }
        newIncomeButton?.setOnClickListener { showNewIncomeBottomSheet() }

        // Navegação para Histórico
        historicoLayout?.setOnClickListener { showHistory() }
        btnSeeAll?.setOnClickListener { showHistory() }
        newHistoryButton?.setOnClickListener { showHistory() }

        // Navegação para Indicadores
        indicatorsText?.setOnClickListener { showIndicators() }
        indicatorsButton?.setOnClickListener { showIndicators() }

        //Navegação para Mapa
        mapButton?.setOnClickListener { showMap() }

        // Navegação Home (Scroll para o topo)
        homeButton?.setOnClickListener {
            // Como o usuário já está na Home, rola a página suavemente de volta para o topo
            val nestedScrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.nestedScrollView)
            nestedScrollView?.smoothScrollTo(0, 0)
        }
    }

    // MÉTODOS DE BOTTOM SHEETS

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

    // MÉTODOS DE NAVEGAÇÃO (INTENTS)

    private fun showProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun showIndicators() {
        val intent = Intent(this, IndicatorsActivity::class.java)
        startActivity(intent)
    }

    private fun showMap() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }


    private fun showHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}