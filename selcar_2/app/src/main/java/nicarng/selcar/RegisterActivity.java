package nicarng.selcar;

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

public class RegisterActivity extends AppCompatActivity {

    private boolean validate = false;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText IDText = findViewById(R.id.IDText);
        final EditText PWText = findViewById(R.id.PWText);
        final EditText PW_CheckText = findViewById(R.id.PW_CheckText);
        final EditText NAMEText = findViewById(R.id.NAMEText);
        final EditText EMAILText = findViewById(R.id.EmailText);
        final EditText PHONEText = findViewById(R.id.Phone_numberText);

        final Button IDCheckButton = findViewById(R.id.IDCheckButton);
        IDCheckButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String userID = IDText.getText().toString();
                if(validate){
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Can't be empty")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog=builder.setMessage("This is valid ID")
                                        .setPositiveButton("Ok",null)
                                        .create();
                                dialog.show();
                                validate = true; //체크 완료
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("This is an invalid ID")
                                        .setNegativeButton("Again",null)
                                        .create();
                                dialog.show();
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
                String userEMAIL = EMAILText.getText().toString();
                String userPHONE = PHONEText.getText().toString();

                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("ID Check first")
                            .setNegativeButton("Again",null)
                            .create();
                    dialog.show();
                    return;
                }

                if(userID.equals("") ||userPASSWORD.equals("")||userPASSWORD_CHECK.equals("")||userNAME.equals("")||userEMAIL.equals("")||userPHONE.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("No Empty Space Please")
                            .setNegativeButton("Again",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(userPASSWORD.equals(userPASSWORD_CHECK)) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Log.d("IN_RESPONSE", response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog = builder.setMessage("Success!!")
                                            .setPositiveButton("Ok", null)
                                            .create();
                                    dialog.show();
                                    //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    finish();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog = builder.setMessage("Fail!!")
                                            .setNegativeButton("Again", null)
                                            .create();
                                    dialog.show();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            dialog = builder.setMessage("PassWord is not correct")
                                    .setNegativeButton("Again",null)
                                    .create();
                            dialog.show();
                        }
                    }
                };
                RegReq regreq = new RegReq(userID,userPASSWORD,userNAME,userEMAIL,userPHONE, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(regreq);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}
