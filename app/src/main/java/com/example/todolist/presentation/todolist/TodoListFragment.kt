package com.example.todolist.presentation.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.TodoListApplication
import com.example.todolist.databinding.FragmentMainScreenBinding
import com.example.todolist.presentation.todoitem.TodoItemFragment
import com.example.todolist.presentation.todoitem.TodoItemScreenMode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import java.util.Collections

class TodoListFragment : Fragment(R.layout.fragment_main_screen) {

    private val viewModel: TodoListViewModel by viewModels {
        (requireActivity().application as TodoListApplication).todoListItemViewModelFactory
    }

    private val mainScreenAdapter: TodoListAdapter by lazy {
        TodoListAdapter(
            onItemClick = viewModel::onTodoItemClicked,
            onItemLongClick = viewModel::onTodoItemLongClicked,
        )
    }

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

        binding.todoVisibility.setOnClickListener {
            viewModel.onEyeClicked()

            viewModel.todoItemsFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { todoItems ->
                    mainScreenAdapter.submitList(todoItems)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

        viewModel.allTodoItemsFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { todoItems ->
                val int=  todoItems.filter { it.isCompleted }.size
                binding.completeTodo.text =
                    String.format(getString(R.string.completed_counter), int)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.buttonAddTodo.setOnClickListener {
            viewModel.onAddItemClicked()
        }

        viewModel.todoItemsFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { todoItems ->
                mainScreenAdapter.submitList(todoItems)

            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isEyeVisibleFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { isEyeVisible ->
                binding.todoVisibility.isActivated = isEyeVisible
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.navigationActionFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { navigationAction ->
                val fragment = when (navigationAction) {
                    is TodoItemsNavigationAction.OpenAddTodoItemScreen -> {
                        val launchMode = TodoItemScreenMode.Add
                        TodoItemFragment.newInstance(launchMode)
                    }

                    is TodoItemsNavigationAction.OpenEditTodoItemScreen -> {
                        val launchMode = TodoItemScreenMode.Edit(id = navigationAction.todoItem.id)
                        TodoItemFragment.newInstance(launchMode)
                    }
                }

                parentFragmentManager.commit {
                    replace(R.id.fragmentContainer, fragment)
                    addToBackStack(null)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupTodoListRecycler(rvTodoList: RecyclerView) {
        rvTodoList.adapter = mainScreenAdapter
        setupSwipeListener(rvTodoList)
        setupSwipeListenerLeft(rvTodoList)
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

                viewModel.onLeftToRightSwiped(item)
                val currentList = mainScreenAdapter.currentList.toMutableList()
                currentList.remove(item)
                mainScreenAdapter.submitList(currentList)
                viewModel.allTodoItemsFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach { todoItems ->
                        val int=  todoItems.filter { it.isCompleted }.size
                        binding.completeTodo.text =
                            String.format(getString(R.string.completed_counter), int)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTodoList)
    }


    private fun setupSwipeListenerLeft(rvTodoList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
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
                val newItem = item.copy(isCompleted = !item.isCompleted)
                mainScreenAdapter.notifyItemChanged(viewHolder.adapterPosition)
                viewModel.onRightToLeftSwiped(item)

                val currentList = mainScreenAdapter.currentList.toMutableList()
                Collections.replaceAll(currentList, item, newItem)
                mainScreenAdapter.submitList(currentList)

                viewModel.allTodoItemsFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach { todoItems ->
                        val int=  todoItems.filter { it.isCompleted }.size
                        binding.completeTodo.text =
                            String.format(getString(R.string.completed_counter), int)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTodoList)
    }
}
