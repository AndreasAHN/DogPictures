package com.ahn.dogpictures.viewModels
import androidx.lifecycle.ViewModel

public class MainViewModel
    (
        private var firstStart : Boolean = true,
        private var lastPicture : String = "",
        private var slideshowRunning : Boolean = false,
        private var slideshowSpeed : Int = 5,
        private var secureRandomPicture : Boolean = false,
        private var slideshowThreadsRunning : Int = 0
    ) : ViewModel()
{
    private val picturesId = ArrayList<String>()

//    @Qualifier
//    annotation class FirstStart
//    @Inject
//    lateinit var firstStart : Boolean
//
//    @Qualifier
//    annotation class LastPicture
//    @Inject
//    lateinit var lastPicture : String
//
//    @Qualifier
//    annotation class SlideshowRunning
//    @Inject
//    lateinit var slideshowRunning : Boolean
//
//    @Qualifier
//    annotation class SlideshowSpeed
//    @Inject
//    lateinit var slideshowSpeed : Int
//
//    @Qualifier
//    annotation class SecureRandomPicture
//    @Inject
//    lateinit var secureRandomPicture : Boolean
//
//    @Qualifier
//    annotation class SlideshowThreadsRunning
//    @Inject
//    lateinit var slideshowThreadsRunning : Int

    public fun getFirstStart(): Boolean
    {
        return this.firstStart
    }

    public fun setFirstStart()
    {
        this.firstStart = false
    }

    public fun getLastPicture(): String
    {
        return this.lastPicture
    }

    public fun setLastPicture(pictureUrl : String)
    {
        this.lastPicture = pictureUrl
    }

    public fun getSlideshowRunning(): Boolean
    {
        return this.slideshowRunning
    }

    public fun setSlideshowRunning(state : Boolean)
    {
        this.slideshowRunning = state
    }

    public fun getSlideshowSpeed(): Int
    {
        return this.slideshowSpeed
    }

    public fun setSlideshowSpeed(speed : Int)
    {
        this.slideshowSpeed = speed
    }

    public fun getSecureRandomPicture(): Boolean
    {
        return this.secureRandomPicture
    }

    public fun setSecureRandomPicture(state : Boolean)
    {
        this.secureRandomPicture = state
    }

    public fun addPictureId(id : String)
    {
        this.picturesId.add(id)
    }

    public fun checkPictureId(id : String) : Boolean
    {
        return this.picturesId.contains(id)
    }

    public fun getSlideshowThreadRunning(): Int
    {
        return this.slideshowThreadsRunning
    }

    public fun addSlideshowThreadRunning()
    {
        this.slideshowThreadsRunning++
    }

    public fun endedSlideshowThreadRunning()
    {
        this.slideshowThreadsRunning--
    }
}