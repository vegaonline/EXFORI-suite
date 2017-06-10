/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import java.io.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javax.swing.JFileChooser;

/**
 * This contains all the information related to reaction / monitor
 *
 * @author vega
 */
public class Reaction {

    //  libList lList = new libList ();
    JFileChooser fileChooser = new JFileChooser();
    File recordsDir;
    BufferedWriter brW;

    ObservableList<String> reactionStr = FXCollections.observableArrayList();

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
    public int atNumReactants = 0;
    public int atNumProducts = 0;
    public int massNumReactants = 0;
    public int massNumProducts = 0;

    private String chkSF3A = "TOT ABS NON TCC";
    private String chkSF3B = "PAI";
    private String chkSF3C = "SCT EL INL THS";
    private String chkSF3D = " F FUS";
    private String chkSF3E = "ELEM MASS ELEM/MASS";
    private String sFinal;

    boolean SF4Blank;
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
    boolean chkReacBalance = false;

    // constructor for the editing
    public static String Reaction(libList lList, int lNo,
            String s1, String s2, String s3, String s4,
            String s5, String s6, String s7, String s8, String s9,
            boolean newExfor) {
        String temp;

        Reaction react = new Reaction();

        react.resetBoolean();

        //react.lList = lListIN;
        react.lineNo = lNo;
        react.SF1 = new SimpleStringProperty(s1);
        react.SF2 = new SimpleStringProperty(s2);
        react.SF3 = new SimpleStringProperty(s3);
        react.SF4 = new SimpleStringProperty(s4);
        react.SF5 = new SimpleStringProperty(s5);
        react.SF6 = new SimpleStringProperty(s6);
        react.SF7 = new SimpleStringProperty(s7);
        react.SF8 = new SimpleStringProperty(s8);
        react.SF9 = new SimpleStringProperty(s9);
        temp = react.doFormReaction(lList, newExfor); // make reaction grammer;
        react.reactionStr.add(temp);

        react.getReacParams(lList);    // check reaction sum

        return temp;
    }

    //constructor for the checker
    public static Reaction Reaction(libList lList, int lNo, String sIN,
            BufferedWriter brLog)
            throws IOException {
        Reaction react = new Reaction();
        react.lineNo = lNo;
        react.parenOK = (sIN.charAt(0) == '(') ? true : false;
        // sIN = sIN.substring (1);
        react.decomposeString(lList, sIN, brLog);

        react.getReacParams(lList);   // check reaction sum

        return react;
    }

    //GET series
    public String getSF1() {
        return SF1.get();
    }

    public String getSF2() {
        return SF2.get();
    }

    public String getSF3() {
        return SF3.get();
    }

    public String getSF4() {
        return SF4.get();
    }

    public String getSF5() {
        return SF5.get();
    }

    public String getSF6() {
        return SF6.get();
    }

    public String getSF7() {
        return SF7.get();
    }

    public String getSF8() {
        return SF8.get();
    }

    public String getSF9() {
        return SF9.get();
    }

    // SET series
    public void setSF1(String sIN) {
        SF1.set(sIN);
    }

    public void setSF2(String sIN) {
        SF2.set(sIN);
    }

    public void setSF3(String sIN) {
        SF3.set(sIN);
    }

    public void setSF4(String sIN) {
        SF4.set(sIN);
    }

    public void setSF5(String sIN) {
        SF5.set(sIN);
    }

    public void setSF6(String sIN) {
        SF6.set(sIN);
    }

    public void setSF7(String sIN) {
        SF7.set(sIN);
    }

    public void setSF8(String sIN) {
        SF8.set(sIN);
    }

