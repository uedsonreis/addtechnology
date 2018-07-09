package mobi.core.concept;

import java.util.HashSet;
import java.util.Set;

import mobi.core.common.Concept;
import mobi.core.common.Relation;

public class Tale extends Concept {

	private static final long serialVersionUID = -6372676657343139280L;

	private Set<Relation> relations = new HashSet<Relation>();
	private String text;
	
	public Tale() {}
	
	public Tale(String uri){
		super(uri);
	}
	
	public Set<Relation> getRelations() {
		return relations;
	}

	public void setRelations(Set<Relation> relations) {
		this.relations = relations;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	
	
}
