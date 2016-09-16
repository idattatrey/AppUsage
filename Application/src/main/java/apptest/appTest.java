package apptest;

import com.example.android.appusagestatistics.TotalTimeOnMobile;

import junit.framework.TestCase;

/**
 * Created by DATTATREY on 16-Sep-16.
 */
public class appTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testMinites() {
        TotalTimeOnMobile totalTimeOnMobile = new TotalTimeOnMobile();
        Boolean result = totalTimeOnMobile.checkValue();
        assertEquals(result, Boolean.TRUE);
    }
}
