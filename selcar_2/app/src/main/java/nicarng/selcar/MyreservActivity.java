package nicarng.selcar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyreservActivity extends AppCompatActivity {
    private ListView listView;
    private MyCarListAdapter adapter;
    private List<Mycarlist> mycarList;
    private List<Mycarlist> saveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreserv);

        Intent intent = getIntent();
        listView = findViewById(R.id.listView);
        mycarList = new ArrayList<Mycarlist>();
        saveList = new ArrayList<Mycarlist>();

        adapter = new MyCarListAdapter(getApplicationContext(), mycarList, this, saveList);
        listView.setAdapter(adapter);

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("mycarlist"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count=0;
            String carimage, carname, carnumber, startdate,enddate, starttime, endtime,location;
            while (count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count);
                carimage = object.getString("car_image");
                carname = object.getString("car_name");
                carnumber = object.getString("car_number");
                startdate = object.getString("reserve_start_date");
                enddate = object.getString("reserve_end_date");
                starttime = object.getString("reserve_starttime");
                endtime = object.getString("reserve_endtime");
                location = object.getString("car_location");

                Mycarlist mycarlist = new Mycarlist(carimage,carname,carnumber,startdate,enddate,starttime,endtime,location);


                mycarList.add(mycarlist);
                saveList.add(mycarlist);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        EditText search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void searchUser(String search){
        mycarList.clear();
        for(int i =0; i < saveList.size(); i++){
            if(saveList.get(i).getCarname().contains(search)){
                mycarList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
