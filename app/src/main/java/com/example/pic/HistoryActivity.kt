package com.example.pic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HistoryActivity : AppCompatActivity() {

    private var pageCnt = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        val homeButton = findViewById<View>(R.id.btnInicio)
        val btnIndicadores = findViewById<View>(R.id.btnIndicadores)
        val btnHistorico = findViewById<View>(R.id.btnHistorico)
        val fabCenter = findViewById<View>(R.id.fabCenter)
        val btnPrevious = findViewById<View>(R.id.btnPrevious)
        val btnNext = findViewById<View>(R.id.btnNext)
        val tvPaginationText = findViewById<TextView>(R.id.tvPaginationText)

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvHistory)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        
        val historyList = listOf(
            HistoryItem("Almoço", "Alimentação · Restaurante · Crédito 1x", "- R$ 38,50", true),
            HistoryItem("Supermercado", "Alimentação · Compras · Débito", "- R$ 150,00", true),
            HistoryItem("Salário", "Trabalho · Pix", "+ R$ 3.500,00", false),
            HistoryItem("Uber", "Transporte · Viagem · Pix", "- R$ 22,90", true)
        )
        recyclerView.adapter = HistoryAdapter(historyList.toMutableList())

        homeButton?.setOnClickListener { showHomeActivity() }
        btnIndicadores?.setOnClickListener { showIndicatorsActivity() }
        fabCenter?.setOnClickListener { showCurrentExpenseBottomSheet() }
        btnPrevious?.setOnClickListener { pageCntUpdate(tvPaginationText, -1) }
        btnNext?.setOnClickListener { pageCntUpdate(tvPaginationText, 1) }


        btnHistorico?.setOnClickListener {
            // Como o usuário já está na Home, rola a página suavemente de volta para o topo
            val nestedScrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.nestedScrollView)
            nestedScrollView?.smoothScrollTo(0, 0)
        }
    }

    private fun showHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showIndicatorsActivity() {
        val intent = Intent(this, IndicatorsActivity::class.java)
        startActivity(intent)
    }

    private fun showCurrentExpenseBottomSheet() {
        val bottomSheet = CurrentExpenseBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "CurrentExpenseBottomSheet")
    }

    private fun pageCntUpdate(tv: TextView?, value: Int){
        pageCnt = (pageCnt + value).coerceIn(1,6)
        tv?.text = "Página $pageCnt de 6"
    }
}

