package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/10.
 */
public class DatabaseHelperTest {

    private DatabaseHelper helper;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        helper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
    }

    @After
    public void tearDown() {
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testGetDatabaseHelper() {

    }

}
