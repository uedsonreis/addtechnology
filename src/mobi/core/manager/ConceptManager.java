package mobi.core.manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mobi.core.common.Concept;
import mobi.core.common.Relation;
import mobi.core.concept.Attribute;
import mobi.core.concept.Class;
import mobi.core.concept.Context;
import mobi.core.concept.Instance;
import mobi.core.concept.Tale;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.GenericRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.InstanceRelation;
import mobi.core.relation.SymmetricRelation;
import mobi.exception.ExceptionEquivalenceInheritanceRelation;
import mobi.exception.ExceptionURI;

public class ConceptManager implements Serializable {

	private static final long serialVersionUID = -5261982195912426870L;

	private Context context = null;
	private HashMap<String, Instance> allInstances                       = new HashMap<String, Instance>();
	private HashMap<String, Class> allClasses                            = new HashMap<String, Class>();
	private HashMap<String, Tale> allTales                               = new HashMap<String, Tale>();
	private HashMap<String, Attribute> allAttributes                     = new HashMap<String, Attribute>();
	private HashMap<String, GenericRelation> allGenericRelations         = new HashMap<String, GenericRelation>();
	private HashMap<String, CompositionRelation> allCompositionRelations = new HashMap<String, CompositionRelation>();
	private HashMap<String, SymmetricRelation> allSymmetricRelations     = new HashMap<String, SymmetricRelation>();
	private HashMap<String, InheritanceRelation> allInheritanceRelations = new HashMap<String, InheritanceRelation>();
	private HashMap<String, EquivalenceRelation> allEquivalenceRelations = new HashMap<String, EquivalenceRelation>();
	private HashMap<String, Concept> allDestroyedConcepts                = new HashMap<String, Concept>();
	private HashMap<String, Concept> allRemovedConcepts                  = new HashMap<String, Concept>();
	private HashMap<String, Set<Class>> allInstanceClassRelation         = new HashMap<String, Set<Class>>();
	private HashMap<String, Set<Instance>> allClassInstanceRelation      = new HashMap<String, Set<Instance>>();
	private HashMap<String, Set<Attribute>> allClassAttributeRelation    = new HashMap<String, Set<Attribute>>();
	private HashMap<String, Set<Attribute>> allClassKeyAttributeRelation = new HashMap<String, Set<Attribute>>();
	private HashMap<String, HashMap<String, String>> instanceAttributeValueHashMap = new HashMap<String, HashMap<String, String>>();

	public ConceptManager() {
	}

	public void addConcept(Concept concept) throws Exception {

		if (concept.getClass().equals(Instance.class)) {
			this.addConcept((Instance) concept);
			return;
		}

		if (concept.getClass().equals(Class.class)) {
			this.addConcept((Class) concept);
			return;
		}

		if (concept.getClass().equals(GenericRelation.class)) {
			this.addConcept((GenericRelation) concept);
			return;
		}

		if (concept.getClass().equals(CompositionRelation.class)) {
			this.addConcept((CompositionRelation) concept);
			return;
		}

		if (concept.getClass().equals(SymmetricRelation.class)) {
			this.addConcept((SymmetricRelation) concept);
			return;
		}

		if (concept.getClass().equals(InheritanceRelation.class)) {
			this.addConcept((InheritanceRelation) concept);
			return;
		}

		if (concept.getClass().equals(EquivalenceRelation.class)) {
			this.addConcept((EquivalenceRelation) concept);
			return;
		}
		
		if (concept.getClass().equals(Tale.class)) {
			this.addConcept((Tale) concept);
			return;
		}
		
		if (concept.getClass().equals(Attribute.class)) {
			this.addConcept((Attribute) concept);
			return;
		}
	}

	private void addConcept(GenericRelation genericRelation) {
		if (!this.allGenericRelations.containsKey(genericRelation.getUri())) {
			this.allGenericRelations.put(genericRelation.getUri(), genericRelation);
		}
	}
	
