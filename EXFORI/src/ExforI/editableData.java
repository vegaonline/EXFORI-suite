/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import java.io.Serializable;
import javafx.beans.property.*;

/**
 *
 * @author vega
 */
public class editableData implements Serializable {

    private final SimpleStringProperty bibItemName;
    private final SimpleStringProperty pointerID;
    private final SimpleStringProperty contentTxt;
    private final SimpleStringProperty subEntNum;

    public editableData(String bibN, String ptrID, String contTxt, String subNum) {
        this.bibItemName = new SimpleStringProperty (bibN);
        this.pointerID = new SimpleStringProperty (ptrID);
        this.contentTxt = new SimpleStringProperty (contTxt);
        this.subEntNum = new SimpleStringProperty (subNum);
    }

    public String getBibItemName() {
        return bibItemName.get ();
    }

    public void setBibItemName(String bName) {
        bibItemName.set (bName);
    }

    
    public StringProperty bibItemNameProperty() {
        return bibItemName;
    }
    

    public String getPointerID() {
        return pointerID.get ();
    }

    public void setPointrID(String ptr) {
        pointerID.set (ptr);
    }

    
    public StringProperty pointerIDProperty() {
        return pointerID;
    }
    

    public String getContentTxt() {
        return contentTxt.get ();
    }

    public void setContentTxt(String cntnt) {
        contentTxt.set (cntnt);
    }

    
    public StringProperty contentTxtProperty() {
        return contentTxt;
    }
    

    public String getSubEntNum() {
        return subEntNum.get ();
    }

    public void setSubEntNum(int k) {
        this.subEntNum.set (Integer.toString (k));
    }

    public StringProperty subEntNumProperty() {
        return subEntNum;
    }

}
