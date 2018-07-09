package mobi.core.concept;

import java.util.HashMap;

import mobi.core.common.Concept;

public class Instance extends Concept {

	private static final long serialVersionUID = -7078943832175294633L;
	
	private Class baseClass;
	private HashMap<String, AttributeValue> attributeValueHashMap = new HashMap<String, AttributeValue>();

	public Instance(String uri) {
		super(uri);
	}

	public Instance(String uri, Class baseClass) {
		super(uri);
		this.baseClass = baseClass;
	}

	public Class getBaseClass() {	
		return this.baseClass;
	}
	public void setBaseClass(Class baseClass) {
		this.baseClass = baseClass;
	}

	public HashMap<String, AttributeValue> getAttributeValueHashMap() {
		return attributeValueHashMap;
	}
	public void setAttributeValueHashMap(HashMap<String, AttributeValue> attributeValueHashMap) {
		this.attributeValueHashMap = attributeValueHashMap;
	}

	public void isSimilar(Instance instancia) {}
}
