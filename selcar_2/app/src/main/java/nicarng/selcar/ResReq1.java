package nicarng.selcar;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ResReq1 extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/userreservelist.php";
    private Map<String,String> parameters;

    public ResReq1(String user_id,String car_image, String car_name, String car_number,String car_km,String car_year, String reserv_start_date, String reserv_end_date, String reserv_starttime, String reserv_endtime,String car_location, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("user_id",user_id);
        parameters.put("car_image",car_image);
        parameters.put("car_name",car_name);
        parameters.put("car_number",car_number);
        parameters.put("car_km",car_km);
        parameters.put("car_year",car_year);
        parameters.put("reserv_start_date",reserv_start_date);
        parameters.put("reserv_end_date",reserv_end_date);
        parameters.put("reserv_starttime",reserv_starttime);
        parameters.put("reserv_endtime",reserv_endtime);
        parameters.put("car_location",car_location);

    }

    @Override
    public Map<String,String> getParams(){

        return parameters;
    }

}
