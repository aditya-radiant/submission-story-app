package com.dicoding.picodiploma.submission_story_app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_story_app.R
import com.dicoding.picodiploma.submission_story_app.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.submission_story_app.model.UserPreferences
import com.dicoding.picodiploma.submission_story_app.ui.Helper
import com.dicoding.picodiploma.submission_story_app.ui.ViewModelFactory
import com.dicoding.picodiploma.submission_story_app.ui.signup.SignUpActivity
import com.dicoding.picodiploma.submission_story_app.ui.story.StoryActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.login)

        setupViewModel()

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        buttonListener()
    }

    override fun onResume() {
        super.onResume()
        initialCheck()
    }

    private fun initialCheck(){
        loginViewModel.getUser().observe(this) {
            if (it.isLogin) {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        val item = menu.findItem(R.id.logout)
        item.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.login_success))
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    val intent = Intent(context, StoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.login_failed) +", $message")
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    showLoading(false)
                }
                create()
                show()

            }
        }
    }

    private fun buttonListener() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            loginViewModel.login(email, pass, object : Helper.ApiCallbackString {
                override fun onResponse(success: Boolean,message: String) {
                    showAlertDialog(success, message)
                }
            })
        }

        binding.signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}