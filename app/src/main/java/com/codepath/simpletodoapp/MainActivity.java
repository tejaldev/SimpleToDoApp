package com.codepath.simpletodoapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.simpletodoapp.adapter.ListItemAdapter;
import com.codepath.simpletodoapp.fragment.EditTaskDialogFragment;
import com.codepath.simpletodoapp.models.Task;
import com.codepath.simpletodoapp.storage.TodoDatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditTaskDialogFragment.EditTaskResultListener {

    private ListView listView;
    private ListItemAdapter itemAdapter;
    private TodoDatabaseHelper todoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewItems);
        itemAdapter = new ListItemAdapter(this, getAllTasksFromDatabase());
        listView.setAdapter(itemAdapter);

        // to edit item
        setupListViewOnClickListener();

        // to remove item
        setupListViewLongClickListener();
    }

    public void onAddItem(View view) {
        EditText editText = (EditText) findViewById(R.id.newItemText);

        // add to db
        // fetch from db
        // update adapter items
        final Task task = todoDatabaseHelper.addTask(editText.getText().toString());
        if (task != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemAdapter.add(task);
                    itemAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(view.getContext(), "Error adding item", Toast.LENGTH_LONG).show();
        }
        resetText(editText);
    }

    public void showEditDialog(Task editTask, int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(editTask, position);
        editTaskDialogFragment.show(fragmentManager, "fragment_edit_dialog");
    }

    private void setupListViewOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Task selectedItem = itemAdapter.getItem(position);
                showEditDialog(selectedItem, position);
            }
        });
    }

    private void setupListViewLongClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                deleteItemFromList(itemAdapter.getItem(position));
                return true;
            }
        });
    }

    private void resetText(EditText editText) {
        editText.setText("");
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

    private void deleteItemFromList(Task selectedItem) {
        boolean result = todoDatabaseHelper.deleteTask(selectedItem.taskId);
        if (result) {
            itemAdapter.remove(selectedItem);
            itemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditCompleted(Task editedTask, int index) {
        todoDatabaseHelper.updateTask(editedTask);
        itemAdapter.update(editedTask, index);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel() {
        // as of now do nothing
    }
}
