package com.ahn.dogpictures.dependencyinjection

import android.content.Context
import com.ahn.dogpictures.BaseApplication
import com.ahn.dogpictures.viewModels.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//This object module where mostly to test injecting with start variables for the application
//SingletonComponent is the upper level of keeping itself alive. The old name where ApplicationComponent

//REF : Video guide of Hilt Dependency Injection with
@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Singleton
    @Provides
    public fun provideInjectedMessage() : String
    {
        return "Hello from module, I'm injected to mainActivity :D"
    }

//    private var firstStart : Boolean = true
//    private var lastPicture : String = ""
//    private var slideshowRunning : Boolean = false
//    private var slideshowSpeed : Int = 5
//    private var secureRandomPicture : Boolean = false
//    private var slideshowThreadsRunning : Int = 0
//
//    @MainViewModel.FirstStart
//    @Singleton
//    @Provides
//    public fun provideFirstStart() : Boolean
//    {
//        return firstStart
//    }
//
//    @MainViewModel.LastPicture
//    @Singleton
//    @Provides
//    public fun provideLastPicture() : String
//    {
//        return lastPicture
//    }
//
//    @MainViewModel.SlideshowRunning
//    @Singleton
//    @Provides
//    public fun provideSlideshowRunning() : Boolean
//    {
//        return slideshowRunning
//    }
//
//    @MainViewModel.SlideshowSpeed
//    @Singleton
//    @Provides
//    public fun provideSlideshowSpeed() : Int
//    {
//        return slideshowSpeed
//    }
//
//    @MainViewModel.SecureRandomPicture
//    @Singleton
//    @Provides
//    public fun provideSecureRandomPicture() : Boolean
//    {
//        return secureRandomPicture
//    }
//
//    @MainViewModel.SlideshowThreadsRunning
//    @Singleton
//    @Provides
//    public fun provideSlideshowThreadsRunning() : Int
//    {
//        return slideshowThreadsRunning
//    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication
    {
        return app as BaseApplication
    }
}