	/**
	 * @author Andr� Schmid
	 * @param attribute		The attribute to be added to MOBI 
	 */
	private void addConcept(Attribute attribute) {
		if (!this.allAttributes.containsKey(attribute.getUri())) {
			this.allAttributes.put(attribute.getUri(), attribute);
		}
	}
	
	private void addConcept(Tale tale) {
		if (!this.allTales.containsKey(tale.getUri())) {
			this.allTales.put(tale.getUri(), tale);
		}
	}

	private void addConcept(Instance instance) {
		if (!this.allInstances.containsKey(instance.getUri())) {
			this.allInstances.put(instance.getUri(), instance);
		}
	}

	private void addConcept(Class c)throws Exception {
		if (!this.allClasses.containsKey(c.getUri())) {
			this.allClasses.put(c.getUri(), c);
		}else{
			throw new Exception("Classe igual");
		}
	}

	private void addConcept(CompositionRelation compositionRelation) {
		String directNameRelation  = this.getDirectNameOfRelation(compositionRelation);
		String inverseNameRelation = this.getInverseNameOfRelation(compositionRelation);

		if (!this.allCompositionRelations.containsKey(directNameRelation + inverseNameRelation)
		 && !this.allCompositionRelations.containsKey(inverseNameRelation + directNameRelation)) {
			compositionRelation.setContext(this.context);

			if (compositionRelation.getType() == Relation.BIDIRECIONAL_COMPOSITION) {
				compositionRelation.setNameA(directNameRelation);
				compositionRelation.setNameB(inverseNameRelation);
			} else
				compositionRelation.setNameA(directNameRelation);

			compositionRelation.setUri(directNameRelation + inverseNameRelation);
			this.allCompositionRelations.put(compositionRelation.getUri(), compositionRelation);
		}
	}

	private void addConcept(SymmetricRelation symmetricRelation) {
		String directNameRelation  = this.getDirectNameOfRelation(symmetricRelation);
		String inverseNameRelation = this.getInverseNameOfRelation(symmetricRelation);

		if (!this.allSymmetricRelations.containsKey(directNameRelation)
		 && !this.allSymmetricRelations.containsKey(inverseNameRelation)) {
			symmetricRelation.setContext(this.context);
			symmetricRelation.setName(directNameRelation);
			symmetricRelation.setUri(directNameRelation);
			this.allSymmetricRelations.put(symmetricRelation.getUri(), symmetricRelation);
		}
	}

	private void addConcept(EquivalenceRelation equivalenceRelation) throws Exception {
		String directNameRelation  = this.getDirectNameOfRelation(equivalenceRelation);
		String inverseNameRelation = this.getInverseNameOfRelation(equivalenceRelation);

		if (!this.allEquivalenceRelations.containsKey(directNameRelation)
		 && !this.allEquivalenceRelations.containsKey(inverseNameRelation)) {

			this.ValidateRelationEquivalenceOrInheritance(equivalenceRelation);
			equivalenceRelation.setContext(this.context);
			equivalenceRelation.setUri(directNameRelation);
			this.allEquivalenceRelations.put(equivalenceRelation.getUri(), equivalenceRelation);
		}
	}

	private void addConcept(InheritanceRelation inheritanceRelation) throws Exception {

		String directNameRelation  = this.getDirectNameOfRelation(inheritanceRelation);
		String inverseNameRelation = this.getInverseNameOfRelation(inheritanceRelation);

		if (!this.allInheritanceRelations.containsKey(directNameRelation)
		 && !this.allInheritanceRelations.containsKey(inverseNameRelation)) {

			this.ValidateRelationEquivalenceOrInheritance(inheritanceRelation);
			inheritanceRelation.setContext(this.context);
			inheritanceRelation.setUri(directNameRelation);
			
			//System.out.println("classe a: " + inheritanceRelation.getClassA().getUri());
			//System.out.println("HERAN�A: " + directNameRelation);
			this.allInheritanceRelations.put(inheritanceRelation.getUri(), inheritanceRelation);
		}
	}

