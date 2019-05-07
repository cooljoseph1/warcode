package warcode;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

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

		Instrumentation inst = InstrumentHook.getInstrumentation();
		for (Class<?> clazz : inst.getInitiatedClasses(this)) {
			if (SPECS.DISALLOWED_CLASSES.contains(clazz.getName())) {
				throw new GameException("You can't import the class " + clazz.getName());
			}
		}

		return c;

	}
}

class InstrumentHook {

	public static void premain(String agentArgs, Instrumentation inst) {
		if (agentArgs != null) {
			System.getProperties().put(AGENT_ARGS_KEY, agentArgs);
		}
		System.getProperties().put(INSTRUMENTATION_KEY, inst);
	}

	public static Instrumentation getInstrumentation() {
		return (Instrumentation) System.getProperties().get(INSTRUMENTATION_KEY);
	}

	// Needn't be a UUID - can be a String or any other object that
	// implements equals().
	private static final Object AGENT_ARGS_KEY = UUID.fromString("887b43f3-c742-4b87-978d-70d2db74e40e");

	private static final Object INSTRUMENTATION_KEY = UUID.fromString("214ac54a-60a5-417e-b3b8-772e80a16667");

}