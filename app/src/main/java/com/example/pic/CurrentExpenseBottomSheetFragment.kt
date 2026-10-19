package com.example.pic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentExpenseBottomSheetFragment : BottomSheetDialogFragment() {

    private var installmentCount = 3
    private val localeBR = Locale("pt", "BR")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeBR)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_current_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etValue = view.findViewById<EditText>(R.id.etValue)
        val tvInstallmentsCount = view.findViewById<TextView>(R.id.tvInstallmentsCount)
        val tvInstallmentsCalc = view.findViewById<TextView>(R.id.tvInstallmentsCalc)
        val btnMinus = view.findViewById<View>(R.id.btnMinusInstallment)
        val btnPlus = view.findViewById<View>(R.id.btnPlusInstallment)
        val tvCapturedDateTime = view.findViewById<TextView>(R.id.tvCapturedDateTime)

        val dateFormat = SimpleDateFormat("dd/MM · HH:mm", localeBR)
        tvCapturedDateTime?.text = dateFormat.format(Date())

        // Set initial installment counter text
        tvInstallmentsCount?.text = "${installmentCount}x"

        // 1. Currency Formatting TextWatcher for R$ prefix
        etValue?.addTextChangedListener(object : TextWatcher{
            private var currentText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?){
                if (s.toString() != currentText){
                    etValue.removeTextChangedListener(this)

                    //Strip all non-digit characters
                    val cleanString = s.toString().replace("[^\\d]".toRegex(),"")

                    if (cleanString.isNotEmpty()){
                        val parsed = cleanString.toDouble() / 100
                        val formatted = currencyFormat.format(parsed)

                        currentText = formatted
                        etValue.setText(formatted)
                        etValue.setSelection(formatted.length)
                    } else {
                        currentText = ""
                        etValue.setText("")
                    }

                    updateInstallmentCalculation(etValue, tvInstallmentsCalc)

                    etValue.addTextChangedListener(this)
                }
            }
        })

        btnMinus?.setOnClickListener {
            if (installmentCount > 1) {
                installmentCount--
                tvInstallmentsCount?.text = "${installmentCount}x"
                updateInstallmentCalculation(etValue, tvInstallmentsCalc)
            }
        }

        btnPlus?.setOnClickListener {
            installmentCount++
            tvInstallmentsCount?.text = "${installmentCount}x"
            updateInstallmentCalculation(etValue, tvInstallmentsCalc)
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

    private fun updateInstallmentCalculation(etValue: EditText?, tvInstallmentsCalc: TextView?) {
        val rawValueString = etValue?.text?.toString()?.replace("[^\\d]".toRegex(), "") ?: ""

        if (rawValueString.isNotEmpty()) {
            val totalValue = rawValueString.toDouble() / 100
            val installmentValue = totalValue / installmentCount
            val formattedInstallment = currencyFormat.format(installmentValue)

            tvInstallmentsCalc?.text = "(${installmentCount}x $formattedInstallment)"
        } else {
            tvInstallmentsCalc?.text = ""
        }
    }
}