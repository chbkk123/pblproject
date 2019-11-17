package com.example.pbl_movie_4jo;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private String URL = "http://wjddn944.cafe24.com/pbl/IdValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);


        parameters = new HashMap<>();
        parameters.put("userID", userID);


    }

    @Override
    public Map<String, String> getParams() {

        return parameters;
    }

}