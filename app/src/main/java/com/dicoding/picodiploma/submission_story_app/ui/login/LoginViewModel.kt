package com.dicoding.picodiploma.submission_story_app.ui.login

import androidx.lifecycle.*
import com.dicoding.picodiploma.submission_story_app.data.repository.UserRepository
import com.dicoding.picodiploma.submission_story_app.model.LoginPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val loginPreferences: LoginPreferences
) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.saveToken(token)
        }
    }

    fun checkIfFirstTime(): LiveData<Boolean> {
        return loginPreferences.isFirstTime().asLiveData()
    }

}