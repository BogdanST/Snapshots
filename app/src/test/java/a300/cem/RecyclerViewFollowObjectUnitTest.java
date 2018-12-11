package a300.cem;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import a300.cem.RecyclerViewFollow.FollowObject;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class RecyclerViewFollowObjectUnitTest {

    private String uid = "812391231312";
    private String email = "newuser@gmail.com";


    @Test
    public void UserObjectUid() throws Exception{
        FollowObject newObj = new FollowObject(uid, email);
        assertThat("The user id is equal with the given uid: 812391231312", newObj.getUid(), is(equalTo("812391231312")));
    }

    @Test
    public void UserObjectEmail() throws Exception{
        FollowObject newObj = new FollowObject(uid, email);
        assertThat("The user id is equal with the given uid: newuser@gmail.com", newObj.getUid(), is(equalTo("newuser@gmail.com")));
    }

    @Before
    public void initTests() {
        FollowObject newObj = new FollowObject(uid, email);
    }
}
