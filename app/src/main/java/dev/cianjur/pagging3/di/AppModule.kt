package dev.cianjur.pagging3.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cianjur.pagging3.api.UnsplashApi
import dev.cianjur.pagging3.db.PhotoDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi = retrofit.create(UnsplashApi::class.java)

    @Provides
    @Singleton
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase = PhotoDatabase.getInstance(context)

}
