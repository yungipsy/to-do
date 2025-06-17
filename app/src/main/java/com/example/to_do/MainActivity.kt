package com.example.to_do

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(tasks)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button).setOnClickListener {
            showAddTaskDialog()
        }

        setupSwipeToDelete()
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val taskInput = dialogView.findViewById<EditText>(R.id.taskInput)

        AlertDialog.Builder(this)
            .setTitle("Добавить задачу")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val taskTitle = taskInput.text.toString().trim()
                if (taskTitle.isNotEmpty()) {
                    adapter.addTask(Task(UUID.randomUUID().toString(), taskTitle))
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeTask(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }
}