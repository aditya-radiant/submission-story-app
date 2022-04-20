package com.dicoding.picodiploma.submission_story_app.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_story_app.data.Injection
import com.dicoding.picodiploma.submission_story_app.data.repository.UserRepository
import com.dicoding.picodiploma.submission_story_app.model.LoginPreferences
import com.dicoding.picodiploma.submission_story_app.ui.login.LoginViewModel

class ViewModelFactory(private val userRepository: UserRepository,
                       private val loginPreferences: LoginPreferences
                       ) : ViewModelProvider.NewInstanceFactory()
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository, loginPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(loginPreferences: LoginPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(),
                loginPreferences
                )
            }.also { instance = it }

    }
}