package http;

import java.io.IOException;

public class HttpFormatException extends IOException {
    public HttpFormatException(String reason){
        super(reason);
    }
}
