package hr.dice.coronavirus.app.di

import android.location.Geocoder
import hr.dice.coronavirus.app.BuildConfig
import hr.dice.coronavirus.app.datastore.DataStoreSelectionManager
import hr.dice.coronavirus.app.networking.CoronavirusApiService
import hr.dice.coronavirus.app.networking.CountryApiService
import hr.dice.coronavirus.app.networking.NewsApiService
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar

private const val COVID19 = "Covid19"
private const val NEWS = "News"

val appModule = module {

    single<Converter.Factory> {
        GsonConverterFactory.create()
    }

    single {
        OkHttpClient.Builder()
            .build()
    }

    single(named(COVID19)) {
        provideRetrofitWithBaseUrl(BuildConfig.BASE_URL_COVID19_API, get(), get())
    }

    single(named(NEWS)) {
        provideRetrofitWithBaseUrl(BuildConfig.BASE_URL_NEWS_API, get(), get())
    }

    single<CoronavirusApiService> {
        get<Retrofit>(named(COVID19)).create(CoronavirusApiService::class.java)
    }

    single<CountryApiService> {
        get<Retrofit>(named(COVID19)).create(CountryApiService::class.java)
    }

    single {
        DataStoreSelectionManager(get())
    }

    single<NewsApiService> {
        get<Retrofit>(named(NEWS)).create(NewsApiService::class.java)
    }

    single {
        Calendar.getInstance()
    }

    single {
        SimpleDateFormat()
    }

    single {
        Geocoder(get())
    }
}

private fun provideRetrofitWithBaseUrl(baseUrl: String, converterFactory: Converter.Factory, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()
}
