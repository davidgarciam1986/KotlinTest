package com.example.kotlintest.common.api

import com.example.kotlintest.common.model.Repository
import com.example.kotlintest.common.model.RepositoryDetails
import com.example.kotlintest.common.model.RepositorySearch
import com.example.kotlintest.common.model.RepositoryTree
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

    interface onGetRepositoryFinishedListener {
        fun onGetRepositoryFail()
        fun onGetRepositorySuccess(repository: RepositoryDetails)
    }

    interface onGetTreeFinishedListener {
        fun onGetTreeSuccess(tree: RepositoryTree)
        fun onGetTreeFail()
    }

    private val githubApi: GithubApi = GithubApi.retrofit.create(GithubApi::class.java)

    fun getPublicRepositoriesSince(
        since: Int,
        listener: onGetPublicRepositoriesSinceFinishedListener,
    )
    {
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
        query: String,
        listener: onSearchPublicRepositoriesFinishedListener,
    ) {
        val call: Call<RepositorySearch> = githubApi.searchPublicRepositories(query)
        call.enqueue(object : Callback<RepositorySearch> {
            override fun onResponse(
                call: Call<RepositorySearch>,
                response: Response<RepositorySearch>,
            ) {
                val body = response.body()
                if (body != null) {
                    listener.onSearchPublicRepositoriesSuccess(body)
                }
            }

            override fun onFailure(call: Call<RepositorySearch>, t: Throwable) {
                listener.onSearchPublicRepositoriesFail()
            }
        })
    }

    fun getTree(name: String, tree: String, listener: onGetTreeFinishedListener) {
        val call: Call<RepositoryTree> = githubApi.getTree(name, tree)
        call.enqueue(object : Callback<RepositoryTree> {
            override fun onResponse(
                call: Call<RepositoryTree>,
                response: Response<RepositoryTree>,
            ) {
                val body = response.body()
                if (body != null) {
                    listener.onGetTreeSuccess(body)
                }
            }

            override fun onFailure(call: Call<RepositoryTree>, t: Throwable) {
                listener.onGetTreeFail()
            }
        })
    }

    fun getRepository(name: String, listener: onGetRepositoryFinishedListener) {
        val call: Call<RepositoryDetails> = githubApi.getRepository(name)
        call.enqueue(object : Callback<RepositoryDetails> {
            override fun onResponse(
                call: Call<RepositoryDetails>,
                response: Response<RepositoryDetails>,
            ) {
                val body = response.body()
                if (body != null) {
                    listener.onGetRepositorySuccess(body)
                }
            }

            override fun onFailure(call: Call<RepositoryDetails>, t: Throwable) {
                listener.onGetRepositoryFail()
            }
        })
    }

}
