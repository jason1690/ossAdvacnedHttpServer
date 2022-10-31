package endpoints;

import database.User;
import database.UserBase;
import http.HttpPostRequest;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseTemplates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginPage implements Endpoint{



    @Override
    public Endpoint getInstance() {
        return new LoginPage();
    }

    @Override
    public HttpResponse process(HttpRequest request) throws IOException {
        if(request.getRequestMethod().equals("GET"))
            return ResponseTemplates.http200Response(new String[]{"static/login/login.html"});
        if(request.getRequestMethod().equals("POST"))
            return processPostData(request);
        throw new IOException("Method not allowed here: "+request.getRequestMethod());
    }

    private HttpResponse processPostData(HttpRequest request) throws IOException {
        HttpPostRequest postRequest = new HttpPostRequest(request);
        String username = postRequest.getPostField("user");
        String password = postRequest.getPostField("pass");
        User user = UserBase.getUser(username);
        if(user==null)
            return ResponseTemplates.http422Response("User "+username+" does not exist");
        if(!user.getPassword().equals(password))
            return ResponseTemplates.http422Response("WRONG PASSWORD");
        String cookie = UserBase.registerCookieSession(user);
        return ResponseTemplates.http200Response("successRedirect.html",getReplacementMap())
                .addHeader("set-cookie",cookie);
    }

    private Map<String,String> getReplacementMap(){
        Map<String,String> retv = new HashMap<>();
        retv.put("MESSAGE","Login Successful");
        retv.put("REDIRECT_PAGE","/user");
        return retv;
    }

    @Override
    public String getLocation() {
        return "/login";
    }


}
