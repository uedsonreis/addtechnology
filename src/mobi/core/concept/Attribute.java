package mobi.core.concept;

import mobi.core.common.Concept;

public class Attribute extends Concept {

	private static final long serialVersionUID = -8570987435663110802L;
	private Class baseClass;
	private AttributeTypeEnum type;
	private boolean many = false;

	public Attribute(String uri, AttributeTypeEnum type) {
		super(uri);
		this.type = type;
	}
	
	public Attribute(String uri, Class baseClass, AttributeTypeEnum type) {
		super(uri);
		this.baseClass = baseClass;
		this.type = type;
	}

	public Class getBaseClass() {
		return baseClass;
	}

	public void setBaseClass(Class baseClass) {
		this.baseClass = baseClass;
	}
	
	public AttributeTypeEnum getType() {
		return type;
	}

	public void setType(AttributeTypeEnum type) {
		this.type = type;
	}

	public boolean isMany() {
		return this.many;
	}
	public void setMany(boolean many) {
		this.many = many;
	}
}
