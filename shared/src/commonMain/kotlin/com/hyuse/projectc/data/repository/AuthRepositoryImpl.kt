package com.hyuse.projectc.data.repository

import com.hyuse.projectc.domain.model.User
import com.hyuse.projectc.domain.repository.AuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Firebase Auth implementation of [AuthRepository].
 *
 * Uses the GitLive Firebase Kotlin SDK which works in commonMain.
 * On Android, this wraps the official Firebase Auth SDK under the hood.
 * On iOS (deferred), it would wrap the Firebase iOS Auth SDK.
 */
class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override val currentUser: Flow<User?> = firebaseAuth.authStateChanged.map { firebaseUser ->
        firebaseUser?.let {
            User(
                uid = it.uid,
                email = it.email ?: "",
                displayName = it.displayName
            )
        }
    }

    override suspend fun signUp(email: String, password: String): User {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
        val fbUser = result.user
            ?: throw IllegalStateException("Sign up succeeded but user is null")
        return User(
            uid = fbUser.uid,
            email = fbUser.email ?: "",
            displayName = fbUser.displayName
        )
    }

    override suspend fun login(email: String, password: String): User {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password)
        val fbUser = result.user
            ?: throw IllegalStateException("Login succeeded but user is null")
        return User(
            uid = fbUser.uid,
            email = fbUser.email ?: "",
            displayName = fbUser.displayName
        )
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
