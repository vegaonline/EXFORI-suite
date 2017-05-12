/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;

/**
 *
 * @author vega
 */

public class SUBENTClasses implements HierarchyData{
    private final SimpleStringProperty subEntName;    
    
    SUBENTClasses (String subEntName){
        this.subEntName = new SimpleStringProperty(subEntName);
    }
    
    private String getName() {
        return subEntName.get ();
    }
    
    private void setName(String subEntryName) {
        subEntName.set (subEntryName);
    }
    
    private ObservableList<SUBENTClasses> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<SUBENTClasses> getChildren() {
        return children;        
    }    
}
