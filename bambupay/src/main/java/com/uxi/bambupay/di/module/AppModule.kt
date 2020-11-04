package com.uxi.bambupay.di.module

import android.app.Application
import androidx.annotation.NonNull
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.uxi.bambupay.BuildConfig
import com.uxi.bambupay.BuildConfig.API_BASE_URL
import com.uxi.bambupay.api.AuthenticationInterceptor
import com.uxi.bambupay.api.UbpService
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.utils.Utils
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Module(includes = [
    ViewModelModule::class
])
class AppModule  {

    @Provides
    @Singleton
    fun providesUtils(application: Application): Utils = Utils(application)

    @Provides
    @Singleton
    fun provideInterceptor(utils: Utils): AuthenticationInterceptor {
        return AuthenticationInterceptor(utils)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthenticationInterceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(3, 10, TimeUnit.MINUTES))
            .addInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.networkInterceptors().add(logging)
        }

        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    @NonNull
    fun provideWebService(httpClient: OkHttpClient): WebService {
        //add logger
        val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        }).create()

//        Log.e("DEBUG", "baseUrl:: ".plus(baseUrl))
        Timber.tag("DEBUG").e(API_BASE_URL)

        // add gsonBuilder here if need to have Deserializer
//        val gsonBuilder = GsonBuilder()
//            .registerTypeAdapter(User::class.java, ErrorDeserializer())
//            .create()

        //add retro builder
        val retroBuilder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))

        retroBuilder.client(httpClient)

        //create retrofit - only this instance would be used in the entire application
        return retroBuilder.build().create(WebService::class.java)
    }

    @Provides
    @Singleton
    @NonNull
    fun provideUbpService(): UbpService {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(3, 10, TimeUnit.MINUTES))

        httpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val builder: Request.Builder
            builder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
            val request = builder.build()
            chain.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        val client = httpClientBuilder.build()
        val retroBuilder = Retrofit.Builder()
            .baseUrl("https://api-uat.unionbankph.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retroBuilder.create(UbpService::class.java)
    }

}