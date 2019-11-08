package nicarng.selcar.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import nicarng.selcar.R;

public class QnAFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public QnAFragment(){}
    private ListView all_view;
    private DbboardAdapter adapter;
    private List<Dbboard> dbboard;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_all,null);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        all_view = view.findViewById(R.id.all_view);
        dblistview();
        return view;
    }

    public void dblistview(){
        new BackgroundTask().execute();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                new BackgroundTask().execute();
                all_view.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            target = "http://wjddn944.cafe24.com/dbboard_qna.php";
        }

        @Override
        protected String doInBackground(Void... params) {


            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "error";

        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            all_view = (ListView) getView().findViewById(R.id.all_view);
            dbboard = new ArrayList<Dbboard>();

            adapter = new DbboardAdapter(getActivity(), dbboard);
            adapter.notifyDataSetChanged();
            all_view.setAdapter(adapter);


            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String idx,subject,editor;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    idx = object.getString("board_idx");
                    subject = object.getString("subject");
                    editor = object.getString("editor");
                    Dbboard dbtitle = new Dbboard(idx,subject,editor);


                    dbboard.add(dbtitle);

                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}