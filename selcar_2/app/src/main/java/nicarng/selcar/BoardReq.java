package nicarng.selcar;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BoardReq extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/board.php";
    private Map<String,String> parameters;

    public BoardReq(String user_id, String Subject, String Description, String Spinner_value, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);


        parameters = new HashMap<>();
        parameters.put("user_id",user_id);
        parameters.put("Subject",Subject);
        parameters.put("Description",Description);
        parameters.put("Spinner_value",Spinner_value);
        Log.e("error","잘안됨");
    }

    @Override
    public Map<String,String> getParams(){

        return parameters;
    }
}
