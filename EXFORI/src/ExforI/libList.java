/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import java.io.*;
import java.util.logging.*;
import javafx.collections.*;

/**
 *
 *
 * @author vega
 */
public class libList<T> {

    boolean isLoaded = false;
    private InputStreamReader inDictStrReader;
    private String dictName;
    private String DICTPathDir = "dict";

    private String instListName
            = "IAEA_DICT_ARC_3_2016_Inst.txt";
    private String jTypeListName
            = "IAEA_DICT_ARC_4_2016_JTYPE.txt";
    private String jourListName
            = "IAEA_DICT_ARC_5_2016_Journal.txt";
    private String reportListName
            = "IAEA_DICT_ARC_6_2016_Reports.txt";
    private String confListName
            = "IAEA_DICT_ARC_7_2016_Confs.txt";
    private String statusListName
            = "IAEA_DICT_ARC_16_2016_status.txt";
    private String facilityListName
            = "IAEA_DICT_ARC_18_2016_facilities.txt";
    private String incSrcListName
            = "IAEA_DICT_ARC_19_2016_INCSRC.txt";
    private String addlResultListName
            = "IAEA_DICT_ARC_20_2016_AddlResult.txt";
    private String methodListName
            = "IAEA_DICT_ARC_21_2016_Methods.txt";
    private String detectorListName
            = "IAEA_DICT_ARC_22_2016_DETECTOR.txt";
    private String analysisListName
            = "IAEA_DICT_ARC_23_2016_Analysis.txt";
    private String dataHeadingListName
            = "IAEA_DICT_ARC_24_2016_DataHeading.txt";
    private String dataUnitsListName
            = "IAEA_DICT_ARC_25_2016_DataUnits.txt";
    private String UnitFamilyListName
            = "IAEA_DICT_ARC_26_2016_Unit_family_code.txt";
    private String compoundListName
            = "IAEA_DICT_ARC_209_2016_ChemicalCompound_SF1.txt";
    private String targetNListName
            = "IAEA_DICT_ARC_227_2016_nuclides.txt";
    private String procListName
            = "IAEA_DICT_ARC_30_2016_ProcessReaction_SF3.txt";
    private String incPListName
            = "IAEA_DICT_ARC_33_2016_particles_SF-2-3-7.txt";
    private String branchListName
            = "IAEA_DICT_ARC_31_2016_BranchesReaction_SF5.txt";
    private String paramSF6ListName
            = "IAEA_DICT_ARC_32_2016_ParametersReaction_SF6.txt";
    private String modifierListName
            = "IAEA_DICT_ARC_34_2016_ModifierReaction_SF8.txt";
    private String dataTypeListName
            = "IAEA_DICT_ARC_35_2016_DataTypeReaction_SF9.txt";
    private String resultsListName
            = "IAEA_DICT_ARC_37_2016_results.txt";
    private String dataListName
            = "IAEA_DICT_ARC_144_2016_DataLib.txt";
    private String bookListName
            = "IAEA_DICT_ARC_207_2016_Books.txt";
    private String prodListName
            = "IAEA_DICT_ARC_236_2016_QUANTITY.txt";

