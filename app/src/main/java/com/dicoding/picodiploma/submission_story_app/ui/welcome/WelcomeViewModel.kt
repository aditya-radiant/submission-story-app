package com.dicoding.picodiploma.submission_story_app.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.submission_story_app.model.UserModel
import com.dicoding.picodiploma.submission_story_app.model.UserPreferences
import kotlinx.coroutines.launch

class WelcomeViewModel(private val pref: UserPreferences) : ViewModel()  {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()

    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}