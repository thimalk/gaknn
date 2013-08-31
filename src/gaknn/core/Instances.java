package gaknn.core;





//import Instance;

/**
 * Instances class represents the data.
 *
 * @author Niroshinie Dayaratne
 */
public class Instances {
    public final static int CAPACITY = 5000;

    /** The dataset's name. */
    protected String m_RelationName;         

    /** The attribute information. */
    protected FastVector m_Attributes;
    protected FastVector m_Instances;
    protected double [][] m_DataSet;
    protected int [] m_ClassIdList;
    private int m_Capacity;

    /** The class attribute's index */
    protected int m_ClassIndex;
    
    protected int m_Lines = 0;
    protected int m_NumAttributes = 0;
    protected int m_NumClases;
    protected String[] m_ClassArray;

    protected int[] WeightVector;

    /** Constructor for instances
     *
     * @param name the name of the data set.
     * @param attInfo the meta data of the attributes.
     */
    public Instances(String name, 
			   FastVector attInfo) {

        m_RelationName = name;
        m_ClassIndex = -1;
        m_Attributes = attInfo;

        for (int i=0; i<attInfo.size(); i++ )
        {
            if (!Attribute(i).IsClassAttribute() && Attribute(i).Selected())
                m_NumAttributes++;		

        }

        if (m_NumAttributes > 0) m_DataSet = new double[CAPACITY][m_NumAttributes];
        m_ClassIdList = new int[CAPACITY];
        m_Capacity = CAPACITY;
    }
    
    /**
     * Constructor creating an empty set of instances. Copies references to the
     * header information from the given set of instances. Sets the capacity of
     * the set of instances to 0 if its negative.
     * 
     * @param dataset the instances from which the header information is to be
     *          taken
     * @param capacity the capacity of the new dataset
     */
    //@author thimal
    public Instances(/* @non_null@ */Instances dataset, int capacity) {
      initialize(dataset, capacity);
    }

    /**
     * initializes with the header information of the given dataset and sets the
     * capacity of the set of instances.
     * 
     * @param dataset the dataset to use as template
     * @param capacity the number of rows to reserve
     */
    //@author thimal
    protected void initialize(Instances dataset, int capacity) {
      if (capacity < 0)
        capacity = 0;

      // Strings only have to be "shallow" copied because
      // they can't be modified.
      m_ClassIndex = dataset.m_ClassIndex;
      m_RelationName = dataset.m_RelationName;
      m_Attributes = dataset.m_Attributes;
      m_Instances = new FastVector(capacity);
    }
    /**
     * Adds one instance to the end of the set. Shallow copies instance before it
     * is added. Increases the size of the dataset if it is not large enough. Does
     * not check if the instance is compatible with the dataset. Note: String or
     * relational values are not transferred.
     * 
     * @param instance the instance to be added
     */
    //thimal
    public void add(/* @non_null@ */Instance instance) {

      Instance newInstance = (Instance) instance.copy();

      newInstance.setDataset(this);
      m_Instances.addElement(newInstance);
    }
    
    /** Sets the weight vector of the attributes
     *
     * @param weights for the attributes.
     */
    public void SetWeightVector(int[] weights){
        this.WeightVector = weights;
    }
	  
    /** Returns the number of attributes in the data set
     *
     * @return the number of attributes.
     */
    public int NumAttributes(){
        return m_NumAttributes;
    }
    
    /** Returns the number of all attributes including the class attributes
     *
     * @return the number of all attributes including the class attributes
     */
    public /*@pure@*/ int NumAllAttributes() {
        return m_Attributes.size();
    }
    
    /** returns the attribute at the given index
     *
     * @param index the index of the attribute.
     * @return the attribute at the given index
     */
    public /*@pure@*/ Attribute Attribute(int index) {
        return (Attribute) m_Attributes.elementAt(index);
    }
	
    /** Removes the attribute at the given index
     *
     * @param index the index of the attribute to be removed
     */
    public void RemoveAttribute(int index){
        m_Attributes.removeElementAt(index);
    }
	
