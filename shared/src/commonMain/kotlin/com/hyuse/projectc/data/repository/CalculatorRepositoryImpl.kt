package com.hyuse.projectc.data.repository

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.model.WaterBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Firestore implementation of [CalculatorRepository].
 * Stores bill calculations in subcollections under each user.
 */
class CalculatorRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore
) : CalculatorRepository {

    private fun electricityCalculatorsCollection(uid: String) =
        firestore.collection("users").document(uid).collection("calculators")

    private fun waterCalculatorsCollection(uid: String) =
        firestore.collection("users").document(uid).collection("water_calculators")

    override suspend fun saveElectricityBill(uid: String, result: ElectricityBillResult) {
        val docRef = if (result.id.isNotEmpty()) {
            electricityCalculatorsCollection(uid).document(result.id)
        } else {
            electricityCalculatorsCollection(uid).document
        }
        val resultWithId = result.copy(id = docRef.id)
        docRef.set(ElectricityBillResult.serializer(), resultWithId)
    }

    override suspend fun getElectricityBillHistory(uid: String): List<ElectricityBillResult> {
        return try {
            val snapshot = electricityCalculatorsCollection(uid)
                .orderBy("timestamp", Direction.DESCENDING)
                .get()
            snapshot.documents.map { doc ->
                doc.data<ElectricityBillResult>()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun observeElectricityBillHistory(uid: String): Flow<List<ElectricityBillResult>> {
        return electricityCalculatorsCollection(uid)
            .orderBy("timestamp", Direction.DESCENDING)
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    doc.data<ElectricityBillResult>()
                }
            }
    }

    override suspend fun deleteElectricityBill(uid: String, resultId: String) {
        electricityCalculatorsCollection(uid).document(resultId).delete()
    }

    override suspend fun saveWaterBill(uid: String, result: WaterBillResult) {
        val docRef = if (result.id.isNotEmpty()) {
            waterCalculatorsCollection(uid).document(result.id)
        } else {
            waterCalculatorsCollection(uid).document
        }
        val resultWithId = result.copy(id = docRef.id)
        docRef.set(WaterBillResult.serializer(), resultWithId)
    }

    override suspend fun getWaterBillHistory(uid: String): List<WaterBillResult> {
        return try {
            val snapshot = waterCalculatorsCollection(uid)
                .orderBy("timestamp", Direction.DESCENDING)
                .get()
            snapshot.documents.map { doc ->
                doc.data<WaterBillResult>()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun observeWaterBillHistory(uid: String): Flow<List<WaterBillResult>> {
        return waterCalculatorsCollection(uid)
            .orderBy("timestamp", Direction.DESCENDING)
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    doc.data<WaterBillResult>()
                }
            }
    }

    override suspend fun deleteWaterBill(uid: String, resultId: String) {
        waterCalculatorsCollection(uid).document(resultId).delete()
    }
}
