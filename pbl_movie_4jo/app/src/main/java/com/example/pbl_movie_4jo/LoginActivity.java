package com.example.pbl_movie_4jo;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());

        final EditText IDText = findViewById(R.id.IDText);
        final EditText PWText =  findViewById(R.id.PWText);
        final Button LoginButton =  findViewById(R.id.LoginButton);
        final TextView RegisterTV=  findViewById(R.id.RegisterTV);

        Toast logoutToast = Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT);
        final Toast logoinToast = Toast.makeText(getApplicationContext(), "Login Seccess", Toast.LENGTH_SHORT);
        logoutToast.setGravity(Gravity.CENTER, 0, -50);
        logoinToast.setGravity(Gravity.CENTER, 0, -50);

        if(session.isUserLoggedIn()){
            finish();
            logoutToast.show();
            session.logoutUser();
        }

        RegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(RegisterIntent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                final String userID = IDText.getText().toString();
                final String userPASSWORD = PWText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("IN_RESPONSE", response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                String userID = jsonResponse.getString("userID");
                                String userPASSWORD = jsonResponse.getString("userPASSWORD");
                                String userNAME = jsonResponse.getString("userNAME");
                                String userPHONENUMBER = jsonResponse.getString("userPHONENUMBER");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userPASSWORD",userPASSWORD);
                                intent.putExtra("userNAME",userNAME);
                                intent.putExtra("userPHONENUMBER",userPHONENUMBER);

                                session.createUserLoginSession(userNAME, userID);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                logoinToast.show();
                                finish();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Fail")
                                        .setNegativeButton("Again",null)
                                        .create()
                                        .show();
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                };

                LogReq logReq = new LogReq(userID, userPASSWORD, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(logReq);
            }
        });
    }
}