	private void ValidateRelationEquivalenceOrInheritance(Relation relation)
			throws Exception {
		if (!relation.getClassA().getUri().equals(relation.getClassB().getUri())) {
			for (InstanceRelation instanceRelation : relation.getInstanceRelationMapA().values()) {
				String instanceBase = instanceRelation.getInstance().getUri();
				for (Instance instance : instanceRelation.getAllInstances().values()) {
					if (!instance.getUri().equals(instanceBase))
						throw new ExceptionEquivalenceInheritanceRelation(
								"The instance "
										+ instanceBase
										+ " of class "
										+ relation.getClassA().getUri()
										+ " has relation with different instance "
										+ instance.getUri() + " of class "
										+ relation.getClassB().getUri() + ".");
				}
			}
		} else
			throw new ExceptionEquivalenceInheritanceRelation("For this type of relation, the classes must be different.");
	}

	private String getDirectNameOfRelation(Relation r) {
		String nameMobiRelation = null;

		if (r.getType() == Relation.INHERITANCE || r.getType() == Relation.EQUIVALENCE)
			nameMobiRelation = r.getClassA().getUri() 
			                 + "_" 
			                 + r.getUri() 
			                 + "_" 
			                 + r.getClassB().getUri();
			//System.out.println("Nome a: " + r.getUri());
		else if (r.getType() == Relation.SYMMETRIC_COMPOSITION)
			nameMobiRelation = r.getClassA().getUri() 
			                 + "_"
			                 + ((SymmetricRelation) r).getName() 
			                 + "_"
			                 + r.getClassB().getUri();
		else if (r.getType() == Relation.UNIDIRECIONAL_COMPOSITION)
			nameMobiRelation = r.getClassA().getUri() 
							 + "_"
							 + ((CompositionRelation) r).getNameA() 
							 + "_"
							 + r.getClassB().getUri();
		else if (r.getType() == Relation.BIDIRECIONAL_COMPOSITION)
			nameMobiRelation = r.getClassA().getUri() 
			                 + "_"
			                 + ((CompositionRelation) r).getNameA() 
			                 + "_"
			                 + r.getClassB().getUri();

		//System.out.println("Retorno: " + nameMobiRelation);
		return nameMobiRelation;
	}

	private String getInverseNameOfRelation(Relation r) {
		String nameMobiRelation = null;

		if (r.getType() == Relation.INHERITANCE	|| r.getType() == Relation.EQUIVALENCE)
			nameMobiRelation = r.getClassB().getUri() 
			                 + "_" 
			                 + r.getUri() 
			                 + "_" 
			                 + r.getClassA().getUri();
		else if (r.getType() == Relation.SYMMETRIC_COMPOSITION)
			nameMobiRelation = r.getClassB().getUri() 
			                 + "_"
			                 + ((SymmetricRelation) r).getName() 
			                 + "_"
			                 + r.getClassA().getUri();
		else if (r.getType() == Relation.UNIDIRECIONAL_COMPOSITION)
			nameMobiRelation = "";
		else if (r.getType() == Relation.BIDIRECIONAL_COMPOSITION)
			nameMobiRelation = r.getClassB().getUri() 
			                 + "_"
			                 + ((CompositionRelation) r).getNameB() 
			                 + "_"
			                 + r.getClassA().getUri();

		return nameMobiRelation;
	}

	public void linkInstances(String instanceVector, String classURI) throws Exception {
		String[] s = instanceVector.split(";");
		for (int i = 0; i < s.length; i++) {
			Instance instance = new Instance(s[i]);
			this.addConcept(instance);
			this.isOneOf(s[i], classURI);
		}
	}

