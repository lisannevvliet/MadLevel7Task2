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

        // Delete the previously created quizzes and create new ones.
        viewModel.createQuizzes()

        // Display a Toast message if the quizzes were not successfully created.
        viewModel.exception.observe(viewLifecycleOwner, { Toast.makeText(activity, it, Toast.LENGTH_LONG).show() })

        // Put the quizzes in the corresponding LiveData and navigate to the QuizFragment upon a click on the "Start quest" button.
        binding.btnStart.setOnClickListener {
            viewModel.getQuizzes()

            findNavController().navigate(R.id.action_StartFragment_to_QuizFragment)
        }
    }

    // Release the view if the fragment is destroyed to prevent a memory leak.
    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}