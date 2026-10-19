package com.example.pic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewIncomeBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_new_income, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnClose)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnCancel)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnSaveIncome)?.setOnClickListener { dismiss() }
    }
}