	public void linkInstances(Set<Instance> instanceSet, Class mobiClass) throws Exception {
		for (Instance instance : instanceSet) {
			this.addConcept(instance);
			this.isOneOf(instance, mobiClass);
		}
	}

	public void isOneOf(String instanceURI, String classURI) throws ExceptionURI {
		Instance instance = this.allInstances.get(instanceURI);
		Class mobiClass   = this.allClasses.get(classURI);

		this.isOneOf(instance, mobiClass);
	}

	public void isOneOf(Instance instance, Class mobiClass) throws ExceptionURI {
		addConcept(instance);

		if (this.allInstanceClassRelation.get(instance.getUri()) == null)
			this.allInstanceClassRelation.put(instance.getUri(), new HashSet<Class>());
		
		if (this.allClassInstanceRelation.get(mobiClass.getUri()) == null)
			this.allClassInstanceRelation.put(mobiClass.getUri(), new HashSet<Instance>());

		if (!this.allInstanceClassRelation.get(instance.getUri()).contains(mobiClass)) {
			this.allInstanceClassRelation.get(instance.getUri()).add(mobiClass);
		} else {
			throw new ExceptionURI(	"This instance is already bound to this class!");
		}
		if (!this.allClassInstanceRelation.get(mobiClass.getUri()).contains(instance)) {
			this.allClassInstanceRelation.get(mobiClass.getUri()).add(instance);
		} else {
			throw new ExceptionURI("This class is already bound to this instance!");
		}

	}
	
	/**
	 * @author Andr� Schmid
	 * Binds an attribute to a class
	 * @param mobiClass		The class to add the attribute
	 * @param attribute		The attribute to add to the class
	 * @throws ExceptionURI
	 * 			if the attribute is already bound to the given class
	 */
	public void belongsTo(Class mobiClass, Attribute attribute, Boolean isKey) throws ExceptionURI {
		addConcept(attribute);
		if (this.allClassAttributeRelation.get(mobiClass.getUri()) == null){
			this.allClassAttributeRelation.put(mobiClass.getUri(), new HashSet<Attribute>());
		}

		if (!this.allClassAttributeRelation.get(mobiClass.getUri()).contains(attribute)) {
			this.allClassAttributeRelation.get(mobiClass.getUri()).add(attribute);

			if (isKey) {
				if (this.allClassKeyAttributeRelation.get(mobiClass.getUri()) == null){
					this.allClassKeyAttributeRelation.put(mobiClass.getUri(), new HashSet<Attribute>());
				}
				if (!this.allClassKeyAttributeRelation.get(mobiClass.getUri()).contains(attribute)) {
					this.allClassKeyAttributeRelation.get(mobiClass.getUri()).add(attribute);
				}
			}

		} else {
			throw new ExceptionURI(	"The attribute " + attribute.getUri() + " is already bound to the class " + mobiClass.getUri() + ".");
		}
	}
	
