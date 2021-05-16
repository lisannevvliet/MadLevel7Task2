package com.example.madlevel7task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentQuizBinding
import com.example.madlevel7task2.model.Quiz
import com.example.madlevel7task2.vm.QuizViewModel

// A simple [Fragment] subclass as the second destination in the navigation.
class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    private lateinit var quizzes: ArrayList<Quiz>
    private lateinit var quiz: Quiz

    // Keep track of the index by counting the amount of correct answers.
    private var index = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment.
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fill the ArrayList of this fragment with the LiveData quizzes and show the first quiz.
        viewModel.quizzes.observe(viewLifecycleOwner) {
            quizzes = it
            updateView()
        }

        binding.btnConfirm.setOnClickListener {
            onConfirm()
        }
    }

    // Bind the ImageView and TextViews to the corresponding building picture, progress indicator, question and answers.
    private fun updateView() {
        // Update the quiz variable to store the current quiz object.
        quiz = quizzes[index - 1]

        val building = resources.getIdentifier("building${index}", "drawable", activity?.packageName)

        binding.ivBuilding.setImageResource(building)
        binding.tvIndex.text = getString(R.string.index, index, quizzes.size)
        binding.tvQuestion.text = quiz.question
        binding.rbFirstAnswer.text = quiz.answers[0]
        binding.rbSecondAnswer.text = quiz.answers[1]
        binding.rbThirdAnswer.text = quiz.answers[2]
    }

    // Move onto the next quiz if the selected answer is correct.
    private fun onConfirm() {
        // Check if any RadioButton is selected.
        if (binding.rgAnswers.checkedRadioButtonId != -1) {
            // Check if there is a next question.
            if (index < quizzes.size) {
                val answer = when {
                    binding.rbFirstAnswer.isChecked -> { 1 }
                    binding.rbSecondAnswer.isChecked -> { 2 }
                    else -> { 3 }
                }

                // Check if the selected answer is equal to the correct answer.
                if (answer == quiz.correctAnswer) {
                    // Deselect the previously selected RadioButton.
                    binding.rgAnswers.clearCheck()

                    // Increment the index by one.
                    index ++

                    // Show the next quiz.
                    updateView()
                } else {
                    // Deselect the previously selected RadioButton.
                    binding.rgAnswers.clearCheck()

                    Toast.makeText(context, "Wrong Answer", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Quiz Completed", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    // Release the view if the fragment is destroyed to prevent a memory leak.
    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}