package com.hyuse.projectc.data.repository

import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.repository.ProfileRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Firestore implementation of [ProfileRepository].
 * Stores user profiles in the "users" collection, with document ID being the UID.
 */
class ProfileRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore
) : ProfileRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun getProfile(uid: String): UserProfile? {
        return try {
            val document = usersCollection.document(uid).get()
            if (document.exists) {
                document.data<UserProfile>()
            } else {
                null
            }
        } catch (e: Exception) {
            // Log error or handle appropriately
            null
        }
    }

    override suspend fun saveProfile(profile: UserProfile) {
        usersCollection.document(profile.uid).set(UserProfile.serializer(), profile)
    }

    override fun observeProfile(uid: String): Flow<UserProfile?> {
        return usersCollection.document(uid).snapshots.map { snapshot ->
            if (snapshot.exists) {
                snapshot.data<UserProfile>()
            } else {
                null
            }
        }
    }
}
