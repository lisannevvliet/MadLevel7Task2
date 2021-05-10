package com.example.madlevel7task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentQuizBinding
import com.example.madlevel7task2.vm.QuizViewModel

// A simple [Fragment] subclass as the second destination in the navigation.
class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    // Keep track of the progress by counting the amount of correct answers.
    private var progress = 1

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the first quiz upon the start of this fragment.
        viewModel.getQuiz(progress.toString())

        observeQuiz()

        binding.btnConfirm.setOnClickListener {
            onConfirm()
        }
    }

    // Upon a change in the LiveData, bind the ImageView and TextViews to the corresponding building picture, progress indicator, question and answers.
    private fun observeQuiz() {
        viewModel.quiz.observe(viewLifecycleOwner, {
            val building = resources.getIdentifier("building${progress}", "drawable", activity?.packageName)

            binding.ivBuilding.setImageResource(building)
            binding.tvProgress.text = getString(R.string.progress, progress.toString(), "5")
            binding.tvQuestion.text = it.question
            binding.rbFirstAnswer.text = it.firstAnswer
            binding.rbSecondAnswer.text = it.secondAnswer
            binding.rbThirdAnswer.text = it.thirdAnswer
        })
    }

    // Check if the answer is correct and move onto the next question if it is.
    private fun onConfirm() {
        // Check if any RadioButton is selected.
        val id: Int = binding.rgAnswers.checkedRadioButtonId

        if (id != -1) {
            // Get the instance of RadioButton.
            val radio: RadioButton = requireView().findViewById(id)

            if (viewModel.correctAnswer(radio.text.toString())) {
                // Deselect the previously selected RadioButton.
                binding.rgAnswers.clearCheck()

                // Increment the progress by one.
                progress ++

                if (progress <= 5) {
                    viewModel.getQuiz(progress.toString())
                } else {
                    Toast.makeText(context, "Quiz Completed", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(context, "Wrong Answer", Toast.LENGTH_LONG).show()
            }
        }
    }
}