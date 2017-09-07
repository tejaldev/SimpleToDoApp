package com.codepath.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;

import com.codepath.simpletodoapp.adapter.RecyclerViewItemAdapter;
import com.codepath.simpletodoapp.models.Task;
import com.codepath.simpletodoapp.storage.TodoDatabaseHelper;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements RecyclerViewItemAdapter.AdapterItemClickListener {

    private final int REQUEST_CODE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerViewItemAdapter recyclerViewItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TodoDatabaseHelper todoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_calendar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, AddActivity.class);
                launchAddActivity(intent, AddActivity.Mode.ADD);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.task_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLongClickable(true);

        // specify an adapter (see also next example)
        recyclerViewItemAdapter = new RecyclerViewItemAdapter(getAllTasksFromDatabase(), this);
        mRecyclerView.setAdapter(recyclerViewItemAdapter);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.requestLayout();
    }

    public void launchAddActivity(Intent intent, @AddActivity.Mode int mode) {
        intent.putExtra(AddActivity.MODE_EXTRA_KEY, mode);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private ArrayList<Task> getAllTasksFromDatabase() {
        createDatabaseHelper();
        return todoDatabaseHelper.getAllTasks();
    }

    private void createDatabaseHelper() {
        if (todoDatabaseHelper == null) {
            todoDatabaseHelper = TodoDatabaseHelper.getInstance(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            int mode = data.getIntExtra(AddActivity.MODE_EXTRA_KEY, 0);
            int pos = data.getIntExtra(AddActivity.INDEX_EXTRA_KEY, 0);
            Task task = data.getParcelableExtra(Task.PARCEL_KEY);

            switch (mode) {
                case AddActivity.Mode.EDIT:
                    recyclerViewItemAdapter.update(task, pos);
                    break;
                default:
                    // add
                    recyclerViewItemAdapter.add(task);
                    break;
            }
            recyclerViewItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(CalendarActivity.this, AddActivity.class);
        intent.putExtra(AddActivity.INDEX_EXTRA_KEY, position);
        intent.putExtra(Task.PARCEL_KEY, recyclerViewItemAdapter.getItemAtPosition(position));
        launchAddActivity(intent, AddActivity.Mode.EDIT);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        Task taskToRemove = recyclerViewItemAdapter.getItemAtPosition(position);
        if (todoDatabaseHelper.deleteTask(taskToRemove.taskId)) {
            recyclerViewItemAdapter.remove(position);
            recyclerViewItemAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }
}
