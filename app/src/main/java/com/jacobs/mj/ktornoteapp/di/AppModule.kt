package com.jacobs.mj.ktornoteapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.jacobs.mj.ktornoteapp.data.local.NotesDatabase
import com.jacobs.mj.ktornoteapp.data.remote.BasicAuthInterceptor
import com.jacobs.mj.ktornoteapp.data.remote.NoteApi
import com.jacobs.mj.ktornoteapp.other.Constants
import com.jacobs.mj.ktornoteapp.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by mj on 2021/03/06 at 9:12 AM
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NotesDatabase::class.java, Constants.notesDatabaseName).build()

    @Provides
    @Singleton
    fun provideNoteDAO(db: NotesDatabase) = db.noteDAO()

    @Provides
    @Singleton
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()

    @Provides
    @Singleton
    fun provideNoteApi(basicAuthInterceptor: BasicAuthInterceptor): NoteApi {
        //  We need to create OkHttp client here
        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)       //  To make sure that headers are added as they are suppose to be
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            context, Constants.ENCRYPTED_SHARED_PREF_NAME, masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}