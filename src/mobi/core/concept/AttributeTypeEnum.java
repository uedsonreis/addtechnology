package mobi.core.concept;

import java.util.HashSet;
import java.util.Set;

public enum AttributeTypeEnum {

	STRING("String"), INTEGER("Integer"), LONG("Long"), DOUBLE("Double"), BOOLEAN("Boolean"), DATE("Date");

	private AttributeTypeEnum(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	private static Set<AttributeTypeEnum> TYPES = new HashSet<AttributeTypeEnum>();

	static {
		TYPES.add(STRING);
		TYPES.add(INTEGER);
		TYPES.add(LONG);
		TYPES.add(DOUBLE);
		TYPES.add(BOOLEAN);
		TYPES.add(DATE);
	}

	public static boolean contains(String name) {
		for (AttributeTypeEnum type : TYPES) {
			if (type.getName().equals(name)) return true;
		}
		return false;
	}
}
