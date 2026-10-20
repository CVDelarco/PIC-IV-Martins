package com.example.pic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewIncomeBottomSheetFragment : BottomSheetDialogFragment() {

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
        return inflater.inflate(R.layout.bottom_sheet_new_income, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etValue = view.findViewById<EditText>(R.id.etValue)
        val etDate = view.findViewById<TextInputEditText>(R.id.etDate)
        val tilDate = view.findViewById<TextInputLayout>(R.id.tilDate)

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

        // Pre-fill with today's date if empty
        if (etDate?.text.isNullOrEmpty()) {
            etDate?.setText(SimpleDateFormat("dd/MM/yyyy", localeBR).format(Date()))
        }

        // Listener to open date picker when tapping the input or start icon
        val openDatePickerListener = View.OnClickListener {
            showDatePicker(etDate)
        }

        etDate?.setOnClickListener(openDatePickerListener)
        tilDate?.setStartIconOnClickListener(openDatePickerListener)

        view.findViewById<View>(R.id.btnClose)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnCancel)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnSaveIncome)?.setOnClickListener { dismiss() }
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