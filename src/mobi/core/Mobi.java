package mobi.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mobi.core.common.Concept;
import mobi.core.common.Relation;
import mobi.core.concept.Attribute;
import mobi.core.concept.Class;
import mobi.core.concept.Context;
import mobi.core.concept.Instance;
import mobi.core.concept.Tale;
import mobi.core.factory.RelationFactory;
import mobi.core.manager.ConceptManager;
import mobi.core.manager.InferenceManager;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.GenericRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.SymmetricRelation;
import mobi.exception.ExceptionURI;

public class Mobi implements Serializable {

	private static final long serialVersionUID = 412219996124461277L;

	private final ConceptManager conceptManager      = new ConceptManager();

	private final RelationFactory relationFactory    = new RelationFactory();

	private final InferenceManager inferenceManager  = new InferenceManager();

	public Mobi(final String contextURI) {
		this.conceptManager.setContext(contextURI);
	}

	public Mobi(final Context context) {
		this.conceptManager.setContext(context);
	}

	/* **************************************************
	 * CONCEPT MANAGER
	 * 
	 */

	public void addConcept(final Concept concept) throws Exception {
		if (concept.getUri() != null){
			new ExceptionURI("Invalid URI name. Maybe it is null?");
		}
		this.conceptManager.addConcept(concept);
	}

	public void linkInstances(final String instanceVector, final String classURI) throws Exception {
		this.conceptManager.linkInstances(instanceVector, classURI);
	}

	public void linkInstances(final Set<Instance> instanceSet, final Class mobiClass) throws Exception {
		this.conceptManager.linkInstances(instanceSet, mobiClass);
	}

	public Set<Class> getInstanceClasses(final String instanceURI) {
		return this.conceptManager.getInstanceClasses(instanceURI);
	}

	public Set<Class> getInstanceClasses(final Instance instance) {
		return this.conceptManager.getInstanceClasses(instance);
	}

	public Set<Instance> getClassInstances(final String classURI){
		return this.conceptManager.getClassInstances(classURI);
	}

	public Set<Instance> getClassInstances(final Class mobiClass){
		return this.conceptManager.getClassInstances(mobiClass);
	}

	public void isOneOf(final String instanceURI, final String classeURI) throws ExceptionURI {
		this.conceptManager.isOneOf(instanceURI, classeURI);
	}

	/**
	 * @author Andr� Schmid
	 * Defines the relation between a Class and an Attribute
	 * @param mobiClass		the Class from MOBI to associate with the Attribute
	 * @param attribute		the Attribute from MOBI to be associated
	 * @throws ExceptionURI	If there is already association between the Attribute and the Class
	 */
	public void belongsTo(final Class mobiClass, final Attribute attribute, Boolean isKey) throws ExceptionURI {
		this.conceptManager.belongsTo(mobiClass, attribute, isKey);
	}

	public void isOneOf(final Instance instance, final Class classe) throws ExceptionURI {
		this.conceptManager.isOneOf(instance, classe);
	}

	/*Method of calling for return the name object property from object property MOBI*/
	public String getPropertyName(final String nameMobiObjectProperty, final Class classA, final Class classB)
	{
		return this.conceptManager.getPropertyName(nameMobiObjectProperty, classA, classB);
	}

	/*Method of calling for return the name of inverse object property from object property*/
	public String getInversePropertyName(final String nameObjectProperty)
	{
		return this.conceptManager.getInversePropertyName(nameObjectProperty);
	}

	/* ***************************************
	 * ******** GET and SET Block ************
	 * ***************************************
	 */
	public HashMap<String, Instance> getAllInstances() {
		return this.conceptManager.getAllInstances();
	}

	public void setAllInstances(final HashMap<String, Instance> allInstances) {
		this.conceptManager.setAllInstances(allInstances);
	}

	public Instance getInstance(final String uri) {
		return this.conceptManager.getInstance(uri);
	}

	public Instance getInstance(final Instance instance) {
		return this.conceptManager.getInstance(instance);
	}

	public void setInstance(final Instance instance) {
		this.conceptManager.setInstance(instance);
	}

