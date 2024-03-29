package com.example.ebroapp.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.domain.repository.DomainRepository
import com.example.ebroapp.di.module.domain.DomainModule
import com.example.ebroapp.di.module.network.NetworkModule
import com.example.ebroapp.utils.map.LocationListener
import com.example.ebroapp.utils.music.Player
import com.example.ebroapp.utils.provider.GsonProvider
import com.example.ebroapp.utils.provider.GsonProviderImpl
import com.example.network.provider.RetrofitProvider
import com.example.network.provider.RetrofitProviderImpl
import com.example.network.repository.RemoteService
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        NetworkModule::class,
        DomainModule::class,
        AppModule.Bindings::class
    ]
)
internal open class AppModule(private val application: Application) {

    @Provides
    internal fun provideContext(): Context = application

    @Provides
    internal fun provideGson(gsonProvider: GsonProvider): Gson = gsonProvider.provideGson()

    @Provides
    internal fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    internal fun providePlayer(context: Context, domainRepository: DomainRepository): Player =
        Player(context, domainRepository).apply { setPlayList(context) }

    @Provides
    internal fun provideLocationListener(context: Context): LocationListener =
        LocationListener(context)

    @Provides
    internal fun provideRemoteService(retrofitProvider: RetrofitProvider): RemoteService =
        retrofitProvider.provideRetrofit()

    @Module
    interface Bindings {
        @Binds
        fun bindGsonProvider(impl: GsonProviderImpl): GsonProvider

        @Binds
        fun bindRetrofitProvider(impl: RetrofitProviderImpl): RetrofitProvider
    }

    companion object {
        private const val APP_PREFERENCES = "APP_PREFERENCES"
    }
}
