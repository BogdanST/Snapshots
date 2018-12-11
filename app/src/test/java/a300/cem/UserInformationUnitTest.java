package a300.cem;

import android.support.test.espresso.core.internal.deps.guava.base.Equivalence;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class UserInformationUnitTest {

    public static ArrayList<String> listFollowing = new ArrayList<>();

    @Before
    public void unitArayEmpty() throws  Exception{
        assertThat("Array is empty when is initialised", listFollowing.size(), is(equalTo(0)));
    }

}
