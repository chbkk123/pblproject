package nicarng.selcar;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class CheckActivity extends AppCompatActivity {
    UserSessionManager session;
    TextView car_name;
    TextView car_number;
    TextView car_location;
    TextView start_date;
    TextView end_date;
    TextView start_time;
    TextView end_time;
    TextView reserving_cash;
    CarInfoActivity Info = (CarInfoActivity) CarInfoActivity._info;
    IndvActivity INDV = (IndvActivity)IndvActivity._indv;
    ResActivity RES = (ResActivity)ResActivity._res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        getSupportActionBar().setTitle("나의 대여 차량");
        ImageView carImage = findViewById(R.id.imageView1);

        session = new UserSessionManager(getApplicationContext());
        ImageView imageView = findViewById(R.id.imageView1);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        if(!session.isUserLoggedIn()){
            finish();
            Toast.makeText(CheckActivity.this, "Login First", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = getIntent();
            String cars = intent.getStringExtra("car_pic");

            if(TextUtils.isEmpty(cars)){
            }else {
                Picasso.with(CheckActivity.this).load(intent.getStringExtra("car_image")).into(carImage);
            }
            String intent_car_name = intent.getStringExtra("car_name");
            car_name = findViewById(R.id.reserving_car_name);
            car_name.setText(intent_car_name);
            String intent_car_number = intent.getStringExtra("car_number");
            car_number = findViewById(R.id.reserving_car_number);
            car_number.setText(intent_car_number);
            String intent_car_location = intent.getStringExtra("car_location");
            car_location = findViewById(R.id.reserving_car_location);
            car_location.setText(intent_car_location);
            String intent_start_date = intent.getStringExtra("reserv_start_date");
            start_date = findViewById(R.id.reserving_start_date);
            start_date.setText(intent_start_date);
            String intent_end_date = intent.getStringExtra("reserv_start_date");
            end_date = findViewById(R.id.reserving_end_date);
            end_date.setText(intent_end_date);
            String intent_start_time = intent.getStringExtra("reserv_starttime");
            start_time = findViewById(R.id.reserving_start_time);
            start_time.setText(intent_start_time);
            String intent_end_time = intent.getStringExtra("reserv_endtime");
            end_time = findViewById(R.id.reserving_end_time);
            end_time.setText(intent_end_time);
            String intent_reserve_cash = intent.getStringExtra("getMinute");
            Log.e("cash",intent_reserve_cash);
            reserving_cash = findViewById(R.id.reserving_cash);
            reserving_cash.setText(intent_reserve_cash+" 원");
        }
    }
    public void ClickButton(View v){
        Toast.makeText(getApplicationContext(), "return HOME!!", Toast.LENGTH_SHORT).show();
        if(CarInfoActivity._info != null){ Info.finish(); }
        if(IndvActivity._indv != null){ INDV.finish(); }
        if(ResActivity._res != null){ RES.finish(); }
        finish();
    }
}
