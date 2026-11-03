package com.example.pic

import com.google.firebase.firestore.DocumentId

data class Category(
    val name: String,
    val amount: Double,
    val percentage: Int,
    val colorHex: String
)

data class ExpenseCategory(
    @DocumentId val id: String = "",
    val name: String = "",
    val subtypes: List<String> = emptyList()
)