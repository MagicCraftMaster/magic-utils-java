package io.magiccraftmaster.util.bind.serializer;

@SuppressWarnings("unused")
public final class SerializationException extends Exception {
	public SerializationException() {}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}

	public SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