	/**
	 * @author Andr� Schmid
	 * Sets a value for a given Attribute that belongs to the given Instance
	 * @param instance		the instance to associate with the attribute and value
	 * @param attribute		The attribute to set the value
	 * @param value			The value to set in the attribute
	 * @throws Exception
	 * 			if the given Instance does not belongs to MOBI;
	 * 			if the given Attribute does not belongs to MOBI;
	 * 			if the given Instance does not belongs to any MOBI Class;
	 * 			if the given Attribute does not belongs to any Instance Classes;
	 * 			if the given Value does not matches with the Attribute Type.
	 */
	public void setAttributeValue(Instance instance, Attribute attribute, String value) throws Exception {
		if(!allInstances.containsKey(instance.getUri())){
			throw new Exception("The instance " + instance.getUri() + " does not belongs to MOBI");
		}
		
		if(!allAttributes.containsKey(attribute.getUri())){
			throw new Exception("The attribute " + attribute.getUri() + " does not belongs to MOBI");
		}
		
		Set<Class> instanceClasses = allInstanceClassRelation.get(instance.getUri());
		if(instanceClasses == null || instanceClasses.size() == 0){
			throw new Exception("The instance " + instance.getUri() + " does not belongs to any MOBI Class");
		}
		boolean instanceClassesHasAttribute = false;
		for (Class class1 : instanceClasses) {
			Set<Attribute> classAttributes = allClassAttributeRelation.get(class1.getUri());
			if(classAttributes != null && classAttributes.contains(attribute)){
				instanceClassesHasAttribute = true;
			}
		}
		if(!instanceClassesHasAttribute){
			throw new Exception("The attribute " + attribute.getUri() + " does not belongs to any instance " + instance.getUri() + " classes");
		}
		if(!instanceAttributeValueHashMap.containsKey(instance.getUri())){
			instanceAttributeValueHashMap.put(instance.getUri(), new HashMap<String, String>());
		}
		
		switch (attribute.getType()) {
		case STRING:
			break;
		case INTEGER:
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new Exception("Incorrect attribute value (" + value.toString() + ") for type Integer");
			}
			break;		
		}
		System.out.println(instance.getUri());
		HashMap<String, String> attributeAndValueMap = instanceAttributeValueHashMap.get(instance.getUri());
		System.out.println(attributeAndValueMap.keySet());
		/*if(attributeAndValueMap.containsKey(attribute.getUri())){
			throw new Exception("There is already an attributed value for the attribute " + attribute.getUri() + " of the instance " + instance.getUri());
		} else {*/
			attributeAndValueMap.put(attribute.getUri(), value);
		/*}*/
	}

	public Set<Instance> getClassInstances(String classURI) {
		return this.getClassInstances((this.getAllClasses().get(classURI)));
	}

	public Set<Instance> getClassInstances(Class mobiClass) {
		return this.allClassInstanceRelation.get(mobiClass.getUri());
	}

	public Set<Class> getInstanceClasses(String instanceURI) {
		return this.getInstanceClasses(this.allInstances.get(instanceURI));
	}

	public Set<Class> getInstanceClasses(Instance instance) {
		return this.allInstanceClassRelation.get(instance.getUri());
	}
	
	private void reloadAllConcepts() {

		for (CompositionRelation cr : allCompositionRelations.values()) {
			cr.setContext(this.context);
		}

		for (SymmetricRelation sr : allSymmetricRelations.values()) {
			sr.setContext(this.context);
		}

		for (InheritanceRelation ir : allInheritanceRelations.values()) {
			ir.setContext(this.context);
		}

		for (EquivalenceRelation er : allEquivalenceRelations.values()) {
			er.setContext(this.context);
		}

	}

	/* ***************************************
	 * ******** GET and SET Block ************
	 * ***************************************
	 */
	public HashMap<String, Instance> getAllInstances() {
		return allInstances;
	}

	public void setAllInstances(HashMap<String, Instance> allInstances) {
		this.allInstances = allInstances;
	}

	public Instance getInstance(String uri) {
		return this.allInstances.get(uri);
	}

	public Instance getInstance(Instance instance) {
		return this.allInstances.get(instance.getUri());
	}

	public void setInstance(Instance instance) {
		this.allInstances.put(instance.getUri(), instance);
	}

	public Context getContext() {
		return this.context;
	}

	public void setContext(Context context) {
		this.context = context;
		this.reloadAllConcepts();
	}

	public void setContext(String contextURI) {
		this.context = new Context(contextURI);
		this.reloadAllConcepts();
	}

	public HashMap<String, Class> getAllClasses() {
		return allClasses;
	}

	public void setAllClasses(HashMap<String, Class> allClasses) {
		this.allClasses = allClasses;
	}

	public Class getClass(String uri) {
		return this.allClasses.get(uri);
	}

	public Class getClass(Class mobiClass) {
		return this.allClasses.get(mobiClass.getUri());
	}

	public void setClass(Class c) {
		this.allClasses.put(c.getUri(), c);
	}

	public HashMap<String, CompositionRelation> getAllCompositionRelations() {
		return allCompositionRelations;
	}

	public void setAllCompositionRelations(HashMap<String, CompositionRelation> allCompositionRelations) {
		this.allCompositionRelations = allCompositionRelations;
	}

	public CompositionRelation getCompositionRelation(String uri) {
		return this.allCompositionRelations.get(uri);
	}

	public CompositionRelation getCompositionRelation(CompositionRelation compositionRelation) {
		return this.allCompositionRelations.get(compositionRelation.getUri());

	}

	public void setCompositionRelation(CompositionRelation compositionRelation) {
		this.allCompositionRelations.put(compositionRelation.getUri(), compositionRelation);
	}

	public void removeInstanceRelation(Relation relation, Instance instanceA,
			Instance instanceB) {
		if ((relation == null) || (instanceA == null) || (instanceB == null))
			return;

		Relation r = null;

		switch (relation.getType()) {

		case Relation.BIDIRECIONAL_COMPOSITION:
		case Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO:
		case Relation.UNIDIRECIONAL_COMPOSITION:
			r = this.allCompositionRelations.get(relation.getUri());
			break;
		case Relation.SYMMETRIC_COMPOSITION:
			r = this.allSymmetricRelations.remove(relation.getUri());
			break;
		case Relation.INHERITANCE:
			r = this.allInheritanceRelations.remove(relation.getUri());
			break;
		case Relation.EQUIVALENCE:
			r = this.allEquivalenceRelations.remove(relation.getUri());
			break;
		default:
			break;
		}

		r.getInstanceRelationMapA().get(instanceA.getUri()).removeInstance(instanceB);
		r.getInstanceRelationMapB().get(instanceB.getUri()).removeInstance(instanceA);

		if (r.getInstanceRelationMapA().get(instanceA.getUri()).getInstanceMapSize() == 0)
			r.getInstanceRelationMapA().remove(instanceA.getUri());
		
		if (r.getInstanceRelationMapB().get(instanceB.getUri()).getInstanceMapSize() == 0)
			r.getInstanceRelationMapB().remove(instanceB.getUri());

	}

	public void removeConcept(Concept concept) {
		if (concept == null)
			return;

		if (concept.getId() == null) {
			this.removeConceptFisicaly(concept);
		} else {
			this.removeConceptLogically(concept);
		}
	}

	private void removeConceptLogically(Concept concept) {
		if (!concept.getClass().equals(GenericRelation.class)) {
			concept.setValid(Boolean.FALSE);
			this.allRemovedConcepts.put(concept.getUri(), concept);
			this.removeConceptFisicaly(concept);
		} else {
			this.allGenericRelations.remove(concept.getUri());
		}
	}

	private void removeConceptFisicaly(Concept concept) {

		if (concept.getClass().equals(Instance.class)) {
			this.allInstances.remove(concept.getUri());
			return;
		}

		if (concept.getClass().equals(Class.class)) {
			this.allClasses.remove(concept.getUri());
			return;
		}

		if (concept.getClass().equals(CompositionRelation.class)) {
			this.allCompositionRelations.remove(concept.getUri());
			return;
		}

		if (concept.getClass().equals(InheritanceRelation.class)) {
			this.allInheritanceRelations.remove(concept.getUri());
			return;
		}

		if (concept.getClass().equals(SymmetricRelation.class)) {
			this.allSymmetricRelations.remove(concept.getUri());
			return;
		}

		if (concept.getClass().equals(EquivalenceRelation.class)) {
			this.allEquivalenceRelations.remove(concept.getUri());
			return;
		}

		if (concept.getClass().equals(GenericRelation.class)) {
			this.allGenericRelations.remove(concept.getUri());
			return;
		}

	}

	public void destroyConcept(Concept concept) {
		if (concept.getId() != null) {
			this.allDestroyedConcepts.put(concept.getUri(), concept);
		}
		this.removeConceptFisicaly(concept);
	}

	public HashMap<String, SymmetricRelation> getAllSymmetricRelations() {
		return allSymmetricRelations;
	}

	public void setAllSymmetricRelations(HashMap<String, SymmetricRelation> allSymmetricRelations) {
		this.allSymmetricRelations = allSymmetricRelations;
	}

	public SymmetricRelation getSymmetricRelation(String uri) {
		return this.allSymmetricRelations.get(uri);
	}

	public SymmetricRelation getSymmetricRelation(SymmetricRelation symmetricRelation) {
		return this.allSymmetricRelations.get(symmetricRelation.getUri());
	}

	public void setCompositionRelation(SymmetricRelation symmetricRelation) {
		this.allSymmetricRelations.put(symmetricRelation.getUri(), symmetricRelation);
	}

	public HashMap<String, InheritanceRelation> getAllInheritanceRelations() {
		return allInheritanceRelations;
	}

	public void setAllInheritanceRelations(HashMap<String, InheritanceRelation> allInheritanceRelations) {
		this.allInheritanceRelations = allInheritanceRelations;
	}

	public InheritanceRelation getInheritanceRelation(String uri) {
		return this.allInheritanceRelations.get(uri);
	}

	public InheritanceRelation getInheritanceRelation(InheritanceRelation inheritanceRelation) {
		return this.allInheritanceRelations.get(inheritanceRelation.getUri());
	}

	public void setInheritanceRelation(InheritanceRelation inheritanceRelation) {
		this.allInheritanceRelations.put(inheritanceRelation.getUri(), inheritanceRelation);
	}

	public HashMap<String, EquivalenceRelation> getAllEquivalenceRelations() {
		return allEquivalenceRelations;
	}

	public void setAllEquivalenceRelations(HashMap<String, EquivalenceRelation> allEquivalenceRelations) {
		this.allEquivalenceRelations = allEquivalenceRelations;
	}

	public EquivalenceRelation getEquivalenceRelation(String uri) {
		return this.allEquivalenceRelations.get(uri);
	}

	public EquivalenceRelation getEquivalenceRelation(EquivalenceRelation equivalenceRelation) {
		return this.allEquivalenceRelations.get(equivalenceRelation.getUri());
	}

	public Boolean isSubClassOf(Class classA, Class classB) {

		for (InheritanceRelation ir : allInheritanceRelations.values()) {
			if ((ir.getClassA().getUri().equals(classA.getUri()))
			   && (ir.getClassB().getUri().equals(classB.getUri()))) {
				return true;
			}
		}

		return false;
	}

	public Boolean isSuperClassOf(Class classA, Class classB) {

		for (InheritanceRelation ir : allInheritanceRelations.values()) {
			if ((ir.getClassA().getUri().equals(classA.getUri()))
					&& (ir.getClassB().getUri().equals(classB.getUri()))) {
				return true;
			}
		}

		return false;
	}

	public Boolean isSuperClass(Class mobiClass) {
		for (InheritanceRelation ir : allInheritanceRelations.values()) {
			if (ir.getClassA().getUri().equals(mobiClass.getUri())) {
				return true;
			}
		}

		return false;
	}

	public Boolean isSubClass(Class mobiClass) {

		for (InheritanceRelation ir : allInheritanceRelations.values()) {
			if (ir.getClassB().getUri().equals(mobiClass.getUri())) {
				return true;
			}
		}

		return false;
	}

	/* Method for return the name object property from object property MOBI */
	public String getPropertyName(String nameMobiObjectProperty, Class classA,	Class classB) {
//		 int quantity = nameMobiObjectProperty.length() -
//		 (classA.getUri().length() + classB.getUri().length() + 2 );
//		 return nameMobiObjectProperty.substring(classA.getUri().length() + 1,
//		 classA.getUri().length() + 1 + quantity);
		return nameMobiObjectProperty;
	}

	/*
	 * Method for return the name of inverse object property from object
	 * property
	 */
	public String getInversePropertyName(String nameObjectProperty) {
		for (CompositionRelation cr : this.allCompositionRelations.values()) {
			if (  (cr.getNameA() != null) 
			   && (this.getPropertyName(cr.getNameA(), cr.getClassA(), cr.getClassB()).equals(nameObjectProperty))
			   && (cr.getNameB() != null))
				return this.getPropertyName(cr.getNameB(), cr.getClassB(), cr.getClassA());

			if (  (cr.getNameB() != null)
			   && (this.getPropertyName(cr.getNameB(), cr.getClassB(), cr.getClassA()).equals(nameObjectProperty)))
				return this.getPropertyName(cr.getNameA(), cr.getClassA(), cr.getClassB());
		}

		return null;
	}

	public HashMap<String, GenericRelation> getAllGenericRelations() {
		return allGenericRelations;
	}

	public void setAllGenericRelations(HashMap<String, GenericRelation> allGenericRelations) {
		this.allGenericRelations = allGenericRelations;
	}

	public HashMap<String, Concept> getAllDestroyedConcepts() {
		return allDestroyedConcepts;
	}

	public void setAllDestroyedConcepts(HashMap<String, Concept> allDestroyedConcepts) {
		this.allDestroyedConcepts = allDestroyedConcepts;
	}

	public HashMap<String, Concept> getAllRemovedConcepts() {
		return allRemovedConcepts;
	}

	public void setAllRemovedConcepts(HashMap<String, Concept> allRemovedConcepts) {
		this.allRemovedConcepts = allRemovedConcepts;
	}

	public HashMap<String, Set<Class>> getAllInstanceClassRelation() {
		return allInstanceClassRelation;
	}

	public void setAllInstanceClassRelation(HashMap<String, Set<Class>> allInstanceClassRelation) {
		this.allInstanceClassRelation = allInstanceClassRelation;
	}

	public HashMap<String, Set<Instance>> getAllClassInstanceRelation() {
		return allClassInstanceRelation;
	}

	public void setAllClassInstanceRelation(HashMap<String, Set<Instance>> allClassInstanceRelation) {
		this.allClassInstanceRelation = allClassInstanceRelation;
	}
	
	public HashMap<String, Tale> getAllTales() {
		return allTales;
	}

	public void setAllTales(HashMap<String, Tale> allTales) {
		this.allTales = allTales;
	}

	public Tale getTale(String uri) {
		return this.allTales.get(uri);
	}

	public Tale getTale(Tale tale) {
		return this.allTales.get(tale.getUri());
	}

	public void setTale(Tale tale) {
		this.allTales.put(tale.getUri(), tale);
	}

	public HashMap<String, Attribute> getAllAttributes() {
		return allAttributes;
	}

	public void setAllAttributes(HashMap<String, Attribute> allAttributes) {
		this.allAttributes = allAttributes;
	}

	public HashMap<String, Set<Attribute>> getAllClassAttributeRelation() {
		return allClassAttributeRelation;
	}

	public HashMap<String, Set<Attribute>> getAllClassKeyAttributeRelation() {
		return allClassKeyAttributeRelation;
	}

	public void setAllClassAttributeRelation(HashMap<String, Set<Attribute>> allClassAttributeRelation) {
		this.allClassAttributeRelation = allClassAttributeRelation;
	}

	public HashMap<String, HashMap<String, String>> getInstanceAttributeValueHashMap() {
		return instanceAttributeValueHashMap;
	}

	public void setInstanceAttributeValueHashMap(
			HashMap<String, HashMap<String, String>> instanceAttributeValueHashMap) {
		this.instanceAttributeValueHashMap = instanceAttributeValueHashMap;
	}
}
