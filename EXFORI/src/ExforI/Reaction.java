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
    String sFinal;
    boolean parenOK = true;
    boolean commaOK = true;

    // constructor for the editing
    public static void Reaction(int lNo,
            String s1, String s2, String s3, String s4,
            String s5, String s6, String s7, String s8, String s9) {
        String temp;

        Reaction react = new Reaction ();

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
        temp = react.doFormReaction (); // make reaction grammer;
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
    public String doFormReaction() {
        String sReac;

        sReac = "(";
        sReac += SF1;
        sReac += "(";
        testSF2 (); // tests SF2
        sReac += SF2;
        testSF3 ();    // tests SF3
        sReac += SF3;

        return sReac;
    }

    public void testSF2() {
        for ( Object cue : lList.d33SF2List ) {
            if ( cue.toString ().contains ((CharSequence) SF2) ) {
                System.out.println ("matching---->" + cue + "<---->" + SF2);
            }
        }
    }

    public void testSF3() {

    }
}
