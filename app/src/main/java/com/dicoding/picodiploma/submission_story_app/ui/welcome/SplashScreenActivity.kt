package com.dicoding.picodiploma.submission_story_app.ui.welcome


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.submission_story_app.R



class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slashscreen)
        supportActionBar?.hide()

    }

}