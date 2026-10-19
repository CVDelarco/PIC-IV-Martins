package com.example.pic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrentExpenseBottomSheetFragment : BottomSheetDialogFragment() {

    private var installmentCount = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_current_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvInstallmentsCount = view.findViewById<TextView>(R.id.tvInstallmentsCount)
        val btnMinus = view.findViewById<View>(R.id.btnMinusInstallment)
        val btnPlus = view.findViewById<View>(R.id.btnPlusInstallment)

        btnMinus?.setOnClickListener {
            if (installmentCount > 1) {
                installmentCount--
                tvInstallmentsCount?.text = "${installmentCount}x"
            }
        }

        btnPlus?.setOnClickListener {
            installmentCount++
            tvInstallmentsCount?.text = "${installmentCount}x"
        }

        val types = listOf("Alimentação", "Moradia", "Transporte", "Lazer")
        val subtypes = listOf("Restaurante", "Bar", "Lanchonete", "Mercado")

        val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, types)
        val subtypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subtypes)

        view.findViewById<AutoCompleteTextView>(R.id.actType)?.setAdapter(typeAdapter)
        view.findViewById<AutoCompleteTextView>(R.id.actSubtype)?.setAdapter(subtypeAdapter)

        view.findViewById<View>(R.id.btnClose)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnCancel)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnSaveExpense)?.setOnClickListener { dismiss() }
    }
}