    /** Sets the class properties of the dataset
     *
     */
    public void SetClassProperties() throws InvalidClassIndexException{
        if (m_ClassIndex >= 0){
            Attribute attr = (Attribute) m_Attributes.elementAt(m_ClassIndex);

            m_NumClases = attr.NumValues();
            m_ClassArray = new String[m_NumClases];

            for (int i=0; i<m_NumClases; i++)
                m_ClassArray[i] = attr.Value(i);

            m_Attributes.removeElementAt(m_ClassIndex);
        }
        else
           throw new InvalidClassIndexException("Invalid Class Index : " + m_ClassIndex);
    }
    
    /** Returns the number of classe
     *
     * @return the number of classes
     */
    public int NumberofClasses(){
        return m_NumClases;
    }

    /** Returns the classes of the instances as a class arreay
     * @return String array of classes.
     */
    public String[] ClassArray(){
        return m_ClassArray;
    }
    
    /** Returns the data set
     *
     * @return the data set of the instances as a 2D double array
     */
    public double[][] DataSet(){
        return m_DataSet;
    }
    
    /** Sets the data set of the instances
     *
     * @param dataSet sets as the data set.
     */
    public void DataSet(double[][] dataSet){
        m_DataSet = dataSet;
    }
    
    /** Returns the attributes of the data set
     *
     * @return the attributes of the data set
     */
    public FastVector Attributes(){
        return m_Attributes;
    }

    /** Returns the number of instances in the data set
     *
     * @return the number of recors in the data set
     */
    public int Size(){
        return m_Lines;
    }

    /** Returns the class i list of the data set
     *
     * @return the class id list of the data set
     */
    public int[] ClassIdList(){
        return m_ClassIdList;
    }
    
    /** Adds an element to the given position
     *
     * @param RecNo the position of the instance.
     * @param values as a double array
     * @param classIndex the class index of the instance
     */
    public void AddElement(int RecNo, double[] values, int classIndex){
        if (RecNo >= m_Capacity){
            m_Capacity = m_Capacity + Double.valueOf(CAPACITY*0.5).intValue();
            if (m_NumAttributes > 0)
                m_DataSet = (double[][])ResizeArray(m_DataSet,m_Capacity);

            m_ClassIdList = (int[])ResizeArray(m_ClassIdList,m_Capacity);
        } 

        if (m_NumAttributes > 0) m_DataSet[RecNo] = values;
        m_ClassIdList[RecNo] = classIndex;
        m_Lines++;
    }
    
    
    /** Compacts the data set to the minimum size
     *
     */
    public void Compact() {
        m_DataSet = (double[][])ResizeArray(m_DataSet,m_Lines);
        m_ClassIdList = (int[])ResizeArray(m_ClassIdList,m_Lines);
    }
    
    /** sets the class index of the data set
     *
     * @param classIndex the class index of the data set.
     */
    public void SetClassIndex(int classIndex) {
        if (classIndex >= NumAllAttributes()) {
            throw new IllegalArgumentException("Invalid class index: " + classIndex);
        }
        m_ClassIndex = classIndex;
    }

    /** Returns the class of the data set
     *
     */
    public int GetClassIndex() {
        return m_ClassIndex;
    }
    
    
    private static Object ResizeArray (Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(
                            elementType,newSize);
        int preserveLength = Math.min(oldSize,newSize);
        if (preserveLength > 0)
            System.arraycopy (oldArray,0,newArray,0,preserveLength);
        return newArray; 
    }
    
    /**
     * Returns the instance at the given position.
     * 
     * @param index the instance's index (index starts with 0)
     * @return the instance at the given position
     */
    // @ requires 0 <= index;
    // @ requires index < numInstances();
    // @ author thimal
    public/* @non_null pure@ */Instance instance(int index) {

      return (Instance) m_Instances.elementAt(index);
    }

  
}
