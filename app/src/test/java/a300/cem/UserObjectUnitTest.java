package a300.cem;

import org.junit.Before;
import org.junit.Test;

public class UserObjectUnitTest {

    private String uid = "812391231312";
    private String email = "newuser@gmail.com";
    private String name = "Paul";
    private String profile;

    @Test
    public void UserObjectUid() throws Exception{
        UserObjectDb newObj = new UserObjectDb(uid, email, name, profile);
        assert(newObj.getUid()).equals("812391231312");
    }

    @Test
    public void UserObjectEmail() throws Exception{
        UserObjectDb newObj = new UserObjectDb(uid, email, name, profile);
        assert(newObj.getEmail()).equals("newuser@gmail.com");
    }

    @Test
    public void UserObjectName() throws Exception{
        UserObjectDb newObj = new UserObjectDb(uid, email, name, profile);
        assert(newObj.getName()).equals("Paul");
    }

    @Test
    public void UserObjectProfile() throws Exception{
        UserObjectDb newObj = new UserObjectDb(uid, email, name, profile);
        assert(newObj.getProfile()).equals("default");
    }

    @Before
    public void initTests() {
        UserObjectDb newObj = new UserObjectDb(uid, email, name, profile);
    }

}
