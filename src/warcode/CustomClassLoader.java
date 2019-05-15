package warcode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import exceptions.GameException;

public class CustomClassLoader extends ClassLoader {

	public CustomClassLoader(ClassLoader cl) {
		super(cl);
	}

	@Override
	public Class<?> loadClass(String path) throws ClassNotFoundException {
		Class<?> c = super.loadClass(path);

		// Check to make sure they don't have static variables
		Field[] variables = c.getDeclaredFields();
		for (Field variable : variables) {
			int modifiers = variable.getModifiers();
			if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers) && !variable.isSynthetic()) {
				// All static variables must either be final or created by the compiler

				throw new GameException(
						"Static variables are not allowed unless they are final:  " + variable.getName());
			}
		}
		
		return c;

	}
}