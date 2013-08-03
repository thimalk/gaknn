
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
    
    /** Returns the element at the given position
     *
     * @param index of the elrment.
     */
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
