package nicarng.selcar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CarUsedReq extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/used.php";
    private Map<String, String> parameters;

    public CarUsedReq(String carnumber,String car_location, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("carnumber",carnumber);
        parameters.put("car_location",car_location);
    }

    @Override
    public Map <String, String> getParams(){
        return parameters;
    }
}
