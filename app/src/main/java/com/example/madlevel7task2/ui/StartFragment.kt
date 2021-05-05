package com.example.madlevel7task2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentStartBinding
import com.example.madlevel7task2.vm.QuizViewModel

// A simple [Fragment] subclass as the default destination in the navigation.
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    // Get an instance of Firestore.
    //private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createQuizzes()
        observeQuizCreation()

        // Navigate to the QuizFragment upon a click on the "Start quest" button.
        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_StartFragment_to_QuizFragment)
        }
    }

    // Create five quizzes.
    private fun createQuizzes() {
        // Delete all previously created quizzes.
        /*for (i in 1..5) {
            firestore.collection("quizzes").document(i.toString()).delete()
            Toast.makeText(activity, "Quiz deleted.", Toast.LENGTH_LONG).show()
        }*/

        viewModel.createQuiz("1", "Who is the co-founder of Android?", "Andy Rubin", "Larry Page & Sergey Brin", "Sundar Pichai", "Andy Rubin")
        viewModel.createQuiz("2", "What was the initial name of Elon Musk's child?", "X Æ A-Xii", "X AE A-XII", "X Æ A-12", "X Æ A-12")
        viewModel.createQuiz("3", "In which year was Apple founded?", "1982", "1976", "1993", "1976")
        viewModel.createQuiz("4", "What is Bill Gates' net worth?", "$123.7 billion", "$145.1 billion", "$254.3 billion", "$145.1 billion")
        viewModel.createQuiz("5", "What was Steve Jobs' cause of death?", "Amyotrophic lateral sclerosis (ALS)", "Dementia", "Neuroendocrine cancer", "Neuroendocrine cancer")
    }

    // Display a Toast message which tells the user whether the profile was successfully created.
    private fun observeQuizCreation() {
        viewModel.createSuccess.observe(viewLifecycleOwner, {
            //Toast.makeText(activity, "Quizzes created", Toast.LENGTH_LONG).show()
        })

        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }
}