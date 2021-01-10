package com.example.cpen321_wedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cpen321_wedo.singleton.RequestQueueSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTaskListActivity extends AppCompatActivity {

    private MaterialEditText tasklistName;
    private MaterialEditText tasklistDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_list);

        tasklistName = findViewById(R.id.tasklist_name);
        tasklistDescription = findViewById(R.id.tasklist_description);
        Button btn_created = findViewById(R.id.btn_add_tasklist);

        btn_created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_tasklistName = tasklistName.getText().toString();
                String txt_description = tasklistDescription.getText().toString();
                if(TextUtils.isEmpty(txt_tasklistName) || TextUtils.isEmpty(txt_description)){
                    Toast.makeText(getApplicationContext(), "Must fill required fields", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent();
                    String taskListID = "SomeID";
                    final JSONObject object = new JSONObject();
                    try {
                        object.put("taskListName",txt_tasklistName);
                        object.put("taskListDescription",txt_description);
                        object.put("taskListID", taskListID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("json", object.toString());
                    setResult(2,intent);
                    finish();
                }
            }
        });
    }
}