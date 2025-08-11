package com.pth.androidapp.base.models

import com.google.firebase.Timestamp
import java.util.UUID

open class BaseModel(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)