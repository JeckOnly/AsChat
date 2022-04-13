package com.android.aschat.di

import android.content.Context
import android.os.Build
import androidx.room.Room
import com.android.aschat.BuildConfig
import com.android.aschat.common.Constants
import com.android.aschat.common.database.AppDatabase
import com.android.aschat.common.network.ApiUrls
import com.android.aschat.common.network.AppServices
import com.android.aschat.feature_login.data.UserDao
import com.android.aschat.feature_login.domain.repo.LoginRepo
import com.android.aschat.util.AppUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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
                    val updateRequest = originalRequest.newBuilder().header("token", "Bearer$token").build();
                    chain.proceed(updateRequest);
                }
            })
            .addInterceptor(Interceptor { chain ->
                var retryNum = 0
                val request: Request = chain.request()
                var response: Response = chain.proceed(request)
                while (!response.isSuccessful && retryNum < Constants.Max_Retry) {
                    retryNum++
                    response = chain.proceed(request)
                }
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
            .addConverterFactory(GsonConverterFactory.create())
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
}