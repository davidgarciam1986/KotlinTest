package com.example.kotlintest.common.api

import com.example.kotlintest.common.model.Repository
import com.example.kotlintest.common.model.RepositoryDetails
import com.example.kotlintest.common.model.RepositorySearch
import com.example.kotlintest.common.model.RepositoryTree

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubApi {
    @Headers(*["Content-Type:application/json"])
    @GET("/repositories")
    fun getPublicRepositoriesSince(@Query("since") since: String): Call<MutableList<Repository>>

    @Headers(*["Content-Type:application/json"])
    @GET("/search/repositories")
    fun searchPublicRepositories(@Query("q") query: String): Call<RepositorySearch>

    @Headers(*["Content-Type:application/json"])
    @GET("/repos/{name}")
    fun getRepository(
        @Path(value = "name",
            encoded = true) name: String?,
    ): Call<RepositoryDetails>

    @Headers(*["Content-Type:application/json"])
    @GET("/repos/{name}/git/trees/{tree}")
    fun getTree(
        @Path(value = "name", encoded = true) name: String,
        @Path(value = "tree", encoded = true) tree: String,
    ): Call<RepositoryTree>

    companion object {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}