package nicarng.selcar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.List;

import static nicarng.selcar.UserCaraddActivity.car_Type;
import static nicarng.selcar.UserCaraddActivity.car_Name;

public class SpinnerAdapter extends BaseAdapter implements OnClickListener{

    private Context context;
    private List<Spinner> spinnerList;
    private Activity UserCaraddActivity;

    protected SpinnerAdapter(Context context, List<Spinner> spinnerList, Activity UserCaraddActivity){
        this.context = context;
        this.spinnerList = spinnerList;
        this.UserCaraddActivity = UserCaraddActivity;
    }

    @Override
    public int getCount(){return spinnerList.size();}

    @Override
    public  Object getItem(int i){
        return spinnerList.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent){
        View v = View.inflate(context,R.layout.spinner,null);
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.spinner,null);
        }

        TextView car_name = v.findViewById(R.id.DbCarName);
        car_name.setText(spinnerList.get(i).getDbCarName());
        car_name.setTextColor(Color.BLACK);

        TextView car_type = v.findViewById(R.id.DbCarType);
        car_type.setText(spinnerList.get(i).getDbCarType());
        car_type.setTextColor(Color.GRAY);
        v.setTag(i);
        v.setOnClickListener(this);
        return v;
    }
    public void onClick(View v){
        int i =(Integer) v.getTag();
        String select_value = spinnerList.get(i).getDbCarName();
        String select_type = spinnerList.get(i).getDbCarType();
        Toast.makeText(context, select_value+"/"+select_type + " 이 선택되었습니다.", Toast.LENGTH_SHORT).show();
        car_Type.setText(select_type);
        car_Name.setText(select_value);
    }
}