    public ObservableList<String> addlResultList
            = FXCollections.observableArrayList ();
    public ObservableList<String> analysisList
            = FXCollections.observableArrayList ();
    public ObservableList<String> bookList
            = FXCollections.observableArrayList ();
    public ObservableList<String> branchList
            = FXCollections.observableArrayList ();
    public ObservableList<String> compoundList
            = FXCollections.observableArrayList ();
    public ObservableList<String> compoundNucArr
            = FXCollections.observableArrayList ();
    public ObservableList<String> confList
            = FXCollections.observableArrayList ();
    public ObservableList<String> d33SF2List
            = FXCollections.observableArrayList ();
    public ObservableList<String> d33SF3List
            = FXCollections.observableArrayList ();
    public ObservableList<String> dataHeadingList
            = FXCollections.observableArrayList ();
    public ObservableList<String> dataHeadFullList
            = FXCollections.observableArrayList ();
    public ObservableList<String> dataList
            = FXCollections.observableArrayList ();
    public ObservableList<String> dataTypeList
            = FXCollections.observableArrayList ();
    public ObservableList<String> dataUnitFullList
            = FXCollections.observableArrayList ();
    public ObservableList<String> dataUnitsList
            = FXCollections.observableArrayList ();
    public ObservableList<String> detectorList
            = FXCollections.observableArrayList ();
    public ObservableList<String> facilList
            = FXCollections.observableArrayList ();
    public ObservableList<String> incPList
            = FXCollections.observableArrayList ();
    public ObservableList<String> incSrcList
            = FXCollections.observableArrayList ();
    public ObservableList<String> instList
            = FXCollections.observableArrayList ();
    public ObservableList<String> jourList
            = FXCollections.observableArrayList ();
    public ObservableList<String> jTypeList
            = FXCollections.observableArrayList ();
    public ObservableList<String> methodList
            = FXCollections.observableArrayList ();
    public ObservableList<String> mixedSF1List
            = FXCollections.observableArrayList ();
    public ObservableList<String> mixedSF2List
            = FXCollections.observableArrayList ();
    public ObservableList<String> mixedSF3List
            = FXCollections.observableArrayList ();
    public ObservableList<String> mixedSF4List
            = FXCollections.observableArrayList ();
    public ObservableList<String> modifierList
            = FXCollections.observableArrayList ();
    public ObservableList<String> monitRefList
            = FXCollections.observableArrayList ();
    public ObservableList<String> paramSF6List
            = FXCollections.observableArrayList ();
    public ObservableList<String> paramSF7List
            = FXCollections.observableArrayList ();
    public ObservableList<String> procList
            = FXCollections.observableArrayList ();
    //public ObservableList<String> prodListFull            = FXCollections.observableArrayList ();
    public ObservableList<String> prodList
            = FXCollections.observableArrayList ();
    public ObservableList<String> reportList
            = FXCollections.observableArrayList ();
    public ObservableList<String> resultsList
            = FXCollections.observableArrayList ();
    public ObservableList<String> statusList
            = FXCollections.observableArrayList ();
    public ObservableList<String> targetNList
            = FXCollections.observableArrayList ();
    public ObservableList<String> THalfList
            = FXCollections.observableArrayList ();
    public ObservableList<String> UnitFamilyList
            = FXCollections.observableArrayList ();

    public static void libList(BufferedWriter brW) {
        libList lList = new libList ();
        lList.loadAllDict (brW);
    }

