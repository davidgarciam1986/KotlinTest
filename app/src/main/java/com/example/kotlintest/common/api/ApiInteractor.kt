package com.example.kotlintest.common.api

import com.example.kotlintest.common.model.Repository
import com.example.kotlintest.common.model.RepositorySearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiInteractor {

    interface onGetPublicRepositoriesSinceFinishedListener {
        fun onGetPublicRepositoriesSinceFail()
        fun onGetPublicRepositoriesSinceSuccess(list: MutableList<Repository>)
    }

    interface onSearchPublicRepositoriesFinishedListener {
        fun onSearchPublicRepositoriesFail()
        fun onSearchPublicRepositoriesSuccess(list: RepositorySearch)
    }

    fun getPublicRepositoriesSince(
        since: Int,
        listener: onGetPublicRepositoriesSinceFinishedListener)
    {
        val githubApi = GithubApi.retrofit.create(GithubApi::class.java)
        val call: Call<MutableList<Repository>> = githubApi.getPublicRepositoriesSince(since.toString())
        call.enqueue(object : Callback<MutableList<Repository>> {
            override fun onResponse(
                call: Call<MutableList<Repository>>,
                response: Response<MutableList<Repository>>,
            ) {
                val body = response.body()
                if (body != null) {
                    listener.onGetPublicRepositoriesSinceSuccess(body)
                }

            }

            override fun onFailure(call: Call<MutableList<Repository>>, t: Throwable) {
                listener.onGetPublicRepositoriesSinceFail()
            }
        })
    }

    fun searchPublicRepositories(
        query: String?,
        listener: onSearchPublicRepositoriesFinishedListener,
    ) {
        val githubApi = GithubApi.retrofit.create(GithubApi::class.java)
        val call: Call<RepositorySearch> = githubApi.searchPublicRepositories(query!!)
        call.enqueue(object : Callback<RepositorySearch?> {
            override fun onResponse(
                call: Call<RepositorySearch?>,
                response: Response<RepositorySearch?>,
            ) {
                val body = response.body()
                if (body != null) {
                    listener.onSearchPublicRepositoriesSuccess(body)
                }
            }

            override fun onFailure(call: Call<RepositorySearch?>, t: Throwable) {
                listener.onSearchPublicRepositoriesFail()
            }
        })
    }

}
