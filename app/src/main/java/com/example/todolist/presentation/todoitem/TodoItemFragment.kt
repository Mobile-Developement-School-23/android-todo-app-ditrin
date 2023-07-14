package com.example.todolist.presentation.todoitem

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.TodoListApplication
import com.example.todolist.databinding.FragmentTodoItemBinding
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem
import com.example.todolist.parseDate
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.*

class TodoItemFragment : Fragment(R.layout.fragment_todo_item) {

    private lateinit var binding: FragmentTodoItemBinding
    private val viewModel: TodoItemViewModel by viewModels {
        (requireActivity().application as TodoListApplication).todoItemViewModelFactory
    }

    @Suppress("DEPRECATION")
    private val screenMode: TodoItemScreenMode by lazy {
        requireArguments().getParcelable(KEY_SCREEN_MODE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchRightMode()
        setupSpinner()
        addTextChangeListeners()

        binding.close.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoItem.collect { todoItem ->
                if (todoItem != null) {
                    setupCurrentItem(todoItem)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deadline.collect {
                if (it != null) {
                    binding.deadline.text = it.parseDate()
                }
            }
        }

        binding.switcher.setOnCheckedChangeListener { _, isChecked ->
            binding.deadline.isVisible = isChecked
            binding.calendar.isVisible = isChecked

            if (isChecked) {
                if (viewModel.deadline.value == null) {
                    viewModel.setDeadline(binding.calendar.date)
                }
            } else {
                viewModel.setDeadline(null)
            }
        }

        binding.delete.setOnClickListener {
            val item = viewModel.todoItem.value
            if (item != null) {
                viewModel.deleteTodoItem(item)
            }
            activity?.onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (val localScreenMode = screenMode) {
            is TodoItemScreenMode.Add -> {
                Toast.makeText(requireContext(), "ADD", Toast.LENGTH_SHORT).show()
                launchAddMode()

            }

            is TodoItemScreenMode.Edit -> {
                Toast.makeText(requireContext(), "EDIT", Toast.LENGTH_SHORT).show()
                launchEditMode(id = localScreenMode.id)
            }
        }
    }

    private fun launchEditMode(id: String) {
        viewModel.setTodoItemId(id)

        binding.save.setOnClickListener {
            val importance = when (binding.importanceSpinner.selectedItemPosition) {
                0 -> Importance.COMMON
                1 -> Importance.LOW
                2 -> Importance.HIGH
                else -> {
                    throw RuntimeException()
                }
            }

            viewModel.editTodoItem(
                binding.textFrame.text.toString(),
                binding.calendar.date,
                importance
            )
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.setDeadline(
                Calendar.getInstance().also { it.set(year, month, dayOfMonth) }.timeInMillis
            )
        }
    }

    private fun launchAddMode() {
        binding.save.setOnClickListener {
            val importance = when (binding.importanceSpinner.selectedItemPosition) {
                0 -> Importance.COMMON
                1 -> Importance.LOW
                2 -> Importance.HIGH
                else -> {
                    throw RuntimeException()
                }
            }
            viewModel.addTodoItem(
                binding.textFrame.text.toString(),
                importance,
                Calendar.getInstance().timeInMillis
            )
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.calendar.setOnDateChangeListener() { _, year, month, dayOfMonth ->
            viewModel.setDeadline(
                Calendar.getInstance().also { it.set(year, month, dayOfMonth) }.timeInMillis
            )
        }
    }

    private fun setupCurrentItem(todoItem: TodoItem) {
        viewModel.setTodoItem(todoItem)
        with(binding) {
            textFrame.setText(todoItem.text)
            viewModel.setText(todoItem.text)
            when (todoItem.importance) {
                Importance.COMMON -> binding.importanceSpinner.setSelection(0)
                Importance.LOW -> binding.importanceSpinner.setSelection(1)
                Importance.HIGH -> binding.importanceSpinner.setSelection(2)
            }

            deadline.isVisible = todoItem.deadline != null
            switcher.isChecked = todoItem.deadline != null
            viewModel.setDeadline(todoItem.deadline)
            if (todoItem.deadline != null) {
                calendar.isVisible = true
                calendar.date = todoItem.deadline
            }
        }
    }

    private fun addTextChangeListeners() {
        binding.textFrame.doAfterTextChanged {
            it?.let {
                viewModel.resetErrorInputText()
            }
        }
    }

    private fun setupSpinner() {
        val spinnerAdapter = TodoItemSpinnerAdapter(
            requireContext(),
            resources.getStringArray(R.array.importance)
        )
        binding.importanceSpinner.adapter = spinnerAdapter
        binding.importanceSpinner.setSelection(0)
    }

    companion object {

        private const val KEY_SCREEN_MODE = "KEY_SCREEN_MODE"

        fun newInstance(
            mode: TodoItemScreenMode,
        ): Fragment {
            return TodoItemFragment().also { fragment ->
                fragment.arguments = bundleOf(
                    KEY_SCREEN_MODE to mode,
                )
            }
        }
    }
}

sealed interface TodoItemScreenMode : Parcelable {

    @Parcelize
    object Add : TodoItemScreenMode

    @Parcelize
    data class Edit(
        val id: String,
    ) : TodoItemScreenMode
}
