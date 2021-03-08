package com.jacobs.mj.ktornoteapp.other

import kotlinx.coroutines.flow.*

/**
 * Created by mj on 2021/03/07 at 2:16 PM
 *
 * This is a generic class that can be reused
 */

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    //  Observe to make sure that we can display loading status
    emit(Resource.loading(null))

    //  Data will first go to database, not using the data from the Api
    val data = query().first()

    //  Flow to process the data
    //  Check condition
    val flow = if (shouldFetch(data)) {
        //  If we need to fetch data

        //  There is already data and needs to be saved in the database and then displayed to the user
        emit(Resource.loading(data))

        try {
            //  Get data from RestApi
            val fetchedResult = fetch()
            //  Save results from Api to local database
            saveFetchResult(fetchedResult)
            //  Query local database
            query().map {
                Resource.success(it)
            }
        } catch (t: Throwable) {
            //  Fetch from the Api was unsuccessful
            onFetchFailed(t)
            query().map {
                Resource.error("Couldn't reach server it might be down", it)
            }
        }
    } else {
        //  If we do not need to fetch data, submit data from local database
        query().map {
            Resource.success(it)
        }
    }
    emitAll(flow)
}