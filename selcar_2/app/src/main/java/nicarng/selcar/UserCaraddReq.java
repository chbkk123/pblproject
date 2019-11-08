package nicarng.selcar;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserCaraddReq extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/usercaradd.php";
    private Map<String,String> parameters;

    public UserCaraddReq(String user_id, String car_image, String car_type, String car_name,
                         String car_number, String car_km, String car_year, String car_kmL,
                         String car_color, String car_location, String car_fuel, String car_trans,
                         Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        parameters=new HashMap<>();
        parameters.put("add_id",user_id);
        parameters.put("car_image",car_image);
        parameters.put("car_type",car_type);
        parameters.put("car_name",car_name);
        parameters.put("car_number",car_number);
        parameters.put("car_km", car_km);
        parameters.put("car_year", car_year);
        parameters.put("car_kmL", car_kmL);
        parameters.put("car_color", car_color);
        parameters.put("car_location", car_location);
        parameters.put("car_fuel", car_fuel);
        parameters.put("car_trans", car_trans);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
