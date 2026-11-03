package com.example.pic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.pic.model.LocationData
import com.example.pic.model.Transaction
import com.example.pic.model.TransactionType
import com.example.pic.repository.FinanceRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewExpenseBottomSheetFragment : BottomSheetDialogFragment() {

    private val localeBR = Locale("pt", "BR")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeBR)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", localeBR).apply {
        // MaterialDatePicker returns UTC timestamps by default
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_new_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etDate = view.findViewById<TextInputEditText>(R.id.etDate)
        val tilDate = view.findViewById<TextInputLayout>(R.id.tilDate)
        val etTime = view.findViewById<TextInputEditText>(R.id.etTime)
        val etValue = view.findViewById< EditText>(R.id.etValue)

        val dateFormatDate = SimpleDateFormat("dd/MM/yyyy", localeBR)
        val dateFormatTime = SimpleDateFormat("HH:mm", localeBR)

        val currentDate = Date()

        val repository = FinanceRepository()

        etDate?.setText(dateFormatDate.format(currentDate))
        etTime?.setText(dateFormatTime.format(currentDate))

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

                    etValue.addTextChangedListener(this)
                }
            }
        })

        etTime?.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                // 1. Remove qualquer caractere que não seja número
                var cleanString = s.toString().replace("[^\\d]".toRegex(), "")

                // 2. Limita a 4 dígitos máximos (HHmm)
                if (cleanString.length > 4) {
                    cleanString = cleanString.substring(0, 4)
                }

                // 3. Valida a hora (não pode ser maior que 23)
                if (cleanString.length >= 2) {
                    val hour = cleanString.substring(0, 2).toIntOrNull() ?: 0
                    if (hour > 23) {
                        cleanString = "23" + cleanString.substring(2)
                    }
                }

                // 4. Valida os minutos (não pode ser maior que 59)
                if (cleanString.length == 4) {
                    val minute = cleanString.substring(2, 4).toIntOrNull() ?: 0
                    if (minute > 59) {
                        cleanString = cleanString.substring(0, 2) + "59"
                    }
                }

                // 5. Insere os dois-pontos (:) automaticamente após o segundo dígito
                val formattedString = if (cleanString.length > 2) {
                    "${cleanString.substring(0, 2)}:${cleanString.substring(2)}"
                } else {
                    cleanString
                }

                etTime.setText(formattedString)
                etTime.setSelection(formattedString.length) // Mantém o cursor no final

                isUpdating = false
            }
        })

        val openDatePickerListener = View.OnClickListener {
            showDatePicker(etDate)
        }
        etDate?.setOnClickListener(openDatePickerListener)
        tilDate?.setStartIconOnClickListener(openDatePickerListener)

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
                description = view.findViewById<EditText>(R.id.etDescription)?.text.toString(),
                category = view.findViewById<AutoCompleteTextView>(R.id.actType)?.text.toString(),
                subcategory = view.findViewById<AutoCompleteTextView>(R.id.actSubtype)?.text.toString(),
                paymentMethod = selectedPaymentMethod,
                yearMonth = currentYearMonth,
                location = LocationData(
                    address = "Rua Augusta, 900 - Consolação, São Paulo",
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

    private fun showDatePicker(etDate: TextInputEditText?) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTheme(R.style.CustomMaterialDatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectionMillis ->
            val formattedDate = dateFormat.format(Date(selectionMillis))
            etDate?.setText(formattedDate)
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
}