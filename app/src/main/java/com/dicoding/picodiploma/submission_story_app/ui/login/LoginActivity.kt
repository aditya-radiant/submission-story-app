package com.dicoding.picodiploma.submission_story_app.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_story_app.R
import com.dicoding.picodiploma.submission_story_app.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.submission_story_app.model.UserPreferences
import com.dicoding.picodiploma.submission_story_app.ui.Helper
import com.dicoding.picodiploma.submission_story_app.ui.Helper.isEmailValid
import com.dicoding.picodiploma.submission_story_app.ui.ViewModelFactory
import com.dicoding.picodiploma.submission_story_app.ui.story.StoryActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.login)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupViewModel()
        buttonListener()
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
                setMessage(getString(R.string.sign_in_failed) +", $message")
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
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

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}