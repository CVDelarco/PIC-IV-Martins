package com.example.pic.repository

import com.example.pic.model.Transaction
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class FinanceRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // Obtém a referência da subcoleção 'transactions' do usuário logado
    private fun getUserTransactionsRef() = auth.currentUser?.uid?.let { uid ->
        db.collection("users").document(uid).collection("transactions")
    } ?: throw IllegalStateException("Usuário não autenticado no Firebase.")

    /**
     * 1. CREATE: Salva um novo lançamento (Despesa ou Ganho)
     */
    suspend fun saveTransaction(transaction: Transaction): Result<String> {
        return try {
            val collectionRef = getUserTransactionsRef()
            val docRef = collectionRef.add(transaction).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 2. READ (Histórico): Busca todos os lançamentos do usuário ordenados do mais recente ao mais antigo
     */
    suspend fun getTransactionHistory(): Result<List<Transaction>> {
        return try {
            val snapshot = getUserTransactionsRef()
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val transactions = snapshot.toObjects(Transaction::class.java)
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 3. READ (Indicadores / Home): Busca lançamentos filtrados por Mês e Ano
     * Compatível diretamente com o currentYearMonth da sua IndicatorsActivity!
     */
    suspend fun getTransactionsByMonth(year: Int, month: Int): Result<List<Transaction>> {
        return try {
            val zoneId = ZoneId.systemDefault()

            // Primeiro segundo do mês (Ex: 01/09/2026 00:00:00)
            val startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0)
            val startTimestamp = Timestamp(Date.from(startOfMonth.atZone(zoneId).toInstant()))

            // Primeiro segundo do mês seguinte (Ex: 01/10/2026 00:00:00)
            val startOfNextMonth = startOfMonth.plusMonths(1)
            val endTimestamp = Timestamp(Date.from(startOfNextMonth.atZone(zoneId).toInstant()))

            val snapshot = getUserTransactionsRef()
                .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThan("timestamp", endTimestamp)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val transactions = snapshot.toObjects(Transaction::class.java)
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 4. DELETE: Exclui um lançamento (para o botão btnDelete da HistoryActivity)
     */
    suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        return try {
            getUserTransactionsRef().document(transactionId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}