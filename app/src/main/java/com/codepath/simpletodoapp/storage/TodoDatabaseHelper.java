package com.codepath.simpletodoapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodoapp.models.Task;

import java.util.ArrayList;

/**
 * Database helper class. Creates and upgrades database
 *
 * @author tejalpar
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = TodoDatabaseHelper.class.getSimpleName();

    // Name and version of DB
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoDB";

    // Table
    private static final String TASK_TABLE = "tasks";

    // Column
    private static final String TASK_ID_KEY = "taskId";
    private static final String TASK_NAME = "taskName";

    // DB helper
    private static TodoDatabaseHelper sInstance;

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TASK_TABLE = "CREATE TABLE IF NOT EXISTS " + TASK_TABLE + " ("
                + TASK_ID_KEY +  " INTEGER PRIMARY KEY,"
                + TASK_NAME + " TEXT"
                + " );";

        sqLiteDatabase.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        if (oldVer != newVer) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public Task addTask(String taskName) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        Task task = null;

        try {

            ContentValues values = new ContentValues();
            values.put(TASK_NAME, taskName);

            long taskId = db.insertOrThrow(TASK_TABLE, null, values);
            db.setTransactionSuccessful();

            task = new Task();
            task.taskId = taskId;
            task.taskName = taskName;

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task info to DB");
        } finally {
            db.endTransaction();
        }
        return task;
    }

    public boolean updateTask(Task task) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {

            ContentValues values = new ContentValues();
            values.put(TASK_NAME, task.taskName);

            int rows = db.update(TASK_TABLE, values, TASK_ID_KEY + "= ?", new String[]{String.valueOf(task.taskId)});
            if (rows == 1) {
                db.setTransactionSuccessful();
                result = true;
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update task info to DB");
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public boolean deleteTask(long taskId) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {

            int rows = db.delete(TASK_TABLE, TASK_ID_KEY + "= ?", new String[]{String.valueOf(taskId)});
            if (rows == 1) {
                db.setTransactionSuccessful();
                result = true;
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to deleting task info from DB");
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        final String SELECT_FROM_TASK_TABLE = String.format("SELECT * FROM %s ", TASK_TABLE);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_FROM_TASK_TABLE, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.taskId = cursor.getInt(cursor.getColumnIndex(TASK_ID_KEY));
                    task.taskName = cursor.getString(cursor.getColumnIndex(TASK_NAME));

                    tasks.add(task);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while trying to getAll tasks.");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }
}
