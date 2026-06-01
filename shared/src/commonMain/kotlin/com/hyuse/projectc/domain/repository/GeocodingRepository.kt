package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.PhotonFeature

interface GeocodingRepository {
    suspend fun searchAddress(query: String, limit: Int = 5): List<PhotonFeature>
}
