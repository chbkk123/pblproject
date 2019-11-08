package nicarng.selcar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DbcarlistAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private List<Dbcarlist> dbcarlist;
    private Activity IndvActivity;
    UserSessionManager session;

    public DbcarlistAdapter(Context context,List<Dbcarlist> dbcarlist,Activity IndvActivity){
        this.context = context;
        this.dbcarlist = dbcarlist;
        this.IndvActivity = IndvActivity;
    }

    @Override
    public int getCount() {
        return dbcarlist.size();
    }

    @Override
    public Object getItem(int i) {
        return dbcarlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        view = View.inflate(context,R.layout.dbcarlist,null);
        ImageView car_image = view.findViewById(R.id.car_image);
        final TextView car_name = view.findViewById(R.id.car_name);
        TextView car_km = view.findViewById(R.id.car_km);
        TextView car_year = view.findViewById(R.id.car_year);
        TextView car_available = view.findViewById(R.id.car_available);
        TextView add_id = view.findViewById(R.id.user_id);

        Picasso.with(context).load(dbcarlist.get(i).getCar_image()).into(car_image);
        car_name.setText(dbcarlist.get(i).getCar_name());
        car_km.setText("km수: "+dbcarlist.get(i).getCar_km()+"/ ");
        car_year.setText("연식: "+dbcarlist.get(i).getCar_year());
        if(dbcarlist.get(i).getCar_available().equals("0")){
            car_available.setText("예약 가능");
            car_available.setTextColor(Color.BLUE);
        }else{
            car_available.setText("예약 불가능");
            car_available.setTextColor(Color.RED);
        }
        add_id.setText("등록자: "+dbcarlist.get(i).getAdd_id()+"님");

        view.setTag(i);
        view.setOnClickListener(this);

        return view;
    }
    public void onClick(View view){
        session = new UserSessionManager(context.getApplicationContext());
        int i = (Integer) view.getTag();
        if(!session.isUserLoggedIn()){
            AlertDialog.Builder login_dialog = new AlertDialog.Builder(IndvActivity);
            login_dialog.setTitle("로그인 알림")
                    .setMessage("로그인이 필요합니다.\n로그인 하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(IndvActivity,LoginActivity.class);
                            IndvActivity.startActivity(intent);
                        }
                    })
                    .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "취소하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "로그인 하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert = login_dialog.create();
            alert.setTitle("로그인 필요");
            alert.show();
        }else {
            if (dbcarlist.get(i).getCar_available().equals("0")) {
                Bundle extras = new Bundle();
                extras.putString("car_image", dbcarlist.get(i).getCar_image());
                extras.putString("car_name", dbcarlist.get(i).getCar_name());
                extras.putString("car_km", dbcarlist.get(i).getCar_km());
                extras.putString("car_year", dbcarlist.get(i).getCar_year());
                extras.putString("add_id",dbcarlist.get(i).getAdd_id());
                extras.putString("car_location",dbcarlist.get(i).getCar_location());

                Intent intent = new Intent(context, CarInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(extras);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "현재 예약이 불가능 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
