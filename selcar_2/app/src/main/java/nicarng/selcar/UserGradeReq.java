package nicarng.selcar;



import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class UserGradeReq extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/UserGradeRegister.php";
    private Map<String,String> parameters;

    public UserGradeReq(String userID, String grade, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);


        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("grade",grade);

    }

    @Override
    public Map<String,String> getParams(){

        return parameters;
    }

}