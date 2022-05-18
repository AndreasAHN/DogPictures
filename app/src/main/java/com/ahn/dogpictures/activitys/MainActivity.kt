package com.ahn.dogpictures.activitys

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.ahn.dogpictures.R
import com.ahn.dogpictures.adapters.DogApiAdapter
import com.ahn.dogpictures.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
public class MainActivity: AppCompatActivity(), CoroutineScope by MainScope()
{
    ////////////////Activity variables/////////////
    private var mainViewModel = MainViewModel()
    private lateinit var textViewLoading : TextView
    private lateinit var textViewSlideShowSpeedText : TextView
    private lateinit var textViewSlideShowSpeed : TextView
    private lateinit var seekBarSlideShowSpeed : SeekBar
    private lateinit var imageViewDogs : ImageView
    private lateinit var switchRandom : Switch
    private lateinit var switchSlideshow : Switch

    private val TAG : String = "MainActivity"
    @Inject
    public lateinit var injectedMessage : String

    ////////////////Android Lifecycle//////////////
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, " : Injected Message = ${injectedMessage}")
    }

    override fun onResume()
    {
        super.onResume()

        textViewLoading = findViewById(R.id.mainactivity_textView_loading)
        initSwitchListener()
        initSeekBarListener()
        initImageListener()

        mainViewModel = ViewModelProvider(this)[MainViewModel()::class.java]

        if(mainViewModel.getFirstStart())
        {
            mainViewModel.setFirstStart()
            this.dogImageCaller()
        }
        else
        {
            imageViewDogs.load(mainViewModel.getLastPicture())

            textViewLoading.visibility = View.GONE

            switchRandom.isChecked = mainViewModel.getSecureRandomPicture()
            seekBarSlideShowSpeed.progress = mainViewModel.getSlideshowSpeed()
            switchSlideshow.isChecked = mainViewModel.getSlideshowRunning()

            if(switchSlideshow.isChecked)
            {
                this.slideshowActivate(true)
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                if(mainViewModel.getSlideshowThreadRunning() < 1) //A bit like a mutex lock
                    slideshowRunner()
            }
            else
                this.slideshowActivate(false)
        }
    }

    override fun onPause()
    {
        super.onPause()
        mainViewModel.setSlideshowRunning(switchSlideshow.isChecked)
        mainViewModel.setSecureRandomPicture(switchRandom.isChecked)
        mainViewModel.setSlideshowSpeed(seekBarSlideShowSpeed.progress)
    }

    override fun onDestroy()
    {
        super.onDestroy()

        if(mainViewModel.getSlideshowThreadRunning() > 0)
            mainViewModel.endedSlideshowThreadRunning()
    }

    ////////////////Listeners////////////////
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun initSwitchListener()
    {
        switchSlideshow = findViewById(R.id.mainactivity_switch_diashow)
        switchSlideshow.setOnCheckedChangeListener{ _, isChecked ->
            val message = if (isChecked)
            {
                this.slideshowActivate(true)
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                if(mainViewModel.getSlideshowThreadRunning() < 1) //A bit like a mutex lock
                    slideshowRunner()
                getString(R.string.SlideshowON)
            }
            else
            {
                this.slideshowActivate(false)
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                getString(R.string.SlideshowOFF)
            }

            Toast.makeText(
                this@MainActivity, message,
                Toast.LENGTH_SHORT
            ).show()

            mainViewModel.setSlideshowRunning(isChecked)
        }

        switchRandom = findViewById(R.id.mainactivity_switch_securerandom)
        switchRandom.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) getString(R.string.SecuringRandompictureON) else getString(R.string.SecuringRandompictureOFF)
            Toast.makeText(
                this@MainActivity, message,
                Toast.LENGTH_SHORT
            ).show()

            mainViewModel.setSecureRandomPicture(isChecked)
        }
    }

    private fun initSeekBarListener()
    {
        textViewSlideShowSpeed = findViewById(R.id.mainactivity_textView_diashowSpeed)
        textViewSlideShowSpeedText = findViewById(R.id.mainactivity_textView_diashowSpeedText)
        seekBarSlideShowSpeed = findViewById(R.id.mainactivity_seekBar_diashowSpeed)

        seekBarSlideShowSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean)
            {
                textViewSlideShowSpeed.text = progress.toString()
                mainViewModel.setSlideshowSpeed(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?)
            {

            }

            override fun onStopTrackingTouch(p0: SeekBar?)
            {

            }
        })

        textViewSlideShowSpeed.text = seekBarSlideShowSpeed.progress.toString()//Needed for onResume()
        this.slideshowActivate(false)
    }

    private fun initImageListener()
    {
        imageViewDogs = findViewById(R.id.mainactivity_imageView)
        imageViewDogs.setOnClickListener()
        {
            dogImageCaller()
        }
    }

    ///////////////Functions///////////////
    //REF : Co-Routines with rest-api example : https://dev.to/rtficial/kotlin-coroutines-and-retrofit-a-practical-approach-to-consuming-rest-apis-in-android-446k
    private fun dogImageCaller()
    {
        launch(Dispatchers.Main)
        {
            // Try catch block to handle exceptions when calling the API.
            try
            {
                textViewLoading.visibility = View.VISIBLE

                val response = DogApiAdapter.dogApiClient.getRandomDogImage()

                // Check if response was successful.
                if (response.isSuccessful && response.body() != null)
                {
                    val data = response.body()!!
                    //Checking if secure random picture is OFF and it's a picture we haven't already seen.
                    if(!mainViewModel.getSecureRandomPicture() || !mainViewModel.checkPictureId(data.message.toString()))
                    {
                        imageViewDogs.load(data.message)
                        textViewLoading.visibility = View.GONE

                        mainViewModel.setLastPicture(data.message.toString())
                        mainViewModel.addPictureId(data.message.toString())
                    }
                    else //If it's an old picture and secure random picture is ON, then we will find a new picture.
                    {
                        dogImageCaller()
                    }
                }
                else
                {
                    // Show API error.
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.CouldntFetchImage) + " ${response.message()}",
                        Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception)
            {
                // Show API error. This is the error raised by the client.
                Toast.makeText(this@MainActivity,
                    getString(R.string.AnErrorOccurred) + " ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Setting visibility
    private fun slideshowActivate(state : Boolean)
    {
        if(state)
        {
            textViewSlideShowSpeedText.visibility = View.VISIBLE
            textViewSlideShowSpeed.visibility = View.VISIBLE
            seekBarSlideShowSpeed.visibility = View.VISIBLE
        }
        else
        {
            textViewSlideShowSpeedText.visibility = View.GONE
            textViewSlideShowSpeed.visibility = View.GONE
            seekBarSlideShowSpeed.visibility = View.GONE
        }
    }

    private fun slideshowRunner()
    {
        mainViewModel.addSlideshowThreadRunning()

        Timer().schedule(mainViewModel.getSlideshowSpeed().toLong()*1000)
        {
            if (mainViewModel.getSlideshowRunning())
            {
                dogImageCaller()
                slideshowRunner()
            }
            mainViewModel.endedSlideshowThreadRunning()
        }
    }
}