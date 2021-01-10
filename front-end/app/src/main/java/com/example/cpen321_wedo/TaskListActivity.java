package com.example.cpen321_wedo;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cpen321_wedo.adapter.TaskListAdapter;
import com.example.cpen321_wedo.models.TaskList;
import com.example.cpen321_wedo.singleton.RequestQueueSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements UpdateTasklistInterface {
    private List<TaskList> lstTaskList;
    private TaskListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TaskList");

        FloatingActionButton fab = findViewById(R.id.fab_tasklist);

        lstTaskList = new ArrayList<>();

        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        myAdapter = new TaskListAdapter(this, lstTaskList, this);
        myrv.setLayoutManager((new StaggeredGridLayoutManager(1, 1)));

        myrv.setAdapter(myAdapter);

        // drag and drop
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAbsoluteAdapterPosition();
                int position_target = target.getAbsoluteAdapterPosition();

                Collections.swap(lstTaskList, position_dragged, position_target);
                myAdapter.notifyItemMoved(position_dragged, position_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("test", "You can swiping " + direction);
            }
        });

        itemTouchHelper.attachToRecyclerView(myrv);


        // fab button clicked:
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskListActivity.this, AddTaskListActivity.class);
                startActivityForResult(intent, 2);
            }
        });

    }

    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data==null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2 && data.hasExtra("json"))
        {
            try {
                JSONObject mJsonObject = new JSONObject(data.getStringExtra("json"));

                TaskList taskList = new TaskList(mJsonObject.getString("taskListName"), mJsonObject.getString("taskListDescription"), mJsonObject.getString("taskListID"));
                lstTaskList.add(taskList);
                myAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void helper(String id) {
        Intent intent = new Intent(TaskListActivity.this, AddTaskListActivity.class);
        intent.putExtra("add" ,false);
        intent.putExtra("taskListID", id);
        startActivityForResult(intent, 3);
    }

}
