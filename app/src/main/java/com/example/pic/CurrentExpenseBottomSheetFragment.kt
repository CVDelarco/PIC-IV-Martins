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
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.pic.model.LocationData
import com.example.pic.model.Transaction
import com.example.pic.model.TransactionType
import com.example.pic.repository.FinanceRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
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

        val repository = FinanceRepository()

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
        view.findViewById<View>(R.id.btnSaveExpense)?.setOnClickListener {

            val inputText = view.findViewById<EditText>(R.id.etValue)?.text.toString()
            val cleanString = inputText.replace("\\D".toRegex(), "")
            val cleanAmount = if (cleanString.isNotEmpty()) {
                cleanString.toDouble() / 100.0
            } else {
                0.0
            }

            val toggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupPayment)
            val checkedButtonId = toggleGroup?.checkedButtonId ?: View.NO_ID
            val selectedPaymentMethod = if (checkedButtonId != View.NO_ID) {
                view.findViewById<MaterialButton>(checkedButtonId)?.text.toString()
            } else {
                "Não informado"
            }

            val currentYearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

            val novaDespesa = Transaction(
                type = TransactionType.EXPENSE.name,
                amount = cleanAmount,
                installments = installmentCount,
                installmentValue = cleanAmount / installmentCount,
                description = view.findViewById<EditText>(R.id.etDescription)?.text.toString(),
                category = view.findViewById<AutoCompleteTextView>(R.id.actType)?.text.toString(),
                subcategory = view.findViewById<AutoCompleteTextView>(R.id.actSubtype)?.text.toString(),
                paymentMethod = selectedPaymentMethod,
                yearMonth = currentYearMonth,
                location = LocationData(
                    address = "Av. Paulista, 1578 – Bela Vista, São Paulo",
                    neighborhood = "Bela Vista",
                    coordinates = GeoPoint(-23.5614, -46.6559)
                )
            )

            // Dispara a coroutine no ciclo de vida do Fragment
            lifecycleScope.launch {
                val result = repository.saveTransaction(novaDespesa)
                result.onSuccess {
                    Toast.makeText(requireContext(), "Despesa salva com sucesso!", Toast.LENGTH_SHORT).show()
                    dismiss()
                }.onFailure { erro ->
                    Toast.makeText(requireContext(), "Erro ao salvar: ${erro.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
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