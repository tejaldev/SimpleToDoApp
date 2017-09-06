package com.codepath.simpletodoapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.simpletodoapp.models.Task;
import com.codepath.simpletodoapp.storage.TodoDatabaseHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static String MODE_EXTRA_KEY = "mode";
    public static String INDEX_EXTRA_KEY = "index";

    @IntDef({Mode.ADD, Mode.EDIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int ADD = 0;
        int EDIT = 1;
    }

    private int mode;
    private int position;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDayOfMonth;

    private boolean isDateSet;
    private Task selectedTask;
    private TodoDatabaseHelper todoDatabaseHelper;
    private TextView selectedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText descriptionText = (EditText) findViewById(R.id.description_edit_text);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        final ImageButton addDateButton = (ImageButton) findViewById(R.id.add_date_button);
        selectedDateTextView = (TextView) findViewById(R.id.selected_date_label);

        mode = getIntent().getExtras().getInt(MODE_EXTRA_KEY);
        position = getIntent().getExtras().getInt(INDEX_EXTRA_KEY);
        if (mode == Mode.EDIT) {
            // populate fields else do nothing
            selectedTask = getIntent().getExtras().getParcelable(Task.PARCEL_KEY);
            if (selectedTask != null) {
                descriptionText.setText(selectedTask.taskName);
                selectedDateTextView.setText(getFormattedDateFromDate(selectedTask.taskDate));
                selectedYear = selectedTask.taskDate.getYear();
                selectedMonth = selectedTask.taskDate.getMonth();
                selectedDayOfMonth = selectedTask.taskDate.getDate();
            }
        }

        todoDatabaseHelper = TodoDatabaseHelper.getInstance(this);
        datePickerDialog.setOnDateSetListener(AddActivity.this);
        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open date picker dialog
                datePickerDialog.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(descriptionText.getText().toString())) {
                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);
                    finish();
                }

                Task task = null;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(AddActivity.MODE_EXTRA_KEY, mode);

                switch (mode) {
                    case Mode.EDIT:
                        task = selectedTask;
                        task.taskName = descriptionText.getText().toString();
                        if (isDateSet) {
                            task.taskDate = new Date(selectedYear - 1900, selectedMonth, selectedDayOfMonth);
                        }
                        todoDatabaseHelper.updateTask(task);
                        resultIntent.putExtra(AddActivity.INDEX_EXTRA_KEY, position);
                        break;
                    default:
                        task = todoDatabaseHelper.addTask(new Task(descriptionText.getText().toString(),
                                new Date(selectedYear - 1900, selectedMonth, selectedDayOfMonth)));
                        break;
                }
                resultIntent.putExtra(Task.PARCEL_KEY, task);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedYear = year;
        selectedMonth = month;
        selectedDayOfMonth = dayOfMonth;
        isDateSet = true;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        selectedDateTextView.setText(simpleDateFormat.format(new Date(selectedYear - 1900, selectedMonth, selectedDayOfMonth)));
    }

    private String getFormattedDateFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
