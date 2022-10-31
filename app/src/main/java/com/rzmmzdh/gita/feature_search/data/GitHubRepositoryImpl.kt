package com.rzmmzdh.gita.feature_search.data

import com.rzmmzdh.gita.feature_search.data.datasource.remote.dto.asItem
import com.rzmmzdh.gita.feature_search.data.datasource.remote.dto.asSearchResult
import com.rzmmzdh.gita.feature_search.domain.datasource.RemoteDataSource
import com.rzmmzdh.gita.feature_search.domain.model.Item
import com.rzmmzdh.gita.feature_search.domain.model.RepositorySearchResult
import com.rzmmzdh.gita.feature_search.domain.model.Result
import com.rzmmzdh.gita.feature_search.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(private val remote: RemoteDataSource) :
    GitHubRepository {
    override suspend fun search(query: String): Flow<Result<RepositorySearchResult>> =
        flow {
            val result = remote.search(query)
            emit(Result.Loading)
            when {
                result.isSuccessful -> {
                    emit(
                        Result.Success(
                            data = remote.search(query).body()?.asSearchResult()
                                ?: RepositorySearchResult()
                        )
                    )
                }
                result.code() in 400..499 -> {
                    emit(
                        Result.Error(
                            Throwable(
                                message =
                                "A network client error has occurred. (${result.code()})"
                            )
                        )
                    )
                }
                result.code() in 500..599 -> {
                    emit(Result.Error(Throwable(message = "A network server error has occurred. (${result.code()})")))
                }
                else -> {
                    emit(
                        Result.Error(
                            Throwable(
                                message = "An unknown error has occurred. " +
                                        "Please make sure you\'re connected to the internet. (${result.code()})"
                            )
                        )
                    )
                }
            }
        }

    override suspend fun getRepo(repo: String): Flow<Result<Item?>> =
        flow {
            emit(Result.Loading)
            try {
                emit(Result.Success(data = remote.getRepo(repo).body()?.asItem()))
            } catch (e: Exception) {
                emit(Result.Error(exception = e))
            }
        }
}
