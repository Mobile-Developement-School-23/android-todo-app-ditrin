package com.example.todolist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoItemBinding
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem
import com.template.todoapp.ui.task_screen.spinner_adapter.TodoItemSpinnerAdapter
import java.lang.RuntimeException

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
        //parseParams()
        viewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        setupSpinner()
        // initViews(view)
        addTextChangeListeners()
        launchRightMode()
        //  observeViewModel()

        binding.close.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun observeViewModel() {

    }


    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getTodoItemId(id)
        viewModel.todoItem.observe(viewLifecycleOwner) {
            etText.setText(it.text)

        }
    }

    private fun launchAddMode() {
        binding.save.setOnClickListener {
            viewModel.addTodoItem(etText.text?.toString(), 2324324L, Importance.COMMON, 28398)
        }
    }

    private fun parseParams() {
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Param screen mode is absent")
        }
        if (screenMode == MODE_EDIT && id == TodoItem.UNDEFINED_ID) {
            throw RuntimeException("Param todo item id is absent")
        }
    }

    private fun initViews(view: View) {
        etText = view.findViewById(R.id.text)
    }

    private fun addTextChangeListeners() {
        binding.textFrame.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
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

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): TodoItemFragment{
            return TodoItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem(id: String): TodoItemFragment{
            return TodoItemFragment(MODE_EDIT)
        }

        private const val ARG_ID: String = "ARG_ID"

        fun getInstance(id: String): Fragment {
            return TodoItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                    Log.d("dark", "$id")
                }
            }
        }
    }


}