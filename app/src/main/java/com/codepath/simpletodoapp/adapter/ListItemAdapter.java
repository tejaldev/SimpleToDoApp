package com.codepath.simpletodoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodoapp.R;
import com.codepath.simpletodoapp.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * List item adapter
 *
 * @author tejalpar
 */
public class ListItemAdapter extends ArrayAdapter<Task> {

    private List<Task> taskList;

    public ListItemAdapter(Context context, ArrayList<Task> taskList) {
        super(context, R.layout.task_row_layout, taskList);
        this.taskList = taskList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder holder = null;

        if (convertView == null) {
            /* means create an item from inflater */

            // inflate layout for convertView
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.task_row_layout, parent, false);

            // populate holder & add it as a tag to convertView to be used later
            holder = new ListViewHolder();
            holder.taskItemText = (TextView) convertView.findViewById(R.id.task_item_text);
            convertView.setTag(holder);

        } else {
            // just retrieve holder from tag
            holder = (ListViewHolder) convertView.getTag();
        }

        // now bind data to view
        holder.taskItemText.setText(taskList.get(position).taskName);

        return convertView;
    }

    public void update(Task object, int index) {
        taskList.set(index, object);
    }

    private static class ListViewHolder {
        public TextView taskItemText;
    }
}
