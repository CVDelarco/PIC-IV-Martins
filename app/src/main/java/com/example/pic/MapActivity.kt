package com.example.pic

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import androidx.core.graphics.toColorInt

class MapActivity : AppCompatActivity() {

    private lateinit var btnfilterCurrent: MaterialButton
    private lateinit var btnfilter3Months: MaterialButton
    private lateinit var btnfilterAll: MaterialButton
    private lateinit var btnfilterType: MaterialButton

    private lateinit var filterButtons: List<MaterialButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        val fabCenter = findViewById<View>(R.id.fabCenter)
        val homeButton = findViewById<View>(R.id.btnInicio)
        val indicatorsButton = findViewById<View>(R.id.btnIndicadores)
        val mapButton = findViewById<View>(R.id.btnMapa)
        val historyButton = findViewById<View>(R.id.btnHistorico)

        btnfilterCurrent = findViewById(R.id.btnFilterCurrent)
        btnfilter3Months = findViewById(R.id.btnFilter3Months)
        btnfilterAll = findViewById(R.id.btnFilterAll)
        btnfilterType = findViewById(R.id.btnFilterType)
        filterButtons = listOf(btnfilterCurrent, btnfilter3Months, btnfilterAll, btnfilterType)

        btnfilterCurrent.setOnClickListener { selectFilter(btnfilterCurrent)}
        btnfilter3Months.setOnClickListener { selectFilter(btnfilter3Months)}
        btnfilterAll.setOnClickListener { selectFilter(btnfilterAll)}
        btnfilterType.setOnClickListener { selectFilter(btnfilterType)}

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

    private fun selectFilter(selectedButton: MaterialButton) {
        val colorActiveBg = Color.WHITE
        val colorActiveText = "#255493".toColorInt()

        val colorInactiveBg = "#33FFFFFF".toColorInt()
        val colorInactiveText = Color.WHITE

        for(button in filterButtons){
            button.backgroundTintList = ColorStateList.valueOf(colorInactiveBg)
            button.setTextColor(colorInactiveText)
            button.typeface = Typeface.create(button.typeface, Typeface.NORMAL)

            selectedButton.backgroundTintList = ColorStateList.valueOf(colorActiveBg)
            selectedButton.setTextColor(colorActiveText)
            selectedButton.typeface = Typeface.create(selectedButton.typeface, Typeface.BOLD)
        }
    }
}