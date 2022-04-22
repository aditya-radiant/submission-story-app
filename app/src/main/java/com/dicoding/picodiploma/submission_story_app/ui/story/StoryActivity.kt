package com.dicoding.picodiploma.submission_story_app.ui.story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_story_app.R
import com.dicoding.picodiploma.submission_story_app.databinding.ActivityStoryBinding
import com.dicoding.picodiploma.submission_story_app.model.UserPreferences
import com.dicoding.picodiploma.submission_story_app.ui.ViewModelFactory
import com.dicoding.picodiploma.submission_story_app.ui.welcome.WelcomeViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class StoryActivity : AppCompatActivity() {

    private val binding: ActivityStoryBinding by lazy {
        ActivityStoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.story)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val welcomeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))

        )[WelcomeViewModel::class.java]

        if (item.itemId == R.id.settings) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }else if (item.itemId == R.id.logout) {
            /*welcomeViewModel.logout()
            finish()*/
        }

        return super.onOptionsItemSelected(item)
    }
}