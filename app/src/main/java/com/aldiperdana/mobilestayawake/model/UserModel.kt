package com.aldiperdana.mobilestayawake.model

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)