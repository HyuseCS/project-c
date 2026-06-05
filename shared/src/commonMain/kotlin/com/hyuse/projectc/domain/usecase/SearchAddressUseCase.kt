package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.PhotonFeature
import com.hyuse.projectc.domain.repository.GeocodingRepository

class SearchAddressUseCase(
    private val repository: GeocodingRepository
) {
    suspend operator fun invoke(query: String): List<PhotonFeature> {
        return repository.searchAddress(query)
    }
}
