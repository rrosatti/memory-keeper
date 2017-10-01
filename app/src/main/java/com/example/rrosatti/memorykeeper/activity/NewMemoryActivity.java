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
        etTitle = (EditText) findViewById(R.id.activityNewMemoryEtTitle);
        etLongDescription = (EditText) findViewById(R.id.activityNewMemoryEtDescription);
        btAddImage = (Button) findViewById(R.id.activityNewMemoryBtAddImage);
        btCancel = (Button) findViewById(R.id.activityNewMemoryBtCancel);
        btOk = (Button) findViewById(R.id.activityNewMemoryBtOk);
    }
}
