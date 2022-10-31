package http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class HttpPostRequest{

    private HashMap<String,String> postDataMap = new HashMap<>();

    private HttpRequest request;

    public HttpPostRequest(HttpRequest request) throws IOException {
        if(!request.getRequestMethod().equals("POST"))
            throw new IOException("TRYING TO PARSE POST DATA FROM "+request.getRequestMethod()+" REQUEST");
        this.request=request;
        if(request.getHeader("content-length")==null)
            return;
        if(request.getHeader("content-length").equals("0"))
            return;
        byte[] postData = new byte[Integer.valueOf(request.getHeader("content-length"))];
        request.getContent().read(postData);
        parsePostData(new String(postData));
    }

    private void parsePostData(String postData){
        String[] fields = postData.split("\\&");
        for(String s : fields){
            String[] values = s.split("\\=");
            postDataMap.put(values[0],values[1]);
        }
    }

    public String[] getPostKeySet(){
        return postDataMap.keySet().toArray(new String[0]);
    }


    public String getPostField(String s) {
        return postDataMap.get(s);
    }
}
