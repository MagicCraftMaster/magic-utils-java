package io.magiccraftmaster.util.bind.serializer;

import java.time.LocalDate;

public final class LocalDateSerializer implements Serializer<LocalDate> {
	@Override
	public Object serialize(LocalDate localDate) {
		return localDate.toString();
	}

	@Override
	public LocalDate deserialize(Object o) throws SerializationException {
		if (o instanceof String && ((String) o).matches("\\d{4}-\\d{1,2}-\\d{1,2}")) return LocalDate.parse((String) o);
		throw new SerializationException("Unknown format");
	}

	@Override
	public Class<?> getType() {
		return LocalDate.class;
	}
}
