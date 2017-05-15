/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

/**
 *
 * @author vega
 * @param <T> where T can be any operator
 */
public interface Comparator<T> {
    public int compare(T o1, T o2);    
}
