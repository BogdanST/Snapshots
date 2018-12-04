package a300.cem.RecyclerViewFollow;

public class UsersObject {
    private String email;
    private String uid;

    //Constructor for UserObject [Email, uid]
    public UsersObject(String email, String uid){
        this.email = email;
        this.uid = uid;
    }

    //get each parameter individually
    public String getUid() {
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }


}
