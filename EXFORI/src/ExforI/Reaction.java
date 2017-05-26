/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import javafx.beans.property.*;
import javafx.collections.*;

/**
 * This contains all the information related to reaction / monitor
 *
 * @author vega
 */
public class Reaction {

    libList lList = new libList ();

    ObservableList<String> reactionStr = FXCollections.observableArrayList ();
    private SimpleStringProperty SF1;
    private SimpleStringProperty SF2;
    private SimpleStringProperty SF3;
    private SimpleStringProperty SF4;
    private SimpleStringProperty SF5;
    private SimpleStringProperty SF6;
    private SimpleStringProperty SF7;
    private SimpleStringProperty SF8;
    private SimpleStringProperty SF9;
    private int lineNo;
    private boolean SF4Blank;
    private String chkSF3A = "TOT ABS NON TCC";
    private String chkSF3B = "PAI";
    private String chkSF3C = "SCT EL INL THS";
    private String chkSF3D = " F FUS";
    private String chkSF3E = "ELEM MASS ELEM/MASS";
    String sFinal;
    boolean parenOK = true;
    boolean commaOK = true;
    boolean SF1Wrong = false;
    boolean SF2Wrong = false;
    boolean SF3Wrong = false;
    boolean SF4Wrong = false;
    boolean SF5Wrong = false;
    boolean SF6Wrong = false;
    boolean SF7Wrong = false;
    boolean SF8Wrong = false;
    boolean SF9Wrong = false;

    // constructor for the editing
    public static void Reaction(libList lListIN, int lNo,
            String s1, String s2, String s3, String s4,
            String s5, String s6, String s7, String s8, String s9,
            boolean newExfor) {
        String temp;

        Reaction react = new Reaction ();

        react.resetBoolean ();

        react.lList = lListIN;
        react.lineNo = lNo;
        react.SF1 = new SimpleStringProperty (s1);
        react.SF2 = new SimpleStringProperty (s2);
        react.SF3 = new SimpleStringProperty (s3);
        react.SF4 = new SimpleStringProperty (s4);
        react.SF5 = new SimpleStringProperty (s5);
        react.SF6 = new SimpleStringProperty (s6);
        react.SF7 = new SimpleStringProperty (s7);
        react.SF8 = new SimpleStringProperty (s8);
        react.SF9 = new SimpleStringProperty (s9);
        temp = react.doFormReaction (newExfor); // make reaction grammer;
        System.out.println ("Reaction made in Reaction class->" + temp);
        react.reactionStr.add (temp);
    }

    //constructor for the checker
    public static void Reaction(int lNo, String sIN) {
        Reaction react = new Reaction ();
        react.lineNo = lNo;
        react.parenOK = (sIN.charAt (0) == '(') ? true : false;
        sIN = sIN.substring (1);
        react.SF1 = new SimpleStringProperty (sIN.substring (0, sIN.
                indexOf ("(")));
        sIN = sIN.substring (sIN.indexOf ("(") + 1);
    }

    //GET series
    public String getSF1() {
        return SF1.get ();
    }

    public String getSF2() {
        return SF2.get ();
    }

    public String getSF3() {
        return SF3.get ();
    }

    public String getSF4() {
        return SF4.get ();
    }

    public String getSF5() {
        return SF5.get ();
    }

    public String getSF6() {
        return SF6.get ();
    }

    public String getSF7() {
        return SF7.get ();
    }

    public String getSF8() {
        return SF8.get ();
    }

    public String getSF9() {
        return SF9.get ();
    }

    // SET series
    public void setSF1(String sIN) {
        SF1.set (sIN);
    }

    public void setSF2(String sIN) {
        SF2.set (sIN);
    }

    public void setSF3(String sIN) {
        SF3.set (sIN);
    }

    public void setSF4(String sIN) {
        SF4.set (sIN);
    }

    public void setSF5(String sIN) {
        SF5.set (sIN);
    }

    public void setSF6(String sIN) {
        SF6.set (sIN);
    }

    public void setSF7(String sIN) {
        SF7.set (sIN);
    }

    public void setSF8(String sIN) {
        SF8.set (sIN);
    }

    public void setSF9(String sIN) {
        SF9.set (sIN);
    }

    // DATAPROPERTY
    public StringProperty SF1Property() {
        return SF1;
    }

    public StringProperty SF2Property() {
        return SF2;
    }

    public StringProperty SF3Property() {
        return SF3;
    }

    public StringProperty SF4Property() {
        return SF4;
    }

    public StringProperty SF5Property() {
        return SF5;
    }

    public StringProperty SF6Property() {
        return SF6;
    }

    public StringProperty SF7Property() {
        return SF7;
    }

    public StringProperty SF8Property() {
        return SF8;
    }

    public StringProperty SF9Property() {
        return SF9;
    }

    // construct the full i-th reaction 
    public String doFormReaction(boolean willNotTest) {
        String sReac;

        sReac = "(";
        sReac += SF1.get ();
        sReac += "(";
        if ( !willNotTest ) {
            testSF2 ();   // tests SF2
        }
        sReac += SF2.get () + ",";
        if ( !willNotTest ) {
            testSF3 ();    // tests SF3
        }
        sReac += SF3.get () + ")";

        testSF4 ();  // tests SF4
        sReac += SF4.get () + ",";

        testSF5();
        
        
        return sReac;
    }

    public void testSF2() {
        for ( Object cue2 : lList.d33SF2List ) {
            if ( !cue2.toString ().contains ((CharSequence) SF2.get ()) ) {
                SF2.set ("");
            }
        }
    }

    public void testSF3() {
        for ( Object cue3 : lList.d33SF3List ) {
            if ( !cue3.toString ().contains ((CharSequence) SF3.get ()) ) {
                SF3.set ("");
            }
        }
    }

    public void testSF4() {
        // I am now pending SEQ for SF% issue for heaviest element for SF4
        boolean isReso = false;
        boolean isNuclide = false;
        boolean isVariable = false;

        String SF4Local = SF4.get ();
        for ( Object cue4 : lList.mixedSF4List ) {
            String cue4S = cue4.toString ();
            if ( cue4S.contains (SF4Local) ) {
                if ( cue4S.substring (39, 40).contains (".") ) {
                    isReso = true;
                }
            }
        }
        for ( Object cue4 : lList.targetNList ) {
            String cue4S = cue4.toString ();
            if ( cue4S.contains (SF4Local) ) {
                isNuclide = true;
            }
        }

        if ( chkSF3E.contains (SF4Local) ) {
            isVariable = true;
        }

        if ( isReso ||
                (chkSF3A.contains (SF3.get ())) ) {
            SF4.set ("");
        }

        if ( chkSF3D.contains (SF3.get ()) ) {
            if ( !isNuclide || !isVariable ) {
                SF4.set ("");
            }
        }
        if ( chkSF3C.contains (SF3.get ()) ) {
            SF4.set (SF1.get ());
        }
        if ( chkSF3B.contains (SF3.get ()) ) {
            SF4.set (SF1.get ());
        }
        if ( SF3.get ().contains ("X") ) {
            if (isNuclide||isVariable){
                SF4Wrong=false;
            }else{
                SF4Wrong=true;
            }
        }
    }
    
    private void testSF5(){
        
    }
    
    private void testSF6(){
        
    }

    public void resetBoolean() {
        SF1Wrong = false;
        SF2Wrong = false;
        SF3Wrong = false;
        SF4Wrong = false;
        SF5Wrong = false;
        SF6Wrong = false;
        SF7Wrong = false;
        SF8Wrong = false;
        SF9Wrong = false;
    }
}
