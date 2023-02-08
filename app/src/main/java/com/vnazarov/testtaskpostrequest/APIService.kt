package com.vnazarov.testtaskpostrequest

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @POST("ServiceDemonstrator/api.php")
    fun sendUserFeedback(
        @Field("text") name: String,
        @Field("checkbox1") check1: Int,
        @Field("checkbox2") check2: Int,
        @Field("checkbox3") check3: Int,
        @Field("mode") radio: String,
        @Field("selector") option: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("ServiceDemonstrator/api.php")
    fun sendUserFeedback(
        @FieldMap map: Map<String, Any>
    ): Call<ResponseBody>
}