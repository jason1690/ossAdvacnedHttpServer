package database;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private String password;
    private String description;
    private List<User> friends = new ArrayList<>();
    private long createDateUnixTime;

    public User(String name,String password){
        this.name=name;
        this.password=password;
        this.description="default description";
        createDateUnixTime = Instant.now().getEpochSecond();
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void addFriend(User user){
        friends.add(user);
    }

    public List<User> getFriends(){
        return friends;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public long getCreateDateUnixTime() {
        return createDateUnixTime;
    }

}
