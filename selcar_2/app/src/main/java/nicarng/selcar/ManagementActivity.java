package nicarng.selcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManagementActivity extends AppCompatActivity {

    private ListView listView;
    private UserListAdapter adapter;
    private List<User> userList;
    private List<User> saveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        Intent intent = getIntent();

        listView = findViewById(R.id.listView);
        userList = new ArrayList<User>();
        saveList = new ArrayList<User>();

        adapter = new UserListAdapter(getApplicationContext(), userList, this, saveList);
        listView.setAdapter(adapter);

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("users"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count=0;
            String userID, userPassword, userName;
            while (count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count);
                userID = object.getString("id");
                userPassword = object.getString("password");
                userName = object.getString("name");
                User user = new User(userID, userPassword, userName);

                userList.add(user);
                saveList.add(user);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        EditText search = (EditText) findViewById(R.id.search);
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
        userList.clear();
        for(int i =0; i < saveList.size(); i++){
            if(saveList.get(i).getUserID().contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
