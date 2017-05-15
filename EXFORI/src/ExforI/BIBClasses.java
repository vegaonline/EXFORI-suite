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
public class BIBClasses {  // implements Comparable<BIBClasses> {

    private final SimpleStringProperty lineNum;
    private final SimpleStringProperty subEntNum;
    private final SimpleStringProperty entryNum;
    private final SimpleStringProperty strItem;
    //private final ObservableList<String> strItem = FXCollections.
    //        observableArrayList ();
    private final ObservableList children = FXCollections.observableArrayList ();

    public BIBClasses(String linenum, String entry, String subEntNum,
            String str1) {
        this.lineNum = new SimpleStringProperty (linenum);
        this.entryNum = new SimpleStringProperty (entry);
        this.subEntNum = new SimpleStringProperty (subEntNum);
        this.strItem = new SimpleStringProperty (str1);
    }

    public void setStrItem(String s1) {
        this.strItem.set (s1);
    }

    public String getStrItem() {
        return this.strItem.get ();
    }

    public String getLineNum() {
        return (this.lineNum.get ());
    }

    public String getEntryNum() {
        return this.entryNum.get ();
    }

    public String getSubEntNum() {
        String finalStr = "";
        finalStr = this.subEntNum.getValue ();
        return finalStr;
    }

    public void setSubEntNum(String entryNum) {
        String finalStr = "";
        String mid1 = "   ";
        String mid2 = "";
        String last = subEntNum.get ();
        if ( last.length () < 2 ) {
            mid2 = "00";
        }
        if ( last.length () == 2 ) {
            mid2 = "0";
        }
        finalStr = "SUBENT" + mid1 + entryNum + mid2 + last;
        this.subEntNum.set (finalStr);
    }

    public ObservableList getChildren() {
        return children;
    }
}
