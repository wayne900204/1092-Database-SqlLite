package com.example.sqllite_crud;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton addUser;
    MyAdapter myAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    UserSql userSql = new UserSql(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialById();

        dataList = userSql.getData();
        settingAppBar();
        settingRecycleView();
    }

    private void initialById(){
        recyclerView = findViewById(R.id.recycler);
        addUser = findViewById(R.id.btn_AddUser);
    }
    // BuildToolBar
    private void settingAppBar() {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDialog();
            }
        });
    }

    private void settingRecycleView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        myAdapter = new MyAdapter(dataList,userSql,MainActivity.this);
        recyclerView.setAdapter(myAdapter);
    }


    private void _showDialog() {
        final EditText firstName,lastName;
        final Button btnAdd;
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        firstName = dialog.findViewById(R.id.firstName);
        lastName = dialog.findViewById(R.id.lastName);
        btnAdd = dialog.findViewById(R.id.add);
        dialog.getWindow().setAttributes(lp);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty()) {
                    myAdapter.addItem(firstName.getText().toString(), lastName.getText().toString(),userSql);
                    firstName.setText(null);
                    lastName.setText(null);
                    dialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "此處不能為空白", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });
        dialog.show();
    }
}