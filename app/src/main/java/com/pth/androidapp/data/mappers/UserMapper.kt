package com.pth.androidapp.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.pth.androidapp.domain.entities.User

fun FirebaseUser.toDomainUser(): User {
    return User(
        id = this.uid,
        email = this.email
    )
}