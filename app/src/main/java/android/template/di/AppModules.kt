/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.di

import android.app.Application
import android.template.data.DefaultNoteRepository
import android.template.data.NoteRepository
import android.template.data.local.database.AppDatabase
import android.template.data.local.database.NoteDao
import android.template.data.remote.NoteApiService
import android.template.ui.note.NoteViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

val databaseModule = module {
    single {
        androidx.room.Room.databaseBuilder(
            get<Application>(),
            AppDatabase::class.java,
            "notes"
        ).fallbackToDestructiveMigration().build()
    }
    single<NoteDao> { get<AppDatabase>().noteDao() }
}

val networkModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<NoteApiService> { get<Retrofit>().create(NoteApiService::class.java) }
}

val appModule = module {
    single<NoteRepository> { DefaultNoteRepository(get(), get()) }
    viewModel { NoteViewModel(get()) }
}