    public void setSF9(String sIN) {
        SF9.set(sIN);
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
    public String doFormReaction(libList lList, boolean willNotTest) {
        String sReac;

        sReac = "(";
        SF1Wrong = testSF1(lList, "");
        sReac += SF1.get();
        sReac += "(";
        if (!willNotTest) {
            SF2Wrong = testSF2(lList, "");   // tests SF2
            if (SF2Wrong) {
                SF2.set("");
            }
        }
        sReac += SF2.get() + ",";
        if (!willNotTest) {
            SF3Wrong = testSF3(lList, "");    // tests SF3
            if (SF3Wrong) {
                SF3.set("");
            }
        }
        sReac += SF3.get() + ")";

        SF4Wrong = testSF4(lList, "");  // tests SF4
        if (SF4Wrong) {
            SF4.set("");
        }
        sReac += SF4.get() + ",";

        if (!SF5.get().isEmpty()) {
            SF5Wrong = testSF5(lList, "");
            if (SF5Wrong) {
                SF5.set("");
            }
            sReac += SF5.get() + ",";
        }

        SF6Wrong = testSF6(lList, "");
        if (SF6Wrong) {
            SF6.set("");
        }
        sReac += SF6.get();

        if (!SF7.get().isEmpty()) {
            SF7Wrong = testSF7(lList, "");
            if (SF7Wrong) {
                SF7.set("");
            }
            sReac += "," + SF7.get();
        }

        if (!SF8.get().isEmpty()) {
            SF8Wrong = testSF8(lList, "");
            if (SF8Wrong) {
                SF8.set("");
            }
            sReac += "," + SF8.get();
        }

        if (!SF9.get().isEmpty()) {
            SF9Wrong = testSF9(lList, "");
            if (SF9Wrong) {
                SF9.set("");
            }
            sReac += "," + SF9.get();
        }
        sReac += ")";

        reacWrong
                = (!parenOK || !commaOK || SF1Wrong || SF2Wrong || SF3Wrong
                || SF4Wrong || SF6Wrong) ? true : false;

        sReac = checkblankComma(sReac);

        return sReac;
    }

    public boolean testSF1(libList lList, String sIN) {
        boolean wrong = true;
        // getReacParams (sIN);
        return wrong;
    }

    public boolean testSF2(libList lList, String sIN) {
        boolean isWrong = true;
        SF2Wrong = true;
        String chkIT = (sIN.isEmpty()) ? SF2.get() : sIN;
        for (Object cue2 : lList.mixedSF2List) {
            String tmp = (chkIT.length() < 7) ? cue2.toString().substring(0,
                    6).trim() : cue2.toString().substring(0, 12).trim();
            if (tmp.contains(chkIT) && (tmp.length() == chkIT.length())) {
                isWrong = false;
                break;
            }
        }
        return isWrong;
    }

    public boolean testSF3(libList lList, String sIN) {
        boolean isWrong = true;
        SF3Wrong = true;
        String chkIT = (sIN.isEmpty()) ? SF3.get() : sIN;
        for (Object cue3 : lList.mixedSF3List) {
            String tmp = (chkIT.length() < 7) ? cue3.toString().substring(0,
                    6).trim() : cue3.toString().substring(0, 12).trim();
            if (tmp.contains(chkIT) && (tmp.length() == chkIT.length())) {
                isWrong = false;
                break;
            }
        }
        return isWrong;
    }

    public boolean testSF4(libList lList, String sIN) {
        // I am now pending SEQ for SF% issue for heaviest element for SF4
        boolean isReso = false;
        boolean isNuclide = false;
        boolean isVariable = false;
        boolean wrong = true;
        SF4Wrong = true;

        String SF4Local = (sIN.isEmpty()) ? SF4.get() : sIN;
        if (sIN.contains("RESO")) {
            wrong = false;
            return wrong;
        }

        for (Object cue4 : lList.mixedSF4List) {
            String cue4S = cue4.toString();
            if (cue4S.contains(SF4Local)) {      //&& (cue4S.length () == SF4Local.length ()) ) {
                wrong = false;
                break;
            }
        }

        for (Object cue4 : lList.mixedSF4List) {
            String cue4S = cue4.toString();
            if (cue4S.contains(SF4Local)) {
                if (cue4S.length() > 41 && cue4S.substring(39, 40).contains(
                        ".")) {
                    isReso = true;
                    break;
                }
            }
        }
        for (Object cue4 : lList.targetNList) {
            String tmp = (SF4Local.length() < 13) ? cue4.toString().
                    substring(0, 12).trim() : cue4.toString();
            if (tmp.contains(SF4Local)
                    && (tmp.length() == SF4Local.length())) {
                isNuclide = true;
                break;
            }
        }

        if (chkSF3E.contains(SF4Local)) {
            isVariable = true;
        }

        if ((isReso || (chkSF3A.contains(SF3.get())))
                && sIN.isEmpty() && wrong) {
            SF4.set("");
        }

        if (chkSF3D.contains(SF3.get()) && sIN.isEmpty() && wrong) {
            if (!isNuclide || !isVariable) {
                SF4.set("");
            }
        }

        if (wrong && chkSF3C.contains(SF3.get()) && SF4.get().contains(SF1.
                get())) {
            wrong = false;
        }
        if (wrong && chkSF3B.contains(SF3.get()) && SF4.get().contains(SF1.
                get())) {
            wrong = false;
        }

        if (wrong && chkSF3C.contains(SF3.get()) && sIN.isEmpty()) {
            SF4.set(SF1.get());
            wrong = false;
        }
        if (wrong && chkSF3B.contains(SF3.get()) && sIN.isEmpty()) {
            SF4.set(SF1.get());
            wrong = false;
        }

        if (SF3.get().contains("X")) {
            if (isNuclide || isVariable) {
                wrong = false;
            } else {
                wrong = true;
            }
        }
        return wrong;
    }

    public boolean testSF5(libList lList, String sIN) {
        boolean wrong = true;
        SF5Wrong = true;

        return wrong;
    }

    public boolean testSF6(libList lList, String sIN) {
        boolean wrong = true;
        SF6Wrong = true;

        String chkIT = (sIN.isEmpty()) ? SF6.get() : sIN;

        for (Object cue6 : lList.paramSF6List) {
            String tmp = cue6.toString().substring(0, 4).trim();
            if (tmp.contains(chkIT)) {
                wrong = false;
                break;
            }
        }

        return wrong;
    }

    public boolean testSF7(libList lList, String sIN) {
        boolean wrong = true;
        SF7Wrong = true;
        String[] sArr = {};

        if (sIN.contains("/")) {
            sArr = sIN.split("/");
            System.out.println(sArr.length + " 0->" + sArr[0] + " 1->"
                    + sArr[1]);
        }
        if (sArr.length == 0) {
            String chkIT = (sIN.isEmpty()) ? SF7.get() : sIN;
            for (Object cue4 : lList.d33SF7List) {
                if (cue4.toString().contains(chkIT)) {
                    wrong = false;
                }
            }
        } else {
            for (int ii = 0; ii < sArr.length; ii++) {
                String chkIT = (sIN.isEmpty()) ? SF7.get() : sIN;
                for (Object cue4 : lList.d33SF7List) {
                    if (cue4.toString().contains(chkIT)) {
                        wrong = false;
                    }
                }
            }
        }
        // confused about RSD. if SF7 tag for particle then compulsorily RSD?
        return wrong;
    }

    public boolean testSF8(libList lList, String sIN) {
        boolean wrong = true;
        SF8Wrong = true;
        String chkIT = (sIN.isEmpty()) ? SF8.get() : sIN;
        for (Object cue8 : lList.modifierList) {
            if (cue8.toString().contains(chkIT)) {
                wrong = false;
                break;
            }
        }
        return wrong;
    }

    public boolean testSF9(libList lList, String sIN) {
        boolean wrong = true;
        boolean wrongOld = true;

        SF9Wrong = true;
        String chkIT = (sIN.isEmpty()) ? SF9.get() : sIN;
        String[] strArr = {""};
        if (chkIT.contains("/")) {
            strArr = chkIT.split("/");
        }
        int llen = strArr.length;

        if (llen > 0) {
            for (int i1 = 0; i1 < llen; i1++) {
                String str1 = strArr[i1];
                for (Object cue9 : lList.dataTypeList) {
                    if (cue9.toString().contains(str1)) {
                        wrong = false;
                        //return itIs;
                    }
                }
                if (i1 > 0 && (wrongOld != wrong)) {

                }
                wrongOld = wrong;
            }
        } else {
            for (Object cue9 : lList.dataTypeList) {
                if (cue9.toString().contains(chkIT)) {
                    wrong = false;
                    break;
                }
            }
        }
        return wrong;
    }

    public String checkblankComma(String sOut) {
        String temp = "";
        String key = SF6.get();
        int nKey = sOut.indexOf(key);
        temp = sOut.substring(0, nKey);// +","+sOut.substring (nKey);
        temp += ",";
        temp += sOut.substring(nKey);
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
    public void decomposeString(libList lList, String sIN, BufferedWriter brLog)
            throws
            IOException {
        String tmp;
        if (sIN.isEmpty()) {
            brLog.append("******  Reaction transferred to checker is NULL \n");
            return;
        }
        sIN = sIN.substring(1);
        tmp = sIN.substring(0, sIN.indexOf("("));        // (
        SF1Wrong = testSF1(lList, tmp);
        SF1 = new SimpleStringProperty(tmp);         // (SF1

        sIN = sIN.substring(sIN.indexOf("(") + 1);       // (SF1(
        tmp = sIN.substring(0, sIN.indexOf(","));
        SF2Wrong = testSF2(lList, tmp);
        if (!SF2Wrong) {
            SF2 = new SimpleStringProperty(tmp);        // (SF1(SF2
        } else {
            brLog.append(" Check SF2 " + tmp + "\n");
            SF2 = new SimpleStringProperty("");        // (SF1(SF2
        }

        sIN = sIN.substring(sIN.indexOf(",") + 1);       // (SF1(SF2, 
        tmp = sIN.substring(0, sIN.indexOf(")"));
        SF3Wrong = testSF3(lList, tmp);
        if (!SF3Wrong) {
            if (tmp.contains("0-G-")) {
                tmp.replace("0-G-0", "G");
            }
            SF3 = new SimpleStringProperty(tmp);        // (SF1(SF2, SF3
        } else {
            brLog.append(" Check SF3 " + tmp + "\n");
            SF3 = new SimpleStringProperty("");        // (SF1(SF2, SF3
        }

        sIN = sIN.substring(sIN.indexOf(")") + 1);       // (SF1(SF2, SF3)
        tmp = sIN.substring(0, sIN.indexOf(","));
        tmp = (tmp.isEmpty()) ? "RESO" : tmp;
        SF4Wrong = testSF4(lList, tmp);
        if (!SF4Wrong && !tmp.contains("RESO")) {
            SF4 = new SimpleStringProperty(tmp);         // (SF1(SF2, SF3)SF4
        } else if (!SF4Wrong && tmp.contains("RESO")) {
            SF4 = new SimpleStringProperty("");         // (SF1(SF2, SF3)SF4
        } else if (SF4Wrong) {
            brLog.append(" Check SF4 " + tmp + "\n");
            SF4 = new SimpleStringProperty("");         // (SF1(SF2, SF3)SF4
        }

        //sIN = sIN.substring (sIN.indexOf (",") + 1);        // // (SF1(SF2, SF3)SF4,
        tmp = sIN.substring(0, sIN.lastIndexOf(")"));

        //tmp = "2-He-4,,DA/DE,P/CA40";    // testing
        String[] QtyData = tmp.split(",");
        int llen = QtyData.length;

        for (int ix = 1; ix <= (llen - 1); ix++) {
            boolean iloop = true;
            int iii = llen - ix;
            String tmpo = QtyData[iii];
            if (tmpo.trim().isEmpty()) {
                break;
            }

            SF9Wrong = testSF9(lList, tmpo);
            SF8Wrong = testSF8(lList, tmpo);
            SF7Wrong = testSF7(lList, tmpo);
            SF6Wrong = testSF6(lList, tmpo);
            SF5Wrong = testSF5(lList, tmpo);

            if (!SF9Wrong) {
                SF9 = new SimpleStringProperty(tmpo);
            } else if (SF9Wrong && SF6Wrong) {
                brLog.append(" Check SF9 " + tmpo + "\n");
                SF9 = new SimpleStringProperty("");
            }

            if (!SF8Wrong) {
                SF8 = new SimpleStringProperty(tmpo);
            } else if (SF8Wrong && SF6Wrong) {
                brLog.append(" Check SF8 " + tmpo + "\n");
                SF8 = new SimpleStringProperty("");
            }

            if (!SF7Wrong) {
                SF7 = new SimpleStringProperty(tmpo);
            } else if (SF7Wrong && SF6Wrong) {
                brLog.append(" Check SF7 " + tmpo + "\n");
                SF7 = new SimpleStringProperty("");
            }

            if (!SF6Wrong) {
                SF6 = new SimpleStringProperty(tmpo);
            } else if (SF6Wrong && SF6Wrong) {
                brLog.append(" Check SF6 " + tmpo + "\n");
                SF6 = new SimpleStringProperty("");
            }

            if (!SF5Wrong) {
                SF5 = new SimpleStringProperty(tmpo);
            } else if (SF5Wrong && SF6Wrong) {
                brLog.append(" Check SF5 " + tmpo + "\n");
                SF5 = new SimpleStringProperty("");
            }
        }
        reacWrong
                = (SF1Wrong || SF2Wrong || SF3Wrong || SF4Wrong || SF6Wrong)
                        ? true : false;
    }

    private void getReacParams(libList lList) {
        String sIN;
        String[] sComps;

        //------------------ SF1 ---------------
        sIN = SF1.get();
        sComps = sIN.split("-");
        if (sComps.length >= 3) {   // for metastable etc
            if (exforUtil.isNumeric(sComps[0])) {
                atNumReactants = Integer.parseInt(sComps[0]);
                chkReacBalance = true;
            }
            if (exforUtil.isNumeric(sComps[2])) {
                massNumReactants = Integer.parseInt(sComps[2]);
            } else {
                chkReacBalance = false;
            }
        }
        //------------- SF1

        //---------------------- SF2 -----------------
        sIN = SF2.get();
        sComps = sIN.split("-");
        if (sComps.length == 1) {
            for (Object myItem : lList.particleAtMassList) {
                String cmpStr = myItem.toString().substring(0, myItem.toString().indexOf(" "));
                if (cmpStr.contains(sComps[0]) && (cmpStr.length() == sComps[0].length())) {
                    String tmp = myItem.toString().substring(myItem.toString().indexOf(" ")).trim();
                    sComps = tmp.split("-");
                    atNumReactants += Integer.parseInt(sComps[0]);
                    massNumReactants += Integer.parseInt(sComps[2]);
                }
            }
        } else if (sComps.length > 2) {
            if (exforUtil.isNumeric(sComps[0])) {
                atNumReactants += Integer.parseInt(sComps[0]);
            }
            if (exforUtil.isNumeric(sComps[2])) {
                massNumReactants += Integer.parseInt(sComps[2]);
            }
        }
        //-------------- SF2

        //---------------------- SF3 -----------------
        boolean is33 = false;
        boolean is30 = false;
        sIN = SF3.get();
        sComps = sIN.split("-");
        if (sComps.length == 1) {
            for (Object myItem : lList.particleAtMassList) {
                String cmpStr = myItem.toString().substring(0, myItem.toString().indexOf(" "));
                if (cmpStr.contains(sComps[0]) && (cmpStr.length() == sComps[0].length())) {
                    String tmp = myItem.toString().substring(myItem.toString().indexOf(" ")).trim();
                    sComps = tmp.split("-");
                    atNumReactants += Integer.parseInt(sComps[0]);
                    massNumReactants += Integer.parseInt(sComps[2]);
                    is33 = true;
                }
            }
            if (!is33) {
                for (Object myItem : lList.procList) {
                    if (myItem.toString().contains(sComps[0])) {
                        is30 = true;
                    }
                }
            }
        } else if (sComps.length > 2 && !is30) {
            if (exforUtil.isNumeric(sComps[0])) {
                atNumReactants += Integer.parseInt(sComps[0]);
            }
            if (exforUtil.isNumeric(sComps[2])) {
                massNumReactants += Integer.parseInt(sComps[2]);
            }
        }
        //-------------- SF3

        //-------------------------- SF4 -----------------
        is33 = false;
        boolean is236 = false;
        sIN = SF4.get();
        sComps = sIN.split("-");
        if (sComps.length == 1) {
            for (Object myItem : lList.particleAtMassList) {
                String cmpStr = myItem.toString().substring(0, myItem.toString().indexOf(" "));
                if (cmpStr.contains(sComps[0]) && (cmpStr.length() == sComps[0].length())) {
                    String tmp = myItem.toString().substring(myItem.toString().indexOf(" ")).trim();
                    sComps = tmp.split("-");
                    atNumReactants += Integer.parseInt(sComps[0]);
                    massNumReactants += Integer.parseInt(sComps[2]);
                    is33 = true;
                }
            }
        } else {
            for (Object myItem : lList.prodList) {
                if (myItem.toString().contains(sComps[0])) {
                    is236 = true;
                }
            }
            if (!is236 && !is33) {
                if (exforUtil.isNumeric(sComps[0])) {
                    atNumReactants = Integer.parseInt(sComps[0]);
                    chkReacBalance = true;
                }
                if (exforUtil.isNumeric(sComps[2])) {
                    massNumReactants = Integer.parseInt(sComps[2]);
                } else {
                    chkReacBalance = false;
                }
            }

        }
    }

}
