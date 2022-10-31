package http;

import utils.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final String HTTP_VERSION="http/1.1";
    private int code;
    private String message;

    private Map<String,String> headers = new HashMap<>();
    private InputStream content;

    public void addContent(InputStream content) throws IOException {
        headers.put("content-length",String.valueOf(content.available()));
        this.content=content;
    }

    public void addFile(File f ) throws IOException{
        FileInputStream fin = new FileInputStream(f);
        headers.put("content-length",String.valueOf(fin.available()));
        this.content=fin;
    }

    public void setCode(int code){
        this.code=code;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public HttpResponse addHeader(String key,String value){
        headers.put(key,value);
        return this;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public String getHeader(String key){
        return headers.get(key);
    }

    public InputStream getContent(){
        return content;
    }

    public void send(OutputStream output) throws IOException {
        byte[] buffer = new byte[2048];

        StringBuilder requestBuilder = new StringBuilder(HTTP_VERSION+" "+code +" "+ message+"\n");
        for(String key : headers.keySet())
            requestBuilder.append(key).append(": ").append(headers.get(key)).append("\n");
        requestBuilder.append("\n");

        output.write(requestBuilder.toString().getBytes());

        for(int i=0;i<Integer.valueOf(headers.get("content-length"));i+=buffer.length){
            int bufferLength=buffer.length;
            if(i+2048>Integer.valueOf(headers.get("content-length")))
                bufferLength=Integer.valueOf(headers.get("content-length"))-i;
            content.read(buffer);
            output.write(Utils.trimByteArray(buffer,bufferLength));
        }
    }
}
