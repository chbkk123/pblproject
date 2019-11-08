package nicarng.selcar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;


public class NoticeBoardActivity extends AppCompatActivity {
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    UserSessionManager_ID session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board);
        context=getApplicationContext();

        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("전체"));
        tabLayout.addTab(tabLayout.newTab().setText("잡담"));
        tabLayout.addTab(tabLayout.newTab().setText("옥션"));
        tabLayout.addTab(tabLayout.newTab().setText("질의응답"));
        tabLayout.addTab(tabLayout.newTab().setText("공지사항"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewpager_content);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session_id = new UserSessionManager_ID(getApplicationContext());
                if(!session_id.isUserLoggedIn_ID()){
                    AlertDialog.Builder login_dialog = new AlertDialog.Builder(NoticeBoardActivity.this);

                    login_dialog.setTitle("로그인 알림")
                            .setMessage("로그인이 필요합니다.\n로그인 하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(NoticeBoardActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(NoticeBoardActivity.this, "로그인 하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = login_dialog.create();
                    alert.setTitle("로그인 필요");
                    alert.show();
                }else {
                    Intent intent = new Intent(NoticeBoardActivity.this, DetailActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
