package com.dicoding.picodiploma.submission_story_app.ui.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_story_app.R
import com.dicoding.picodiploma.submission_story_app.model.UserPreferences
import com.dicoding.picodiploma.submission_story_app.ui.ViewModelFactory
import com.dicoding.picodiploma.submission_story_app.ui.login.LoginActivity
import com.dicoding.picodiploma.submission_story_app.ui.story.StoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class WelcomeActivity : AppCompatActivity() {
    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        supportActionBar?.hide()
        checkIfSessionValid()
    }

    private fun setUp(){
        welcomeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[WelcomeViewModel::class.java]
    }

    private fun checkIfSessionValid(){
        setUp()
        welcomeViewModel.getUser().observe(this) {
            if (it.isLogin) {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

}