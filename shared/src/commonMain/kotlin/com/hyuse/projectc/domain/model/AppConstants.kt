package com.hyuse.projectc.domain.model

/**
 * A central list of supported currencies across the application.
 */
object AppConstants {
    val currencies = listOf(
        "₱ (Philippines)" to "₱",
        "$ (USA)" to "$",
        "€ (Eurozone)" to "€",
        "£ (UK)" to "£",
        "¥ (Japan)" to "¥",
        "₩ (South Korea)" to "₩"
    )

    /**
     * Helper to format double values to currency strings.
     * Replaces Android's NumberFormat for consistent KMP behavior.
     */
    fun formatCurrency(amount: Double, symbol: String): String {
        // Simple decimal formatting for common usage.
        // For production, a more robust library might be used.
        val formatted = if (amount % 1 == 0.0) {
            amount.toLong().toString()
        } else {
            // Manual formatting to 2 decimals for KMP common code
            val parts = amount.toString().split(".")
            val integerPart = parts[0]
            val decimalPart = if (parts.size > 1) {
                if (parts[1].length >= 2) parts[1].substring(0, 2)
                else parts[1].padEnd(2, '0')
            } else {
                "00"
            }
            "$integerPart.$decimalPart"
        }
        return "$symbol$formatted"
    }
}
