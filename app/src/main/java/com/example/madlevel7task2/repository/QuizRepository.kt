package com.example.madlevel7task2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7task2.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuizRepository {
    // Get an instance of Firestore.
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()
    val quiz: LiveData<Quiz>
    get() = _quiz

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val createSuccess: LiveData<Boolean>
    get() = _createSuccess

    // Persist a quiz to Firestore.
    suspend fun createQuiz(document: String, quiz: Quiz) {
        try {
            withTimeout(5000) {
                firestore.collection("quizzes").document(document).set(quiz)

                _createSuccess.value = true
            }
        } catch (e: Exception) {
            throw QuizSaveError(e.message.toString(), e)
        }
    }

    // Retrieve a quiz from Firestore.
    suspend fun getQuiz(document: String) {
        try {
            //withTimeout(5000) {
                val quiz = firestore.collection("quizzes").document(document).get().await()

                val question = quiz.getString("question").toString()
                val firstAnswer = quiz.getString("firstAnswer").toString()
                val secondAnswer = quiz.getString("secondAnswer").toString()
                val thirdAnswer = quiz.getString("thirdAnswer").toString()
                val correctAnswer = quiz.getString("correctAnswer").toString()

                _quiz.value = Quiz(question, firstAnswer, secondAnswer, thirdAnswer, correctAnswer)
            //}
        } catch (e: Exception) {
            throw QuizRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    // Create the errors for when a quiz can not be saved or retrieved.
    class QuizSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class QuizRetrievalError(message: String) : Exception(message)
}