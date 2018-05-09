package io.magiccraftmaster.util.bind.serializer;

import java.time.OffsetDateTime;

public final class OffsetDateTimeSerializer implements Serializer<OffsetDateTime> {
	@Override
	public String serialize(OffsetDateTime offsetDateTime) {
		return offsetDateTime.toString();
	}

	@Override
	public OffsetDateTime deserialize(Object o) throws SerializationException {
		if (o instanceof String) return OffsetDateTime.parse((String) o);
		throw new SerializationException();
	}

	@Override
	public Class<?> getType() {
		return OffsetDateTime.class;
	}
}
