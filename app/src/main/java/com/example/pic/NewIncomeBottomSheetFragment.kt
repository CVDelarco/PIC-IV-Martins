package com.example.pic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewIncomeBottomSheetFragment : BottomSheetDialogFragment() {

    private val localeBR = Locale("pt", "BR")
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

        val etDate = view.findViewById<TextInputEditText>(R.id.etDate)
        val tilDate = view.findViewById<TextInputLayout>(R.id.tilDate)

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
            .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectionMillis ->
            val formattedDate = dateFormat.format(Date(selectionMillis))
                etDate?.setText(formattedDate)
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
}