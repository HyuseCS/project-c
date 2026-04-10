package com.hyuse.projectc.domain.model

data class ElectricityAppliance(
    val id: String,
    val name: String,
    val wattage: Double,
    val hoursPerDay: Double,
    val daysPerMonth: Int,
    val quantity: Int
) {
    /**
     * Calculates the estimated monthly power consumption for this appliance in kWh.
     * Formula: (Watts × Hours per Day × Days per Month × Quantity) / 1000
     */
    val monthlyKwh: Double
        get() = (wattage * quantity * hoursPerDay * daysPerMonth) / 1000.0
}
