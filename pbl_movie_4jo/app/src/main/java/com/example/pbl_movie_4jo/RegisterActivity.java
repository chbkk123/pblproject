package com.example.pbl_movie_4jo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity  extends AppCompatActivity {
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText IDText = findViewById(R.id.IDText);
        final EditText PWText = findViewById(R.id.PWText);
        final EditText PW_CheckText = findViewById(R.id.PW_CheckText);
        final EditText NAMEText = findViewById(R.id.NAMEText);
        final EditText AgeText = findViewById(R.id.AgeText);
        final EditText PHONEText = findViewById(R.id.Phone_numberText);

        final Button IDCheckButton = findViewById(R.id.IDCheckButton);

        final AlertDialog.Builder IDCheck = new AlertDialog.Builder(this);
        final AlertDialog.Builder finishCheck = new AlertDialog.Builder(this);

        IDCheckButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userID = IDText.getText().toString();
                if(userID.equals("")){
                    IDCheck.setMessage("Can't be empty")
                            .setPositiveButton("Again",null)
                            .create()
                            .show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                IDCheck.setMessage("This is valid ID")
                                        .setPositiveButton("Ok",null)
                                        .create();
                                IDCheck.show();
                                validate = true; //체크 완료
                            }else {
                                IDCheck.setMessage("This is an invalid ID")
                                        .setPositiveButton("Again",null)
                                        .create();
                                IDCheck.show();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        Button FinishButton = findViewById(R.id.FinishButton);
        FinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = IDText.getText().toString();
                final String userPASSWORD = PWText.getText().toString();
                final String userPASSWORD_CHECK = PW_CheckText.getText().toString();
                String userNAME = NAMEText.getText().toString();
                String userAGE = AgeText.getText().toString();
                String userPHONE = PHONEText.getText().toString();

                if(!validate){
                    finishCheck.setMessage("ID Check first")
                            .setPositiveButton("Again",null)
                            .create();
                   finishCheck.show();
                    return;
                }

                if(userID.equals("") ||userPASSWORD.equals("")||userPASSWORD_CHECK.equals("")||userNAME.equals("")||userAGE.equals("")||userPHONE.equals("")){
                    finishCheck.setMessage("No Empty Space Please")
                            .setPositiveButton("Again",null)
                            .create();
                    finishCheck.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(userPASSWORD.equals(userPASSWORD_CHECK)) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Log.e("IN_RESPONSE", response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success){
                                    finishCheck.setMessage("Success!!");
                                    finishCheck.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create();

                                    finishCheck.show();
                                }else {
                                    finishCheck.setMessage("Fail!!")
                                            .setPositiveButton("Again", null)
                                            .create();
                                    finishCheck.show();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            finishCheck.setMessage("PassWord is not correct")
                                    .setPositiveButton("Again",null)
                                    .create();
                            finishCheck.show();
                        }
                    }
                };
                RegReq regreq = new RegReq(userID, userPASSWORD, userNAME, userAGE, userPHONE, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(regreq);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
