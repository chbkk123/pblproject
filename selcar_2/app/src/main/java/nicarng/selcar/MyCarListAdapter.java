package nicarng.selcar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class MyCarListAdapter extends BaseAdapter {
    private Context context;
    private List<Mycarlist> mycarList;
    private Activity parentActivity;
    private List<Mycarlist> saveList;
    int selecteditem;
                // 위치 추가
    public MyCarListAdapter(Context context, List<Mycarlist> mycarList, Activity parentActivity, List<Mycarlist> saveList){
        this.context = context;
        this.mycarList = mycarList;
        this.parentActivity = parentActivity;
        this.saveList = saveList;
    }

    @Override
    public int getCount() {
        return mycarList.size();
    }

    @Override
    public Object getItem(int i) {
        return mycarList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.mycarlist, null);
        ImageView carimage = v.findViewById(R.id.carimg);
        TextView carname = (TextView) v.findViewById(R.id.carname);
        final TextView carnumber = (TextView) v.findViewById(R.id.carnumber);
        TextView startdate = (TextView) v.findViewById(R.id.startdate);
        TextView enddate = v.findViewById(R.id.enddate);
        TextView starttime = (TextView) v.findViewById(R.id.starttime);
        TextView endtime = v.findViewById(R.id.endtime);
        final String realLocation;
                //위치 추가
        Picasso.with(context).load(mycarList.get(i).getCarimage()).into(carimage);
        carname.setText(mycarList.get(i).getCarname());
        carnumber.setText(mycarList.get(i).getCarnumber());
        startdate.setText(mycarList.get(i).getStartdate());
        enddate.setText(mycarList.get(i).getEnddate());
        starttime.setText(mycarList.get(i).getStarttime());
        endtime.setText(mycarList.get(i).getEndtime());
        realLocation = mycarList.get(i).getLocation();

        v.setTag(mycarList.get(i).getCarnumber());

        Button location = v.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapActivity2.class);
                intent.putExtra("real_location", realLocation);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);

            }
        });

        Button delete = v.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context,"예약을 취소하였습니다.",Toast.LENGTH_SHORT).show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                mycarList.remove(i);
                                for(int i = 0; i < saveList.size(); i++){
                                    if(saveList.get(i).getCarnumber().equals(carnumber.getText().toString())){
                                        saveList.remove(i);
                                        break;

                                    }
                                }
                                notifyDataSetChanged();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                CarDelReq cardelReq = new CarDelReq(carnumber.getText().toString(),responseListener );
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                queue.add(cardelReq);
            }
        });


        final Button used = v.findViewById(R.id.used);


        used.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] location = {"공학관 주차장","학술정보관 주차장", "인재원 주차장"};
                android.app.AlertDialog.Builder used_dialog = new android.app.AlertDialog.Builder(parentActivity);
                used_dialog.setTitle("어떤 장소에 반납하십니까?")
                        .setSingleChoiceItems(location, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selecteditem = which;
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,location[selecteditem]+"에 반납하셨습니다.",Toast.LENGTH_SHORT).show();
                                used.setText(location[selecteditem]);
                                String car_location = used.getText().toString();
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success){
                                                mycarList.remove(i);
                                                for(int i = 0; i < saveList.size(); i++){
                                                    if(saveList.get(i).getCarnumber().equals(carnumber.getText().toString())){
                                                        saveList.remove(i);
                                                        break;

                                                    }
                                                }
                                                notifyDataSetChanged();
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                CarUsedReq carusedReq = new CarUsedReq(carnumber.getText().toString(),car_location,responseListener );
                                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                                queue.add(carusedReq);
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                android.app.AlertDialog alert = used_dialog.create();
                alert.show();

                Toast.makeText(context,"사용을 완료하였습니다.",Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }
}
