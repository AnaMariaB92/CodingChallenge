package com.example.proficiencyexercise.network

import com.example.proficiencyexercise.model.About
import com.example.proficiencyexercise.util.Constants
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FactApiInterface {

    @GET("/s/2iodh4vg0eortkl/facts.json")
    fun getFacts(): Observable<About>

    companion object {
        fun create(): FactApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

            return retrofit.create(FactApiInterface::class.java)
        }
    }
}