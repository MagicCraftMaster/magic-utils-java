package io.magiccraftmaster.util.bind;

import io.magiccraftmaster.util.bind.serializer.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class ObjectMapper {
	private static final List<String> primitives;
	static {
		primitives = new ArrayList<>();
		// Add types
		primitives.add(String.class.getName());
		primitives.add(Integer.class.getName());
		primitives.add(Double.class.getName());
		primitives.add(Long.class.getName());
		primitives.add(Float.class.getName());
		primitives.add(Short.class.getName());
		primitives.add(Boolean.class.getName());
		primitives.add(Character.class.getName());
	}

	private final List<Serializer<?>> serializers = new ArrayList<>();

	public ObjectMapper(boolean useDefaultSerializers) {
		if (useDefaultSerializers) {
			addSerializer(new ColorSerializer());
			addSerializer(new OffsetDateTimeSerializer());
			addSerializer(new LocalDateSerializer());
		}
	}

	public void addSerializer(Serializer<?> serializer) {
		serializers.add(serializer);
	}

	@SuppressWarnings("WeakerAccess")
	public Map<String,Object> serialize(Object object) throws IllegalAccessException, SerializationException {
		Map<String,Object> map = new LinkedHashMap<>();
		for (Field field : object.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(Bind.class)) continue;
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			if (!field.getAnnotation(Bind.class).required() && String.valueOf(field.get(object)).matches("null|0|0\\.0|false")) continue;
			String key = field.getAnnotation(Bind.class).value().equals(Bind.DEFAULT_VALUE) ? field.getName() : field.getAnnotation(Bind.class).value();

			if (field.getType().isArray()) {
				List<Object> list = new ArrayList<>();
				Object array = field.get(object);
				for (int i = 0, length = Array.getLength(array); i<length; i++) {
					Object o = Array.get(array,i);
					if (o == null) continue;
					if (o.getClass().isPrimitive() || primitives.contains(o.getClass().getName()))
						list.add(o);
					else if (field.getType().getComponentType().isEnum())
						list.add(o.toString());
					else
						list.add(serialize(Array.get(array,i)));
				}
				map.put(key,list);
				continue;
			}

			if (field.getType().isPrimitive() || primitives.contains(field.getType().getName())) {
				map.put(key, field.get(object));
				continue;
			}

			if (field.getType().isAnnotationPresent(Serializable.class)) map.put(key, serialize(field.get(object)));

			for (Serializer serializer : serializers) {
				if (serializer.getType() == field.getType()) {
					//noinspection unchecked
					map.put(key, field.get(object) != null ? serializer.serialize(field.get(object)) : null);
					break;
				}
			}

			if (field.getType().isEnum()) {
				map.put(key, field.get(object).toString());
			}

			if (!map.containsKey(key)) throw new SerializationException();
		}
		return map;
	}

	public <T> T deserialize(Map<String,Object> map, Class<T> clazz) throws SerializationException, IllegalAccessException {
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		for (Field field : clazz.getDeclaredFields()) {
			if (!field.isAnnotationPresent(Bind.class)) continue;
			boolean accessible = field.isAccessible();
			if (!accessible) field.setAccessible(true);
			String key = field.getAnnotation(Bind.class).value().equals(Bind.DEFAULT_VALUE) ? field.getName() : field.getAnnotation(Bind.class).value();
			if (field.getAnnotation(Bind.class).required() && !map.containsKey(key)) throw new SerializationException(String.format("Key[\"%s\"](%s) is required", key, field.getType().getName()));
			if (!map.containsKey(key)) continue;

			if (field.getType().isArray()) {
				if (!(map.get(key) instanceof List)) throw new SerializationException(String.format("Key[\"%s\"](%s) is an array", key, field.getType().getCanonicalName()));
				List<?> list = (List<?>) map.get(key);
				Object array = Array.newInstance(field.getType().getComponentType(), list.size());
				for (int i = 0, listSize = list.size(); i < listSize; i++) {
					Object object = null;
					if (field.getType().getComponentType().isPrimitive() || primitives.contains(field.getType().getComponentType().getName())) {
						if (list.get(i).getClass().isPrimitive() || primitives.contains(list.get(i).getClass().getName())) {
							object = list.get(i);
						}
					} else if (field.getType().getComponentType().isEnum()) {
						if (!(list.get(i) instanceof String)) throw new SerializationException(String.format("Key[\"%s\"] is an enum and should be represented as a string name of the constant", key));
						//noinspection unchecked
						object = Enum.valueOf((Class<Enum>) field.getType().getComponentType(), (String) list.get(i));
					} else if (field.getType().getComponentType().isAnnotationPresent(Serializable.class)) {
						if (!(list.get(i) instanceof Map)) throw new SerializationException(String.format("Key[\"%s\"](%s) should be a map", key, field.getType().getComponentType().getCanonicalName()));
						//noinspection unchecked
						object = deserialize((Map<String, Object>) list.get(i), field.getType().getComponentType());
					} else {
						for (Serializer serializer : serializers) {
							if (serializer.getType() == field.getType().getComponentType()) {
								object = serializer.deserialize(map.get(key));
								break;
							}
						}
					}
					Array.set(array, i, object);
				}
				field.set(t, array);
				continue;
			}

			if (field.getType().isPrimitive() || primitives.contains(field.getType().getName())) {
				if (field.getType().getName().equals("int") && map.get(key).getClass() == Integer.class) field.set(t, map.get(key));
				if (field.getType().getName().equals("double") && map.get(key).getClass() == Double.class) field.set(t, map.get(key));
				if (field.getType().getName().equals("float") && map.get(key).getClass() == Float.class) field.set(t, map.get(key));
				if (field.getType().getName().equals("long") && map.get(key).getClass() == Long.class) field.set(t, map.get(key));
				if (field.getType().getName().equals("short") && map.get(key).getClass() == Short.class) field.set(t, map.get(key));
				if (field.getType().getName().equals("boolean") && map.get(key).getClass() == Boolean.class) field.setBoolean(t, ((boolean) map.get(key)) ? Boolean.TRUE : Boolean.FALSE);
				if (field.getType().getName().equals("char") && map.get(key).getClass() == Character.class) field.set(t, map.get(key));
				if (field.getType() == map.get(key).getClass()) field.set(t, map.get(key));
				continue;
			}

			if (field.getType().isAnnotationPresent(Serializable.class)) {
				if (map.get(key) instanceof Map) {
					//noinspection unchecked
					field.set(t, deserialize((Map<String, Object>) map.get(key), field.getType()));
					continue;
				} else
					throw new SerializationException(String.format("Key[\"%s\"](%s) should be a map", key, field.getType().getCanonicalName()));
			}

			boolean handled = false;
			for (Serializer serializer : serializers) {
				if (serializer.getType() == field.getType()) {
					field.set(t, map.get(key) != null ? serializer.deserialize(map.get(key)) : null);
					handled = true;
					break;
				}
			}
			if (handled) continue;

			if (field.getType().isEnum()) {
				if (!(map.get(key) instanceof String)) throw new SerializationException(String.format("Key[\"%s\"] is an enum and should be represented as a string name of the constant", key));
				//noinspection unchecked
				field.set(t, Enum.valueOf((Class<Enum>) field.getType(), (String) map.get(key)));
				continue;
			}

			throw new SerializationException(String.format("Failed to handle field[\"%s\"]", field.getName()));
		}
		return t;
	}
}
