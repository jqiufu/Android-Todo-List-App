package com.example.cpen321_wedo.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cpen321_wedo.adapter.TaskAdapter;
import com.example.cpen321_wedo.models.Task;
import com.example.cpen321_wedo.R;
import com.example.cpen321_wedo.singleton.RequestQueueSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class TaskFragment extends Fragment {

    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;
    private final String taskListId;

    public TaskFragment(String taskListId) {
        this.taskListId = taskListId;
        tasks = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView taskRecyclerView;
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        taskRecyclerView = view.findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskRecyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecor = new DividerItemDecoration(Objects.requireNonNull(getContext()), VERTICAL);
        taskRecyclerView.addItemDecoration(itemDecor);

        taskAdapter = new TaskAdapter(getContext(), getActivity(), taskListId);
        taskAdapter.setTasks(tasks);

        taskRecyclerView.setAdapter(taskAdapter);

        return view;
    }

    public void addTask(Task task) {
        taskAdapter.addTask(task);
    }

    public void updateTask(String taskName, String taskType, String taskDescription, String taskLocation, long dateModifiedInMilliSeconds, int position) {
        taskAdapter.updateTask(taskName, taskType, taskDescription, taskLocation, dateModifiedInMilliSeconds, position);
    }

    public void toggleItemViewType () { taskAdapter.toggleItemViewType(); }

    public void deleteTasksSelected() {
        taskAdapter.deleteTasksSelected();
    }

    public void setMenu(Menu menu) {
        this.taskAdapter.setMenu(menu);
    }
}
