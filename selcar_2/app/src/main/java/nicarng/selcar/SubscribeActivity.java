package nicarng.selcar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubscribeActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    RadioGroup rg;
    UserSessionManager_ID session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        session_id = new UserSessionManager_ID(getApplicationContext());

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(SubscribeActivity.this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Basic");
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "레이"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "소울"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "스파크"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "모닝"));

        ExpandableListAdapter.Item places1 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Pro");
        places1.invisibleChildren = new ArrayList<>();
        places1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "소나타"));
        places1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "아반테"));
        places1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "그랜져"));
        places1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "제네시스"));

        ExpandableListAdapter.Item places2 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Premium");
        places2.invisibleChildren = new ArrayList<>();
        places2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "에쿠스"));
        places2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "K9"));
        places2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "SM7"));
        places2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "벤츠"));

        data.add(places);
        data.add(places1);
        data.add(places2);

        recyclerview.setAdapter(new ExpandableListAdapter(data));

        rg = findViewById(R.id.rg);

        Button sub = findViewById(R.id.sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = session_id.getUserDetails_ID().get("id");

                RadioButton rb = findViewById(rg.getCheckedRadioButtonId());

                final String grade = rb.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(SubscribeActivity.this);
                alert.setTitle("구독 정보 확인")
                        .setMessage(grade + "  을(를) 구독하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Log.d("IN_RESPONSE", response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        UserGradeReq userGradeReq = new UserGradeReq(userID,grade,responseListener );

                        RequestQueue queue = Volley.newRequestQueue(SubscribeActivity.this);
                        queue.add(userGradeReq);

                        Intent intma = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intma);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alert.create().show();
            }
        });
    }
}