package nicarng.selcar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ReservlistActivity extends AppCompatActivity {
    UserSessionManager session;
    UserSessionManager_ID session_id;
    UserSessionManager_Grade session_grade;
    private List<User> userList;
    private List<User> saveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservlist);

        session = new UserSessionManager(getApplicationContext());
        session_id = new UserSessionManager_ID(getApplicationContext());
        session_grade = new UserSessionManager_Grade((getApplicationContext()));
    }

    @Override
    protected void onResume(){
        super.onResume();
        final TextView userid = findViewById(R.id.user_id);
        final TextView user_grade = findViewById(R.id.user_grade);
        final TextView useremail = findViewById(R.id.user_email);
        final TextView userphonenumber = findViewById(R.id.user_phonenumber);
        final TextView logout_text=findViewById(R.id.logout);
        final TextView withdrawal_text = findViewById(R.id.withdrawal);

        ImageButton myreservelist = findViewById(R.id.myreservelist);
        ImageButton register_my_car = findViewById(R.id.register_my_car);
        ImageButton checked_my_car = findViewById(R.id.checked_my_car);
        ImageButton subscribe_go = findViewById(R.id.subscribe_go);

        ImageView imageView = findViewById(R.id.profileimage);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        if(!session_id.isUserLoggedIn_ID()&&!session.isUserLoggedIn()&&!session_grade.isUserLoggedIn_GRADE()){
            userid.setText("로그인 필요");
            user_grade.setText("구독등급 정보");
            useremail.setText("이메일 주소");
            userphonenumber.setText("휴대폰 번호");
            withdrawal_text.setText("회원등록");

            logout_text.setText("로그인");
            logout_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ReservlistActivity.this,LoginActivity.class));
                }
            });

        }else {
            userid.setText(session.getUserDetails().get("name")+"님");
            user_grade.setText("구독등급 : "+session_grade.getUserDetails_GRADE().get("grade"));
            useremail.setText(session.getUserDetails().get("email"));
            userphonenumber.setText(session_id.getUserDetails_ID().get("phonenumber"));
            logout_text.setText("로그아웃");
            logout_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ReservlistActivity.this,LoginActivity.class));
                    userid.setText("로그인 필요");
                    user_grade.setText("구독등급 정보");
                    useremail.setText("이메일 주소");
                    userphonenumber.setText("휴대폰 번호");
                    withdrawal_text.setText("회원등록");
                    logout_text.setText("로그인");
                }
            });

            withdrawal_text.setText("회원탈퇴");
            withdrawal_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String userID = session_id.getUserDetails_ID().get("id");
                    AlertDialog.Builder login_dialog = new AlertDialog.Builder(ReservlistActivity.this);
                    login_dialog.setTitle("회원탈퇴 알림")
                            .setMessage("회원을 탈퇴합니다.\n정말 탈퇴 하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if(success){
                                                    Toast.makeText(getApplicationContext(), "회원탈퇴를 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    DelReq delReq = new DelReq(userID,responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(ReservlistActivity.this);
                                    queue.add(delReq);
                                }
                            })
                            .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "취소하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = login_dialog.create();
                    alert.show();
                }
            });
        }

        myreservelist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!session_id.isUserLoggedIn_ID() && !session.isUserLoggedIn()&&!session_grade.isUserLoggedIn_GRADE()){
                        Toast.makeText(ReservlistActivity.this, "로그인을 해야합니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        new BackgroundTask().execute();
                    }
                }
        });

        register_my_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!session_id.isUserLoggedIn_ID() && !session.isUserLoggedIn()&&!session_grade.isUserLoggedIn_GRADE()){
                        Toast.makeText(ReservlistActivity.this, "로그인을 해야합니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(ReservlistActivity.this, UserCaraddActivity.class);
                        ReservlistActivity.this.startActivity(intent);
                    }
                }
        });

        checked_my_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!session_id.isUserLoggedIn_ID() && !session.isUserLoggedIn()&&!session_grade.isUserLoggedIn_GRADE()){
                        Toast.makeText(ReservlistActivity.this, "로그인을 해야합니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        new Check_my_car().execute();
                    }
                }
        });

        subscribe_go.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(!session_id.isUserLoggedIn_ID() && !session.isUserLoggedIn()&&!session_grade.isUserLoggedIn_GRADE()){
                        Toast.makeText(ReservlistActivity.this, "로그인을 해야합니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(ReservlistActivity.this, SubscribeActivity.class);
                        startActivity(intent);
                    }
                }
        });

    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String target;

        @Override
        protected  void onPreExecute(){
            target = "http://wjddn944.cafe24.com/mycarlist.php?userID=";
        }

        @Override
        protected String doInBackground(Void... params){
            try{
                String userID =  session_id.getUserDetails_ID().get("id");
                URL url = new URL(target+userID);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
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
            Intent intent = new Intent(ReservlistActivity.this, MyreservActivity.class);
            intent.putExtra("mycarlist", result);
            ReservlistActivity.this.startActivity(intent);
        }
    }

    class Check_my_car extends AsyncTask<Void, Void, String>{
        String target;
        protected  void onPreExecute(){
            target = "https://wjddn944.cafe24.com/checked_my_car.php";
        }
        protected String doInBackground(Void... params){
            try{
                //String userID =  session.getUserDetails().get("name");
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
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

        public void onPostExecute(String result){
            Intent intent = new Intent(ReservlistActivity.this,CheckedmycarActivity.class);
            intent.putExtra("checkedmycar",result);
            ReservlistActivity.this.startActivity(intent);
        }
    }
}
