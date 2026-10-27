package com.example.pic

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class IndicatorsActivity : AppCompatActivity() {

    private lateinit var adapter: CategoryAdapter
    private var isDescending = true
    private var currentYearMonth: YearMonth = YearMonth.now()
    private lateinit var tvDetailsTitle: TextView

    // Lista original com todos os dados
    private val allCategories = listOf(
        Category("Moradia", 1200.0, 51, "#8B3214"),
        Category("Alimentação", 4000.0, 23, "#D97724"),
        Category("Transporte", 392.0, 17, "#DC8A43"),
        Category("Lazer", 225.0, 10, "#E6B36A")
    )
    val localeBR = Locale("pt", "BR")
    private val monthYearFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMMM 'de' yyyy", localeBR)
    val dateFormat = SimpleDateFormat("MMMM 'de' yyyy", localeBR)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indicators)

        tvDetailsTitle = findViewById(R.id.tvDetailsTitle)
        val homeButton = findViewById<View>(R.id.homeButton)
        val indicatorsButton = findViewById<View>(R.id.indicatorsButton)
        val mapButton = findViewById<View>(R.id.mapButton)
        val historyButton = findViewById<View>(R.id.HistoricoButton)
        val fabCenter = findViewById<View>(R.id.fabCenter)
        val tvMonthYear = findViewById<TextView>(R.id.tvMonthYear)
        val btnPrevMonth = findViewById<ImageView>(R.id.btnPrevMonth)
        val btnNextMonth = findViewById<ImageView>(R.id.btnNextMonth)
        val formattedDate = dateFormat.format(Date()).replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeBR) else it.toString() }

        val recyclerView = findViewById<RecyclerView>(R.id.rvCategories)
        adapter = CategoryAdapter(getSortedCategories())
        recyclerView.adapter = adapter

        adapter.submitList(getSortedCategories())
        updateTitleText(tvDetailsTitle)

        tvDetailsTitle.setOnClickListener {
            isDescending = !isDescending
            adapter.submitList(getSortedCategories())
            updateTitleText(tvDetailsTitle)
        }

        // Exibe a data inicial no tvMonthYear
        updateMonthYearDisplay(tvMonthYear)

        //Clique para Voltar 1 Mês
        btnPrevMonth?.setOnClickListener {
            currentYearMonth = currentYearMonth.minusMonths(1)
            updateMonthYearDisplay(tvMonthYear)
            onMonthChanged()
        }

        //Clique para Avançar 1 Mês
        btnNextMonth?.setOnClickListener {
            if(currentYearMonth < YearMonth.now()){
                    currentYearMonth = currentYearMonth.plusMonths(1)
                    updateMonthYearDisplay(tvMonthYear)
                    onMonthChanged()
                }
        }

        tvMonthYear?.text = formattedDate
        homeButton?.setOnClickListener { showHomeActivity() }
        fabCenter?.setOnClickListener { showCurrentExpenseBottomSheet() }
        mapButton?.setOnClickListener { showMapActivity() }
        historyButton?.setOnClickListener { showHistoryActivity() }

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

    private fun showMapActivity() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    private fun showHistoryActivity(){
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun showCurrentExpenseBottomSheet() {
        val bottomSheet = CurrentExpenseBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "CurrentExpenseBottomSheet")
    }

    private fun updateMonthYearDisplay(tvMonthYear: TextView?) {
        val formatted = currentYearMonth.format(monthYearFormatter)
        val capitalized = formatted.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(localeBR) else it.toString()
        }
        tvMonthYear?.text = capitalized
    }

    /**
     * Método chamado sempre que o usuário muda o mês
     */
    private fun onMonthChanged() {
        getSortedCategories()

    }

    private fun getSortedCategories(): List<Category>{
        return if (isDescending){
            allCategories.sortedByDescending { it.amount }
        } else {
            allCategories.sortedBy { it.amount }
        }
    }

    private fun updateTitleText(titleView: TextView?){
        val arrow = if (isDescending) "▼" else "▲"
        titleView?.text = "Por pagamento ($arrow)"
    }
}