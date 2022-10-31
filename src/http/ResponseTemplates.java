package http;

import http.HttpResponse;

import java.io.*;
import java.util.Date;
import java.util.Map;

public class ResponseTemplates {


    public static HttpResponse http200Response(String[] filename) throws IOException {
        HttpResponse response = createResponse(200,"OK");
        response.addFile(new File(filename[0]));
        if(filename.length>1)
            response.addHeader("content-type",filename[1]);
        return response;
    }

    public static HttpResponse http200Response(String fname) throws IOException {
        return http200Response(new String[]{fname});
    }

    public static HttpResponse http400Response(){
        return createResponse(400,"BAD REQUEST");
    }

    public static HttpResponse http404Response() throws IOException {
        HttpResponse response = createResponse(404,"NOT FOUND");
        response.addFile(new File("errorpages/404page.html"));
        return response;
    }

    public static HttpResponse http422Response(String reason) throws IOException{
        HttpResponse response = createResponse(422,"UNPROCESSABLE ENTITY");
        response.addContent(loadErrorTemplate(reason));
        return response;
    }

    public static HttpResponse http500Response(){
        HttpResponse response = createResponse(500,"INTERNAL SERVER ERROR");
        try{
            response.addFile(new File("errorpages/500page.html"));
        }catch(IOException e){
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse http501Response(){
        return createResponse(501,"NOT IMPLEMENTED");
    }


    private static HttpResponse addDefaultHeaders(HttpResponse response){
        response.addHeader("server","cosway-advance (version 0)");
        response.addHeader("cache-control","private, max-age=0");
        response.addHeader("date",new Date().toString());
        response.addHeader("connection","close");
        return response;
    }

    private static InputStream loadErrorTemplate(String message) throws IOException {
        FileInputStream fin = new FileInputStream("templates/errorTemplate.html");
        String doc = new String(fin.readAllBytes());
        return new ByteArrayInputStream(doc.replace("${MESSAGE}",message).getBytes());
    }

    private static InputStream loadTemplate(String file,String message) throws IOException {
        FileInputStream fin = new FileInputStream("templates/"+file);
        String doc = new String(fin.readAllBytes());
        return new ByteArrayInputStream(doc.replace("${MESSAGE}",message).getBytes());
    }

    private static HttpResponse createResponse(int code,String message){
        HttpResponse retv = new HttpResponse();
        retv.setCode(code);
        retv.setMessage(message);
        return addDefaultHeaders(retv);
    }

    public static HttpResponse http200Response(String templateFile, String message) throws IOException {
        HttpResponse response = createResponse(200,"UNPROCESSABLE ENTITY");
        response.addContent(loadTemplate(templateFile,message));
        return response;
    }

    public static HttpResponse http200Response(String templateFile, Map<String,String> replacements) throws IOException {
        HttpResponse response = createResponse(200,"UNPROCESSABLE ENTITY");
        FileInputStream fin = new FileInputStream("templates/"+templateFile);
        String doc = new String(fin.readAllBytes());
        for(String s : replacements.keySet())
            doc = doc.replace("${"+s+"}",replacements.get(s));
        response.addContent(new ByteArrayInputStream(doc.getBytes()));
        return response;
    }
}
