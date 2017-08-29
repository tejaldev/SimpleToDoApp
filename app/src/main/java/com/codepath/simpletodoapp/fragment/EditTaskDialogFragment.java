package com.codepath.simpletodoapp.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.simpletodoapp.R;
import com.codepath.simpletodoapp.models.Task;

/**
 * This Dialog fragment handles task edit functionality
 *
 * @author tejalpar
 */
public class EditTaskDialogFragment extends DialogFragment {

    private int itemIndex;
    private Task taskToEdit;
    private EditText editText;

    public interface EditTaskResultListener {
        void onEditCompleted(Task editedTask, int index);

        void onCancel();
    }

    public EditTaskDialogFragment() {
        // Required empty public constructor
    }

    public static EditTaskDialogFragment newInstance(Task selectedTask, int index) {
        Bundle args = new Bundle();

        EditTaskDialogFragment fragment = new EditTaskDialogFragment();
        args.putInt("index", index);
        args.putParcelable(Task.PARCEL_KEY, selectedTask);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            itemIndex = getArguments().getInt("index");
            taskToEdit = getArguments().getParcelable(Task.PARCEL_KEY);
        }

        setButtonClickListener(view);
        editText = (EditText) view.findViewById(R.id.editItemText);
        getDialog().setTitle("Edit name");
        editText.setText(taskToEdit.taskName);
        editText.requestFocus();
    }

    public void setButtonClickListener(final View rootView) {
        final Button editButton = (Button) rootView.findViewById(R.id.btnEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskToEdit.taskName = editText.getText().toString();
                EditTaskResultListener listener = (EditTaskResultListener) getActivity();
                listener.onEditCompleted(taskToEdit, itemIndex);
                dismiss();
            }
        });

        Button cancelButton = (Button) rootView.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaskResultListener listener = (EditTaskResultListener) getActivity();
                listener.onCancel();
                dismiss();
            }
        });
    }
}
