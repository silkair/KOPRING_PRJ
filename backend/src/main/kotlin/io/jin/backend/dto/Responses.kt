package io.jin.backend.dto

data class GeneralResponse<T>(
    val data: T?,
    val message: String,
)