	public Context getContext() {
		return this.conceptManager.getContext();
	}

	public void setContext(final Context context) {
		this.conceptManager.setContext(context);
	}

	public HashMap<String, Class> getAllClasses() {
		return this.conceptManager.getAllClasses();
	}

	public void setAllClasses(final HashMap<String, Class> allClasses) {
		this.conceptManager.setAllClasses(allClasses);
	}

	public Class getClass(final String uri) {
		return this.conceptManager.getClass(uri);
	}

	public Class getClass(final Class mobiClass) {
		return this.conceptManager.getClass(mobiClass);
	}

	public void setClass(final Class c) {
		this.conceptManager.setClass(c);
	}

	public HashMap<String, Relation> getAllRelations(){
		HashMap<String, Relation> relations = new HashMap<String, Relation>();
		relations.putAll(this.conceptManager.getAllCompositionRelations());
		relations.putAll(this.conceptManager.getAllEquivalenceRelations());
		relations.putAll(this.conceptManager.getAllInheritanceRelations());
		relations.putAll(this.conceptManager.getAllSymmetricRelations());

		return relations;
	}

	public List<Relation> getAllClassRelations(final Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();

		for (Relation relation : this.getAllRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}

	/**
	 * @author Andr� Schmid
	 * @param mobiClass		The Class from MOBI to search its Attributes
	 * @return	A List of Attribute from the given Class
	 */

	public List<Attribute> getAllClassAttributes(final Class mobiClass){
		List<Attribute> attributeList = new ArrayList<Attribute>();
		System.out.println(this.conceptManager.getAllClassAttributeRelation());
		System.out.println(this.conceptManager.getAllClassAttributeRelation().get(mobiClass.getUri()));

		Set<Attribute> attributeSet = this.conceptManager.getAllClassAttributeRelation().get(mobiClass.getUri());
		if(attributeSet != null){
			for (Attribute attribute : attributeSet) {
				attributeList.add(attribute);
			}
		}

		return attributeList;
	}

	public Set<Attribute> getKeyAttribute(String mobiClass) {
		return this.conceptManager.getAllClassKeyAttributeRelation().get(mobiClass);
	}

	/**
	 * @author Andr� Schmid
	 * Calls the setAttributeValue from the ConceptManager
	 * @param instance
	 * @param attribute
	 * @param attributeType
	 * @param value
	 * @throws Exception
	 */
	public void setAttributeValue(final Instance instance, final Attribute attribute, final String value) throws Exception {
		this.conceptManager.setAttributeValue(instance, attribute, value);
	}

	public List<Relation> getAllClassCompositionRelations(final Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();

		for (Relation relation : this.getAllCompositionRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}

	public List<Relation> getAllClassSymmetricRelations(final Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();

		for (Relation relation : this.getAllSymmetricRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}

	public List<Relation> getAllClassEquivalenceRelations(final Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();

		for (Relation relation : this.getAllEquivalenceRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}

	public List<Relation> getAllClassInheritanceRelations(final Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();

		for (Relation relation : this.getAllInheritanceRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}

	public HashMap<String, CompositionRelation> getAllCompositionRelations() {
		return this.conceptManager.getAllCompositionRelations();
	}

	public void setAllCompositionRelations(final HashMap<String, CompositionRelation> allCompositionRelations) {
		this.conceptManager.setAllCompositionRelations(allCompositionRelations);
	}

	public CompositionRelation getCompositionRelation(final String uri) {
		return this.conceptManager.getCompositionRelation(uri);
	}

	public void setCompositionRelation(final CompositionRelation compositionRelation) {
		this.conceptManager.setCompositionRelation(compositionRelation);
	}

	public HashMap<String, SymmetricRelation> getAllSymmetricRelations() {
		return this.conceptManager.getAllSymmetricRelations();
	}

	public void setAllSymmetricRelations(final HashMap<String, SymmetricRelation> allSymmmetricRelations) {
		this.conceptManager.setAllSymmetricRelations(allSymmmetricRelations);
	}

	public SymmetricRelation getSymmetricRelation(final String uri) {
		return this.conceptManager.getSymmetricRelation(uri);
	}

	public SymmetricRelation getSymmetricRelation(final SymmetricRelation symmetricRelation) {
		return this.conceptManager.getSymmetricRelation(symmetricRelation);
	}

	public void setCompositionRelation(final SymmetricRelation symmetricRelation) {
		this.conceptManager.setCompositionRelation(symmetricRelation);
	}

	public HashMap<String, InheritanceRelation> getAllInheritanceRelations() {
		return this.conceptManager.getAllInheritanceRelations();
	}

	public void setAllInheritanceRelations(final HashMap<String, InheritanceRelation> allInheritanceRelations) {
		this.conceptManager.setAllInheritanceRelations(allInheritanceRelations);
	}

	public InheritanceRelation getInheritanceRelation(final String uri) {
		return this.conceptManager.getInheritanceRelation(uri);
	}

	public InheritanceRelation getInheritanceRelation(final InheritanceRelation inheritanceRelation) {
		return this.conceptManager.getInheritanceRelation(inheritanceRelation);
	}

	public void setInheritanceRelation(final InheritanceRelation inheritanceRelation) {
		this.conceptManager.setInheritanceRelation(inheritanceRelation);
	}

	public HashMap<String, EquivalenceRelation> getAllEquivalenceRelations() {
		return this.conceptManager.getAllEquivalenceRelations();
	}

	public void setAllEquivalenceRelations(final HashMap<String, EquivalenceRelation> allEquivalenceRelations) {
		this.conceptManager.setAllEquivalenceRelations(allEquivalenceRelations);
	}

	public EquivalenceRelation getEquivalenceRelation(final String uri) {
		return this.conceptManager.getEquivalenceRelation(uri);
	}

	public EquivalenceRelation getEquivalenceRelation(final EquivalenceRelation equivalenceRelation) {
		return this.conceptManager.getEquivalenceRelation(equivalenceRelation);
	}

	public HashMap<String, GenericRelation> getAllGenericRelations() {
		return this.conceptManager.getAllGenericRelations();
	}



	public void setAllGenericRelations(final HashMap<String, GenericRelation> allGenericRelations) {
		this.conceptManager.setAllGenericRelations(allGenericRelations);
	}


	public HashMap<String, Concept> getAllDestroyedConcepts() {
		return this.conceptManager.getAllDestroyedConcepts();
	}


	public void setAllDestroyedConcepts(final HashMap<String, Concept> allDestroyedConcepts) {
		this.conceptManager.setAllDestroyedConcepts(allDestroyedConcepts);
	}


	public HashMap<String, Concept> getAllRemovedConcepts() {
		return this.conceptManager.getAllRemovedConcepts();
	}


	public void setAllRemovedConcepts(final HashMap<String, Concept> allRemovedConcepts) {
		this.conceptManager.setAllRemovedConcepts(allRemovedConcepts);
	}


	public HashMap<String, Set<Class>> getAllInstanceClassRelation() {
		return this.conceptManager.getAllInstanceClassRelation();
	}


	public void setAllInstanceClassRelation(final HashMap<String, Set<Class>> allInstanceClassRelation) {
		this.conceptManager.setAllInstanceClassRelation(allInstanceClassRelation);
	}


	public HashMap<String, Set<Instance>> getAllClassInstanceRelation() {
		return this.conceptManager.getAllClassInstanceRelation();
	}


	public void setAllClassInstanceRelation(final HashMap<String, Set<Instance>> allClassInstanceRelation) {
		this.conceptManager.setAllClassInstanceRelation(allClassInstanceRelation);
	}


	public HashMap<String, Tale> getAllTales(){
		return this.conceptManager.getAllTales();
	}

	public void setAllTales (final HashMap<String, Tale> allTales){
		this.conceptManager.setAllTales(allTales);
	}

	/* **************************************************
	 * VERIFICATION METHODS
	 * 
	 */
	public Boolean isSubClassOf(final Class classA, final Class classB) {
		return this.conceptManager.isSubClassOf(classB, classA);
	}

	public Boolean isSuperClassOf(final Class classA, final Class classB) {
		return this.conceptManager.isSuperClassOf(classA, classB);
	}

	public Boolean isSuperClass(final Class mobiClass) {
		return this.conceptManager.isSuperClass(mobiClass);
	}

	public Boolean isSubClass(final Class mobiClass) {
		return this.conceptManager.isSubClass(mobiClass);
	}

	/* **************************************************
	 * REMOVE METHODS
	 * 
	 */
	public void removeInstanceRelation(final Relation relation, final Instance instanceA, final Instance instanceB) {
		this.conceptManager.removeInstanceRelation(relation, instanceA, instanceB);
	}

	public void removeConcept(final Concept concept) {
		this.conceptManager.removeConcept(concept);
	}

	public void destroyConcept(final Concept concept) {
		this.conceptManager.destroyConcept(concept);
	}

	/* **************************************************
	 * RELATION FACTORY
	 * 
	 */
	public Relation createGenericRelation(final String name) {
		return this.relationFactory.createGenericRelation(name);
	}

	public Relation createBidirecionalCompositionRelationship(final String nameA, final String nameB) {
		return this.relationFactory.createBidirecionalCompositionRelationship(nameA, nameB);
	}

	public Relation createBidirecionalCompositionHasBelongsToRelationship(final String nameA, final String nameB) {
		return this.relationFactory.createBidirecionalCompositionHasBelongsToRelationship(nameA, nameB);
	}

	public Relation createEquivalenceRelation(final String name) {
		return this.relationFactory.createEquivalenceRelation(name);
	}

	public Relation createInheritanceRelation(final String name) {
		return this.relationFactory.createInheritanceRelation(name);
	}

	public Relation createSymmetricRelation(final String name) {
		return this.relationFactory.createSymmetricRelation(name);
	}

	public Relation createUnidirecionalCompositionRelationship(final String nameA) {
		return this.relationFactory.createUnidirecionalCompositionRelationship(nameA);
	}

	/* **************************************************
	 * RELATION CONVERTION METHODS
	 * 
	 */
	public Relation convertToBidirecionalCompositionRelationship(final Relation relation, final String nameA, final String nameB) throws Exception {
		Relation newRelation = this.relationFactory.convertToBidirecionalCompositionRelationship(relation, nameA, nameB);
		if (this.getCompositionRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
		return newRelation;
	}

	public Relation convertToBidirecionalCompositionHasBelongsToRelationship(final Relation relation, final String nameA, final String nameB) throws Exception {
		Relation newRelation = this.relationFactory.convertToBidirecionalCompositionHasBelongsToRelationship(relation, nameA, nameB);
		if (this.getCompositionRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
		return newRelation;
	}

	public Relation convertToEquivalenceRelation(final Relation relation, final String name) throws Exception {
		Relation newRelation = this.relationFactory.convertToEquivalenceRelation(relation, name);
		if (this.getEquivalenceRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
		return newRelation;
	}

	public Relation convertToInheritanceRelation(final Relation relation, final String name) throws Exception {
		Relation newRelation = this.relationFactory.convertToInheritanceRelation(relation, name);
		if (this.getInheritanceRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
		return newRelation;
	}

	public Relation convertToSymmetricRelation(final Relation relation, final String name) throws Exception {
		Relation newRelation = this.relationFactory.convertToSymmetricRelation(relation, name);
		if (this.getSymmetricRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
		return newRelation;
	}

	public Relation convertToUnidirecionalCompositionRelationship(final Relation relation, final String name) throws Exception {
		Relation newRelation = this.relationFactory.convertToUnidirecionalCompositionRelationship(relation, name);
		if (this.getCompositionRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
		return newRelation;
	}

	/* **************************************************
	 * INFERENCE METHODS
	 * 
	 */
	public Collection<Integer> infereRelation(final Relation relation){
		return this.inferenceManager.infereRelation(relation);
	}

	public String getRelationPossibilitiesString(final Relation relation){
		return this.inferenceManager.getRelationPossibilitiesString(relation);
	}

	public Attribute getAttribute(String uri) {
		return this.conceptManager.getAllAttributes().get(uri);
	}
}
