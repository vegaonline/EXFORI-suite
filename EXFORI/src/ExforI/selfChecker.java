/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javax.swing.JFileChooser;

/**
 *
 * @author vega
 */
public class selfChecker<T> {

    // from other class
    libList lList;


    JFileChooser fileChooser = new JFileChooser ();
    File recordsDir;
    BufferedWriter brW;
    private String EXFPathDir = "EXFOR_Compiled_Files";
    Stage myDialog = new Stage ();
    Stage mystage;
    Scene myDialogScene;
    private TextArea tf = new TextArea ();
    private Button okButton = new Button ("OK");
    String displayMsg = "";
    boolean notOK = false;
    boolean subentryNew = true;
    int subEntCnt = 0;// count of SUBENT
    int entInSUBENT = 0; // entry counts within a SUBENT
    int lineCnt = 0;
    int BIBN1 = 0;
    int BIBN2 = 0;
    int COMN1 = 0;
    int COMN2 = 0;
    int DATAN1 = 0;
    int DATAN2 = 0;
    int obligatoryConst = 8;
    int[] obligatory = new int[obligatoryConst];
    Map<Integer, String> obligHGet = new HashMap<> ();
    String loggerFile = "";
    String reactTagLines = "";
    String monitorTagLines = "";

    @SuppressWarnings ("unchecked")
    public static void selfChecker(String entryNum, String fName,
            ObservableList<editableData> myData, BufferedWriter brLog, libList lListIN) throws InterruptedException {

        selfChecker sc = new selfChecker ();
        boolean isEntry = true;
        boolean chkTab1 = true, chkTab2 = true, chkTab3 = true;
        boolean chkTab4 = true, chkTab5 = true, chkTab6 = true;

        sc.lList = lListIN;
        sc.setHeadObliged ();

        if ( !sc.lList.isLoaded ) {
            sc.lList.loadAllDict (brLog);
        }

        for ( int ii = 0; ii < sc.obligatoryConst; ii++ ) {
            sc.obligatory[ii] = 0;
        }

        sc.myDialog.setTitle ("Compiling  " + fName + " .... ");
        if ( !sc.myDialog.getModality ().toString ().contains ("MODAL") ) {
            sc.myDialog.initModality (Modality.WINDOW_MODAL);
            //myDialog.initOwner (myStage);
        }
        VBox vb1 = new VBox ();
        sc.tf.setPrefWidth (800);
        vb1.getChildren ().addAll (sc.tf, sc.okButton);
        sc.myDialogScene = new Scene (vb1, 900, 250);
        sc.myDialog.setScene (sc.myDialogScene);
        sc.myDialog.show ();

        if ( entryNum.isEmpty () ) {
            popupMsg.warnBox ("EntryNum has not been set.. Exiting....",
                    "Attention! No entryNum");
            return;
        }

        sc.okButton.setOnAction (e -> {
            sc.loggerFile = fName + ".CHK";
            sc.setDefaultDirExt (sc.EXFPathDir);
            try {
                sc.brW = new BufferedWriter (new FileWriter (sc.loggerFile));
                sc.brW.write (sc.displayMsg);
            } catch (IOException ex) {
                Logger.getLogger (selfChecker.class.getName ()).log (
                        Level.SEVERE, null, ex);
            }
            try {
                sc.brW.close ();
            } catch (IOException ex) {
                Logger.getLogger (selfChecker.class.getName ()).
                        log (Level.SEVERE, null, ex);
            }
            sc.tf.clear ();
            sc.displayMsg = "";
            sc.myDialog.close ();
        });

        // Get total line number
        int totLineNum = myData.size ();

        sc.tagReact (myData);

        for ( int ii = 0; ii < totLineNum; ii++ ) {
            String s1 = myData.get (ii).getBibItemName ().toString ();
            String s2 = myData.get (ii).getPointerID ().toString ();
            String s3 = myData.get (ii).getContentTxt ().toString ();
            String s4 = myData.get (ii).getSubEntNum ().toString ();

            if ( s4.length () != 14 ) {
                sc.displayMsg
                        += "Error in subentry numbering. Please check one space at the end of the line.";
            }
            s4 = s4.trim ();

            String temp = s1;
            if ( temp.contains ("ENTRY") && !temp.contains ("END") ) {
                sc.notOK = false;
                sc.doCheckEntry (entryNum, s1, s2, s3, s4);
            }
            if ( temp.contains ("SUBENT") && !temp.contains ("END") ) {
                sc.notOK = false;
                sc.doCheckSubEnt (entryNum, s1, s2, s3, s4);
            }
            if ( temp.contains ("SUBENT") && temp.contains ("END") ) {
                sc.notOK = false;
                sc.doCheckSubEnt (entryNum, s1, s2, s3, s4);//check counts later                
            }
            if ( temp.contains ("BIB") ) {
                sc.notOK = false;
                if ( !sc.doCheckBIB (myData, ii) ) {
                    sc.displayMsg += " Error in BIB entry. \n";
                }
            }
            if ( temp.contains ("TITLE") ) {
                sc.obligatory[0] = 1;
            }
            if ( temp.contains ("AUTHOR") ) {
                sc.notOK = false;
                if ( !sc.doCheckAuthor (ii, myData) ) {
                    sc.displayMsg += "*** ERROR in AUTHOR ******\n";
                } else {
                    sc.obligatory[1] = 1;
                }
            }
            if ( temp.contains ("INSTITUTE") ) {
                sc.notOK = false;
                if ( !sc.doCheckInst (entryNum, s1, s2, s3, s4) ) {
                    sc.displayMsg += " *** ERROR in INSTITUTE *****\n";
                } else {
                    sc.obligatory[2] = 1;
                }
            }
            if ( temp.contains ("REFERENCE") ) {
                sc.notOK = false;
                if ( !sc.doCheckRef (ii, myData) ) {
                    sc.displayMsg += "*** ERROR in Reference. *****\n";
                } else {
                    sc.obligatory[3] = 1;
                }
            }
            if ( temp.contains ("FACILITY") ) {
                sc.notOK = false;
                if ( !sc.doCheckFac (ii, myData) ) {
                    sc.displayMsg += "*** ERROR in Facility.  ******\n";
                } else {
                    sc.obligatory[4] = 1;
                }
            }
            if ( temp.contains ("SAMPLE") ) {
                sc.obligatory[5] = 1;
            }
            if ( temp.contains ("INC-SOURCE") ) {
                sc.notOK = false;
                if ( !sc.doCheckIncSrc (ii, myData) ) {
                    sc.displayMsg += "*** ERROR in INC-SOURCE. *****\n";
                }
            }
            if ( temp.contains ("DETECTOR") ) {
                sc.notOK = false;
                if ( !sc.doCheckDetector (s3) ) {
                    sc.displayMsg += "*** ERROR in DETECTOR *****\n";
                } else {
                    sc.obligatory[6] = 1;
                }
            }

            if ( temp.contains ("HISTORY") ) {
                sc.obligatory[7] = 1;
            }

            if ( temp.contains ("REACTION") || temp.contains ("MONITOR") ) {
                if ( !sc.doCheckReaction (ii, myData) ) {
                    sc.notOK = true;
                }
            }

            if ( temp.contains ("COMMON") && !temp.contains ("NO") && !temp.
                    contains ("END") ) {
                sc.notOK = false;
                if ( !sc.doCheckCommon (ii, myData) ) {
                    sc.displayMsg += "*** ERROR in COMMON ...\n";
                }
            }

            if ( temp.contains ("DATA") && s2.isEmpty () && !temp.
                    contains ("NO") && !temp.
                    contains ("END") ) {
                sc.notOK = false;
                if ( !sc.doCheckData (ii, myData) ) {
                    sc.displayMsg += "*** ERROR in DATA.... \n";
                }
            }

            if ( temp.contains ("NOCOMMON") && temp.contains ("NO") ) {
                String[] SA = s3.trim ().split ("\\s+");
                if ( !sc.chkTab (s3) ) {
                    sc.displayMsg
                            += ("***ERROR Tabbing of entries for NOCOMMON at line " +
                            ii + " is wrong \n");
                    sc.notOK = true;
                }
                for ( int i1 = 0; i1 < SA.length; i1++ ) {
                    if ( !SA[i1].trim ().contains ("0") ) {
                        sc.displayMsg += "***ERROR NOCOMMON must have 0 0 \n";
                        sc.notOK = true;
                    }
                }
            }
        }

        sc.displayMsg
                += "================= Obligatory Headers ====================\n";
        for ( int jj = 0; jj < sc.obligatoryConst; jj++ ) {
            if ( sc.obligatory[jj] == 1 ) {
                sc.displayMsg += sc.obligHGet.get (jj) + " PRESENT.\n";
            } else if ( sc.obligatory[jj] == 0 ) {
                sc.displayMsg += sc.obligHGet.get (jj) + " ABSENT.\n";
            }
        }
        sc.tf.setText (sc.displayMsg);
    }

