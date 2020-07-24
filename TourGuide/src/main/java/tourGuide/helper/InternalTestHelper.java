package tourGuide.helper;

public class InternalTestHelper {

	// TODO Set to 5 users for development tests...
	private static int internalUserNumber = 5;
	
	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}
	
	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}
