package nicarng.selcar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class ResActivity extends AppCompatActivity {
    UserSessionManager_ID session_id;
    public static Activity _res;
    long now = System.currentTimeMillis();
    DateTime dateTime = new DateTime();
    String today = dateTime.toString("yyyy년 MM월 dd일");

    //밑 코드는 joda time Lib 사용 시 utc 기반의 시간만 제공되기에 급하게 추가한 것.
    Date date = new Date(now);
    SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");
    String time_now = SDF.format(date);


    TextView reserveday ;
    TextView start_date;
    TextView end_date;
    private int myear,mmonth,mdate;
    final Calendar c = Calendar.getInstance();

    CarInfoActivity Info = (CarInfoActivity) CarInfoActivity._info;
    IndvActivity INDV = (IndvActivity)IndvActivity._indv;

    Date time1;
    long reqtime1;
    Date time2;
    long reqtime2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        getSupportActionBar().setTitle("정보 입력");

        JodaTimeAndroid.init(this);
        _res = ResActivity.this;

        session_id = new UserSessionManager_ID(getApplicationContext());


        final TextView car_name = findViewById(R.id.car_name);
        final TextView car_km = findViewById(R.id.car_num);
        final TextView car_year = findViewById(R.id.car_color);
        final TextView start_time = findViewById(R.id.start_time);
        final TextView end_time = findViewById(R.id.end_time);

        Intent intent = getIntent();

        final String intent_car_image = intent.getStringExtra("car_image");
        final String intent_car_name = intent.getStringExtra("car_name");
        final String intent_car_number = intent.getStringExtra("car_number");
        final String intent_car_km = intent.getStringExtra("car_km");
        final String intent_car_year = intent.getStringExtra("car_year");
        final String intent_car_location = intent.getStringExtra("car_location");

        reserveday = findViewById(R.id.reserveDay);
        reserveday.setText("오늘은 "+today+" 입니다."); //기본 설정 : 현재 날짜

        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);

        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mdate = c.get(Calendar.DAY_OF_MONTH);
        
        final DatePickerDialog dialog1 = new DatePickerDialog(this,listener1,myear,mmonth,mdate);
        dialog1.getDatePicker().setMinDate(now-100);
        final ImageButton start_date_choice = findViewById(R.id.start_date_choice);
        start_date_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
            }
        });

        final DatePickerDialog dialog2 = new DatePickerDialog(this,listener2,myear,mmonth,mdate);
        dialog2.getDatePicker().setMinDate(now-100);
        ImageButton end_date_choice = findViewById(R.id.end_date_choice);
        end_date_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
            }
        });

        ImageButton start_time_choice = findViewById(R.id.start_time_choice);
        start_time_choice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final TimePickerDialog dialog = new TimePickerDialog(ResActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                            String msg = String.format("%d 시 %d 분" ,hour ,min);
                            String msg_convert = String.format("%d:%d" ,hour ,min);
                            try {
                                time1 = SDF.parse(msg_convert);
                                reqtime1 = time1.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        start_time.setText(msg);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                dialog.show();


            }
        });
        ImageButton end_time_choice = findViewById(R.id.end_time_choice);
        end_time_choice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimePickerDialog dialog = new TimePickerDialog(ResActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        if(start_time.getText().toString().length() == 0){
                            Toast.makeText(ResActivity.this, "시작시간을 입력해 주세요!", Toast.LENGTH_SHORT).show();

                        }else {
                            String msg = String.format("%d 시 %d 분", hour, min);
                            String msg_convert2 = String.format("%d:%d",hour,min);
                            try {
                                time2 = SDF.parse(msg_convert2);
                                reqtime2 = time2.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            end_time.setText(msg);
                        }
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지
                dialog.show();

            }
        });
        Button btn_go = findViewById(R.id.Finish);
        btn_go.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            final String user_id = session_id.getUserDetails_ID().get("id");
                            final String car_image = intent_car_image;
                            final String car_name = intent_car_name;
                            final String car_number = intent_car_number;
                            final String car_km = intent_car_km;
                            final String car_year = intent_car_year;
                            final String car_location = intent_car_location;
                            final String reserv_start_date = start_date.getText().toString();
                            final String reserv_end_date = end_date.getText().toString();
                            final String reserv_starttime = start_time.getText().toString();
                            final String reserv_endtime = end_time.getText().toString();

                            long getMinute = (reqtime2  - reqtime1) / 60000L;
                            final Integer reserve_cash = (int)(long)getMinute*120;

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ResActivity.this);
                                            Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                                            intent.putExtra("car_name",car_name);
                                            intent.putExtra("car_number",car_number);
                                            intent.putExtra("reserv_start_date", reserv_start_date);
                                            intent.putExtra("reserv_end_date", reserv_end_date);
                                            intent.putExtra("reserv_starttime", reserv_starttime);
                                            intent.putExtra("reserv_endtime", reserv_endtime);
                                            intent.putExtra("getMinute",String.valueOf(reserve_cash));
                                            intent.putExtra("car_location",intent_car_location);
                                            if(CarInfoActivity._info != null){
                                                Info.finish();
                                            }
                                            if(IndvActivity._indv != null){
                                                INDV.finish();
                                            }
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ResActivity.this);
                                            builder.setMessage("Fail!!")
                                                    .setNegativeButton("Again", null)
                                                    .create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            ResReq1 resReq1 = new ResReq1(user_id, car_image, car_name, car_number,car_km,car_year, reserv_start_date, reserv_end_date, reserv_starttime, reserv_endtime, car_location, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(ResActivity.this);
                            queue.add(resReq1);

                    }
                });
        }
    private DatePickerDialog.OnDateSetListener listener1 =new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            String ymd1 = String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth);
            start_date.setText(ymd1);
        }
    };

    private DatePickerDialog.OnDateSetListener listener2 =new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            String ymd2 = String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth);
            end_date.setText(ymd2);
        }
    };

    public void ClickButton(View v) {

    }
}

