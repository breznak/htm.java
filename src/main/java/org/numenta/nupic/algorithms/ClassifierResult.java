package org.numenta.nupic.algorithms;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Container for the results of a classification computation by the
 * {@link CLAClassifier}
 * 
 * @author David Ray
 *
 * @param <T>
 */
public class ClassifierResult<T> {
	/** Array of actual values */
	private T[] actualValues;
	
	/** Map of step count -to- probabilities */
	TIntObjectMap<double[]> probabilities = new TIntObjectHashMap<double[]>();
	
	
	/**
	 * Returns the actual value for the specified bucket index
	 * 
	 * @param bucketIndex
	 * @return
	 */
	public T getActualValue(int bucketIndex) {
		if(actualValues == null || actualValues.length < bucketIndex + 1) {
			return null;
		}
		return (T)actualValues[bucketIndex];
	}
	
	/**
	 * Returns all actual values entered
	 * 
	 * @return  array of type &lt;T&gt;
	 */
	public T[] getActualValues() {
		return actualValues;
	}
	
	/**
	 * Sets the array of actual values being entered.
	 * 
	 * @param values
	 * @param &lt;T&gt;[]	the value array type
	 */
	public void setActualValues(T[] values) {
		actualValues = values;
	}
	
	/**
	 * Returns a count of actual values entered
	 * @return
	 */
	public int getActualValueCount() {
		return actualValues.length;
	}
	
	/**
	 * Returns the probability at the specified index for the given step
	 * @param step
	 * @param bucketIndex
	 * @return
	 */
	public double getStat(int step, int bucketIndex) {
		return probabilities.get(step)[bucketIndex];
	}
	
	/**
	 * Sets the array of probabilities for the specified step
	 * @param step
	 * @param votes
	 */
	public void setStats(int step, double[] votes) {
		probabilities.put(step, votes);
	}
	
	/**
	 * Returns the probabilities for the specified step
	 * @param step
	 * @return
	 */
	public double[] getStats(int step) {
		return probabilities.get(step);
	}
	
	/**
	 * Returns the input value corresponding with the highest probability
	 * for the specified step.
	 * 
	 * @param step		the step key under which the most probable value will be returned.
	 * @return
	 */
	public T getMostProbableValue(int step) {
		int idx = -1;
		if(probabilities.get(step) == null || (idx = getMostProbableBucketIndex(step)) == -1) {
			return null;
		}
		return getActualValue(idx);
	}
	
	/**
	 * Returns the bucket index corresponding with the highest probability
	 * for the specified step.
	 * 
	 * @param step		the step key under which the most probable index will be returned.
	 * @return			-1 if there is no such entry
	 */
	public int getMostProbableBucketIndex(int step) {
		if(probabilities.get(step) == null) return -1;
		
		double max = 0;
		int bucketIdx = -1;
		int i = 0;
		for(double d : probabilities.get(step)) {
			if(d > max) {
				max = d;
				bucketIdx = i;
			}
			++i;
		}
		return bucketIdx;
	}
	
	/**
	 * Returns the count of steps
	 * @return
	 */
	public int getStepCount() {
		return probabilities.size();
	}
	
	/**
	 * Returns the count of probabilities for the specified step
	 * @param	the step indexing the probability values
	 * @return
	 */
	public int getStatCount(int step) {
		return probabilities.get(step).length;
	}
	
	/**
	 * Returns a set of steps being recorded.
	 * @return
	 */
	public int[] stepSet() {
		return probabilities.keySet().toArray();
	}
}
