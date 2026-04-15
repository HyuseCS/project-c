package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.model.WaterBillResult

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
     * Deletes a specific water bill calculation by [resultId] for the given [uid].
     */
    suspend fun deleteWaterBill(uid: String, resultId: String)
}
