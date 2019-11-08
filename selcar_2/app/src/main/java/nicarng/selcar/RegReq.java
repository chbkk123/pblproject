package nicarng.selcar;



import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class RegReq extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/register.php";
    private Map<String,String> parameters;

    public RegReq(String userID, String userPASSWORD, String userNAME,String userEMAIL, String userPHONE, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);


        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPASSWORD",userPASSWORD);
        parameters.put("userNAME",userNAME);
        parameters.put("userEMAIL",userEMAIL);
        parameters.put("userPHONE",userPHONE);

    }

    @Override
    public Map<String,String> getParams(){

        return parameters;
    }

}
