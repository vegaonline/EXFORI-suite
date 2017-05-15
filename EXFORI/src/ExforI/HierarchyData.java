/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javafx.collections.ObservableList;

/**
 *
 * @author vega
 */

/**
 * this is used to mark an object as HierarchyData and can be used as a data 
 * source
 */ 
        
public interface HierarchyData<T extends HierarchyData> {    
    /**
     * children collected recursively
     * @return A list of children
     */
    ObservableList<T> getChildren();
}
    
