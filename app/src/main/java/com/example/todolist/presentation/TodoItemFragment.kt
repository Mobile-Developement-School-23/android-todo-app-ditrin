package com.example.todolist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoItemBinding
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem
import com.template.todoapp.ui.task_screen.spinner_adapter.TodoItemSpinnerAdapter
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Locale

class TodoItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val id: String = TodoItem.UNDEFINED_ID
) : Fragment(R.layout.fragment_todo_item) {
    private lateinit var binding: FragmentTodoItemBinding
    private lateinit var viewModel: TodoItemViewModel
    private lateinit var etText: EditText

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
        viewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        setupSpinner()
        addTextChangeListeners()
        launchRightMode()

        binding.close.setOnClickListener {
            activity?.onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoItemState.collect { todoItemNull ->
                todoItemNull?.let { todoItem ->
                    changeUi(todoItem)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deadline.collect {
                if (it != null) {
                    binding.deadline.text = it.toDateString()
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
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getTodoItemId(id)
        binding.save.setOnClickListener {
            viewModel.editTodoItem("df", 12L, Importance.LOW, 12)
            activity?.onBackPressed()
        }
    }

    private fun launchAddMode() {
        viewModel.todoItem.observe(viewLifecycleOwner) {
            binding.save.setOnClickListener {
                viewModel.addTodoItem(etText.text?.toString(), 2324324L, Importance.COMMON, 28398)
            }
        }
    }

    private fun changeUi(todoItem: TodoItem) {
        with(binding) {
            etText.setText(todoItem.text)
            viewModel.setTaskText(todoItem.text)
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
        binding.textFrame.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputText()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setupSpinner() {
        val spinnerAdapter = TodoItemSpinnerAdapter(
            requireContext(),
            resources.getStringArray(R.array.importance)
        )
        binding.importanceSpinner.adapter = spinnerAdapter
        binding.importanceSpinner.setSelection(0)
    }

    fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
        val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
        return df.format(this)
    }

    companion object {

        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        private const val ARG_ID: String = "ARG_ID"

        fun getInstance(mode: String): Fragment {
            when (mode) {
                MODE_ADD -> return TodoItemFragment(MODE_ADD, ARG_ID)
                else -> {
                    return TodoItemFragment(MODE_EDIT, mode).apply {
                        arguments = Bundle().apply {
                            putString(ARG_ID, id)
                        }
                    }
                }
            }
        }
    }
}