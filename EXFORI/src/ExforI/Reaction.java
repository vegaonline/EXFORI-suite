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
    public int lineNo;
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
    boolean reacWrong = false;

    // constructor for the editing
    public static String Reaction(libList lListIN, int lNo,
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
        react.reactionStr.add (temp);
        return temp;
    }

    //constructor for the checker
    public static Reaction Reaction(int lNo, String sIN) {
        Reaction react = new Reaction ();
        react.lineNo = lNo;
        react.parenOK = (sIN.charAt (0) == '(') ? true : false;
        // sIN = sIN.substring (1);
        react.decomposeString (sIN);

        return react;
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
            SF2Wrong = testSF2 ("");   // tests SF2
        }
        sReac += SF2.get () + ",";
        if ( !willNotTest ) {
            testSF3 ("");    // tests SF3
        }
        sReac += SF3.get () + ")";

        testSF4 ("");  // tests SF4
        sReac += SF4.get () + ",";

        if ( !SF5.get ().isEmpty () ) {
            testSF5 ("");
            sReac += SF5.get () + ",";
        }

        testSF6 ();
        sReac += SF6.get ();

        if ( !SF7.get ().isEmpty () ) {
            testSF7 ();
            sReac += "," + SF7.get ();
        }

        if ( !SF8.get ().isEmpty () ) {
            testSF8 ();
            sReac += "," + SF8.get ();
        }

        if ( !SF9.get ().isEmpty () ) {
            testSF9 ();
            sReac += "," + SF9.get ();
        }
        sReac += ")";

        reacWrong
                = (!parenOK || !commaOK || !SF1Wrong || !SF2Wrong || !SF3Wrong ||
                !SF4Wrong || !SF5Wrong || !SF6Wrong || !SF7Wrong || !SF8Wrong ||
                !SF9Wrong) ? true : false;

        sReac = checkblankComma (sReac);

        return sReac;
    }

    public boolean testSF2(String sIN) {
        boolean wrong = true;
        String chkIT = (sIN.isEmpty ()) ? SF2.get () : sIN;
        for ( Object cue2 : lList.d33SF2List ) {
            if ( cue2.toString ().contains (chkIT) ) {
                wrong = false;
            }
        }
        if ( wrong ) {
            SF2.set ("");
        }
        return wrong;
    }

    public boolean testSF3(String sIN) {
        boolean Wrong = true;
        String chkIT = (sIN.isEmpty ()) ? SF3.get () : sIN;
        for ( Object cue3 : lList.d33SF3List ) {
            if ( cue3.toString ().contains (chkIT) ) {
                Wrong = false;
            }
        }
        if ( Wrong ) {
            SF3.set ("");
        }
        return Wrong;
    }

    public boolean testSF4(String sIN) {
        // I am now pending SEQ for SF% issue for heaviest element for SF4
        boolean isReso = false;
        boolean isNuclide = false;
        boolean isVariable = false;
        boolean wrong = true;

        String SF4Local = (sIN.isEmpty ()) ? SF4.get () : sIN;
        if ( sIN.contains ("RESO") ) {
            wrong = true;
            return wrong;
        }
        for ( Object cue4 : lList.mixedSF4List ) {
            String cue4S = cue4.toString ();
            if ( cue4S.contains (SF4Local) ) {
                if ( cue4S.length () > 41 && cue4S.substring (39, 40).contains (
                        ".") ) {
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
            if ( isNuclide || isVariable ) {
                SF4Wrong = false;
            } else {
                SF4Wrong = true;
            }
        }
        return wrong;
    }

    public void testSF5(String sIN) {

    }

    public void testSF6() {
        if ( SF6.get ().isEmpty () ) {
            SF6Wrong = true;
        }
    }

    public void testSF7() {
        boolean isPartSF7 = false;
        for ( Object cue4 : lList.d33SF7List ) {
            if ( cue4.toString ().contains (SF4.get ()) ) {
                isPartSF7 = true;
            }
        }
        // confused about RSD. if SF7 tag for particle then compulsorily RSD?
    }

    public void testSF8() {

    }

    public void testSF9() {

    }

    public String checkblankComma(String sOut) {
        String temp = "";
        String key = SF6.get ();
        int nKey = sOut.indexOf (key);
        temp = sOut.substring (0, nKey);// +","+sOut.substring (nKey);
        temp += ",";
        temp += sOut.substring (nKey);
        return temp;
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

    // decompose a string into reaction components
    public void decomposeString(String sIN) {
        String tmp;
        if ( sIN.isEmpty () ) {
            return;
        }
        sIN = sIN.substring (1);
        tmp = sIN.substring (0, sIN.indexOf ("("));        // (
        SF1 = new SimpleStringProperty (tmp);         // (SF1

        sIN = sIN.substring (sIN.indexOf ("(") + 1);       // (SF1(
        tmp = sIN.substring (0, sIN.indexOf (","));
        SF2Wrong = testSF2 (tmp);
        if ( !SF2Wrong ) {
            SF2 = new SimpleStringProperty (tmp);        // (SF1(SF2
        } else {
            SF2 = new SimpleStringProperty ("");        // (SF1(SF2
        }

        sIN = sIN.substring (sIN.indexOf (",") + 1);       // (SF1(SF2, 
        tmp = sIN.substring (0, sIN.indexOf (")"));
        SF3Wrong = testSF3 (tmp);
        if ( !SF3Wrong ) {
            if ( tmp.contains ("0-G-") ) {
                tmp.replace ("0-G-0", "G");
            }
            SF3 = new SimpleStringProperty (tmp);        // (SF1(SF2, SF3
        } else {
            SF3 = new SimpleStringProperty ("");        // (SF1(SF2, SF3
        }

        sIN = sIN.substring (sIN.indexOf (")") + 1);       // (SF1(SF2, SF3)
        tmp = sIN.substring (0, sIN.indexOf (","));
        tmp = (tmp.isEmpty ()) ? "RESO" : tmp;
        SF4Wrong = testSF4 (tmp);
        if ( !SF4Wrong ) {
            SF4 = new SimpleStringProperty (tmp);         // (SF1(SF2, SF3)SF4
        } else {
            SF4 = new SimpleStringProperty ("");         // (SF1(SF2, SF3)SF4
        }

        sIN = sIN.substring (sIN.indexOf (",") + 1);        // // (SF1(SF2, SF3)SF4,
        int nComma = 0;
        for (int i2  =0; i2 < sIN.length (); i2++){
            if (sIN.charAt (i2)==','){
                ++nComma;
            }
        }
        
        
        tmp = sIN.substring (0, sIN.indexOf (","));
        SF5 = new SimpleStringProperty (tmp);

        sIN = sIN.substring (sIN.indexOf (",") + 1);
        tmp = sIN.substring (0, sIN.indexOf (","));
        SF6 = new SimpleStringProperty (tmp);

        sIN = sIN.substring (sIN.indexOf (",") + 1);
        tmp = sIN.substring (0, sIN.indexOf (","));
        SF7 = new SimpleStringProperty (tmp);

        sIN = sIN.substring (sIN.indexOf (",") + 1);
        tmp = sIN.substring (0, sIN.indexOf (","));
        SF8 = new SimpleStringProperty (tmp);

        sIN = sIN.substring (sIN.indexOf (",") + 1);
        tmp = sIN.substring (0, sIN.indexOf (")"));
        SF9 = new SimpleStringProperty (tmp);
    }

}
