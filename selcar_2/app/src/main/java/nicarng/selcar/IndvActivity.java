package nicarng.selcar;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IndvActivity extends AppCompatActivity {

    public static Activity _indv;
    private ListView carlistview;
    private DbcarlistAdapter adapter;
    private List<Dbcarlist> dbcarlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indv);
        _indv = IndvActivity.this;

        final int getInt = getIntent().getIntExtra("tagnum",0);

        class BackgroundTask extends AsyncTask<Void,Void,String> {
            String target;

            @Override
            protected  void onPreExecute(){
                target = "http://wjddn944.cafe24.com/dbcarlist.php?tagnum=";
            }

            @Override
            protected String doInBackground(Void... params){
                try{
                    int tagnum = getInt;
                    URL url = new URL(target+tagnum);
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
                carlistview = findViewById(R.id.carlistview);
                dbcarlist = new ArrayList<Dbcarlist>();

                adapter = new DbcarlistAdapter(getApplicationContext(), dbcarlist,IndvActivity.this);
                carlistview.setAdapter(adapter);
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count=0;
                    String car_image, car_name, car_km, car_year,car_location,car_available,add_id;
                    while (count < jsonArray.length()){
                        JSONObject object = jsonArray.getJSONObject(count);
                        car_image = object.getString("car_image");
                        car_name = object.getString("car_name");
                        car_km = object.getString("car_km");
                        car_year = object.getString("car_year");
                        car_location = object.getString("car_location");
                        car_available = object.getString("car_available");
                        add_id = object.getString("add_id");
                        Dbcarlist dbcar = new Dbcarlist(car_image,car_name, car_km, car_year,car_location,car_available,add_id);

                        dbcarlist.add(dbcar);
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        new BackgroundTask().execute();
    }
}
