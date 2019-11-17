package com.example.pbl_movie_4jo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        session = new UserSessionManager(getApplicationContext());

        final Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        final Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Button button2 = findViewById(R.id.button2);
        if(session.isUserLoggedIn()){
            button2.setText("로그아웃 테스트");
            Log.e("Test", "로그인됨");
        }else {
            button2.setText("로그인 테스트");
            Log.e("Test", "로그인안됨");
        }
    }
}
