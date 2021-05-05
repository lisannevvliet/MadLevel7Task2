package com.example.madlevel7task2.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madlevel7task2.model.Quiz
import com.example.madlevel7task2.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "FIRESTORE"
    private val quizRepository: QuizRepository = QuizRepository()

    val quiz: LiveData<Quiz> = quizRepository.quiz

    val createSuccess: LiveData<Boolean> = quizRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
    get() = _errorText

    // Persist a quiz to Firestore.
    fun createQuiz(document: String, question: String, firstAnswer: String, secondAnswer: String, thirdAnswer: String, correctAnswer: String) {
        val quiz = Quiz(question, firstAnswer, secondAnswer, thirdAnswer, correctAnswer)

        viewModelScope.launch {
            try {
                quizRepository.createQuiz(document, quiz)
            } catch (ex: QuizRepository.QuizSaveError) {
                val errorMsg = "Something went wrong while saving quiz."
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    // Retrieve a quiz from Firestore.
    fun getQuiz(document: String) {
        viewModelScope.launch {
            try {
                quizRepository.getQuiz(document)
            } catch (ex: QuizRepository.QuizRetrievalError) {
                val errorMsg = "Something went wrong while retrieving quiz."
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    // Check if the selected answer is equal to the correct answer.
    fun correctAnswer(answer: String): Boolean {
        return quiz.value?.correctAnswer.equals(answer, ignoreCase = true)
    }
}