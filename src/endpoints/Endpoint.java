package endpoints;

import http.*;

import java.io.IOException;

public interface Endpoint {

    public Endpoint getInstance();

    public HttpResponse process(HttpRequest request) throws IOException;

    public String getLocation();
}
