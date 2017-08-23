package com.codepath.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.codepath.simpletodoapp.utils.FileUtility;

public class EditItemActivity extends AppCompatActivity {
    private int itemIndex = 0;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        itemIndex = getIntent().getIntExtra("position", 0);
        String selectedItem = getIntent().getStringExtra("item");

        editText = (EditText) findViewById(R.id.editItemText);
        editText.setText(selectedItem);
    }

    public void onSaveItem(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("item", editText.getText().toString());
        resultIntent.putExtra("position", itemIndex);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
