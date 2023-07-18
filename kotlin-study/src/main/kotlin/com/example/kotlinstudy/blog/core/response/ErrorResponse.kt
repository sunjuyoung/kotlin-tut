package com.example.kotlinstudy.blog.core.response

data class ErrorResponse (
    val message: String,
    val errorType: String = "Invalid Argument"
        )