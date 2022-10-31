package database;

import utils.Utils;

import java.util.concurrent.ConcurrentHashMap;

public class UserBase {

    private static ConcurrentHashMap<String,User> users = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,User> cookieSessions = new ConcurrentHashMap<>();




    public static void addUser(String name,String password){
        if(users.contains(name))
            return;
        else users.put(name,new User(name,password));
    }

    public static User getUser(String user) {
        return users.get(user);
    }

    public static String registerCookieSession(User user){
        pruneCookies(user);
        String cookie = Utils.genRandomString(10);
        cookieSessions.put(cookie,user);
        return cookie;
    }

    public static User getCookieSession(String cookie){
        return cookieSessions.get(cookie);
    }

    public static void pruneCookies(User user){
        for(String s : cookieSessions.keySet())
            if(cookieSessions.get(s).getName()==user.getName())
                cookieSessions.remove(s);
    }

}
