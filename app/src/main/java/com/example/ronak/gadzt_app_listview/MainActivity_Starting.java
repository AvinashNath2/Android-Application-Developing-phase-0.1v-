package com.example.ronak.gadzt_app_listview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity_Starting extends AppCompatActivity {

    private LinearLayout mobile_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__starting);

        mobile_view = (LinearLayout)findViewById(R.id.mobile_gadgets);

        mobile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_Starting.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
}
