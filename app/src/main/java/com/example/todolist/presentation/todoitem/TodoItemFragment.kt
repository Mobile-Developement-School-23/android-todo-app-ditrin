package com.example.todolist.presentation.todoitem

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoItemBinding
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.util.Locale

class TodoItemFragment : Fragment(R.layout.fragment_todo_item) {

    private lateinit var binding: FragmentTodoItemBinding
    private lateinit var viewModel: TodoItemViewModel

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
        viewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]

        setupSpinner()
        addTextChangeListeners()
        launchRightMode()

        binding.close.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.todoItemFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { todoItem ->
                if (todoItem != null) {
                    changeUi(todoItem)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

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
        when (val localScreenMode = screenMode) {
            is TodoItemScreenMode.Add -> launchAddMode()
            is TodoItemScreenMode.Edit -> launchEditMode(id = localScreenMode.id)
        }
    }

    private fun launchEditMode(id: String) {
        viewModel.onItemIdGot(id)
        binding.save.setOnClickListener {
            viewModel.editTodoItem("df", 12L, Importance.LOW, 12)
            activity?.onBackPressed()
        }
    }

    private fun launchAddMode() {
        viewModel.todoItem.observe(viewLifecycleOwner) {
            binding.save.setOnClickListener {
                viewModel.addTodoItem(binding.textFrame.text?.toString(), 2324324L, Importance.COMMON, 28398)
            }
        }
    }

    private fun changeUi(todoItem: TodoItem) {
        with(binding) {
            textFrame.setText(todoItem.text)
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

    private fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
        val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
        return df.format(this)
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
    data object Add : TodoItemScreenMode

    @Parcelize
    data class Edit(
        val id: String,
    ) : TodoItemScreenMode
}
