package com.example.todolist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.todoListTemp
import com.example.todolist.databinding.FragmentMainScreenBinding

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
            mainScreenAdapter.todoList = it
        }
    }

    private fun setupTodoListRecycler(rvTodoList: RecyclerView) {
        with(rvTodoList) {
            mainScreenAdapter = MainScreenAdapter().apply { todoList = todoListTemp }
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
                val item = mainScreenAdapter.todoList[viewHolder.adapterPosition]
                viewModel.deleteTodoItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTodoList)
    }

    private fun setupClickListener() {
        mainScreenAdapter.onTodoClickListener = {
            //переход на экран редактирования
        }
    }

    private fun setupLongClickListener() {
        mainScreenAdapter.onTodoLongClickListener = {
            viewModel.changeCompletedState(it)
        }
    }
}