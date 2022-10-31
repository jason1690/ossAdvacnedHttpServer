package endpoints;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseTemplates;

import java.io.IOException;

public class UserEditPage implements Endpoint{
    @Override
    public Endpoint getInstance() {
        return new UserEditPage();
    }

    @Override
    public HttpResponse process(HttpRequest request) throws IOException {
        if(request.getRequestMethod().equals("GET"))
            return processGetRequest(request);
        if(request.getRequestMethod().equals("POST"))
            return processPostRequest(request);
        return ResponseTemplates.http422Response("Method not allowed here");
    }

    private HttpResponse processGetRequest(HttpRequest request){

    }

    private HttpResponse processPostRequest(HttpRequest request){

    }

    @Override
    public String getLocation() {
        return "/user/edit";
    }
}
