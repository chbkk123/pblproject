package nicarng.selcar;


import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends AppCompatActivity {
    UserSessionManager session;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        session = new UserSessionManager(getApplicationContext());

        final Spinner category_spinner = findViewById(R.id.category_spinner);
        final EditText subject = findViewById(R.id.Subject);
        final EditText description = findViewById(R.id.Description);


        Button finish = findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_id = session.getUserDetails().get("name");
                String Spinner_value = category_spinner.getSelectedItem().toString();
                String Subject = subject.getText().toString();
                String Description = description.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Log.d("IN_RESPONSE", response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                    dialog = builder.setMessage("Success!!")
                                            .setPositiveButton("Ok", null)
                                            .create();
                                    dialog.show();
                                    //Intent intent = new Intent(DetailActivity.this, NoticeBoardActivity.class);
                                   // DetailActivity.this.startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                };
                BoardReq boardreq = new BoardReq(user_id,Subject, Description, Spinner_value, responseListener);
                RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);
                queue.add(boardreq);
            }
        });

    }
    @Override
    protected void onDestroy(){
        if(dialog != null){
            dialog.dismiss();
        }

        super.onDestroy();
    }
}
