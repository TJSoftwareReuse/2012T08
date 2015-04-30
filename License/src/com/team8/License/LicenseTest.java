import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LicenseTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInLicenseTrue() {
		License t = new License(3);
		assertTrue(t.inLicense());
	}
	@Test
	public void testInLicenseFalse(){
		License t = new License(2);
		t.inLicense();
		t.inLicense();
		assertFalse(t.inLicense());
		License k = new License(0);
		assertFalse(k.inLicense());
	}
	@Test
	public void testGetLicense() {
		License t =new License(3);
		assertEquals(3,t.getLicense());
	}

}