    /*
     * Load all the dictionary into associated lists @author Abhijit
     * Bhattacharyya
     */
    public void loadDictsHere(ObservableList<String> list, String tst,
            String fName, int col, BufferedWriter brW, boolean flag) {
        BufferedReader br;
        int i = 0;
;
        dictName = DICTPathDir + "/" + fName;
        try {
            InputStream inDictStr = getClass ().getResourceAsStream (dictName);
            if ( inDictStr == null ) {
                System.out.println ("Error: File " + fName + " not found.....");
                System.exit (89);
            }
            inDictStrReader = new InputStreamReader (inDictStr);
            br = new BufferedReader (inDictStrReader);
            String line;
            String l1 = "";
            String delims = "[ ]+";
            String l2 = "";
            String l3 = "";
            String l4 = "";
            String temp = "";
            String temp1 = "";
            String lx = "";
            int j = 0;
            String FNM = fName.substring (14);
            FNM = FNM.substring (0, FNM.indexOf ("_"));
            while ((line = br.readLine ()) != null) {
                if ( (FNM.contains ("144")) ) {
                    line = line.substring (col);
                    l1 = line.substring (0, 11);
                    l2 = line.substring (31);
                    if ( exforUtil.isAlpha (l1.substring (0, 2)) ) {
                        lx = l1 + " " + l2;
                        list.add (lx);
                        temp = l1;
                        temp1 = l2.substring (0, 4);
                    } else {
                        l1 = temp;
                        l3 = temp1;
                        if ( exforUtil.isNumeric (l2.substring (4, 5)) ) {
                            lx = l1 + " " +
                                    temp1 + "   " + l2.substring (4);
                            list.add (lx);
                        }
                    }
                }
                if ( line.charAt (5) != ' ' ) {
                    if ( fName.contains ("JTYPE") ) {
                        line = line.substring (col - 1);
                    } else {
                        line = line.substring (col);
                    }
                    switch (FNM) {
                        case "3":
                            l1 = line.substring (0, line.indexOf (" "));
                            l2 = line.substring (38);
                            lx = l1 + "  " + l2;
                            break;
                        case "4":
                            l1 = line.substring (0, 2);
                            temp = line.substring (31);
                            l2 = temp.substring (0, 5);
                            l3 = temp.substring (8);
                            lx = l1 + "  " + l2 + " " + l3;
                            break;
                        case "5":
                            l1 = line.substring (0, 6);
                            l2 = line.substring (35, 39);
                            l3 = line.substring (42);
                            lx = l1 + "  " + l2 + " " + l3;
                            break;
                        case "6":
                            l1 = line.substring (0, 11);
                            l2 = line.substring (35, 38);
                            l3 = line.substring (38);
                            lx = l1 + "  " + l2 + " " + l3;
                            break;
                        case "7":
                        //case "144":
                        case "207":
                            l1 = line.substring (0, 11);
                            l2 = line.substring (31, 84);
                            lx = l1 + "  " + l2;
                            break;
                        case "16":
                        case "18":
                        case "19":
                        case "20":
                        case "21":
                        case "22":
                        case "23":
                        case "37":
                            l1 = line.substring (0, 6);
                            l2 = line.substring (31, 84);
                            lx = l1 + "  " + l2;
                            break;
                        case "24":
                            if ( !flag ) {
                                l1 = line.substring (0, 11);
                                l2 = line.substring (43 - col, 44 - col);
                                l3 = line.substring (45);
                                lx = l1 + " " + l2 + " " + l3;
                            } else {
                                lx = line;
                            }
                            break;
                        case "25":
                            if ( !flag ) {
                                l1 = line.substring (0, 11);
                                l2 = line.substring (31, 65);
                                lx = l1 + " " + l2;
                            } else {
                                lx = line;
                            }
                            break;
                        case "26":
                            lx = line;
                            break;
                        case "30":
                        case "32":
                            l1 = line.substring (0, 4);
                            l2 = line.substring (41, 96);
                            lx = l1 + "  " + l2;
                            break;
                        case "33":
                            if ( line.substring (43, 44).contains ("2") ) { // checking SF2 flag
                                l1 = line.substring (0, 6);
                                l2 = line.substring (40, 96);
                                lx = l1 + "  " + l2;
                                d33SF2List.add (lx);
                            }
                            if ( line.substring (43, 44).contains ("3") ) { // checking SF3 flag
                                l1 = line.substring (0, 6);
                                l2 = line.substring (40, 96);
                                lx = l1 + "  " + l2;
                                d33SF3List.add (lx);
                            }
                            break;
                        case "31":
                        case "34":
                            l1 = line.substring (0, 6);
                            l2 = line.substring (46, 96);
                            lx = l1 + "  " + l2;
                            break;
                        case "35":
                            l1 = line.substring (0, 6);
                            l2 = line.substring (41, 96);
                            lx = l1 + "  " + l2;
                            break;
                        case "209":
                            l1 = line.substring (0, 11);
                            l2 = line.substring (31, 37);
                            l3 = line.substring (71, 96);
                            lx = l1 + " " + l2 + " " + l3;
                            break;
                        case "227":
                            temp = line.substring (52, 62);
                            temp = temp.substring (0, temp.indexOf (" "));
                            temp = (exforUtil.isNumeric (temp)) ? temp : "";
                            l1 = line.substring (0, 12);
                            l2 = line.substring (31, 37);
                            lx = l1 + "  " + l2 + "  " + temp;
                            break;
                        case "236":
                            l1 = line.substring (0, line.indexOf (" "));
                            l2 = line.substring (31, 40);
                            //lx = l1 + " " + l2;
                            lx = line.substring (0, 55).trim ();
                            //System.out.println(lx+"<--->"+line.substring (39,40));
                            //System.out.println (lx);
      //                      prodListFull.add (line);
                            break;
                    }
                    list.add (lx);
                }
            }
            br.close ();
            inDictStrReader.close ();
            inDictStr.close ();
            // System.out.println ("Loading of " + fName + " is SUCCESSFUL...");
            brW.write ("Loading of " + dictName + " is SUCCESSFUL...\n");
            isLoaded = true;
        } catch (IOException e) {
            System.out.println ("Loading of " + dictName + " his FAILED...");
            try {
                brW.write ("Loading of " + dictName + " his FAILED...\n");
                brW.write ("\t\t\t\t EXITING...");

            } catch (IOException ex) {
                Logger.getLogger (MainScreenController.class
                        .getName ()).
                        log (Level.SEVERE, null, ex);
            }
            System.out.println ("\t\t\t\t EXITING...");
            System.exit (99);
        }
    }

