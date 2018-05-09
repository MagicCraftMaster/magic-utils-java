package io.magiccraftmaster.util.bind.serializer;

@SuppressWarnings("unused")
public interface Serializer<T> {
	Object serialize(T t);
	T deserialize(Object o) throws SerializationException;
	Class<?> getType();
}
