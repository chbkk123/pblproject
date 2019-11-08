package nicarng.selcar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InnerBoard extends AppCompatActivity {
    UserSessionManager_ID session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_board);
        session_id = new UserSessionManager_ID(getApplicationContext());

        final TextView subject = findViewById(R.id.sub);
        final TextView description = findViewById(R.id.des);
        final TextView editor = findViewById(R.id.editor_inner);

        Intent intent = getIntent();
        final String intent_idx = intent.getStringExtra("idx");
        String intent_subject = intent.getStringExtra("Subject");
        String intent_editor = intent.getStringExtra("Editor");
        subject.setText(intent_subject);
        editor.setText(intent_editor);

        class Get_DES extends AsyncTask<Void,Void,String> {

            String target;

            @Override
            protected  void onPreExecute(){
                target = "http://wjddn944.cafe24.com/GetDes.php?idx=";
            }



            @Override
            protected String doInBackground(Void... params){
                try{
                    URL url = new URL(target+intent_idx);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp;
                    StringBuilder stringBuilder = new StringBuilder();
                    while((temp = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(temp +"\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return  stringBuilder.toString().trim();

                } catch(Exception e){
                    e.printStackTrace();
                }
                return "error";
            }

            @Override
            public void onProgressUpdate(Void... values){
                super.onProgressUpdate(values);
            }

            @Override
            public void onPostExecute(String result){
                try{
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("response");
                    int count = 0;
                    String user_id,board_subject,board_description;
                    while (count < array.length()){
                        JSONObject jsonObject = array.getJSONObject(count);
                        user_id = jsonObject.getString("editor");
                        board_subject = jsonObject.getString("subject");
                        board_description = jsonObject.getString("description");
                        count ++;
                        description.setText(board_description);


                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }new Get_DES().execute();
    }
}
