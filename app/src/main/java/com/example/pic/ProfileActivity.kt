package com.example.pic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnBack = findViewById<View>(R.id.btnBack)
        val btnSave = findViewById<View>(R.id.btnSave)
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val cardUserName = findViewById<TextView>(R.id.cardUserName)
        val btnNewSubtype = findViewById<View>(R.id.btnNewSubtype)
        val chipGroupAlimentacao = findViewById<ChipGroup>(R.id.chipGroupAlimentacao)
        val chipGroupMoradia = findViewById<ChipGroup>(R.id.chipGroupMoradia)
        val chipGroupTransporte = findViewById<ChipGroup>(R.id.chipGroupTransporte)
        val chipGroupLazer = findViewById<ChipGroup>(R.id.chipGroupLazer)

        for(i in 0 until chipGroupAlimentacao.childCount){
            val chip = chipGroupAlimentacao.getChildAt(i) as? Chip

            chip?.setOnClickListener { clickedView ->
                chipGroupAlimentacao.removeView(clickedView)
            }
        }

        for(i in 0 until chipGroupMoradia.childCount){
            val chip = chipGroupMoradia.getChildAt(i) as? Chip

            chip?.setOnClickListener { clickedView ->
                chipGroupMoradia.removeView(clickedView)
            }
        }

        for(i in 0 until chipGroupTransporte.childCount){
            val chip = chipGroupTransporte.getChildAt(i) as? Chip

            chip?.setOnClickListener { clickedView ->
                chipGroupTransporte.removeView(clickedView)
            }
        }

        for(i in 0 until chipGroupLazer.childCount){
            val chip = chipGroupLazer.getChildAt(i) as? Chip

            chip?.setOnClickListener { clickedView ->
                chipGroupLazer.removeView(clickedView)
            }
        }



        updateCardInitials(tvUserName,cardUserName)

        btnNewSubtype?.setOnClickListener { showBottomSheetNewSubtype() }
        btnBack?.setOnClickListener { this.finish() }
        btnSave?.setOnClickListener { this.finish() }
    }

    private fun updateCardInitials(tvUserName: TextView?, cardUserName: TextView?) {
        val fullName = tvUserName?.text?.toString()?.trim() ?: return

        if (fullName.isEmpty()) {
            cardUserName?.text = ""
            return
        }

        val initials = fullName
            .split("\\s+".toRegex()) // Split by single or multiple spaces
            .take(2)                 // Take up to the first 2 words
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")

        cardUserName?.text = initials
    }

    private fun showBottomSheetNewSubtype() {
        val bottomSheet = NewSubtypeBottomSheetFragment()
        bottomSheet.show(supportFragmentManager, "NewSubtypeBottomSheet")
    }
}