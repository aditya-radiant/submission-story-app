package com.dicoding.picodiploma.submission_story_app.ui.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission_story_app.data.repository.UserRepository

class SignUpViewModel(private val userRepository: UserRepository): ViewModel() {
    fun registerUser(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}