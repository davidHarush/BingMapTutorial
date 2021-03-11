package com.david.haru.bingmaptutorial.places

data class RepoResponse<D>(
        val data: D? = null,
        val status: ResultType = ResultType.SUCCESS,
        val throwable: Throwable = NullPointerException()
)
sealed class ResultType {
    object SUCCESS: ResultType()
    object FAIL: ResultType()
}
