package com.codepath.simpletodoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.simpletodoapp.utils.FileUtility;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    private ListView listView;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewItems);

        items = FileUtility.readItemsFromFile(getFilesDir());
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemAdapter);

        // to remove item
        setupListViewLongClickListener();

        // to edit item
        setupListViewOnClickListener();
    }

    public void onAddItem(View view) {
        EditText editText = (EditText) findViewById(R.id.newItemText);
        itemAdapter.add(editText.getText().toString());
        editText.setText("");
        FileUtility.writeItemsToFile(getFilesDir(), items);
    }

    public void launchEditActivity(String item, int position) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setupListViewLongClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                items.remove(position);
                itemAdapter.notifyDataSetChanged();
                FileUtility.writeItemsToFile(getFilesDir(), items);
                return true;
            }
        });
    }

    private void setupListViewOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = itemAdapter.getItem(position);
                launchEditActivity(selectedItem, position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String item = data.getExtras().getString("item");
            int position = data.getExtras().getInt("position", 0);

            // modify item list
            items.set(position, item);
            itemAdapter.notifyDataSetChanged();
            FileUtility.writeItemsToFile(getFilesDir(), items);
        }
    }
}
