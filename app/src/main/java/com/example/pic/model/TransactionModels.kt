package com.example.pic.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName

enum class TransactionType {
    INCOME, EXPENSE
}

data class LocationData(
    @get:PropertyName("address") @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("neighborhood") @set:PropertyName("neighborhood")
    var neighborhood: String = "",

    @get:PropertyName("coordinates") @set:PropertyName("coordinates")
    var coordinates: GeoPoint? = null
)

data class Transaction(
    @DocumentId
    val id: String = "",

    @get:PropertyName("type") @set:PropertyName("type")
    var type: String = TransactionType.EXPENSE.name, // "INCOME" ou "EXPENSE"

    @get:PropertyName("amount") @set:PropertyName("amount")
    var amount: Double = 0.0,

    @get:PropertyName("installments") @set:PropertyName("installments")
    var installments: Int = 1,

    @get:PropertyName("installment_value") @set:PropertyName("installment_value")
    var installmentValue: Double = 0.0,

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("category") @set:PropertyName("category")
    var category: String = "", // Ex: "Alimentação", "Moradia" (ou "Salário" para ganhos)

    @get:PropertyName("subcategory") @set:PropertyName("subcategory")
    var subcategory: String = "", // Ex: "Restaurante", "Aluguel"

    @get:PropertyName("payment_method") @set:PropertyName("payment_method")
    var paymentMethod: String = "", // Ex: "Crédito", "Pix", "Débito", "Dinheiro"

    @get:PropertyName("timestamp") @set:PropertyName("timestamp")
    var timestamp: Timestamp = Timestamp.now(),

    // Facilita filtrar na IndicatorsActivity usando YearMonth (Ex: "2026-09")
    @get:PropertyName("year_month") @set:PropertyName("year_month")
    var yearMonth: String = "",

    @get:PropertyName("location") @set:PropertyName("location")
    var location: LocationData? = null
)