package nicarng.selcar;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import nicarng.selcar.tutorial.TutorialActivity;

public class MainActivity extends ActivityGroup implements NavigationView.OnNavigationItemSelectedListener{
    UserSessionManager session;
    NavigationView navigationView;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());
        navigationView = findViewById(R.id.nav_view);
        view = navigationView.getHeaderView(0);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Selpcar");

        TabHost tabHost = findViewById(R.id.tab_host);
        tabHost.setup(getLocalActivityManager());

        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab Spec 1");
        ts1.setIndicator("홈");
        ts1.setContent(new Intent(this,MapActivity.class));
        tabHost.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab Spec 2");
        ts2.setIndicator("게시판");
        ts2.setContent(new Intent(this,NoticeBoardActivity.class));
        tabHost.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost.newTabSpec("Tab Spec 3");
        ts3.setIndicator("정보");
        ts3.setContent(new Intent(this,ReservlistActivity.class));
        tabHost.addTab(ts3);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button logButton2 = view.findViewById(R.id.LoginButton);
        logButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, poplogin.class);
                startActivity(intent);
            }
        });
    }


    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String target;

        @Override
        protected  void onPreExecute(){
            target = "http://wjddn944.cafe24.com/list.php";
        }

        @Override
        protected String doInBackground(Void... params){
            try{
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

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
            intent.putExtra("users", result);
            MainActivity.this.startActivity(intent);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        Button regButton = view.findViewById(R.id.RegisterButton);
        String userID =  session.getUserDetails().get("name");
        navigationView = findViewById(R.id.nav_view);
        view = navigationView.getHeaderView(0);
        final TextView welcomemessage = view.findViewById(R.id.well);
        final String message = "환영합니다.  " + userID +"님!";
        Button logButton = view.findViewById(R.id.LoginButton);
        if(session.isUserLoggedIn()){
            logButton.setText("logout");
            welcomemessage.setText(message);
            regButton.setVisibility(View.GONE);
        } else {
            logButton.setText("LOG-IN");
            welcomemessage.setText("Wellcome to selpcar!!!");
            regButton.setVisibility(View.VISIBLE);
            regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentreg = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intentreg);
                }
            });
        }
    }

    public void moveLoginActivity(View view){
        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentLogin);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_subscribe) {
            Toast.makeText(this, "subscribe", Toast.LENGTH_SHORT).show();
            Intent intentSub = new Intent(getApplicationContext(), SubscribeActivity.class);
            startActivity(intentSub);
        } else if (id == R.id.nav_register) {
            Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
            Intent intentRes = new Intent(getApplicationContext(), ReservlistActivity.class);
            startActivity(intentRes);
        } else if (id == R.id.nav_reservation) {
            Toast.makeText(this, "reservation", Toast.LENGTH_SHORT).show();
            Intent intentres = new Intent(getApplicationContext(), ReservlistActivity.class);
            startActivity(intentres);
        } else if (id == R.id.nav_info) {
            Toast.makeText(this, "info", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_board) {
            Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_guide) {
            Toast.makeText(this, "guide", Toast.LENGTH_SHORT).show();
            Intent intentTut = new Intent(getApplicationContext(), TutorialActivity.class);
            startActivity(intentTut);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
