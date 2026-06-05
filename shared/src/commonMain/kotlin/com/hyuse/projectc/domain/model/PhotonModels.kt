package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PhotonResponse(
    val features: List<PhotonFeature>
)

@Serializable
data class PhotonFeature(
    val properties: PhotonProperties,
    val geometry: PhotonGeometry
)

@Serializable
data class PhotonProperties(
    val name: String? = null,
    val street: String? = null,
    val city: String? = null,
    val country: String? = null,
    val postcode: String? = null,
    val state: String? = null,
    val housenumber: String? = null
) {
    fun getFormattedAddress(): String {
        val parts = mutableListOf<String>()
        name?.let { parts.add(it) }
        street?.let { parts.add(it) }
        housenumber?.let { parts.add(it) }
        city?.let { parts.add(it) }
        state?.let { parts.add(it) }
        country?.let { parts.add(it) }
        return parts.distinct().joinToString(", ")
    }
}

@Serializable
data class PhotonGeometry(
    val coordinates: List<Double> // [longitude, latitude]
) {
    val latitude: Double get() = coordinates[1]
    val longitude: Double get() = coordinates[0]
}
