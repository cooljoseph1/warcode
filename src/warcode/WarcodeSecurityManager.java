package warcode;

import java.io.FileDescriptor;

public class WarcodeSecurityManager extends SecurityManager {
	@Override
	public void checkRead(FileDescriptor filedescriptor) {
		throw new SecurityException("Sorry, you are not allowed to read files.");
	}

	@Override
	public void checkRead(String filename) {
		throw new SecurityException("Sorry, you are not allowed to read files.");
	}

	@Override
	public void checkRead(String filename, Object executionContext) {
		throw new SecurityException("Sorry, you are not allowed to read files.");
	}
}
