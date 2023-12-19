package com.aldiperdana.mobilestayawake.data

import com.aldiperdana.mobilestayawake.response.LoginResponse
import com.aldiperdana.mobilestayawake.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.Date

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("registerUser")
    suspend fun register(
        @Field("nama")nama: String,
        @Field("tempatLahir") tempatLahir: String,
        @Field("tanggalLahir") tanggalLahir: Date,
        @Field("golDarah") golDarah: String,
        @Field("jenisKelamin") jenisKelamin: String,
        @Field("pekerjaan") pekerjaan: String,
        @Field("alamat") alamat: String,
        @Field("noTelepon") noTelepon: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("kodeReferral") kodeReferral: String
    ): RegisterResponse

}