    /*
     * loads the lists with dictionary @author Abhijit Bhattacharyya
     */
    public void loadAllDict(BufferedWriter brW) {
        boolean full = false;
        loadDictsHere (instList, "", instListName, 12, brW, full);                           // _3_ 
        loadDictsHere (jTypeList, "", jTypeListName, 12, brW, full);                    // _4_ from 11 
        loadDictsHere (jourList, "", jourListName, 12, brW, full);                           // _5_ 
        loadDictsHere (reportList, "", reportListName, 12, brW, full);                   // _6_ 
        loadDictsHere (confList, "", confListName, 12, brW, full);                           // _7_ 
        loadDictsHere (statusList, "", statusListName, 12, brW, full);                       // _16_
        loadDictsHere (facilList, "", facilityListName, 12, brW, full);                          // _18_ 
        loadDictsHere (incSrcList, "", incSrcListName, 12, brW, full);                       // _19_ 
        loadDictsHere (addlResultList, "", addlResultListName, 12, brW, full);           // _20_ 
        loadDictsHere (methodList, "", methodListName, 12, brW, full);                   // _21_ 
        loadDictsHere (detectorList, "", detectorListName, 12, brW, full);               // _22_ 
        loadDictsHere (analysisList, "", analysisListName, 12, brW, full);                   // _23_ 
        loadDictsHere (dataHeadingList, "", dataHeadingListName, 12, brW, full);     // _24_ 
        loadDictsHere (dataUnitsList, "", dataUnitsListName, 12, brW, full);             // _25_ 
        loadDictsHere (dataHeadFullList, "", dataHeadingListName, 12, brW, true);     // _24 FULL_ 
        loadDictsHere (dataUnitFullList, "", dataUnitsListName, 12, brW, true);             // _25 FULL_ 
        loadDictsHere (UnitFamilyList, "", UnitFamilyListName, 12, brW, full);             // _26_ 
        loadDictsHere (procList, "", procListName, 12, brW, full);                           // _30_ 
        loadDictsHere (branchList, "", branchListName, 12, brW, full);                    // _31_
        loadDictsHere (paramSF6List, "", paramSF6ListName, 12, brW, full);           // _32_
        loadDictsHere (incPList, "", incPListName, 12, brW, full);                           // _33_ 
        loadDictsHere (paramSF7List, "", incPListName, 12, brW, full);                   // _33_ 
        loadDictsHere (modifierList, "", modifierListName, 12, brW, full);               // _34_ 
        loadDictsHere (dataTypeList, "", dataTypeListName, 12, brW, full);           // _35_ 
        loadDictsHere (resultsList, "", resultsListName, 12, brW, full);                 // _37_ 
        loadDictsHere (dataList, "", dataListName, 12, brW, full);                   //_144_ 
        loadDictsHere (monitRefList, "", dataListName, 12, brW, full);
        loadDictsHere (bookList, "", bookListName, 12, brW, full);                       //_207_
        loadDictsHere (compoundList, "", compoundListName, 12, brW, full);      //_209_
        loadDictsHere (targetNList, "", targetNListName, 12, brW, full);             //_227_ 
        loadDictsHere (prodList, "", prodListName, 12, brW, full);                       //_236_
        
        doProcessList();
    }

    public void doProcessList() {
        // reaction related
        //                  SF1
        mixedSF1List.addAll (targetNList);          // d227 -> SF1
        mixedSF1List.addAll (compoundList);         // d209 -> SF1
        //                  SF2
        mixedSF2List.addAll (d33SF2List);    //incPList);             // d33  -> SF2  d33SF2L checks SF2 flag
        mixedSF2List.addAll (targetNList);          // d227 -> SF2
        //                  SF3
        mixedSF3List.addAll (procList);             // d30  -> SF3
        mixedSF3List.addAll (d33SF3List);    // incPList);             // d33  -> SF3 d33SF3List checks SF3 flag
        mixedSF3List.addAll (targetNList);          // d227 -> SF3
        //                  SF4
        mixedSF4List.addAll (prodList);             // d236 -> SF4
        mixedSF4List.addAll (incPList);             // d33  -> SF4
        mixedSF4List.addAll (targetNList);          // d227 -> SF4    
    }

    /*
     * This deletes elements of lists for releasing memories @author Abhijit
     * Bhattacharyya
     */
    public void doClearArrays() {
        instList = null;
        jourList = null;
        bookList = null;
        confList = null;
        dataList = null;
        reportList = null;
        facilList = null;
        jTypeList = null;
        incSrcList = null;
        addlResultList = null;
        methodList = null;
        analysisList = null;
        statusList = null;
        detectorList = null;
        targetNList = null;
        compoundList = null;
        compoundNucArr = null;
        mixedSF1List = null;
        mixedSF2List = null;
        incPList = null;
        procList = null;
        mixedSF3List = null;
        prodList = null;
        mixedSF4List = null;
        branchList = null;
        paramSF6List = null;
        paramSF7List = null;
        modifierList = null;
        dataTypeList = null;
        resultsList = null;
        System.gc ();
    }

}
