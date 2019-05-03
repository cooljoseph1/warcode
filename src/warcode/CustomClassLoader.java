package warcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
		Field[] variables = c.getDeclaredFields();
		for (Field variable : variables) {
			int modifiers = variable.getModifiers();
			if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers) && !variable.isSynthetic()) {
				// All static variables must either be final or created by the compiler
				// (synthetic)

				throw new GameException(
						"Static variables are not allowed unless they are final:  " + variable.getName());
			}
		}

		return c;

	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] b = loadClassFromFile(name);
		return defineClass(name, b, 0, b.length);
	}

	private byte[] loadClassFromFile(String fileName) {
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(fileName.replace('.', File.separatorChar) + ".class");
		byte[] buffer;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int nextValue = 0;
		try {
			while ((nextValue = inputStream.read()) != -1) {
				byteStream.write(nextValue);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = byteStream.toByteArray();
		return buffer;
	}
}