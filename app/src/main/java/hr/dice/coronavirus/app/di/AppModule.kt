package hr.dice.coronavirus.app.di

import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_COVID19_API = "https://api.covid19api.com/"
private const val BASE_URL_NEWS_API = "http://api.mediastack.com/v1"
private const val COVID19 = "Covid19"
private const val NEWS = "News"

val appModule = module {

    fun provideRetrofitWithBaseUrl(baseUrl: String, converterFactory: GsonConverterFactory, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create()
    }

    single {
        OkHttpClient.Builder()
            .build()
    }

    single(named(COVID19)) {
        provideRetrofitWithBaseUrl(BASE_URL_COVID19_API, get(), get())
    }

    single(named(NEWS)) {
        provideRetrofitWithBaseUrl(BASE_URL_NEWS_API, get(), get())
    }
}