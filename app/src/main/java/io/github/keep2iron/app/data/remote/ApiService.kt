package io.github.keep2iron.app.data.remote

import io.github.keep2iron.app.entity.BaseResponse
import io.github.keep2iron.app.entity.Movie
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 13:48
 */
interface ApiService {

    @POST("index/movie")
    @FormUrlEncoded
    fun indexMovie(@Field("index") index: Int, @Field("size") size: Int): Flowable<BaseResponse<List<Movie>>>

}
