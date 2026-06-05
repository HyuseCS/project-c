package com.hyuse.projectc.data.repository

import com.hyuse.projectc.domain.model.PhotonFeature
import com.hyuse.projectc.domain.model.PhotonResponse
import com.hyuse.projectc.domain.repository.GeocodingRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GeocodingRepositoryImpl(
    private val httpClient: HttpClient
) : GeocodingRepository {
    
    override suspend fun searchAddress(query: String, limit: Int): List<PhotonFeature> {
        if (query.isBlank()) return emptyList()
        
        return try {
            val response: PhotonResponse = httpClient.get("https://photon.komoot.io/api/") {
                parameter("q", query)
                parameter("limit", limit)
            }.body()
            response.features
        } catch (e: Exception) {
            emptyList()
        }
    }
}