    private void setHeadObliged() {
        obligHGet.put (0, "TITLE");
        obligHGet.put (1, "AUTHOR");
        obligHGet.put (2, "INSTITUTE");
        obligHGet.put (3, "REFERENCE");
        obligHGet.put (4, "FACILITY");
        obligHGet.put (5, "SAMPLE");
        obligHGet.put (6, "DETECTOR");
        obligHGet.put (7, "HISTORY");
    }

    /**
     * Set the default directory
     *
     * @param fPath
     */
    private void setDefaultDirExt(String fPath) {
        recordsDir = new File (fPath);
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }
        if ( !fileChooser.getCurrentDirectory ().toString ().contentEquals (
                fPath) ) {
            fileChooser.setCurrentDirectory (recordsDir);
        }
    }

    /**
     * Test grammer for all the reaction supplied
     *
     * @param s3
     */
    private void CheckReactGrammer(String s3) { // parenthesis already removed
        System.out.print (s3 + "-->");
        String SF1 = s3.substring (0, s3.indexOf ("("));
        String SF2 = s3.substring (s3.indexOf ("(") + 1, s3.indexOf (","));
        String SF3 = s3.substring (s3.indexOf (",") + 1, s3.indexOf (")"));
        s3 = s3.substring (s3.indexOf (")"));
        String SF4 = s3.substring (s3.indexOf (")") + 1, s3.indexOf (","));
        s3 = s3.substring (s3.indexOf (",")+1);
        System.out.println("s3-->"+s3);
        String SF5 =s3.substring (0, s3.indexOf (","));
        String SF6 = "";
        String SF7 = "";
        String SF8 = "";
        String SF9 = "";
        System.out.print ("SF1->" + SF1 + " SF2->" + SF2 + " SF3->" + SF3 +
                " SF4->" + SF4);
        System.out.println (" SF5->" + SF5 + " SF6->" + SF6 + " SF7->" + SF7 +
                " SF8->" + SF8 + " SF9->" + SF9);
    }

    /**
     * Generate a string of line numbers for all the reaction and monitors in
     * two separate strings
     *
     * @param myData
     */
    private void tagReact(ObservableList<editableData> myData) {
        int totNum = myData.size ();

        for ( int ii = 0; ii < totNum; ii++ ) {
            String s1 = myData.get (ii).getBibItemName ().trim ();

            if ( s1.contains ("REAC") ) {
                if ( reactTagLines.length () == 0 ) {
                    reactTagLines = Integer.toString (ii);
                } else {
                    reactTagLines += " " + Integer.toString (ii);
                }
            }
            if ( s1.contains ("MONITOR") ) {
                if ( monitorTagLines.length () == 0 ) {
                    monitorTagLines = Integer.toString (ii);
                } else {
                    monitorTagLines += " " + Integer.toString (ii);
                }
            }
        }

    }

    /**
     * This checks error in reaction
     *
     * @param ii
     * @param myData
     * @return
     */
    private boolean doCheckReaction(int ii, ObservableList<editableData> myData) {
        boolean isOK = true;
        String s3 = "";

        
        // check pointer for multiline
        int jCnt = 1;
        int jj1 = ii + 1;

        while (myData.get (jj1).getBibItemName ().isEmpty ()) {
            ++jCnt;
            String t1 = myData.get (jj1 - 1).getPointerID ();
            int tV1 = (exforUtil.isNumeric (t1)) ? Integer.parseInt (t1) : 0;
            if ( tV1 == 0 ) {
                isOK = false;
                notOK = true;
                displayMsg
                        += "***ERROR Multiline reactions must have pointer. \n";
                return isOK;
            }

            String t2 = myData.get (jj1).getPointerID ();
            int tV2 = (exforUtil.isNumeric (t2)) ? Integer.parseInt (t2) : 0;
            if ( !exforUtil.isNumeric (t2) || (tV2 != (tV1 + 1)) ) {
                isOK = false;
                notOK = true;
                displayMsg
                        += "*** ERROR pointer values are wrong for Reactions. \n";
                return isOK;
            }
            ++jj1;
        }

        // check reaction
        for ( int i1 = 0; i1 < jCnt; i1++ ) {
            jj1 = ii + i1;
            s3 = myData.get (jj1).getContentTxt ().trim ();
            

            // check parenthesis
            if ( s3.charAt (0) != '(' ) {
                isOK = false;
                notOK = true;
                displayMsg += (s3.charAt (0) +
                        " reaction must be enclosed within parenthesis. \n");
                return isOK;
            }
            int s3last = s3.length () - 1;
            if ( s3.charAt (s3last) != ')' ) {
                isOK = false;
                notOK = true;
                displayMsg += (s3.charAt (0) +
                        " reaction must be enclosed within parenthesis. \n");
                return isOK;
            }
            s3 = s3.substring (1, s3last); //removing parenthesis
            //  String keyW = s3.substring (s3.lastIndexOf (','));
            //    System.out.println (keyW);

        }

        // check grammer for reaction
        CheckReactGrammer (s3);
        return isOK;
    }

    /**
     * Checking the ENTRY line
     */
    private void doCheckEntry(String EN, String s1, String s2, String s3,
            String s4) {
        boolean isOK = true;
        String tmpArr[] = s3.split ("\\s+");
        int datCnt = tmpArr.length - 1;

        if ( !chksubEntNum (EN, s1, s4) ) {
            displayMsg += (" Error in sub entry numbering for " + s1 + "\n");
            isOK = false;
            notOK = true;
        }

        if ( !chkTab (s3) ) {
            displayMsg += (" Tabbing of entries " + s1 +
                    " are not OK. Check spacing .. \n");
            isOK = false;
            notOK = false;
        }

        if ( tmpArr[1].length () != 5 ) {
            displayMsg
                    += " Accession number wrong. It should have 5 characters length. \n";
            isOK = false;
            notOK = true;
        }
        if ( tmpArr[2].length () != 8 ) {
            displayMsg
                    += " Date format is wrong. It should have 8 characters (YYYYMMDD) length.\n";
            isOK = false;
            notOK = true;
        }
        if ( isOK ) {
            // displayMsg += " ENTRY OK. \n";
        }
    }

    /**
     * Generating counts for DATA SECTION
     *
     * @param myData
     * @param kk
     */
    private void setDataRecNum(ObservableList<editableData> myData, int kk) {
        int lastN = myData.size ();
        DATAN1 = 0;
        DATAN2 = 0;
        lineCnt = 0;
        int jj = kk + 3;
        ++lineCnt;

        String tmp = myData.get (jj).getBibItemName ().trim () + ":::::: " +
                myData.get (jj).getContentTxt ().trim ();
        ++DATAN2;
        String[] parts = tmp.split ("\\s+");
        DATAN1 = parts.length;
        while (!tmp.contains ("ENDDATA") && jj < (lastN - 1)) {
            ++jj;
            ++lineCnt;
            tmp = myData.get (jj).getBibItemName ().trim ();
            ++DATAN2;
        }
        --DATAN2;

    }

    /**
     * Generating counts for COMMON
     *
     * @param myData
     * @param kk
     */
    private void setCommonrecNum(ObservableList<editableData> myData, int kk) {
        int lastN = myData.size ();
        COMN1 = 0;
        COMN2 = 0;
        lineCnt = 0;
        int jj = kk + 1;
        ++lineCnt;
        String tmp = myData.get (jj).getBibItemName () + " " + myData.get (jj).
                getContentTxt ();
        ++COMN2;
        String[] parts = tmp.split ("\\s+");
        COMN1 = parts.length;
        while (!tmp.contains ("ENDCOMMON") && jj < (lastN - 1)) {
            ++jj;
            ++lineCnt;
            tmp = myData.get (jj).getBibItemName ();
            ++COMN2;
        }
        --COMN2;
    }

    /**
     * Checks Header and Units
     *
     * @param kk
     * @param myData
     */
    private boolean checkHeadUnits(int kk, ObservableList<editableData> myData) {
        boolean isOK = false;
        String s1 = myData.get (kk + 1).getBibItemName () + " " + myData.get (
                kk + 1).getContentTxt ().trim ();
        String s2 = myData.get (kk + 2).getBibItemName () + " " + myData.get (
                kk + 2).getContentTxt ().trim ();
        String[] HeadArr = s1.split ("\\s+");
        String[] UnitArr = s2.split ("\\s+");
        String HeadKeyType = "";
        String UnitKeyType = "";
        String dummy = "";

        for ( Object HeadList : lList.dataHeadFullList ) {
            String HStr = HeadList.toString ().trim ();
            String keyH = HStr.substring (0, HStr.indexOf (" ")).trim ();
            int cue1 = HStr.indexOf (" ");
            int cue2 = cue1 + 17;
            dummy = HStr.substring (cue1).trim ();
            dummy = dummy.substring (0, 15).trim ();
            for ( String HA : HeadArr ) {
                HA = HA.trim ();
                if ( keyH.matches (HA) ) {
                    HeadKeyType += (dummy.substring (10).trim () + " ");
                }
            }
        }

        for ( Object UnitList : lList.dataUnitFullList ) {
            String UStr, keyU;
            UStr = UnitList.toString ();
            if ( UStr.charAt (0) != ' ' ) {
                keyU = UStr;
            } else {
                continue;
            }
            if ( UStr.contains ("SEE TEXT") ) {
                continue;
            }
            for ( String UA : UnitArr ) {
                UA = UA.trim ();

                if ( keyU.substring (0, keyU.indexOf (" ")).matches (UA) ) {
                    UnitKeyType += keyU.substring (66, 70);
                }
            }
        }

        String[] HK = HeadKeyType.split ("\\s+");
        String[] UK = UnitKeyType.split ("\\s+");

        for ( int ii = 0; ii < HK.length; ii++ ) {
            if ( !HK[ii].contains ("*") ) {
                if ( !HK[ii].contains (UK[ii]) ) {
                    isOK = false;
                    notOK = true;
                    displayMsg
                            += "***ERROR Please check Unit and Header for COMMON \n";
                    return isOK;
                } else {
                    isOK = true;
                    notOK = false;
                }
            } else {
                if ( !UK[ii].contains ("PC") ) {
                    isOK = false;
                    notOK = true;
                    displayMsg
                            += "***ERROR Please check Unit for data header \n";
                    return isOK;
                } else {
                    isOK = true;
                    notOK = false;
                }
            }
        }
        return isOK;
    }

    /**
     * Checking COMMON
     *
     * @param ii
     * @param myData
     * @return
     */
    private boolean doCheckCommon(int ii, ObservableList<editableData> myData) {
        boolean isOK = true;
        setCommonrecNum (myData, ii);

        String strCompare = exforUtil.fixString11 (Integer.toString (COMN1)) +
                exforUtil.fixString11 (Integer.toString (COMN2));

        if ( !myData.get (ii).getContentTxt ().matches (strCompare) ) {
            isOK = false;
            notOK = true;
            displayMsg += "*** COMMON: counts are not matching *****\n";
            return isOK;
        }
        isOK = checkHeadUnits (ii, myData);
        if ( !isOK ) {
            notOK = true;
        }
        return isOK;
    }

    private boolean doCheckData(int ii, ObservableList<editableData> myData) {
        boolean isOK = true;

        setDataRecNum (myData, ii);

        String strCompare = exforUtil.fixString11 (Integer.toString (DATAN1)) +
                exforUtil.fixString11 (Integer.toString (DATAN2));

        if ( !myData.get (ii).getContentTxt ().trim ().matches (strCompare.
                trim ()) ) {
            isOK = false;
            notOK = true;
            displayMsg += "***ERROR DATA: counts are not matching...****\n";
            return isOK;
        } else {
            isOK = true;
            System.out.println ("DATA matches.......;");
        }
        //isOK=checkHeadUnits(ii, myData); //-----> make for DATA from Reaction
        if ( !isOK ) {
            notOK = true;
        }
        return isOK;
    }

    /**
     * It checks detector
     *
     * @param s3
     * @return
     */
    private boolean doCheckDetector(String s3) {
        boolean isOK = true;
        boolean isDET = false;

        s3 = s3.substring (0, s3.indexOf (' '));
        s3 = s3.trim ();
        if ( s3.charAt (0) != '(' ) {
            displayMsg += " DETECTOR must begin with '(' \n  ";
            isOK = false;
            notOK = true;
        }
        int lastW = s3.length () - 1;
        lastW = (s3.charAt (lastW) == ',') ? --lastW : lastW;
        if ( s3.charAt (lastW) != ')' ) {
            displayMsg += " DETECTOR must end with ')'. \n";
            isOK = false;
            notOK = true;
        }
        s3 = s3.substring (1, s3.length () - 1);
        String[] strArr = s3.split ("\\,");

        if ( strArr.length != 0 ) {
            for ( int ij = 0; ij < strArr.length; ij++ ) {
                s3 = strArr[ij];
                for ( Object det : lList.detectorList ) {
                    if ( det.toString ().contains (s3) ) {
                        isDET = true;
                        break;
                    }
                }
            }
        }
        if ( !isDET ) {
            isOK = false;
            notOK = true;
            displayMsg += "DETECTOR entry " + s3 +
                    " is not found in the dictionary.\n";
        }

        return isOK;
    }

    /**
     * This checks INC-SOURCE
     *
     * @param s3
     * @return
     */
    private boolean doCheckIncSrc(int ii, ObservableList<editableData> myData) {
        boolean isOK = true;
        boolean isIncSrc = false;
        String s3 = "";

        int lCount = 1;
        int jj1 = ii + 1;

        // first check if there are pointers for multiline        
        while (myData.get (jj1).getBibItemName ().isEmpty ()) {
            ++lCount;
            String t1 = myData.get (jj1 - 1).getPointerID ();
            int tV1 = (exforUtil.isNumeric (t1)) ? Integer.parseInt (t1) : 0;
            if ( tV1 == 0 ) {
                isOK = false;
                notOK = true;
                displayMsg
                        += "For multiline INC-SOURCE one must add integer pointer. \n";
                return isOK;
            }

            String t2 = myData.get (jj1).getPointerID ();
            int tV2 = (exforUtil.isNumeric (t2)) ? Integer.parseInt (t2) : 0;
            if ( !exforUtil.isNumeric (t2) || (tV2 != (tV1 + 1)) ) {
                isOK = false;
                notOK = true;
                displayMsg
                        += "*** ERROR Please pointer values for multiline INC-SOURCE. \n";
                return isOK;
            }
            System.out.println (t1 + "  " + t2);
            ++jj1;

        }

        // Now check parenthesis for the keywords
        for ( int i1 = 0; i1 < lCount; i1++ ) {
            int i2 = i1 + ii;
            s3 = myData.get (i2).getContentTxt ();
            s3 = s3.substring (0, s3.indexOf (')') + 1);
            if ( s3.charAt (0) != '(' ) {
                displayMsg
                        += "***ERROR  INC-SOURCE keyword must be within parenthesis. \n";
                isOK = false;
                notOK = true;
                return isOK;
            }

            if ( s3.charAt (s3.length () - 1) != ')' ) {
                displayMsg
                        += "***ERROR  INC-SOURCE keyword must be within parenthesis. \n";
                isOK = false;
                notOK = true;
                return isOK;
            }
        }

        s3 = s3.substring (1, s3.length () - 1);
        String[] strArr = s3.split ("\\,");
        if ( strArr.length !=
                0 ) {
            for ( int ij = 0; ij < strArr.length; ij++ ) {
                s3 = strArr[ij];
                for ( Object incSrc : lList.incSrcList ) {
                    if ( incSrc.toString ().contains (s3) ) {
                        isIncSrc = true;
                        break;
                    }
                }
            }
        }
        if ( !isIncSrc ) {
            isOK = false;
            notOK = true;
            displayMsg += "INC-SOURCE entry " + s3 +
                    " is not found in the dictionary.\n";
        }

        return isOK;
    }

    /**
     * This is to check facility for one row which could be repeated for
     * multiple rows for multiple facilities
     *
     * @param s3
     * @return
     */
    private boolean doCheckFACPart(String s3) {
        boolean isOK = true;
        boolean isFac = false;
        boolean isInst = false;

        if ( s3.charAt (0) != '(' ) {
            displayMsg += " Reference must begin with '(' \n  ";
            isOK = false;
            notOK = true;
        }
        int lastW = s3.length () - 1;
        lastW = (s3.charAt (lastW) == ',') ? --lastW : lastW;
        if ( s3.charAt (lastW) != ')' ) {
            displayMsg += " Refernce must end with ')'. \n";
            isOK = false;
            notOK = true;
        }
        s3 = s3.substring (1, s3.length () - 1);
        String[] strArr = s3.split ("\\,");

        String facility = strArr[0];
        for ( Object fac : lList.facilList ) {
            if ( fac.toString ().contains (facility) ) {
                isFac = true;
                break;
            }
        }
        if ( !isFac ) {
            isOK = false;
            notOK = true;
            displayMsg += "*** ERROR Facility Type " + facility +
                    " is not matching with Dictionary. *****\n";
        }

        String institute = strArr[1];
        for ( Object inst : lList.instList ) {
            if ( inst.toString ().contains (institute) ) {
                isInst = true;
                break;
            }
        }
        if ( !isInst ) {
            isOK = false;
            notOK = true;
            displayMsg
                    += "*** ERROR Institute " + institute + " for facility " +
                    facility + "  is not matching with dictionary.\n";
        }
        return isOK;
    }

    /**
     * This is for checking Facilities
     *
     * @param kk
     * @param myData
     * @return
     */
    private boolean doCheckFac(int kk, ObservableList<editableData> myData) {
        boolean isOK = true;
        String s3 = myData.get (kk).getContentTxt ();
        s3 = s3.trim ();

        isOK = doCheckFACPart (s3);
        int counter = kk + 1;
        while (myData.get (counter).getBibItemName ().isEmpty ()) {
            s3 = myData.get (counter).getContentTxt ().trim ();
            isOK = doCheckFACPart (s3);
            ++counter;
        }
        return isOK;
    }

    /**
     * Checks for SUBENT only
     *
     * @param EN
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     */
    private void doCheckSubEnt(String EN, String s1, String s2, String s3,
            String s4) {
        boolean isOK = true;
        String tmpArr[] = s3.split ("\\s+");
        int datCnt = tmpArr.length - 1;

        if ( !chksubEntNum (EN, s1, s4) ) {
            displayMsg += (" Error in sub entry numbering for " + s1 + "\n");
            isOK = false;
            notOK = true;
        }

        if ( !chkTab (s3) ) {
            displayMsg += " Tabbing of entries are not OK. Check spacing .. \n";
            isOK = false;
            notOK = false;
        }

        if ( !s1.contains ("END") ) {
            String ent1 = EN + exforUtil.fixStr0s (Integer.toString (subEntCnt),
                    3);
            if ( !tmpArr[1].contains (ent1) ) {
                displayMsg
                        += (" Error in SUBENT entry for subentry # " + subEntCnt +
                        "\n");
                isOK = false;
                notOK = true;
            }
        }
        if ( isOK && s1.contains ("SUBENT") && !s1.contains ("END") ) {
            displayMsg += (" SUBENT " + subEntCnt + " OK. \n");
        }
        if ( isOK && s1.contains ("ENDSUBENT") ) {
            displayMsg += (" ENDSUBENT " + " OK. \n");
        }
    }

    private boolean refCheckPart(String s3) {
        boolean isOK = true;
        boolean isjType = false;
        boolean isJour = false;

        if ( s3.charAt (0) != '(' ) {
            displayMsg += " Reference must begin with '(' \n  ";
            isOK = false;
            notOK = true;
        }

        int lastW = s3.length () - 1;
        lastW = (s3.charAt (lastW) == ',') ? --lastW : lastW;

        if ( s3.charAt (lastW) != ')' ) {
            displayMsg += " Refernce must end with ')'. \n";
            isOK = false;
            notOK = true;
        }
        s3 = s3.substring (1, s3.length () - 1);
        // test this line 
        String[] strArr = s3.split ("\\,");

        String jType = strArr[0];
        for ( Object jT : lList.jTypeList ) {
            if ( jT.toString ().contains (jType) ) {
                isjType = true;
                break;
            }
        }
        if ( !isjType ) {
            isOK = false;
            notOK = true;
            displayMsg += "*** ERROR Journal Type " + jType +
                    "  is not matching with Dictionary. \n";
            // return isOK;
        }
        String jName = strArr[1];
        if ( jType.matches ("J") ) {
            for ( Object jour : lList.jourList ) {
                if ( jour.toString ().contains (jName) ) {
                    isJour = true;
                    break;
                }
            }
        }
        if ( !isJour ) {
            isOK = false;
            notOK = true;
            displayMsg += "*** ERROR Journal name " + jName +
                    " for Journal type mentioned " + jType +
                    " is not matching with dictionary. \n";
            //return isOK;
        }

        return isOK;
    }

    /**
     * This checks references with dictionary
     *
     * @param ii
     * @param myData
     * @return
     */
    private boolean doCheckRef(int ii, ObservableList<editableData> myData) {
        boolean isOK = true;

        String s3 = myData.get (ii).getContentTxt ();
        s3 = s3.trim ();

        isOK = refCheckPart (s3);
        int counter = ii + 1;
        while (myData.get (counter).getBibItemName ().isEmpty ()) {
            s3 = myData.get (counter).getContentTxt ().trim ();
            isOK = refCheckPart (s3);
            ++counter;
        }
        return isOK;
    }

    /**
     * this sets parameters for BIB
     *
     * @param myData
     * @param kk
     */
    private void setBIBrecNum(ObservableList<editableData> myData, int kk) {
        int lastN = myData.size ();
        BIBN1 = 0;
        BIBN2 = 0;
        for ( int jj = kk + 1; jj < lastN; jj++ ) {
            String temp = myData.get (jj).getBibItemName ();
            if ( !temp.contains ("ENDBIB") && !temp.isEmpty () ) {
                ++BIBN1;
            } else if ( temp.contains ("ENDBIB") ) {
                BIBN2 = jj - kk - 1;
                break;
            }
        }
    }

    /**
     * Checks BIB and ENDBIB entry
     *
     * @param myData
     * @param kk
     * @return
     */
    private boolean doCheckBIB(ObservableList<editableData> myData, int kk) {
        boolean isOK = true;
        String s1 = myData.get (kk).getBibItemName ();
        String s3 = myData.get (kk).getContentTxt ().trim ();

        if ( !chkTab (s3) ) {
            displayMsg += " Tabbing of entries for " + s1 +
                    " are not OK. Check spacing .. \n";
            isOK = false;
            notOK = false;
        }

        if ( !myData.get (kk).getBibItemName ().contains ("ENDBIB") ) {
            setBIBrecNum (myData, kk);
            String s3Eval = exforUtil.fixString11 (Integer.toString (BIBN1)) +
                    exforUtil.fixString11 (Integer.toString (BIBN2));
            if ( !s3Eval.contains (s3) ) {
                displayMsg += (" *** Error in BIB parameters see line number" +
                        (kk + 1));
                isOK = false;
                notOK = true;
            }
        } else {
            String s3Eval = exforUtil.fixString11 (Integer.toString (BIBN2)) +
                    exforUtil.fixString11 (Integer.toString (0));
            BIBN1 = 0;
            BIBN2 = 0;
            if ( !s3Eval.contains (s3) ) {
                displayMsg += (" *** Error in BIB parameters see line number" +
                        (kk + 1));
                isOK = false;
                notOK = true;
            }
        }
        return isOK;
    }

    /**
     * Institute checker
     *
     * @param EN
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     * @return
     */
    private boolean doCheckInst(String EN, String s1, String s2, String s3,
            String s4) {
        boolean isOK = true;

        s3 = s3.trim ();
        if ( s3.charAt (0) != '(' ) {
            displayMsg += "Institute must have '(' at the beginning\n";
            isOK = false;
            notOK = true;
        }
        if ( s3.charAt (s3.length () - 1) != ')' ) {
            displayMsg += "Institute must have ')' at the end. \n";
            isOK = false;
            notOK = true;
        }
        if ( !isOK ) {
            return isOK;
        }

        s3 = s3.substring (1, s3.length () - 1);
        isOK = false;
        for ( Object o : lList.instList ) {
            if ( o.toString ().contains (s3) ) {
                isOK = true;
                return isOK;
            }
        }
        return isOK;
    }

    /**
     * This checks AUTHOR entry
     *
     * @param EN
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     * @return
     */
    private boolean doCheckAuthor(int ii, ObservableList<editableData> myData) {
        boolean isOK = true;

        String thisStr = myData.get (ii).getContentTxt ().trim ();
        String s3 = "";
        int kLine = ii + 1;

        if ( !thisStr.endsWith (",") ) {
            thisStr += " ";
        }

        while (myData.get (kLine).getBibItemName ().isEmpty ()) {
            thisStr += myData.get (kLine).getContentTxt ().trim ();
            if ( !thisStr.endsWith (",") ) {
                thisStr += " ";
            }
            ++kLine;
        }
        s3 = thisStr.trim ();

        if ( s3.charAt (0) != '(' ) {
            displayMsg += (" Author field must have  '(' at the beginning. \n");
            isOK = false;
        }
        int s3l = s3.length () - 1;

        if ( s3.charAt (s3l) != ')' ) {
            displayMsg += (" Author field must have  ')' at the end. \n");
            isOK = false;
        }
        return isOK;
    }

    /**
     * checking tabs of entry
     *
     * @param sIN
     * @return
     */
    private boolean chkTab(String sIN) {
        boolean isOK = true;
        String tmpArr[] = sIN.split ("\\s+");
        int sLen = tmpArr.length - 1;
        int spaces = sIN.length () - sIN.replace (" ", "").length ();
        int words = sIN.length () - spaces;
        int totC = 0;
        for ( int ii = 1; ii <= sLen; ii++ ) {
            totC += tmpArr[ii].length ();
        }
        totC += spaces;
        isOK = (totC == sLen * 11) ? true : false;
        return isOK;
    }

    /**
     * this checks the sub entry numbering for entry heads
     *
     * @param EN
     * @param s1
     * @param sIN
     * @return
     */
    private boolean chksubEntNum(String EN, String s1, String sIN) {
        boolean isOK = true;
        String finalSubentNum = "";

        if ( s1.contains ("ENTRY") && !s1.contains ("END") ) {
            finalSubentNum = EN + "000" + "00001";
            if ( !finalSubentNum.matches (sIN) ) {
                isOK = false;
            }
            finalSubentNum = "";
            return isOK;
        }

        if ( s1.contains ("SUBENT") && !s1.contains ("END") && subentryNew ) {
            subentryNew = false;
            ++subEntCnt;
            lineCnt = 1;
            String entry1 = Integer.toString (subEntCnt);
            String entry2 = Integer.toString (lineCnt);
            finalSubentNum = EN + exforUtil.fixStr0s (entry1, 3) + exforUtil.
                    fixStr0s (entry2, 5);
            if ( !finalSubentNum.matches (sIN) ) {
                isOK = false;
            }
            finalSubentNum = "";
            return isOK;
        }

        if ( s1.contains ("ENDSUBENT") && !subentryNew ) {
            lineCnt = 99999;
            String entry1 = Integer.toString (subEntCnt);
            String entry2 = Integer.toString (lineCnt);
            finalSubentNum = EN + exforUtil.fixStr0s (entry1, 3) + exforUtil.
                    fixStr0s (entry2, 5);
            subentryNew = true;
            if ( !finalSubentNum.matches (sIN) ) {
                isOK = false;
            }
            finalSubentNum = "";
            return isOK;
        }
        return isOK;
    }
}
