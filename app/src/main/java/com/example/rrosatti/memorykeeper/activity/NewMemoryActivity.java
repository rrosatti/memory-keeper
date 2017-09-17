package com.example.rrosatti.memorykeeper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rrosatti.memorykeeper.R;

public class NewMemoryActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etLongDescription;
    private Button btAddImage;
    private Button btCancel;
    private Button btOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memory);

        iniViews();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void iniViews() {
        etTitle = (EditText) findViewById(R.id.activity_new_memory_et_title);
        etLongDescription = (EditText) findViewById(R.id.activity_new_memory_et_description);
        btAddImage = (Button) findViewById(R.id.activity_new_memory_bt_add_image);
        btCancel = (Button) findViewById(R.id.activity_new_memory_bt_cancel);
        btOk = (Button) findViewById(R.id.activity_new_memory_bt_ok);
    }
}
