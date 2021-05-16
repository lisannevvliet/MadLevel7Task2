package com.example.madlevel7task2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7task2.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.withTimeout

class QuizRepository {

    // Get an instance of Firestore.
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Initialize the LiveData in which the quizzes will be stored.
    private val _quizzes: MutableLiveData<ArrayList<Quiz>> = MutableLiveData()
    val quizzes: LiveData<ArrayList<Quiz>> get() = _quizzes

    // Initialize the LiveData in which the exceptions will be stored.
    private var _exception: MutableLiveData<String> = MutableLiveData()
    val exception: LiveData<String> get() = _exception

    // Persist a quiz to Firestore.
    suspend fun createQuiz(document: Int, quiz: Quiz) {
        try {
            withTimeout(5000) {
                // Delete the previously created quiz.
                firestore.collection("quizzes").document(document.toString()).delete()
                // Create a new quiz.
                firestore.collection("quizzes").document(document.toString()).set(quiz)
                }
            } catch (exception: Exception) {
                _exception.value = "Something went wrong while saving quiz."
            }
        }

    // Retrieve the quizzes from Firestore.
    suspend fun getQuizzes() {
        try {
            withTimeout(5000) {
                // Listen to the Firestore quizzes collection.
                firestore.collection("quizzes").addSnapshotListener { snapshot, exception ->
                    val quizzes = ArrayList<Quiz>()

                    if (snapshot != null) {
                        // Add all quizzes from the Firestore quizzes collection to the list.
                        for (document in snapshot.documents) {
                            val quiz = document.toObject(Quiz::class.java)

                            if (quiz != null) {
                                quizzes.add(quiz)
                            }
                        }

                        // Store the quizzes in the corresponding LiveData.
                        _quizzes.value = quizzes
                    } else if (exception != null) {
                        _exception.value = "Something went wrong while retrieving quiz."
                    }
                }
            }
        } catch (exception: Exception) {
            _exception.value = "Something went wrong while retrieving quiz."
        }
    }
}