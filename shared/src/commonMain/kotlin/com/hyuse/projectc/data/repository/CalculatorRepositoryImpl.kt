package com.hyuse.projectc.data.repository

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore

/**
 * Firestore implementation of [CalculatorRepository].
 * Stores electricity bill calculations in the "calculators" subcollection under each user.
 */
class CalculatorRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore
) : CalculatorRepository {

    private fun calculatorsCollection(uid: String) =
        firestore.collection("users").document(uid).collection("calculators")

    override suspend fun saveElectricityBill(uid: String, result: ElectricityBillResult) {
        val docRef = if (result.id.isNotEmpty()) {
            calculatorsCollection(uid).document(result.id)
        } else {
            calculatorsCollection(uid).document
        }
        val resultWithId = result.copy(id = docRef.id)
        docRef.set(ElectricityBillResult.serializer(), resultWithId)
    }

    override suspend fun getElectricityBillHistory(uid: String): List<ElectricityBillResult> {
        return try {
            val snapshot = calculatorsCollection(uid)
                .orderBy("timestamp", Direction.DESCENDING)
                .get()
            snapshot.documents.map { doc ->
                doc.data<ElectricityBillResult>()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteElectricityBill(uid: String, resultId: String) {
        calculatorsCollection(uid).document(resultId).delete()
    }
}
