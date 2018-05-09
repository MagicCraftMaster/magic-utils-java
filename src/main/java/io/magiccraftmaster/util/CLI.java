package io.magiccraftmaster.util;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class CLI {
	public final class Argument {
		private final String full, shorthand, description;

		Argument(String full, String shorthand, String description) {
			this.full = full;
			this.shorthand = shorthand;
			this.description = description;
		}

		String getFull() {
			return full;
		}

		String getShorthand() {
			return shorthand;
		}

		String getDescription() {
			return description;
		}
	}
	public interface ArgumentRunnable {
		void run(String argument);
	}
	public final class ArgumentException extends Exception {
		ArgumentException() {
		}

		ArgumentException(String message) {
			super(message);
		}

		ArgumentException(String message, Throwable cause) {
			super(message, cause);
		}

		ArgumentException(Throwable cause) {
			super(cause);
		}

		ArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

	public CLI() {
		addOption("help","h","Shows this message", this::help);
	}

	private final Map<Argument,Object> argumentMap = new LinkedHashMap<>();

	public void addOption(String full, String shorthand, String description, Runnable runnable) {
		argumentMap.put(new Argument("--"+full, "-"+shorthand, description), runnable);
	}
	public void addOption(String full, String shorthand, String description, ArgumentRunnable runnable) {
		argumentMap.put(new Argument("--"+full, "-"+shorthand, description), runnable);
	}

	public void handle(String[] arguments) throws ArgumentException {
		for (int i = 0, argumentsLength = arguments.length; i < argumentsLength; i++) {
			String argument = arguments[i];
			// ============================================================
			if (!argument.startsWith("-")) continue; // Skip non-args
			//noinspection LoopStatementThatDoesntLoop
			for (Argument definedArgument : argumentMap.keySet()) {
				// Check if isn't an option
				if (!definedArgument.getFull().equals(argument) && !definedArgument.getShorthand().equals(argument)) continue;

				Object runnable = argumentMap.get(definedArgument);
				if (runnable instanceof Runnable) ((Runnable) runnable).run();
				else if (runnable instanceof ArgumentRunnable) {
					if (arguments.length <= i)
						throw new ArgumentException("The argument \"" + definedArgument + "\" requires an option");
					if (arguments[i + 1].startsWith("-"))
						throw new ArgumentException("The argument \"" + definedArgument + "\" requires an option! Given: \"" + arguments[i + 1] + "\"");
					((ArgumentRunnable) runnable).run(arguments[i + 1]);
				}
				break;
			}
		}
	}

	private void help() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Argument argument : argumentMap.keySet()) {
			stringBuilder.append(argument.getShorthand()).append(", ").append(argument.getFull());
			if (argumentMap.get(argument) instanceof ArgumentRunnable) stringBuilder.append(" [option]");
			stringBuilder.append("\n\t").append(argument.getDescription()).append('\n');
		}
		System.out.println(StringUtils.box(stringBuilder.toString().split("\n")));
	}
}
