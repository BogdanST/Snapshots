package a300.cem;

import org.junit.Before;
import org.junit.Test;


import a300.cem.RecyclerViewStory.StoryObject;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RecyclerViewStoryObjectUnitTest {

    private String uid = "812391231312";
    private String email = "newuser@gmail.com";
    private String chatOrStory = "chat";
    private String CoppychatOrStory = "story";


    @Test
    public void UserObjectUid() throws Exception{
        StoryObject newObj = new StoryObject(uid, email, chatOrStory);
        assertThat("The user id is equal with the given uid: 812391231312", newObj.getUid(), is(equalTo("812391231312")));
    }

    @Test
    public void UserObjectEmail() throws Exception{
        StoryObject newObj = new StoryObject(uid, email, chatOrStory);
        assertThat("The user id is equal with the given uid: newuser@gmail.com", newObj.getUid(), is(equalTo("newuser@gmail.com")));
    }
    @Test
    public void UserObjectChat() throws Exception{
        StoryObject newObj = new StoryObject(uid, email, chatOrStory);
        assertThat("The user id is equal with the given uid: newuser@gmail.com", newObj.getUid(), is(equalTo("chat")));
    }
    @Test
    public void UserObjectStory() throws Exception{
        StoryObject newObj = new StoryObject(uid, email, CoppychatOrStory);
        assertThat("The user id is equal with the given uid: newuser@gmail.com", newObj.getUid(), is(equalTo("story")));
    }

    @Test
    public void Equals() throws Exception{
        StoryObject newObj = new StoryObject(uid, email, CoppychatOrStory);
        assertThat("@Test the override function equals", newObj.equals(newObj), is(equalTo(true)));
    }

    @Before
    public void initTests() {
        StoryObject newObj = new StoryObject(uid, email, chatOrStory);
    }
}
