package warcode;

import java.io.FileDescriptor;
import java.security.Permission;

public class WarcodeSecurityManager extends SecurityManager {
	private boolean permissionsAllowed = true;

	public void checkRead(FileDescriptor filedescriptor) {
		if (!permissionsAllowed) {
			throw new SecurityException("You are not allowed to read or write files.");
		}
	}

	public void checkRead(String filename) {
		if (!permissionsAllowed) {
			throw new SecurityException("You are not allowed to read or write files.");
		}
	}

	public void checkRead(String filename, Object executionContext) {
		if (!permissionsAllowed) {
			throw new SecurityException("You are not allowed to read or write files.");
		}
	}

	public void checkWrite(FileDescriptor filedescriptor) {
		if (!permissionsAllowed) {
			throw new SecurityException("You are not allowed to read or write files.");
		}
	}

	public void checkWrite(String filename) {
		if (!permissionsAllowed) {
			throw new SecurityException("You are not allowed to read or write files.");
		}
	}
	
	public void checkPermission(Permission permission) {
		if(!permissionsAllowed) {
			throw new SecurityException("You are not allowed to do that!  Illegally tried to " + permission);
		}
	}

	protected void setPermissionsAllowed(boolean bool) {
		permissionsAllowed = bool;
	}

}
