package com.xubo.inputcodeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xubo.inputcodeviewlib.InputCodeView;

public class MainActivity extends AppCompatActivity {
    private InputCodeView demo1_cv;
    private Button demo1_get_btn;
    private Button demo1_clear_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demo1_cv = findViewById(R.id.demo1_cv);
        demo1_get_btn = findViewById(R.id.demo1_get_btn);
        demo1_clear_btn = findViewById(R.id.demo1_clear_btn);

        demo1_get_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = demo1_cv.getInputText();
                if ("".equals(text)) {
                    Toast.makeText(MainActivity.this, "没有输入任何内容", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "获取内容:" + text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        demo1_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo1_cv.clear();
            }
        });
    }
}
