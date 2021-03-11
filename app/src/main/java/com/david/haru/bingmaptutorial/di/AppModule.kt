package com.david.haru.bingmaptutorial.di


import android.content.Context
import com.david.haru.bingmaptutorial.location.LocationRepository
import com.david.haru.bingmaptutorial.location.LocationRepositoryImpl
import com.david.haru.bingmaptutorial.places.IPlacesApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by David Harush
 * On 09/03/2021.
 *
 * Specifies **how** to bind injected dependencies to the [ApplicationComponent] for global application scoped objects.
 *
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val TIME_OUT = 5L

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()


    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(appContext)

    @Provides
    @Singleton
    fun provideGlobalOkHttpClient(
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideWebService(
            okHttpClient: OkHttpClient
    ): IPlacesApiService = createWebService(okHttpClient,
            IPlacesApiService.BASE_URL
    )


    private inline fun <reified T> createWebService(
            okHttpClient: OkHttpClient,
            url: String,
            converterFactory: Converter.Factory = GsonConverterFactory.create()
    ): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
        return retrofit.create(T::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
interface AppBoundedInstances {

    @Binds
    fun bindLocationRepository(
            locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository


}