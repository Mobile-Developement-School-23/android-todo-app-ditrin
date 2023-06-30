package com.example.todolist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.FragmentMainScreenBinding
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private lateinit var viewModel: MainScreenViewModel

    private lateinit var mainScreenAdapter: MainScreenAdapter

    private lateinit var binding: FragmentMainScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTodoListRecycler(binding.todoList)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        viewModel.todoList.observe(viewLifecycleOwner) {
            mainScreenAdapter.submitList(it)
            fillCompleteString(it)
        }

        viewModel.todoList.observe(viewLifecycleOwner) {
            mainScreenAdapter.setOnClickListener { id ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, TodoItemFragment.getInstance(id.id))
                    .addToBackStack(MainScreenFragment().tag)
                    .commit()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.changeVisible.collectLatest { complete ->
                    val todoItems: List<TodoItem>? = if (complete) {
                        viewModel.todoList.value?.filter { todoItem -> !todoItem.isCompleted }
                    } else {
                        viewModel.todoList.value
                    }
                    mainScreenAdapter.submitList(todoItems)
                }
            }
        }

        binding.todoVisibility.setOnClickListener {
            binding.todoVisibility.isActivated = !binding.todoVisibility.isActivated
            viewModel.visible = binding.todoVisibility.isActivated
        }

        binding.buttonAddTodo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TodoItemFragment())
                .addToBackStack(MainScreenFragment().tag)
                .commit()
        }
    }

    private fun fillCompleteString(todoList: List<TodoItem>) {
        val completeCounter = todoList.filter { it.isCompleted }
        binding.completeTodo.text =
            String.format(getString(R.string.completed_counter), completeCounter.size)
    }

    private fun setupTodoListRecycler(rvTodoList: RecyclerView) {
        with(rvTodoList) {
            mainScreenAdapter = MainScreenAdapter()
            adapter = mainScreenAdapter
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvTodoList)
    }

    private fun setupSwipeListener(rvTodoList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = mainScreenAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteTodoItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTodoList)
    }

    private fun setupClickListener() {
        mainScreenAdapter.onTodoClickListener = { id ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TodoItemFragment.getInstance(id.id))
                .addToBackStack(MainScreenFragment().tag)
                .commit()
        }
    }

    private fun setupLongClickListener() {
        mainScreenAdapter.onTodoLongClickListener = {
            viewModel.changeCompletedState(it)
        }
    }
}
