package com.aldiperdana.mobilestayawake.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @field:SerializedName("loginResult")
    val message: String? = null,

    @field:SerializedName("message")
    val data: String? = null
)
data class LoginResult (
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)


