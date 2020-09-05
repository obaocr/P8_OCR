package tourGuide.helper;

public class InternalTestHelper {

	// TODO Set to 10 users for development tests...
	private static int internalUserNumber = 10;
	
	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}
	
	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}
