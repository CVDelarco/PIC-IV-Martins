package com.example.pic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewExpenseBottomSheetFragment : BottomSheetDialogFragment() {

    private val localeBR = Locale("pt", "BR")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeBR)

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
        val etTime = view.findViewById<TextInputEditText>(R.id.etTime)
        val etValue = view.findViewById< EditText>(R.id.etValue)

        val dateFormatDate = SimpleDateFormat("dd/MM/yyyy", localeBR)
        val dateFormatTime = SimpleDateFormat("HH:mm", localeBR)

        val currentDate = Date()

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

        view.findViewById<View>(R.id.btnClose)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnCancel)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnSaveExpense)?.setOnClickListener { dismiss() }
    }
}