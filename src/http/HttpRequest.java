package http;

import utils.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HttpRequest {

    private String requestMethod;
    private String requestLocation;
    private String httpVersion;
    private Map<String,String> headers = new HashMap<String,String>();
    private InputStream content;
    private String[] operands = new String[0];
    public HttpRequest(InputStream input) throws IOException {
            Scanner  scanner = new Scanner(getHeader(input));
            String headline = scanner.nextLine();
            if(headline.split(" ").length!=3)
                throw new HttpFormatException("Request Line Parse Error "+headline);
            requestMethod = headline.split(" ")[0];
            requestLocation = headline.split(" ")[1];
            if(requestLocation.contains("?")){
                String[] splitLocation = requestLocation.split("\\?");
                requestLocation = splitLocation[0];
                operands = Utils.trimStringArray(splitLocation,1,splitLocation.length);
            }
            httpVersion = headline.split(" ")[2];

            String line;
            while(true){
                line=scanner.nextLine();
                if(line==null)
                    throw new HttpFormatException("Unexpected end of file");

                line = line.trim().replace("\r","");
                if(line.equals(""))
                    break;

                String[] split = line.split(": ");
                if(split.length!=2)
                    throw new HttpFormatException("Error While Parsing Header: "+line);

                headers.put(split[0].trim().toLowerCase(),split[1].trim());
            }
            scanner.close();
            this.content=input;
    }

    public String[] getOperands(){
        return operands;
    }

    public String getHeader(String key){
        return headers.get(key);
    }

    public String[] getHeaderKeys(String key){
        return headers.keySet().toArray(new String[0]);
    }

    public String getRequestMethod (){
        return requestMethod;
    }

    public String getRequestLocation(){
        return requestLocation;
    }

    public String getHttpVersion(){
        return httpVersion;
    }

    public InputStream getContent(){
        return content;
    }

    public String getContentAsString() throws IOException {
        if(headers.get("content-length")==null)
            return "";
        byte[] content = new byte[Integer.valueOf(headers.get("content-length"))];
        this.content.read(content);
        return new String(content);
    }

    private InputStream getHeader(InputStream input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        char lastChar='a';
        int i;
        while((i=input.read())!=-1){
            if(i=='\r')
                continue;
            outputStream.write((byte)i);
            if(lastChar=='\n'&&(char)i=='\n')
                break;
            lastChar=(char)i;
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
