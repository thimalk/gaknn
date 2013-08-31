
package gaknn.core;





/**
 * Reprsents an instance of the data set
 * 
 * @author Niroshinie Dayaratne
 */
public class Instance
implements Copyable {
    
    protected  Instances m_Dataset;

    /** The instance's attribute values. */
    protected double[] m_AttValues;
    protected String m_Class;
    protected int m_ClassIndex;
    
    protected static final double DEFAULT_NUMVALUE = Double.NaN;

     /** Constructor for an instance
     *
     * @param instance constructs an instance similar to this
     */
    public Instance( Instance instance) {
	    
        m_AttValues = instance.m_AttValues;
        m_Dataset = null;
    }
    
    /** Constructor for an instance
     *
     * @param attValues attribute values of the instance.
     */
    public Instance(double[]attValues ){

        m_AttValues = attValues;
        m_Dataset = null;
    }
    
    /** Constructor for an instance
     *
     * @param numAttributes the number of attributes of the instance
     */
    public Instance(int numAttributes)
    {
        m_AttValues = new double[numAttributes];

        for (int i = 0; i < numAttributes; i++) {
            m_AttValues[i] = DEFAULT_NUMVALUE;
        }
        
        m_Dataset = null;
    }
    
    /** Returns the dataset instance belongs to
     *
     * @return data set of the instance.
     */
    public Instances dataset() {
        return m_Dataset;
    }
	
    /** Adds an element to the instance.
     *
     * @param value of the attribute.
     * @param index of the attribute.
     */
    public void AddElement(double value, int index){
        m_AttValues[index] = value;
    }
    /** Return number of values in instance.
    *
    * 
    */
    //@author thimal
    public int numValues(){
    	return m_AttValues.length;
    }
    /**
     * Returns the index of the attribute stored at the given position.
     * Just returns the given value.
     *
     * @param position the position 
     * @return the index of the attribute stored at the given position
     */
    //@author thimal
    public /*@pure@*/ int index(int position) {

      return position;
    }
    /**
     * Returns an instance's attribute value in internal format.
     * Does exactly the same thing as value() if applied to an Instance.
     *
     * @param indexOfIndex the index of the attribute's index
     * @return the specified value as a double (If the corresponding
     * attribute is nominal (or a string) then it returns the value's index as a 
     * double).
     */
    //@author thimal
    public /*@pure@*/ double valueSparse(int indexOfIndex) {

      return m_AttValues[indexOfIndex];
    } 
    /**
     * Tests if a specific value is "missing".
     *
     * @param attIndex the attribute's index
     * @return true if the value is "missing"
     */
    //@author thimal
    public /*@pure@*/ boolean isMissing(int attIndex) {

      if (Double.isNaN(m_AttValues[attIndex])) {
        return true;
      }
      return false;
    }
    /**
     * Tests if the given value codes "missing".
     *
     * @param val the value to be tested
     * @return true if val codes "missing"
     */
    //@author thimal
    public static /*@pure@*/ boolean isMissingValue(double val) {

        return Double.isNaN(val);
      }
    /** Returns the element at the given position
     *
     * @param index of the elrment.
     */
    /**
     * Returns the values of each attribute as an array of doubles.
     *
     * @return an array containing all the instance attribute values
     */
    //@author thimal
    public double[] toDoubleArray() {

      double[] newValues = new double[m_AttValues.length];
      System.arraycopy(m_AttValues, 0, newValues, 0, 
  		     m_AttValues.length);
      return newValues;
    }
    /**
     * Clones the attribute vector of the instance and
     * overwrites it with the clone.
     */
    //@author thimal
    private void freshAttributeVector() {

        m_AttValues = toDoubleArray();
      }
    /**
     * Sets a specific value in the instance to the given value 
     * (internal floating-point format). Performs a deep copy
     * of the vector of attribute values before the value is set.
     *
     * @param attIndex the attribute's index 
     * @param value the new attribute value (If the corresponding
     * attribute is nominal (or a string) then this is the new value's
     * index as a double).  
     */
    //@author thimal
    public void setValue(int attIndex, double value) {
      
      freshAttributeVector();
      m_AttValues[attIndex] = value;
    }
    /**
     * Sets the reference to the dataset. Does not check if the instance
     * is compatible with the dataset. Note: the dataset does not know
     * about this instance. If the structure of the dataset's header
     * gets changed, this instance will not be adjusted automatically.
     *
     * @param instances the reference to the dataset 
     */
    //@author thimal
    public final void setDataset(Instances instances) {
      
      m_Dataset = instances;
    }
    /**
     * Produces a shallow copy of this instance. The copy has
     * access to the same dataset. (if you want to make a copy
     * that doesn't have access to the dataset, use 
     * <code>new Instance(instance)</code>
     *
     * @return the shallow copy
     */
    //@ also ensures \result != null;
    //@ also ensures \result instanceof Instance;
    //@ also ensures ((Instance)\result).m_Dataset == m_Dataset;
    public /*@pure@*/ Object copy() {

      Instance result = new Instance(this);
      result.m_Dataset = m_Dataset;
      return result;
    }

    public double GetElementAt(int index){
        return m_AttValues[index];
    }
    
    /** returns the elements of the instance
     *
     * @return attribute values of the instance.
     */
    public double[] GetElements(){
        return m_AttValues;
    }

    /** Sets the class value of the instance
     *
     * @param cls value of the instance.
     */
    public void SetClassVlaue(String cls){
        m_Class = cls;
    }

    /** Returns the class value of the instance
     *
     * @return the class value of the instace.
     */
    public String GetClassValue(){
        return m_Class;
    }

    /** Sets the class index of an instance
     *
     * @param index the index of the instance.
     */
    public void SetClassIndex(int index){
        m_ClassIndex = index;
    }

    /** Returns the class index of the instance
     *
     * @return the class index of the instance
     */
    public int GetClassIndex(){
        return m_ClassIndex;
    }
    
    /** Makes a shallow copy of the instance.
     */
    public /*@pure@*/ Object Copy() {
        Instance result = new Instance(this);
        result.m_Dataset = m_Dataset;
        return result;
    }

}
