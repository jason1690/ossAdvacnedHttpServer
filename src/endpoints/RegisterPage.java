package endpoints;

import database.UserBase;
import http.HttpPostRequest;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseTemplates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterPage implements Endpoint{


    @Override
    public Endpoint getInstance() {
        return new RegisterPage();
    }

    @Override
    public HttpResponse process(HttpRequest request) throws IOException {
        if(request.getRequestMethod().equalsIgnoreCase("GET"))
            return ResponseTemplates.http200Response(new String[]{"static/register/index.html","text/html"});
        if(request.getRequestMethod().equalsIgnoreCase("POST"))
            return processRegisterData(request);
        throw new IOException("Method not allowed here: "+request.getRequestMethod());
    }

    @Override
    public String getLocation() {
        return "/register";
    }

    private HttpResponse processRegisterData(HttpRequest request) throws IOException {
        HttpPostRequest postRequest = new HttpPostRequest(request);
        String username = postRequest.getPostField("name");
        String password = postRequest.getPostField("pass");
        if(UserBase.getUser(postRequest.getPostField("name"))!=null)
            return ResponseTemplates.http422Response("User "+username+" already exists");
        UserBase.addUser(username,password);
        return ResponseTemplates.http200Response("successRedirect.html",getReplacementMap())
            .addHeader("set-cookie",UserBase.registerCookieSession(UserBase.getUser(username)));
    }

    private Map<String,String> getReplacementMap(){
        Map<String,String> retv = new HashMap<>();
        retv.put("MESSAGE","User Created Successfully");
        retv.put("REDIRECT_PAGE","/user");
        return retv;
    }
}
