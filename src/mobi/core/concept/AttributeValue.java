package mobi.core.concept;

public class AttributeValue {
	
	private AttributeTypeEnum attributeType;
	private String value;

	public AttributeValue(AttributeTypeEnum attributeType, String value) {
		this.attributeType = attributeType;
		this.value = value;
	}
	
	public AttributeTypeEnum getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeTypeEnum attributeType) {
		this.attributeType = attributeType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
