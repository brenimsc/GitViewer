package com.breno.gitviewer.di

import com.breno.gitviewer.follow.viewmodel.FollowViewModel
import com.breno.gitviewer.main.viewmodel.MainViewModel
import com.breno.gitviewer.model.User
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.slide.viewmodel.SlideUserListViewModel
import com.breno.gitviewer.user.SearchUserViewModel
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.github.com/"
val moduleNetwork = module {
    factory {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    single {
        createService<GitApi>(get(), get())
    }
    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}

val moduleViewModel = module {
    viewModel { MainViewModel(get()) }
    viewModel { (repo: String, user: String) ->
        PostDetailViewModel(
            repo = repo,
            nameUser = user,
            service = get()
        )
    }
    viewModel { (user: User) ->
        UserDetailsViewModel(user, get())
    }

    viewModel { (title: String, url: String) ->
        UserListViewModel(title, url, get())
    }
    viewModel { (typeRequest: String) ->
        FollowViewModel(typeRequest, get())
    }

    viewModel { (firstTab: String) ->
        SlideUserListViewModel(firstTab)
    }

    viewModel {
        SearchUserViewModel(get())
    }

}

private inline fun <reified T> createService(
    factory: Moshi,
    okHttpClient: OkHttpClient
): T {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(factory))
        .build()
        .create(T::class.java)
}