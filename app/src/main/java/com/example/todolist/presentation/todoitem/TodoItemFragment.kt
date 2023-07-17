package com.example.todolist.presentation.todoitem

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.TodoListApplication
import com.example.todolist.presentation.compose.TaskScreenWithBottomDio
import com.example.todolist.presentation.compose.TodoAppTheme
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.*

class TodoItemFragment : Fragment(R.layout.fragment_todo_item) {

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

        return inflater.inflate(R.layout.fragment_todo_item, container, false)
            .apply {
                findViewById<ComposeView>(R.id.task_compose_screen).setContent {
                    TodoAppTheme {
                        TaskScreenWithBottomDio(viewModel::onAction, viewModel::state.get())
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchRightMode()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.closeScreen.collect {
                if (it) {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun launchRightMode() {
        when (val localScreenMode = screenMode) {
            is TodoItemScreenMode.Add -> {
                launchAddMode()
            }

            is TodoItemScreenMode.Edit -> {
                launchEditMode(id = localScreenMode.id)
            }
        }
    }

    private fun launchEditMode(id: String) {
        viewModel.setTodoItemId(id)
    }

    private fun launchAddMode() {
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
