package nicarng.selcar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CarInfoActivity extends AppCompatActivity {
    public static Activity _info;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        getSupportActionBar().setTitle("상세 정보");
        _info = CarInfoActivity.this;
        session = new UserSessionManager(getApplicationContext());

        Intent intent = getIntent();
        ImageView intent_car_image = findViewById(R.id.intent_car_image);
        Picasso.with(CarInfoActivity.this).load(intent.getStringExtra("car_image")).into(intent_car_image);

        TextView car_name = findViewById(R.id.car_name);
        final String intent_car_name = intent.getStringExtra("car_name");
        car_name.setText(intent_car_name);

        TextView car_year = findViewById(R.id.car_year);
        final String intent_car_year = intent.getStringExtra("car_year");
        car_year.setText(intent_car_year+"년");

        TextView car_km = findViewById(R.id.car_km);
        final String intent_car_km = intent.getStringExtra("car_km");
        car_km.setText(intent_car_km+"km");

        final String intented_car_image = intent.getStringExtra("car_image");

        final TextView car_number = findViewById(R.id.car_number);
        final TextView car_type = findViewById(R.id.car_type);
        final TextView car_fuel = findViewById(R.id.car_fuel);
        final TextView car_trans = findViewById(R.id.car_trans);
        final TextView car_color = findViewById(R.id.car_color);
        final TextView car_location = findViewById(R.id.car_location);

        class Car_number extends AsyncTask<Void,Void,String> {
            String target;

            @Override
            protected  void onPreExecute(){
                target = "http://wjddn944.cafe24.com/get_car_number.php?add_id=";
            }

            @Override
            protected String doInBackground(Void... params){
                try{
                    String add_id =  getIntent().getStringExtra("add_id");
                    String car_name = getIntent().getStringExtra("car_name");
                    String car_year = getIntent().getStringExtra("car_year");
                    String car_km = getIntent().getStringExtra("car_km");

                    URL url = new URL(target+add_id+"&car_name="+car_name+"&car_year="+car_year+"&car_km="+car_km);
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
                }catch(Exception e){
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
                    String get_car_number,get_car_type,get_car_fuel,get_car_trans,get_car_color,get_car_location;
                    while (count < array.length()){
                        JSONObject jsonObject = array.getJSONObject(count);
                        get_car_number = jsonObject.getString("car_number");
                        get_car_type = jsonObject.getString("car_type");
                        get_car_fuel = jsonObject.getString("car_fuel");
                        get_car_trans = jsonObject.getString("car_trans");
                        get_car_color = jsonObject.getString("car_color");
                        get_car_location = jsonObject.getString("car_location");
                        count ++;
                        car_number.setText(get_car_number);
                        car_type.setText(get_car_type);
                        car_fuel.setText(get_car_fuel);
                        car_trans.setText(get_car_trans);
                        car_color.setText(get_car_color);
                        car_location.setText(get_car_location);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }

        new Car_number().execute();

        Button reserveButton = findViewById(R.id.reserveButton);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("car_image",intented_car_image);
                extras.putString("car_name",intent_car_name);
                extras.putString("car_number",car_number.getText().toString());
                extras.putString("car_km",intent_car_km);
                extras.putString("car_year",intent_car_year);
                extras.putString("car_location",car_location.getText().toString());

                Intent intent = new Intent(CarInfoActivity.this,ResActivity.class);
                intent.putExtras(extras);
                CarInfoActivity.this.startActivity(intent);
            }
        });
    }
}
