package io.magiccraftmaster.util.bind.serializer;

import java.awt.Color;

public final class ColorSerializer implements Serializer<Color> {
	@Override
	public String serialize(Color color) {
		return "0x" + Integer.toHexString(color.getRGB());
	}

	@Override
	public Color deserialize(Object o) throws SerializationException {
		if (o instanceof Integer) return new Color((int) o);
		if (o instanceof String) return new Color(Integer.parseInt(((String) o).replaceFirst("0x|#",""),16));
		throw new SerializationException();
	}

	@Override
	public Class<?> getType() {
		return Color.class;
	}
}
