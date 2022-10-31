package endpoints;

import database.User;
import database.UserBase;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseTemplates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserPage implements Endpoint{


    @Override
    public Endpoint getInstance() {
        return new UserPage();
    }

    @Override
    public HttpResponse process(HttpRequest request) throws IOException {
        if(request.getOperands().length!=0){
            User user = UserBase.getUser(request.getOperands()[0]);
            if(user==null)
                return ResponseTemplates.http422Response("User "+request.getOperands()[0]+" Does not exist");
            return buildUserResponse(user);
        }


        if(request.getHeader("cookie")==null || (UserBase.getCookieSession(request.getHeader("cookie"))==null))
            return ResponseTemplates.http422Response("you are not logged in");

        return buildUserResponse(UserBase.getCookieSession(request.getHeader("cookie")));
    }

    @Override
    public String getLocation() {
        return "/user";
    }

    private HttpResponse buildUserResponse(User user) throws IOException {
        Map<String,String> replacements = new HashMap<>();
        replacements.put("USERNAME",user.getName());
        replacements.put("DESCRIPTION",user.getDescription());
        replacements.put("CREATION_DATE",String.valueOf(user.getCreateDateUnixTime()));
        replacements.put("FRIENDS_LIST",buildFriendsList(user));
        return ResponseTemplates.http200Response("userpage.html",replacements);
    }

    private String buildFriendsList(User user){
        StringBuilder sb = new StringBuilder("<ul>");
        for(User u : user.getFriends()){
            sb.append("<li><a href=\"/users?")
                    .append(u.getName())
                    .append("\">")
                    .append(u.getName())
                    .append("</a></li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
