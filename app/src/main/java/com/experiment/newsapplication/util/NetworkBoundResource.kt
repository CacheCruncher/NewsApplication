package com.experiment.newsapplication.util

import com.experiment.newsapplication.data.APIResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

inline fun <DatabaseResultType, NetworkResultType> networkBoundResource(
    crossinline fetchFromDatabase: () -> Flow<DatabaseResultType>,
    crossinline fetchFromNetwork: suspend () -> NetworkResultType,
    crossinline insertFetchResultInDataBase: suspend (NetworkResultType) -> Unit,
    crossinline shouldFetchFromNetwork: suspend (DatabaseResultType) -> Boolean = { true }
) = channelFlow {
    val firstField = fetchFromDatabase().first()
    if (shouldFetchFromNetwork(firstField)) {
        val loadingJob = launch {
            fetchFromDatabase().collect { databaseRow ->
                send(APIResult.Success(databaseRow))
            }
        }
        try {
            insertFetchResultInDataBase(fetchFromNetwork())
            loadingJob.cancel()
            fetchFromDatabase().collect { databaseRow ->
                send(APIResult.Success(databaseRow))
            }
        } catch (t: Throwable) {
            loadingJob.cancel()
            fetchFromDatabase().collect { databaseRow ->
                send(APIResult.Error(databaseRow, t))
            }
        }
    } else {
        fetchFromDatabase().collect { send(APIResult.Success(it)) }
    }
}