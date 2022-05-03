package com.android.aschat.di

import android.content.Context
import android.os.Build
import androidx.room.Room
import com.android.aschat.BuildConfig
import com.android.aschat.common.Constants
import com.android.aschat.common.database.AppDatabase
import com.android.aschat.common.network.ApiUrls
import com.android.aschat.common.network.AppConfigDeserializer
import com.android.aschat.common.network.AppServices
import com.android.aschat.feature_home.domain.repo.HomeRepo
import com.android.aschat.feature_host.domain.repo.HostRepo
import com.android.aschat.feature_login.data.UserDao
import com.android.aschat.feature_login.domain.model.appconfig.ConfigList
import com.android.aschat.feature_login.domain.repo.LoginRepo
import com.android.aschat.feature_rank.domain.repo.RankRepo
import com.android.aschat.util.AppUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


/**
 * 全局的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    网络

    @Singleton
    @Provides
    @Named("client")
    fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                // 添加header
                val original = chain.request();
                val request = original.newBuilder()
                    .header("ver", BuildConfig.VERSION_NAME)
                    .header("device-id", AppUtil.getAndroidId(context))
                    .header("utm-source", "")
                    .header("model", Build.MODEL)
                    .header("lang", "")
                    .header("is_anchor", "false")
                    .header("pkg", BuildConfig.APPLICATION_ID)//包名
                    .header("platform", "Android") //Android or iOS
                    .method(original.method, original.body)
                    .build();
                chain.proceed(request);
            })
            .addInterceptor (Interceptor { chain ->
                // 添加token
                val token = SpUtil.get(context, SpConstants.TOKEN, "") as String
                if (token.isEmpty()) {
                    val originalRequest = chain.request();
                    chain.proceed(originalRequest);
                }else {
                    val originalRequest = chain.request();
                    //key的话以后台给的为准，我这边是叫token
                    val updateRequest = originalRequest.newBuilder().header("Authorization", "Bearer$token").build();
                    chain.proceed(updateRequest);
                }
            })
            .addInterceptor(Interceptor { chain ->
                // 添加重试，以及报错处理
                var retryNum = 0
                val request: Request = chain.request()
                var response: Response
                do {
                    retryNum++
                    try {
                        response = chain.proceed(request)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        response = Response.Builder()
                            .request(request)
                            .protocol(Protocol.HTTP_1_1)
                            .code(999)
                            .message("okhttp error")
                            .body(Constants.Error_Custom_Json.toResponseBody(null)).build() // 在这里设置了json为错误的json
                    }
                }while (!response.isSuccessful && retryNum < Constants.Max_Retry)
                return@Interceptor response
            })
            .build()
    }

    @Singleton
    @Provides
    @Named("Retrofit")
    fun provideRetrofit(@Named("client") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiUrls.BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(ConfigList::class.java, AppConfigDeserializer())
                        .create()
                )
            )
            .build()
    }

    @Singleton
    @Provides
    @Named("AppServices")
    fun provideAppServices(@Named("Retrofit") retrofit: Retrofit): AppServices {
        return retrofit.create(AppServices::class.java)
    }

    @Singleton
    @Provides
    @Named("Context")
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

//    数据库

    @Singleton
    @Provides
    @Named("AppDatabase")
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Singleton
    @Provides
    @Named("UserDao")
    fun provideUserDao(@Named("AppDatabase") database: AppDatabase) :UserDao{
        return database.userDao()
    }

//    仓库
    @Singleton
    @Provides
    @Named("LoginRepo")
    fun provideLoginRepo(@Named("AppServices") services: AppServices, @Named("UserDao") userDao: UserDao): LoginRepo{
        return LoginRepo(services, userDao)
    }

    @Singleton
    @Provides
    @Named("HomeRepo")
    fun provideHomeRepo(@Named("AppServices") services: AppServices): HomeRepo{
        return HomeRepo(services)
    }

    @Singleton
    @Provides
    @Named("HostRepo")
    fun provideHostRepo(@Named("AppServices") services: AppServices): HostRepo {
        return HostRepo(services)
    }

    @Singleton
    @Provides
    @Named("RankRepo")
    fun provideRankRepo(@Named("AppServices") services: AppServices): RankRepo {
        return RankRepo(services)
    }
}