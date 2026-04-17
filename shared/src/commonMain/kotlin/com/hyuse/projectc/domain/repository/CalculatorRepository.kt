package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.model.WaterBillResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for managing calculator history in Firestore.
 */
interface CalculatorRepository {
    /**
     * Saves an electricity bill calculation result for the given [uid].
     */
    suspend fun saveElectricityBill(uid: String, result: ElectricityBillResult)

    /**
     * Retrieves all electricity bill calculation history for the given [uid],
     * sorted by timestamp descending (most recent first).
     */
    suspend fun getElectricityBillHistory(uid: String): List<ElectricityBillResult>

    /**
     * Observes electricity bill calculation history for the given [uid] in real-time.
     */
    fun observeElectricityBillHistory(uid: String): Flow<List<ElectricityBillResult>>

    /**
     * Deletes a specific electricity bill calculation by [resultId] for the given [uid].
     */
    suspend fun deleteElectricityBill(uid: String, resultId: String)

    /**
     * Saves a water bill calculation result for the given [uid].
     */
    suspend fun saveWaterBill(uid: String, result: WaterBillResult)

    /**
     * Retrieves all water bill calculation history for the given [uid],
     * sorted by timestamp descending (most recent first).
     */
    suspend fun getWaterBillHistory(uid: String): List<WaterBillResult>

    /**
     * Observes water bill calculation history for the given [uid] in real-time.
     */
    fun observeWaterBillHistory(uid: String): Flow<List<WaterBillResult>>

    /**
     * Deletes a specific water bill calculation by [resultId] for the given [uid].
     */
    suspend fun deleteWaterBill(uid: String, resultId: String)
}
