package com.example.pic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewExpenseBottomSheetFragment : BottomSheetDialogFragment() {

    private val localeBR = Locale("pt", "BR")

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

        val dateFormatDate = SimpleDateFormat("dd/MM/yyyy", localeBR)
        val dateFormatTime = SimpleDateFormat("HH:mm", localeBR)

        val currentDate = Date()

        etDate?.setText(dateFormatDate.format(currentDate))
        etTime?.setText(dateFormatTime.format(currentDate))

        view.findViewById<View>(R.id.btnClose)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnCancel)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnSaveExpense)?.setOnClickListener { dismiss() }
    }
}