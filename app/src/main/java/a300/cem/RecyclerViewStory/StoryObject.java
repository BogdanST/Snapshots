package a300.cem.RecyclerViewStory;

public class StoryObject {
    private String email;
    private String uid;
    private String chatOrStory;

    //Constructor for UserObject [Email, uid]
    public StoryObject(String email, String uid, String chatOrStory){
        this.email = email;
        this.uid = uid;
        this.chatOrStory = chatOrStory;
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

    public String getChatOrStory() {
        return chatOrStory;
    }

    public void setChatOrStory(String chatOrStory){
        this.chatOrStory = chatOrStory;
    }


    //This function will solve the issue of returning an another object (with a different story), but with a same uid and email.
    @Override
    public boolean equals(Object obj){

        boolean same = false;
        if(obj != null && obj instanceof StoryObject){
            same = this.uid == ((StoryObject)obj).uid;
        }
        return same;
    }


    //this function will solve the problem of ".contains" from StoryFragment
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.uid == null ? 0 : this.uid.hashCode());
        return result;
    }

}
