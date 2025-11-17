package com.pth.androidapp.domain.entities


data class User(
    val id: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val age: Int?,
    val gender: String?,
    val phoneNumber: String?,
    val address: String?,
    val avatar: String?,
    val createdAt: String,
    val updatedAt: String,
)
