package com.example.fiberdesk_app.models

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String?
)