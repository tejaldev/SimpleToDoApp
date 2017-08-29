package com.codepath.simpletodoapp.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Class which holds task details
 *
 * @author tejalpar
 */
public class Task implements Parcelable {
    public static final String PARCEL_KEY = "TASK_INFO";

    public long taskId;
    public String taskName;

    public Task(Parcel source) {
        // read values from parcel
        readFromParcel(source);
    }

    public Task() {
        // default constructor
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(taskId);
        dest.writeString(taskName);
    }

    public static final Parcelable.Creator<Task> CREATOR
            = new Parcelable.Creator<Task>() {

        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[0];
        }
    };

    private void readFromParcel(Parcel source) {
        // read inorder
        taskId = source.readLong();
        taskName = source.readString();
    }
}
