package com.aldiperdana.mobilestayawake.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: LoginResult,

    )
data class LoginResult (
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String
)


