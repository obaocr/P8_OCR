package tourGuide.helper;

/**
 * internalUserNumber is the number of User for test mode
 */
public class InternalTestHelper {

	private static int internalUserNumber = 5000;

	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}
	
	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}
