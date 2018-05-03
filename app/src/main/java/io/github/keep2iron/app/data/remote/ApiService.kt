package io.github.keep2iron.app.data.remote

import io.github.keep2iron.app.model.BaseResponse
import io.github.keep2iron.app.model.GsonIndex
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 13:48
 */
interface ApiService {

    @POST("/api/index/models")
    @FormUrlEncoded
    fun indexModels(@Field("index") index: Int, @Field("size") size: Int): Observable<BaseResponse<List<GsonIndex>>>

    @POST("/api/index/banner")
    fun indexBanner(): Observable<BaseResponse<List<GsonIndex>>>

}
