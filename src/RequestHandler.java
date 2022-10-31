import http.HttpFormatException;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseTemplates;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable{
    private Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        HttpRequest request =parseRequest();
        HttpResponse response = generateResponse(request);
        try{
            response.send(client.getOutputStream());
            close();
        }catch(IOException e){
            e.printStackTrace();
            close();
        }
    }

    private HttpResponse generateResponse(HttpRequest request){
        try{
            return createResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseTemplates.http500Response();
        }
    }

    private HttpResponse createResponse(HttpRequest request) throws Exception{
        if(!Config.supportedRequestMethods.contains(request.getRequestMethod()))
            return ResponseTemplates.http501Response();
        if(Config.endpointMap.containsKey(request.getRequestLocation()))
            return Config.endpointMap.get(request.getRequestLocation()).getInstance().process(request);
        if(Config.fileMap.containsKey(request.getRequestLocation()))
            return ResponseTemplates.http200Response(Config.fileMap.get(request.getRequestLocation()));
        return ResponseTemplates.http404Response();
    }

    private HttpRequest parseRequest(){
        try{
            return new HttpRequest(client.getInputStream());
        }catch(IOException e){
            processRequestError(e);
            throw new RuntimeException(e);
        }
    }

    private void processRequestError(IOException error){
        error.printStackTrace();
        try{
            if(error instanceof HttpFormatException)
                ResponseTemplates.http400Response().send(client.getOutputStream());
            else
                ResponseTemplates.http500Response().send(client.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
