package com.codepath.simpletodoapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class RecyclerViewItemAdapter extends RecyclerView.Adapter<RecyclerViewItemAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private static AdapterItemClickListener itemClickListener;

    public interface AdapterItemClickListener {
        void onItemClick(View view, int position);
        boolean onItemLongClick(View view, int position);
    }

    public RecyclerViewItemAdapter(ArrayList<Task> taskList, AdapterItemClickListener clickListener) {
        this.taskList = taskList;
        this.itemClickListener = clickListener;
    }

    @Override
    public RecyclerViewItemAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create view by inflating layout
        LinearLayout rootLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row_layout, parent, false);
        return new TaskViewHolder(rootLayout);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.taskItemTextView.setText(taskList.get(position).taskName);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public Task getItemAtPosition(int position) {
        return taskList.get(position);
    }

    public void add(Task object) {
        if (taskList != null) {
            taskList.add(object);
        } else {
            taskList = new ArrayList<>();
            taskList.add(object);
        }
        notifyDataSetChanged();
    }

    public void update(Task object, int index) {
        taskList.set(index, object);
        notifyItemChanged(index);
    }

    public void remove(int index) {
        Task object = taskList.remove(index);
        notifyItemRemoved(index);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView taskItemTextView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskItemTextView = (TextView) itemView.findViewById(R.id.task_item_text);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, this.getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return itemClickListener.onItemLongClick(v, this.getLayoutPosition());
        }
    }
}
