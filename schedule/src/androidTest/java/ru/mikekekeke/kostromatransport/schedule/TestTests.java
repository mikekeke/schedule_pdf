package ru.mikekekeke.kostromatransport.schedule;

import android.test.AndroidTestCase;

import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;

/**
 * Created by Mikekeke on 19-May-16.
 */
public class TestTests extends AndroidTestCase {

    public void testDataSchemeExistence() {
        assertTrue("Error", (mContext.getFileStreamPath(DataScheme.SCHEME_FILE)).exists());
    }

}
