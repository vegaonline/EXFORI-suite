/*
 * EXFOR-I -> Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * FXML Controller class
 *
 * @author Abhijit Bhattacharyya
 */
public class MainScreenController { //implements Initializable {

    // from other class
    libList lList = new libList ();
    

    //  file 
    JFileChooser fileChooser = new JFileChooser ();
    private File exfFileWrite;
    private File exfFileRead;
    private File recordsDir;
    public BufferedWriter brW;
    // private InputStream inDictStr;
    private InputStreamReader inDictStrReader;

    private FileFilter filter;
    private String logFileName;
    private String DICTPathDir = "dict";
    private String EXFPathDir = "EXFOR_Compiled_Files";
    private String cssDir = "CSS";
    private String checkerDir = "checker";
    private String rootDir = null;
    private String cssName = null;
    private String fName = null;
    private String dictName;
    private String osName = "";
    private String winStr = "Windows";
    private String linStr = "Linux";
    private String pathSEP = "";
    private String zchexCmd = "";
    private String zchexPath = "";
    private String zchexName = "";

    //stage etc.
    private Group rootGr = new Group ();
    private TabPane myTabPane = new TabPane ();
    private Tab tab1 = new Tab ();
    private Tab tab2 = new Tab ();
    private boolean tab1B = false;
    private boolean tab2B = false;
    private boolean tabSelected = false;
    private BorderPane winPane = new BorderPane ();
    private BorderPane winPane1 = new BorderPane ();
    private BorderPane winPane2 = new BorderPane ();
    private Stage myStage;
    private Scene myRootScene;
    final Stage myDialog1 = new Stage ();
    final Stage myDialog = new Stage ();
    private Scene myDialogScene;
    Process p;
    boolean txtEntered;
    boolean entTextEntered = false;

    // Variables etc
    private int setB1Count = 0;
    public String entryNum = "";
    private Boolean fNameSet = false;
    private Boolean BLoadOldFile = false;
    private String bibI = null;
    private String bibCont = null;
    private int lineN = 1;
    private String thisEntryLine;
    private int lastLine = 0;
    private int subentNum = 0;
    private int myDataN = 0;
    private int totCheck = 0;
    private int bibEntNum = 0;
    private int multiLine = 0;
    private int reacLabel = 1;
    private String[] reacStrLen;
    private String demoStr0 = "";
    private String demoStr1 = "";
    private String tmp1 = "";
    private String tmp2 = "";
    private String tmp3 = "";
    private String tmp4 = "";
    private String subEnt = "";
    private boolean addLReac = false;
    private String[] sComp;
    private int addLReacCnt = 0;
    private ArrayList<String> sub4Common = new ArrayList<String> ();
    private int numR = 1;
    private int numC = 1;
    private int maxW = 130;
    private int r1 = 0;
    private int r2 = 0;
    private boolean r1B = false;
    private boolean r2B = false;
    private boolean numColStatus = false;
    private int maxRowNum = 0;
    private int lineCount = 1;
    private boolean addRCBB = false;
    private int rowCOMDataCount = 0;
    private int colCOMCount = 0;
    private int rowCOMtotalCount = 0;
    private boolean isBIB = false;
    private int BIBN1 = 0;
    private int BIBN2 = 0;
    private boolean isCOM = false;
    private int COMN1 = 0;
    private int tmpINT = 0;
    private int COMN2 = 0;
    private int subENTNum = 0;
    private boolean bOrdered = false;
    private int cutPoint = 54;
    private boolean isData = false;
    private int DATN1 = 0;
    private int DATN2 = 0;
    private boolean isOrdered = false;
    private int lastRowVal = 0;
    private boolean append;
    private int theNDX = 0;
    private boolean isSelect = false;

    private ScrollPane sp1 = new ScrollPane ();
    private HBox masterHB = new HBox ();
    private VBox masterVB1 = new VBox ();
    private VBox masterVB2 = new VBox ();

    private VBox vbContMenu = new VBox (10);
    private VBox VBsub01 = new VBox ();
    private VBox VBsub02 = new VBox ();

    Tooltip t1 = new Tooltip ("Enter ENTRY number in box and press SET");
    Tooltip t2 = new Tooltip ("Entering ENTRYNUM will automatically set date");
    Tooltip t3 = new Tooltip ("Click RightButton to see menu.");

    private Tooltip tp1 = new Tooltip ();
    private Tooltip tp2 = new Tooltip ();
    private Tooltip tp3 = new Tooltip ();
    private Tooltip tp4 = new Tooltip ();
    private Tooltip tp5 = new Tooltip ();
    private Tooltip tp6 = new Tooltip ();
    private Tooltip tp7 = new Tooltip ();
    private Tooltip tp8 = new Tooltip ();
    private Tooltip tp9 = new Tooltip ();
    private Tooltip tp10 = new Tooltip ();

    private String titleTip
            = "Here you may add Title of the Article as free text.  \n Please press accept to enter data \n else press Cancel to reject entry. ";
    private String authorTip
            = "Add Author(list). Blanks between names are permitted after comma \n while not premitted following initals.\n  \"And\" must not be followed by comma. For a big group, the first author followed by comma \n followed by the last author with \'+\' followed by comma and Group name may be provided.  ";
    String sampleTip = "Add sample.";
    String historyTip
            = "Here you may change History specifying the compiler name.";
    String commentTip = "Here you may enter comment as free text.";
    String errAnalysTip = "";
    String corrTip = "";
    String instTip
            = "Enter name of the institute. Try search by typing institute name and scroll to choose. Backspace will delete search string. Then select to enter in the window and press enter to add in the editor.FF";
    String incSrcTip
            = "Enter incident source type. Try search by typing institute name and scroll to choose. Backspace will delete search string. Then select to enter in the window and press enter to add in the editor.FF";
    String nucDDTip
            = " Select Nuclide. One may search by typing alphabets or using backspace to delete.";
    String halfLifeTip
            = " Check Half Life and also unit";
    String radSF1Tip
            = " Select type of radiation for SF1.. One may search by typing alphabets or using backspace to delete.";
    String radSF2Tip
            = " Select Energy for SF2. ";
    String radSF3Tip
            = " Select abundance of observed radiation per decay for SF3.";

    String SF1RTip = " Select Target Nucleus";
    String SF2RTip = " Select Incident Particle.";
    String SF3RTip = " Select Process.";
    String SF4RTip = " Select reaction Product.";
    String SF5RTip = " Select Branch.";
    String SF6RTip = " Select Parameter.";
    String SF7RTip = " Select Particle Considered.";
    String SF8RTip = " Select Modifier.";
    String SF9RTip = " Select Data Type.";
    String SF3Add = "Please press Add after selecting SF3.";

    //final ContextMenu cMenu = new ContextMenu();
    // final ContextMenu addNew = new ContextMenu();
    final ContextMenu chkMenuEdit = new ContextMenu ();
    final MenuItem cEdit = new MenuItem ("Edit");
    final MenuItem cDelete = new MenuItem ("Delete");
    final MenuItem titleM = new MenuItem ("Title");
    boolean bTitle = false;
    final MenuItem authM = new MenuItem ("Author");
    boolean bAuth = false;
    final MenuItem instM = new MenuItem ("Institute");
    boolean bInst = false;
    final MenuItem refM = new MenuItem ("Reference");
    boolean bRef = false;
    final MenuItem facM = new MenuItem ("Facility");
    boolean bFac = false;
    final MenuItem sampM = new MenuItem ("Sample");
    boolean bSamp = false;
    final MenuItem incSM = new MenuItem ("Inc-Source");
    boolean bIncs = false;
    final MenuItem detM = new MenuItem ("Detector");
    boolean bDet = false;
    final MenuItem monitM = new MenuItem ("Monitor");
    boolean bMonit = false;
    final MenuItem monitRefM = new MenuItem ("Monit-Ref");
    boolean bMonitRef = false;
    final MenuItem reacM = new MenuItem ("Reaction");
    boolean bReac = false;
    final MenuItem methodM = new MenuItem ("Method");
    boolean bMethod = false;
    final MenuItem analysM = new MenuItem ("Analysis");
    boolean bAnalys = false;
    final MenuItem histM = new MenuItem ("History");
    boolean bHist = false;
    final MenuItem commentM = new MenuItem ("Comment");
    boolean bComm = false;
    final MenuItem statusM = new MenuItem ("Status");
    boolean bStatus = false;
    final MenuItem errAnalysM = new MenuItem ("Err-Analys");
    boolean bErrAnalys = false;
    final MenuItem addlResultM = new MenuItem ("Addl-Result");
    boolean bAddResult = false;
    final MenuItem commActiveM = new MenuItem ("Common Activate");
    boolean bComActive = false;
    final MenuItem commEditM = new MenuItem ("Common Edit");
    boolean bComEdit = false;
    final MenuItem decayDataM = new MenuItem ("Decay Data");
    boolean bdData = false;
    final MenuItem decayMonM = new MenuItem ("Decay-Mon");
    boolean bdMon = false;
    final MenuItem correctM = new MenuItem ("Correction");
    boolean bCorrect = false;
    final MenuItem dataM = new MenuItem ("Data");
    boolean bData = false;
    final MenuItem flagM = new MenuItem ("Flag");
    boolean bFlag = false;

    final MenuItem newSUBM = new MenuItem ("New SUBENTRY");
    final Menu addM = new Menu ("Add Items");
    final Menu editM = new Menu ("Edit Contents");

    private Label entryL = new Label ("ENTRY: ");
    private TextField entryT = new TextField ();
    private Label numSUBENT = new Label ("Number of SUBENTRIES: ");
    private TextField numSub = new TextField ();

    private Label subENT1T = new Label ("SUBENTRY 1");
    private Label bibSecT = new Label ("BIB Section");
    private Label titleL = new Label ("Title");
    private CheckBox titleC = new CheckBox ();
    int titleCI = 0;
    private Label authL = new Label ("Author");
    private CheckBox authC = new CheckBox ();
    int authCI = 0;
    private Label instL = new Label ("Institute");
    private CheckBox instC = new CheckBox ();
    int instCI = 0;
    private Label refL = new Label ("Reference");
    private CheckBox refC = new CheckBox ();
    int refCI = 0;
    private Label faciL = new Label ("Facility");
    private CheckBox faciC = new CheckBox ();
    int faciCI = 0;
    private Label sampL = new Label ("Sample");
    private CheckBox sampC = new CheckBox ();
    int sampCI = 0;
    private Label detL = new Label ("Detector");
    private CheckBox detC = new CheckBox ();
    int detCI = 0;
    private Label methL = new Label ("Method");
    private CheckBox methC = new CheckBox ();
    int methCI = 0;
    private Label monitL = new Label ("Monitor");
    private CheckBox monitC = new CheckBox ();
    int monitCI = 0;
    private Label incSrcL = new Label ("INC-SOURCE");
    private CheckBox incSrcC = new CheckBox ();
    int incSrcCI = 0;
    private Label analL = new Label ("Analysis");
    private CheckBox analC = new CheckBox ();
    int analCI = 0;
    private Label errAnalL = new Label ("ERR-ANALYSIS");
    private CheckBox errAnalC = new CheckBox ();
    int errAnalCI = 0;
    private Label partDetL = new Label ("PART-DET");
    private CheckBox partDetC = new CheckBox ();
    int partDetCI = 0;
    private Label halfLL = new Label ("Half-Life");
    private CheckBox halfLC = new CheckBox ();
    int halfLCI = 0;
    private Label commentL = new Label ("Comment");
    private CheckBox commentC = new CheckBox ();
    int commentCI = 0;
    private Label commActivL = new Label ("Common Active: ");
    private CheckBox commActC = new CheckBox ();
    int commActCI = 0;
    private Label dataL = new Label ("DATA Active: ");
    private CheckBox dataC = new CheckBox ();
    int dataCI = 0;
    private Label reacL = new Label ("Reaction");
    private CheckBox reacC = new CheckBox ();
    int reacCI = 0;
    private Label flagL = new Label ("Flag");
    private CheckBox flagC = new CheckBox ();
    int flagCI = 0;
    private Label statusL = new Label ();
    private CheckBox statusC = new CheckBox ();
    int statusCI = 0;
    private Label addresL = new Label ("Add-Residue");
    private CheckBox addresC = new CheckBox ();
    int addresCI = 0;
    private Label corrL = new Label ("Correction");
    private CheckBox corrC = new CheckBox ();
    int corrCI = 0;
    private Label subENT2T = new Label ("SUBENTRY 2");
    private Label monitRefL = new Label ("MONIT-REF");
    private Label headingL = new Label ("Heading");
    private Label subAccL = new Label ("SubAccn:");

    private CheckBox headingC = new CheckBox ();
    private boolean headingCB = false;
    int headingNum = 0;

    private CheckBox DDFlagC = new CheckBox ();
    private boolean DDFlagB = false;
    private int DDFlagNum = 0;

    private Button createB = new Button ("Create");
    private Button cancelB = new Button ("Cancel");
    private Button acceptEdit = new Button ("Accept");
    private Button cancelEdit = new Button ("Cancel");
    private Button selectIt = new Button ("Select");
    private Button selectRow = new Button ("Select");
    private Button addL = new Button ("Add record");
    private Button addRow = new Button ("Add Row");
    private Button addCol = new Button ("Add Column");
    private Button DelRow = new Button ("Delete Row");
    private Button DelCol = new Button ("Delete Column");
    private Button addRCB = new Button ("Add");
    private Button chngRCB = new Button ("Change");
    private Button chngCol = new Button ("Change");
    private Button okButton = new Button ("OK");

    private TextField volTxt = new TextField ();
    private TextField volMTxt = new TextField ();
    private TextField pgTxt = new TextField ();
    private TextField pgMTxt = new TextField ();
    private TextField yrTxt = new TextField ();
    private TextField yrMTxt = new TextField ();
    private TextField MMTxt = new TextField ();
    private TextField reptNumTxt = new TextField ();
    private TextField numRow = new TextField ();
    private TextField numRow1 = new TextField ();
    private TextField numRow2 = new TextField ();
    private TextField numCol = new TextField ();
    private TextField numCol1 = new TextField ();
    private TextField numCol2 = new TextField ();
    private TextField TC1 = new TextField ();
    private TextField TC2 = new TextField ();
    private TextField TC3 = new TextField ();
    private TextField TC4 = new TextField ();
    private TextField TC5 = new TextField ();
    private TextField TC6 = new TextField ();
    private TextField subAcc = new TextField ();
    private TextField authT = new TextField ();
    private TextField THalfDDT = new TextField ();
    private TextField SF2EnergyDDT = new TextField ();
    private TextField SF3abandDDT = new TextField ();

    private Label sf1L = new Label ("Target (SF1)");
    private Label sf2L = new Label ("Inc. Particle (SF2)");
    private Label sf3L = new Label ("Process (SF3)");
    private Label sf4L = new Label ("Product (SF4)");
    private Label sf5L = new Label ("Branch (SF5)");
    private Label sf6L = new Label ("Parameter (SF6)");
    private Label sf7L = new Label ("Particle (SF7)");
    private Label sf8L = new Label ("Modifier (SF8)");
    private Label sf9L = new Label ("Data Type (SF9)");
    private Label jtypeL = new Label ("Type of reference:");
    private Label refSrcL = new Label (
            "Journals / Books / Conferences / Reports");
    private Label volL = new Label ("Volume");
    private Label volML = new Label ("Version");
    private Label pgL = new Label ("Page");
    private Label matML = new Label ("Material");
    private Label yrL = new Label ("Year (yyyy)");
    private Label yrML = new Label ("Date");
    private Label reptL = new Label ("Report \nNumber");
    private Label reptMML = new Label ("Month");
    private Label incsrcL = new Label ("Incident sources");
    private Label detectorL = new Label ("Detectors");
    private Label compNucL = new Label ("Nuclide / Compound");
    private Label targetL = new Label ("Target nucleus (SF1)");
    private Label incidentL = new Label ("Incident particle (SF2)");
    private Label procL = new Label ("Process (SF3)");
    private Label productL = new Label ("Reaction product (SF4)");
    private Label qtyBranchL = new Label ("Quantity : Branch (SF5)");
    private Label qtyParamL = new Label ("Quantity : Parameter (SF6)");
    private Label qtyParticleL = new Label ("Quantity : Particle (SF7)");
    private Label qtyModifierL = new Label ("Quantity : Modifier (SF8)");
    private Label qtyDTypeL = new Label ("Quantity : Data Types (SF9)");
    private Label addRCL = new Label ("Add ");
    private Label delRCL = new Label ("Delete ");
    private Label numRowL = new Label (" Rows ");
    private Label numColL = new Label (" Columns ");
    private Label numNumL = new Label ("Number of ");
    private Label nuclL = new Label ("Nuclide");
    private Label HalfL = new Label ("Half Life");
    private Label SF1DDL = new Label ("SF1");
    private Label SF2DDL = new Label ("SF2");
    private Label SF3DDL = new Label ("SF3");
    private Label dataUnitL = new Label ("Units");

    private final Popup popup = new Popup ();

    private TextArea tf = new TextArea ();
    private String putTxt;
    private String putTxtType;

    private TableView<CommonDataClass> matrixData
            = new TableView<CommonDataClass> ();
    private TableColumn[] matrixDataColumns = new TableColumn[6];
    private TableColumn matCol = new TableColumn<> ();
    private final ObservableList<CommonDataClass> matData = FXCollections.
            observableArrayList ();

    private int matrixDataMaxColSize = 6;
    private int matrixDataColSize = 0;
    private int columnIndex = 0;

    String selectionStr = "Please select:";

    private final ObservableList<BIBClasses> bibList = FXCollections.
            observableArrayList ();
    private final ObservableList<BIBClasses> bibSorted = FXCollections.
            observableArrayList (bibList);
    private final ObservableList<lineList> headList = FXCollections.
            observableArrayList ();
    private final ObservableList<String> FixedHeadList = FXCollections.
            observableArrayList ();
    private ObservableList<editableData> myData = FXCollections.
            observableArrayList ();

    private final ComboBox<String> institute = new ComboBox<> ();
    private final ComboBox<String> journals = new ComboBox<> ();
    private final ComboBox<String> jType = new ComboBox<> ();
    private final ComboBox<String> facilCB = new ComboBox<> ();
    private final ComboBox<String> incSrcCB = new ComboBox<> ();
    private final ComboBox<String> detectorCB = new ComboBox<> ();
    private final ComboBox<String> compNucCB = new ComboBox<> ();
    private final ComboBox<String> targetNCB = new ComboBox<> ();
    private final ComboBox<String> compoundCB = new ComboBox<> ();
    private final ComboBox<String> incPCB = new ComboBox<> ();
    private final ComboBox<String> procCB = new ComboBox<> ();
    private final ComboBox<String> prodCB = new ComboBox<> ();
    private final ComboBox<String> branchCB = new ComboBox<> ();
    private final ComboBox<String> paramSF6CB = new ComboBox<> ();
    private final ComboBox<String> paramSF7CB = new ComboBox<> ();
    private final ComboBox<String> modifierCB = new ComboBox<> ();
    private final ComboBox<String> dataTypeCB = new ComboBox<> ();
    private final ComboBox<String> methodCB = new ComboBox<> ();
    private final ComboBox<String> addlResultCB = new ComboBox<> ();
    private final ComboBox<String> statusCB = new ComboBox<> ();
    private final ComboBox<String> dataHeadingCB = new ComboBox<> ();
    private final ComboBox<String> dataUnitsCB = new ComboBox<> ();
    private final ComboBox<String> monitRefCB = new ComboBox<> ();
    private final ComboBox<String> nuclideDDCB = new ComboBox<> ();
    private final ComboBox<String> radSF1DDCB = new ComboBox<> ();
    private final ComboBox<String> dHCB1 = new ComboBox<> ();
    private final ComboBox<String> dHCB2 = new ComboBox<> ();
    private final ComboBox<String> dHCB3 = new ComboBox<> ();
    private final ComboBox<String> dHCB4 = new ComboBox<> ();
    private final ComboBox<String> dHCB5 = new ComboBox<> ();
    private final ComboBox<String> dHCB6 = new ComboBox<> ();
    private final ComboBox<String> dUCB1 = new ComboBox<> ();
    private final ComboBox<String> dUCB2 = new ComboBox<> ();
    private final ComboBox<String> dUCB3 = new ComboBox<> ();
    private final ComboBox<String> dUCB4 = new ComboBox<> ();
    private final ComboBox<String> dUCB5 = new ComboBox<> ();
    private final ComboBox<String> dUCB6 = new ComboBox<> ();

    // Tree view related
    private TreeItem<String> rootNode;
    private TreeItem<String> subEntLeaf;
    private TreeItem<String> bibTag;
    private TreeItem<String> bibItems;
    private TreeItem<String> endBibTag;
    private TreeItem<String> endSUBTag;

    private final Node rootIcon
            = new ImageView (new Image (getClass ().getResourceAsStream (
                    "root.png")));

    // Other widgets from Java
    public String myDate0 = new SimpleDateFormat ("yyyyMMdd").format (Calendar.
            getInstance ().getTime ());
    public String myDateOldFile = "";
    public DropShadow shadow = new DropShadow ();

    private AnchorPane myPane;

//FXML stuffs
    @FXML
    private BorderPane mainPane;
    @FXML
    private final Button setB1 = new Button ("SET");
    @FXML
    private final Button setB2 = new Button ("SET");
    @FXML
    private Button doCheck;
    @FXML
    private Button doOrder;
    @FXML
    private TextField entNumT;
    @FXML
    private TextField entDateT;
    @FXML
    private TreeView<String> subEntTree;
    @FXML
    private MenuItem doOpenExf;
    @FXML
    private MenuItem doSaveExf;
    @FXML
    private MenuItem doSaveAsExf;
    @FXML
    private MenuItem doOrdering;

    @FXML
    private TableView<editableData> editTable = new TableView ();
    @FXML
    private TableColumn<editableData, String> bibHead;
    @FXML
    private TableColumn<editableData, String> bibPtr;
    @FXML
    private TableColumn<editableData, String> bibText;
    @FXML
    private TableColumn<editableData, String> bibLines;
    @FXML
    private MenuItem CheXing;
    @FXML
    private MenuItem Janising;
    @FXML
    private MenuItem selfCheck;

    // FXML based functions
    /*
     * This method helps to order the edited file for use with checx @author
     * Abhijit Bhattacharyya
     */
    @FXML
    private void doProcOrder(ActionEvent event) {
        doProcSE (0);
        bOrdered = true;
    }

    private int doProcSE(int linia) {
        String str1 = "";
        String str2 = "";
        String str3 = "";
        String BIBN1S = "";
        String BIBN2S = "";
        String COMN1S = "";
        String COMN2S = "";
        String DATN1S = "";
        String DATN2S = "";
        int lastEntVal = 0;
        int lineC = 1;
        int subC = 1;
        int tmpI = 0;
        int line = 0;

        if ( entryNum.isEmpty () ) {
            popupMsg.warnBox ("EntryNum has not been set. Exiting.....",
                    "Entry number ?");
            return -9;  //  System.exit (1);
        } else {
            String temp = "";
            str1 = entryNum;

            headStyle ();
            if ( !isOrdered ) {
                lastEntVal = myDataN + 1;
            } else {
                lastEntVal = myDataN;
            }

            for ( int ii = 0; ii < lastEntVal; ii++ ) {
                String s1 = myData.get (ii).getBibItemName ().toString ();
                String s2 = myData.get (ii).getPointerID ().toString ();
                String s3 = myData.get (ii).getContentTxt ().toString ();

                temp = s1;
                if ( temp.contains ("ENTRY") && !temp.contains ("END") ) {
                    str2 = exforUtil.fixStr0s ("0", 3);
                    str3 = exforUtil.fixStr0s ("1", 5);
                } else if ( temp.contains ("ENDSUBENT") ) {
                    str3 = procOutput ("99999", 5);
                    ++subC;
                    lineC = 1;
                } else if ( temp.contains ("ENDENTRY") ) {
                    subC = 999;
                    lineC = 99999;
                    //str2 = fixString11(String.valueOf(subC), 3); 
                    str2 = procOutput (Integer.toString (subC), 3);  // fixStr0s(String.valueOf(subC), 3);
                    str3 = procOutput (Integer.toString (lineC), 5); // fixStr0s(String.valueOf(lineC), 5);
                } else {
                    str2 = exforUtil.fixStr0s (Integer.toString (subC), 3); //procOutput(Integer.toString(subC), 3);
                    tmpI = lineC++;
                    str3 = exforUtil.fixStr0s (Integer.toString (tmpI), 5); // procOutput(Integer.toString(tmpI), 5);
                }

                if ( s1.contains ("BIB") && (linia == 0) && !isBIB ) {
                    setBIBRecNum (ii);
                    if ( isBIB ) {
                        BIBN1S = exforUtil.
                                fixString11 (Integer.toString (BIBN1));
                        BIBN2S = exforUtil.
                                fixString11 (Integer.toString (BIBN2));
                        s3 = BIBN1S + BIBN2S;
                    }
                }

                if ( s1.contains ("ENDBIB") ) {
                    s3 = BIBN2S + exforUtil.fixString11 ("0");
                    BIBN1 = 0;
                    BIBN2 = 0;
                    isBIB = false;
                }

                if ( s1.contains ("COMMON") && (linia == 0) && !isCOM ) {
                    setCommonRecNum (ii);
                    if ( isCOM ) {
                        COMN1S = exforUtil.
                                fixString11 (Integer.toString (COMN1));
                        COMN2S = exforUtil.
                                fixString11 (Integer.toString (COMN2));
                        s3 = COMN1S + COMN2S;
                    }
                }

                if ( s1.contains ("DATA") && (linia == 0) && !isData &&
                        !s1.contains ("DECAY") ) {
                    setDataRecNum (ii);
                    if ( isData ) {
                        DATN1S = exforUtil.
                                fixString11 (Integer.toString (DATN1));
                        DATN2S = exforUtil.
                                fixString11 (Integer.toString (DATN2));
                        s3 = DATN1S + DATN2S;
                    }
                }

                if ( s1.contains ("ENDCOMMON") && (linia == 0) ) {
                    s3 = COMN2S + exforUtil.fixString11 ("0");
                    COMN1 = 0;
                    COMN2 = 0;
                    isCOM = false;
                }

                if ( s1.contains ("ENDDATA") && (linia == 0) ) {
                    s3 = DATN2S + exforUtil.fixString11 ("0");
                    isData = false;
                }

                if ( s1.contains ("NOCOMMON") && (linia == 0) ) {
                    s3 = exforUtil.fixString11 ("0") + exforUtil.fixString11 (
                            "0");
                }

                if ( s1.contains ("SUBENT") && !s1.contains ("END") && (linia ==
                        0) ) {
                    // setSubentNum (ii);
                    String tmp = myData.get (ii - 1).getSubEntNum ().trim ();
                    tmp = tmp.substring (5, 8).trim ();
                    int fac = Integer.parseInt (tmp) + 1;
                    s3 = exforUtil.fixString11 (getSUBENT (fac)) + exforUtil.
                            fixString11 (myDate0);
                }

                if ( s1.contains ("ENDSUBENT") && (linia == 0) ) {
                    String tmp = myData.get (ii - 1).getSubEntNum ().trim ();
                    tmp = tmp.substring (8).trim ();
                    int fac = Integer.parseInt (tmp) - 1;
                    //int fac = subENTNum;
                    s3 = exforUtil.fixString11 (Integer.toString (fac)) +
                            exforUtil.fixString11 ("0");
                }

                if ( s1.matches ("ENDENTRY") && (linia == 0) ) {
                    String tmp = myData.get (ii - 1).getSubEntNum ().trim ();
                    tmp = tmp.substring (5, 8).trim ();
                    tmp = Integer.toString (Integer.parseInt (tmp)).trim ();
                    s3 = exforUtil.fixString11 (tmp) + exforUtil.fixString11 (
                            "0");
                }

                String s4 = str1 + str2 + str3 + " ";
                line = Integer.parseInt (str3.trim ()); // Integer.parseInt(str3.substring(str3.length() - 3));
                if ( linia == 0 ) {
                    myData.set (ii, new editableData (s1, s2, s3, s4));
                } else {
                    if ( s1.contains ("COMMON") ) {
                        line = 99999;
                    }
                    headList.add (new lineList (s1, line));
                }
                if ( s1.contains ("ENDENTRY") ) {
                    isOrdered = true;
                    break;
                }
            }
        }
        isOrdered = true;
        return line;
    }

    private String getLineNum(String myText, boolean isOld) {
        int lineNum = 0;
        if ( !isOld ) {
            String partA = myText.substring (0, 10).trim ();
            String partB = myText.substring (66).trim ();
            lineNum = (partA.contains ("COMMON")
                    ? 99999
                    : Integer.parseInt (partB.substring (8).trim ()));
            headList.add (new lineList (partA, lineNum));
        } else {
            String Head = myText;
            for ( lineList item : headList ) {
                if ( item.getHeading ().contains (Head) ) {
                    lineNum = item.getLine ();
                }
            }
        }
        return Integer.toString (lineNum);
    }

    private int doGetSELineNum(String Head) {
        int line = 0;
        //doProcSE(1);
        for ( lineList item : headList ) {
            if ( item.getHeading ().contains (Head) ) {
                line = item.getLine ();
            }
        }
        return line - 3; // -3  is used to make Title as 1
    }

    private void setBIBRecNum(int kk) {
        String temp1;
        BIBN1 = 0;
        BIBN2 = 0;
        for ( int jj = kk + 1; jj < myDataN; jj++ ) {
            temp1 = myData.get (jj).getBibItemName ();
            if ( !temp1.contains ("ENDBIB") && temp1.length () != 0 && !isBIB ) {
                ++BIBN1;
            } else if ( temp1.contains ("ENDBIB") ) {
                BIBN2 = jj - kk - 1;
                isBIB = true;
                break;
            }
        }
    }

    private void setDataRecNum(int kk) {
        String tmp1;
        String[] parts;
        int jj = kk + 3;
        DATN1 = 0;
        DATN2 = 0;
        tmp1 = myData.get (jj).getBibItemName () + " " + myData.get (jj).
                getContentTxt ();

        ++DATN2;
        parts = tmp1.split ("\\s+");
        DATN1 = parts.length;
        while (!isData && jj < (myDataN - 1)) {
            ++jj;
            tmp1 = myData.get (jj).getBibItemName ();
            ++DATN2;
            if ( tmp1.contains ("ENDDATA") ) {
                isData = true;
                break;
            }
        }
        --DATN1;
        --DATN2;
    }

    private void setCommonRecNum(int kk) {
        String tmp1;
        String[] parts;
        COMN1 = 0;
        COMN2 = 0;
        int jj = kk + 1;
        tmp1 = myData.get (jj).getBibItemName () + " " + myData.get (jj).
                getContentTxt ();
        ++COMN2;
        parts = tmp1.split ("\\s+");
        COMN1 = parts.length;
        while (!isCOM && jj < (myDataN - 1)) {
            ++jj;
            tmp1 = myData.get (jj).getBibItemName ();
            ++COMN2;
            if ( tmp1.contains ("ENDCOMMON") ) {
                isCOM = true;
                break;
            }
        }
        --COMN2;
    }

    private void setSubentNum(int kk) {
        int jj = kk + 1;
        subENTNum = 0;
        while (!myData.get (kk).getBibItemName ().contains ("ENDSUBENT")) {
            ++subENTNum;
            ++kk;
        }
    }

    /**
     * This is a self checking method which someday may compete with JANIS and
     * CHEX
     *
     * @param event
     */
    @FXML
    private void doSelfCheck(ActionEvent event) {
        if ( !entryNum.isEmpty () &&
                !fName.isEmpty () &&
                !myData.isEmpty () &&
                isOrdered ) {
            selfChecker.selfChecker (entryNum, fName, myData, brW);
        }
    }


    /*
     * This method quits the code erasing elements to all the lists so that
     * memory is released back to system without any leakage followed by Garbage
     * Collection jobs @author Abhijit Bhattacharyya
     */
    @FXML
    private void goQuit(ActionEvent event
    ) {
        lList.doClearArrays ();   //doClearArrays ();
        try {
            brW.close ();
            System.gc ();

        } catch (IOException ex) {
            Logger.getLogger (MainScreenController.class
                    .getName ()).
                    log (Level.SEVERE, null, ex);
        }
        System.exit (0);
    }

    /*
     * This code starts working setting the entrynum etc for creating new file
     * or loading an old file @author Abhijit Bhattacharyya
     */
    @FXML
    private void doSetEntry(MouseEvent event
    ) {
        String s1 = entNumT.getText ();
        String s11 = s1.substring (0, 1);
        String s12 = s1.substring (1, 2);
        String s13 = s1.substring (2, 3);

        // entTextEntered = true;
        if ( !s1.isEmpty () && s1.length () <= 5 && !exforUtil.isAlpha (s12) &&
                setB1Count < 1 ) {
            if ( s1.length () < 5 || s1.length () > 5 ) {
                popupMsg.
                        warnBox ("ENTRY Number WRONG Format", "Entry Number...");
                return;
            }
            entryNum = s1;

            s11 = (!exforUtil.isNumeric (s11)) ? s11.toUpperCase () : s11;
            entryNum = entryNum.substring (1);
            entryNum = s11 + entryNum;

             exforUtil.setDefaultDirExt (EXFPathDir);
            rootDir = fileChooser.getCurrentDirectory ().toString ();
            fName = rootDir + pathSEP + entryNum + ".exf";
            exfFileRead = new File (fName);

            if ( exfFileRead.exists () && !exfFileRead.isDirectory () ) {
                System.out.println ("Loading....." + fName);
                fNameSet = false;
                doLoadEXFFile ();
            } else {
                fNameSet = true;
                entDateT.setText (myDate0);
                ++setB1Count;
                doNewFile ();
            }
        } else if ( !exforUtil.isNumeric (s12) ) {
            popupMsg.warnBox (
                    "First character may be alphanumeric while second onwards must be numeric",
                    "Check Entry Number");
        } else {
            entryNum = "0000";
            fNameSet = false;
            fName = null;
            setB1Count = 0;

            if ( s1.length () < 5 || s1.length () > 5 ) {
                popupMsg.
                        warnBox ("ENTRY Number WRONG Format", "Entry Number...");
                return;
            } else {
                popupMsg.warnBox (
                        "EXFOR entry Number has not been selected. Please enter...",
                        "EXFOR entry not selected");
            }
        }
    }

    /*
     * This sets the date as per EXFOR format @author Abhijit Bhattacharyya
     */
    @FXML
    private void doSetDate(MouseEvent event
    ) {
        entDateT.setText (myDate0);
    }

    /*
     * This creates a new EXFOR file @author Abhijit Bhattacharyya
     */
    private void startNewExf(ActionEvent event
    ) {
        boolean isFileOK = false;
        exforUtil.setDefaultDirExt (EXFPathDir);
        rootDir = fileChooser.getCurrentDirectory ().toString ();
        fileChooser.setDialogTitle ("Load EXFOR compilation file.....");
        fileChooser.addChoosableFileFilter (filter);
        int ret = fileChooser.showDialog (null, "Open File");
        if ( ret == JFileChooser.APPROVE_OPTION ) {
            exfFileRead = fileChooser.getSelectedFile ();
            if ( exfFileRead != null ) {
                isFileOK = true;
            }
        }
        if ( ret == JFileChooser.CANCEL_OPTION ) {
            // dummy for do nothin option
        }

        //if ( exfFileRead.exists () ) {
        if ( isFileOK ) {
            if ( exfFileRead.getName ().toString ().substring (
                    exfFileRead.getName ().toString ().length () - 4,
                    exfFileRead.getName ().toString ().length ())
                    .toLowerCase ().contains (".exf") ) {
                fName = exfFileRead.getAbsolutePath ().toString ();
            } else {
                fName = exfFileRead.getAbsolutePath ().toString () + ".exf";
            }
        } else {
            fName = exfFileRead.getAbsolutePath ().toString () + ".exf";
            entryNum = exfFileRead.getName ().toString ();
        }

        if ( exfFileRead.exists () && !exfFileRead.isDirectory () ) {
            doLoadEXFFile ();
        } else {
            entNumT.setText (entryNum);
            entDateT.setText (myDate0);
            doNewFile ();
            ++setB1Count;
        }
    }

    /*
     * This loads an old EXFOR file @author Abhijit Bhattacharyya
     */
    @FXML
    private void doOpenExf(ActionEvent event
    ) {
        boolean isFileOK = false;
        exforUtil.setDefaultDirExt (EXFPathDir);
        rootDir = fileChooser.getCurrentDirectory ().toString ();
        while (exfFileRead == null) {
            fileChooser.setDialogTitle ("Load EXFOR compilation file.....");
            fileChooser.addChoosableFileFilter (filter);
            int ret = fileChooser.showDialog (null, "Open File");
            if ( ret == JFileChooser.APPROVE_OPTION ) {
                exfFileRead = fileChooser.getSelectedFile ();
                if ( exfFileRead != null ) {
                    isFileOK = true;
                }
            }
            if ( ret == JFileChooser.CANCEL_OPTION ) {
                break;
            }
        }
        if ( isFileOK ) {
            fName = exfFileRead.getAbsolutePath ().toString ();
            if ( exfFileRead.exists () && !exfFileRead.isDirectory () ) {
                doLoadEXFFile ();
            } else {
                entNumT.setText (entryNum);
                entDateT.setText (myDate0);
                doNewFile ();
                ++setB1Count;
            }
        }
    }

    /*
     * This saves an working EXFOR file @author Abhijit Bhattacharyya
     */
    @FXML
    private void doSaveExf(ActionEvent event
    ) {
        //doProcSE(0);
        exforUtil.setDefaultDirExt (EXFPathDir);
        trySaving (".exf");
    }

    @FXML
    private void doSaveAsExf(ActionEvent event) {
        //doProcSE(0);
        exforUtil.setDefaultDirExt (EXFPathDir);
        fileChooser.setDialogTitle ("save the EXFOR entry as:");
        int ret = fileChooser.showSaveDialog (fileChooser);
        if ( ret == JFileChooser.CANCEL_OPTION ) {
            // do nothing come out
        } else if ( ret == JFileChooser.APPROVE_OPTION ) {
            fName = fName.substring (0, fName.length () - 9);
            fName += fileChooser.getSelectedFile ();
            //trySaving (".exf");
        }
    }

    @FXML
    private void doRunChex(ActionEvent event) throws IOException {
        String cmd1;
        String cmd2;

        //doProcSE(0);
        trySaving (".x4");
        myDialog.setTitle ("Compiling  " + fName + " .... ");
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }

        VBox vb1 = new VBox ();
        tf.setPrefWidth (800);
        vb1.getChildren ().addAll (tf, okButton);
        myDialogScene = new Scene (vb1, 900, 250);
        myDialog.setScene (myDialogScene);
        myDialog.show ();

        exforUtil.setDefaultDirExt (zchexPath);
        cmd1 = zchexCmd;
        cmd2 = fName;
        String xType = fName.substring (fName.length () - 8, fName.
                length () - 7);
        String optTxt = (xType.contains ("D") || xType.contains ("d")) ? "D"
                : "T";
        String[] commands = {cmd1, cmd2, optTxt};
        try {
            ProcessBuilder proBuilder = new ProcessBuilder (commands);
            proBuilder.directory (new File (zchexPath));
            Process myProc = proBuilder.start ();  // Runtime.getRuntime().exec(cmd1 + " " + cmd2);
            InputStream is = myProc.getInputStream ();
            InputStreamReader isr = new InputStreamReader (is);
            BufferedReader br = new BufferedReader (isr);
            while (myProc.isAlive ()) {
                String thisLine;
                if ( (thisLine = br.readLine ()) != null ) {
                    Platform.runLater (new Runnable () {
                        @Override
                        public void run() {
                            tf.appendText (thisLine + "\n");
                            try {
                                brW.write (thisLine + "\n");
                            } catch (IOException ex) {
                                Logger.getLogger (MainScreenController.class.
                                        getName ()).log (Level.SEVERE, null, ex);
                            }
                        }
                    });
                }
            }
        } catch (IOException ex) {
            try {
                brW.write ("Could not compile using ZCHEX");
                Logger
                        .getLogger (MainScreenController.class
                                .getName ()).
                        log (Level.SEVERE, null, ex);

            } catch (IOException ex1) {
                Logger.getLogger (MainScreenController.class
                        .getName ()).
                        log (Level.SEVERE, null, ex1);
            }
        }

        zchexName = fName + ".err";
        tf.appendText (" ");
        tf.appendText ("=========================================");
        tf.appendText (" ");
        BufferedReader br1 = new BufferedReader (new FileReader (
                zchexName));
        String line;
        while ((line = br1.readLine ()) != null) {
            tf.appendText (line);
        }
        br1.close ();

        okButton.setOnAction (
                (ActionEvent event1) -> {
                    tf.clear ();
                    myDialog.close ();
                }
        );
    }

    // save file
    /**
     * This will try to save
     *
     * @param newExt
     */
    private void trySaving(String newExt) {
        String fName1;
        try {
            fName1 = fName.substring (0, fName.length () - 4);

            fName1 = (!newExt.isEmpty ()) ? fName1 + newExt : fName1 + ".x4";

            zchexName = fName1;
            File fileName = new File (fName1);
            FileWriter fw = new FileWriter (fileName);
            Writer output = new BufferedWriter (fw);

            for ( int i = 0; i < myDataN; i++ ) {
                String str1 = myData.get (i).getBibItemName ();
                String str2 = myData.get (i).getPointerID ();
                String str3 = myData.get (i).getContentTxt ();
                String str4 = myData.get (i).getSubEntNum ();

                str1 = procOutput (str1, 0);
                str2 = procOutput (str2, 1);
                str3 = procOutput (str3, 2);

                fw.write (str1 + str2 + str3 + str4 + "\n");
            }
            output.close ();
            fw.close ();
            System.gc ();

        } catch (IOException ex) {
            Logger.getLogger (MainScreenController.class
                    .getName ()).
                    log (Level.SEVERE, null, ex);
        }
    }

    /**
     * Janis Checker
     *
     * @param event
     */
    @FXML
    private void doRunJanis(ActionEvent event) {
        popupMsg.warnBox ("", titleTip);
        //
        // myDialog.setTitle ("Compipling " + fName + ".....");
        // if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
        //     myDialog.initModality (Modality.WINDOW_MODAL);
        //     myDialog.initOwner (myStage);
        // }
        //  VBox vb1 = new VBox ();
        //  tf.setPrefWidth (800);
        //  vb1.getChildren ().addAll (tf, okButton);
        //  myDialogScene = new Scene (vb1, 900, 250);
        //  myDialog.setScene (myDialogScene);
        //  myDialog.show ();

    }

    /*
     * This helps to make a tree @author Abhijit Bhattacharyya
     */
    @FXML
    private void doMakeTree(MouseEvent event) {
        startTree ();
    }

    private void startTree() {
        String title = "ENTRY " + entryNum;
        rootNode = new TreeItem<String> (title); //entryNum);
        rootNode.setExpanded (true);
        fixTreeList ();
        subEntTree = new TreeView<String> (rootNode);
        subEntTree.setEditable (true);
        subEntTree.getSelectionModel ().setSelectionMode (
                SelectionMode.MULTIPLE);

        mainPane.setLeft (subEntTree);
        mainPane.getStyleClass ().addAll ("pane");
    }

    private void fixTreeList() {
        if ( !rootNode.getChildren ().isEmpty () ) {
            rootNode.getChildren ().clear ();
        }
        int count = 0;

        for ( int i = 0; i < subentNum; i++ ) {
            count = Integer.parseInt (getSUBENT (i + 1).
                    substring (getSUBENT (i + 1).length () - 3));
            String subEntData = "SUBENT    " + getSUBENT (i + 1); // i+ 1 to start from 1 and not 0
            subEntLeaf = new TreeItem<String> (subEntData);
            subEntLeaf.setExpanded (true);
            rootNode.getChildren ().add (subEntLeaf);
            bibTag = new TreeItem<String> ("BIB");
            bibTag.setExpanded (true);
            subEntLeaf.getChildren ().add (bibTag);

            for ( int j = 0; j < bibList.size (); j++ ) {
                String tmpSE = bibList.get (j).getSubEntNum ();
                tmpSE = (tmpSE.length () > 5) ? tmpSE.substring (5, 8).trim ()
                        : tmpSE;
                int c1 = Integer.parseInt (tmpSE);
                if ( c1 == count ) {
                    bibItems = new TreeItem<String> (bibList.get (j).
                            getStrItem ());
                    bibTag.getChildren ().add (bibItems);
                }
            }
            if ( bibTag.getChildren ().isEmpty () ) {
                subEntLeaf.setExpanded (false);
            }
            endBibTag = new TreeItem<String> ("ENDBIB");
            subEntLeaf.getChildren ().add (endBibTag);
            endSUBTag = new TreeItem<String> ("ENDSUBENT");
            rootNode.getChildren ().add (endSUBTag);
        }
    }

    private void fixTreeList(int mySE) {
        if ( !rootNode.getChildren ().isEmpty () ) {
            rootNode.getChildren ().clear ();
        }
        int count = 0;
        for ( int i = 0; i < mySE; i++ ) {
            count = Integer.parseInt (getSUBENT (i + 1).
                    substring (getSUBENT (i + 1).length () - 3));
            String subEntData = "SUBENT    " + getSUBENT (i + 1); // i+ 1 to start from 1 and not 0
            subEntLeaf = new TreeItem<String> (subEntData);
            subEntLeaf.setExpanded (true);
            rootNode.getChildren ().add (subEntLeaf);
            bibTag = new TreeItem<String> ("BIB");
            bibTag.setExpanded (true);
            subEntLeaf.getChildren ().add (bibTag);

            for ( int j = 0; j < bibList.size (); j++ ) {
                String tmpSE = bibList.get (j).getSubEntNum ().trim ();
                // String tmpSE = bibList.get (j).getSubEntNum ().
                //        substring (5, 8).trim ();
                int c1 = Integer.parseInt (tmpSE);
                if ( c1 == count ) {
                    bibItems = new TreeItem<String> (bibList.get (j).
                            getStrItem ());
                    bibTag.getChildren ().add (bibItems);
                }
            }
            if ( bibTag.getChildren ().isEmpty () ) {
                subEntLeaf.setExpanded (false);
            }
            endBibTag = new TreeItem<String> ("ENDBIB");
            subEntLeaf.getChildren ().add (endBibTag);
            endSUBTag = new TreeItem<String> ("ENDSUBENT");
            rootNode.getChildren ().add (endSUBTag);
        }
    }

    /*
     * Selection rule for tee @author Abhijit Bhattacharyya
     */
    private void setTreeSelection() {
        subEntTree.getSelectionModel ().selectedItemProperty ()
                .addListener (new ChangeListener<TreeItem> () {
                    public void changed(ObservableValue ov,
                            TreeItem oldItem,
                            TreeItem newItem) {
                        if ( newItem != null ) {
                            fixTreeList ();
                            subEntTree.refresh ();
                        }
                    }
                });
        /*
         * subEntTree.getSelectionModel () .selectedItemProperty () .addListener
         * (new ChangeListener<TreeItem>() { @Override public void
         * changed(ObservableValue ov, TreeItem oldValue, TreeItem newValue) {
         * if (newValue!=null) { if (event.getButton () == MouseButton.SECONDARY
         * && event.getClickCount () == 1) { System.out.println ("Right button
         * clicked in Tree"); } } } });
         */
    }

    /*
     * This helps to prepare the file for saving with proper tabbing. @author
     * Abhijit Bhattacharyya
     */
    private String procOutput(String in1, int tabP) {
        int len0 = 10;
        int len1 = 1;
        int len2 = 55;
        String out1 = "";
        String tmp = "";
        if ( tabP == 0 ) {

            if ( in1.isEmpty () ) {
                out1 = new String (new char[len0]).replace ("\0", " ");
            } else if ( in1.length () < len0 ) {
                tmp = new String (new char[len0 - in1.length ()]).replace ("\0",
                        " ");
                out1 = in1 + tmp;
            } else {
                out1 = in1;
            }
        } else if ( tabP == 1 ) {
            if ( in1.isEmpty () ) {
                out1 = new String (new char[len1]).replace ("\0", " ");
            } else {
                out1 = in1;
            }
        } else if ( tabP == 2 ) {
            if ( in1.isEmpty () ) {
                out1 = new String (new char[len2]).replace ("\0", " ");
            } else {
                out1 = String.format ("%1$-" + len2 + "s", in1);
            }
        } else if ( tabP > 2 ) {
            int l1 = tabP - in1.length ();
            out1 = new String (new char[l1]).replace ("\0", "0");
            out1 += in1;
        }
        return out1;
    }

    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
    }

    public void setMyScene(Scene myscene) {
        this.myRootScene = myscene;
    }

    public void setCSS(String cssname) {
        this.cssName = cssname;
    }

    /**
     * This is setup coding for OS It determines OS and then fixes path
     * separator etc.
     */
    private void setupOS() {
        osName = System.getProperty ("os.name").trim ();
        if ( osName.contains (winStr) ) {
            pathSEP = "\\";
            zchexPath = rootDir + pathSEP + checkerDir + pathSEP +
                    "zchex_windows";
            zchexCmd = zchexPath + pathSEP + "zchexw.exe";
        }
        if ( osName.contains (linStr) ) {
            pathSEP = "/";
            zchexPath = rootDir + pathSEP + checkerDir + pathSEP + "zchex_linux";
            zchexCmd = zchexPath + pathSEP + "zchexl.exe";
        }
    }

    /**
     * Initializes the controller class.
     */
    //@Override
    public void initialize() {
        Tooltip.install (setB1, t1);
        Tooltip.install (entNumT, t1);
        Tooltip.install (entDateT, t2);
        //Tooltip.install(editTable, t3);        

        //setDefaultDirExt (DICTPathDir);
       // rootDir = fileChooser.getCurrentDirectory ().toString ();
        //rootDir = rootDir.substring (0, rootDir.length () - 5);

        try {
            brW = new BufferedWriter (new FileWriter ("logFile.txt"));

        } catch (IOException ex) {
            Logger.getLogger (MainScreenController.class
                    .getName ()).
                    log (Level.SEVERE, null, ex);
        }
        CreateHeads ();

        setupOS ();

        startProcess ();
        // makeTree();
    }

    /*
     * This creates reference Heads
     */
    private void CreateHeads() {
        FixedHeadList.addAll ("TITLE", "AUTHOR", "INSTITUTE",
                "REFERENCE", "FACILITY");
        FixedHeadList.addAll ("SAMPLE", "INC-SOURCE", "DETECTOR", "MONITOR",
                "MONIT-REF", "DECAY-MON");
        FixedHeadList.addAll ("REL-REF", "CORRECTION", "ADD-RES", "STATUS",
                "HISTORY", "COMMON", "NOCOMMON");
        FixedHeadList.addAll ("REACTION", "DECAY-DATA", "METHOD", "ERR-ANALYS",
                "COMMENT", "DATA");
        FixedHeadList.addAll (
                "ENTRY", "BIB", "ENDBIB", "SUBENT", "ENDSUBENT", "ENDENTRY",
                "ENDCOMMON"
        );
    }

    private void setCellValueFactories() {
        // Main Data Table

        bibHead.setCellValueFactory (cellData -> cellData.getValue ().
                bibItemNameProperty ());
        bibPtr.setCellValueFactory (cellData -> cellData.getValue ().
                pointerIDProperty ());
        bibText.setCellValueFactory (cellData -> cellData.getValue ().
                contentTxtProperty ());
        bibLines.setCellValueFactory (cellData -> cellData.getValue ().
                subEntNumProperty ());

    }

    /**
     * Set Row Factory for the TableView
     */
    public void setRowFactory() {
        editTable.setRowFactory ((TableView<editableData> tv) -> {
            final TableRow<editableData> row = new TableRow<editableData> ();
            row.setOnMouseClicked ((MouseEvent event) -> {
                editableData clickedRow = row.getItem ();
                tmp4 = "";

                tmp4 = clickedRow.bibItemNameProperty ().getValue ();

                if ( !row.isEmpty () &&
                        event.getButton () == MouseButton.SECONDARY &&
                        event.getClickCount () == 1 &&
                        !(tmp4.contains ("COMMON")) ) {

                    row.setContextMenu (chkMenuEdit);
                    int rowIndex = row.getIndex ();
                    resetMenuEditAdd (tmp4, false, rowIndex);
                    cEdit.setOnAction ((ActionEvent event1) -> {
                        doAddEditDelete (rowIndex, tmp4, "Edit");
                    });
                    cDelete.setOnAction ((ActionEvent event1) -> {
                        doAddEditDelete (rowIndex, tmp4, "Delete");
                    });

                    if ( !append ) {
                        doAddEditDelete (rowIndex, "", "Add");
                    } else {
                        doAddEditDelete (rowIndex, tmp4, "Add");    // tmp4 was "" before
                    }
                } else if ( !row.isEmpty () &&
                        event.getButton () == MouseButton.SECONDARY &&
                        event.getClickCount () == 1 &&
                        (tmp4.contains ("COM")) ) {
                    doAddEditDelete (row.getIndex (), tmp4, "");
                }
                if ( row.isEmpty () && event.getButton () ==
                        MouseButton.SECONDARY &&
                        event.getClickCount () == 1 ) {
                    String s1 = "";
                    int rowIndex = row.getIndex ();
                    resetMenuEditAdd (s1, true, rowIndex);
                    row.setContextMenu (chkMenuEdit);
                    doAddEditDelete (rowIndex, "", "Add"); // -1
                }
            });
            return row;
        }
        );
    }

    /**
     * Change the cell selection boolean value of TableView
     */
    public void setRowSelection() {
        // editTable :: Big Table
        editTable.getSelectionModel ().clearSelection ();
        editTable.getSelectionModel ().setCellSelectionEnabled (false);
        editTable.setRowFactory (param -> {
            return new TableRow () {
                @Override
                public void updateIndex(int i) {
                    super.updateIndex (i);
                    setMinHeight (50 * i);
                }
            };
        });
    }

    /**
     * Change the cell selection boolean value of TableView
     */
    public void setCellSelection() {
        // editTable:: Big Table
        editTable.getSelectionModel ().clearSelection ();
        editTable.getSelectionModel ().setCellSelectionEnabled (true);
    }

    /**
     * Set Cell factory for the TableView
     *
     */
    public void setCellFactory() {
        // editableData
        final TableColumn<editableData, String> col
                = new TableColumn<editableData, String> ();
        col.setCellFactory (param -> {
            return new TableCell<editableData, String> () {
                public void updateItem(String myItem, boolean empty) {

                    super.updateItem (myItem, empty);

                    if ( myItem == null || empty ) {
                        setText (null);
                        setStyle ("");
                    } else {
                        Text text = new Text (myItem);
                        text.setWrappingWidth (param.getPrefWidth () - 35);
                        setText (myItem);
                        this.setPrefHeight (
                                text.getLayoutBounds ().getHeight () + 10);
                        this.setGraphic (text);

                    }
                }
            };
        });
    }

    private void getAddMenus(String s1, int SE) {
        String subEnt;
        append = false;
        if ( myData.get (SE).getBibItemName ().contains ("ENDENTRY") ) {
            append = true;
        }

        if ( append ) {
            subEnt = myData.get (SE - 2).getSubEntNum ();
            SE = (subEnt.length () > 5)
                    ? Integer.parseInt (subEnt.substring (9).trim ())
                    : Integer.parseInt (subEnt.trim ());

            subEnt = (subEnt.length () > 6) ? subEnt.substring (5, 8).trim ()
                    : subEnt;

        } else {
            subEnt = findSubEnt (SE);
        }

        if ( (bOrdered || BLoadOldFile) && !append ) {
            subEnt = subEnt.substring (5, 8).trim ();
        }

        if ( !s1.contains ("END") || !s1.contains ("ENTRY") ||
                !s1.contains ("SUBENT") || !s1.contains ("ENDCOMMON") ||
                !s1.contains ("ENDSUBENT") ) {
            if ( subEnt.length () > 3 ) {
                subEnt = subEnt.substring (5, 8);
            }
            SE = Integer.parseInt (subEnt);  // finds subentry
        } else if ( s1.contains ("ENDENTRY") ) {
            // to be done
        } else {
            return;
        }

        addM.getItems ().clear ();
        if ( !bTitle && SE == 1 ) {
            addM.getItems ().add (titleM);
        }
        if ( !bAuth && SE == 1 ) {
            addM.getItems ().add (authM);
        }
        if ( !bInst && SE == 1 ) {
            addM.getItems ().add (instM);
        }
        if ( !bRef && SE == 1 ) {
            addM.getItems ().add (refM);
        }
        if ( !bFac && SE == 1 ) {
            addM.getItems ().add (facM);
        }
        if ( !bReac && SE != 1 ) {          // AVAILABLE sub=2,....
            addM.getItems ().add (reacM);
        }
        if ( !bSamp ) {
            addM.getItems ().add (sampM); // available in sub = ANY
        }
        if ( !bDet ) {                     // available in sub = ANY
            addM.getItems ().add (detM);
        }
        if ( !bMethod ) {
            addM.getItems ().add (methodM);
        }
        if ( !bMonit && SE == 1 ) {
            addM.getItems ().add (monitM);
        }
        if ( !bMonitRef ) {
            addM.getItems ().add (monitRefM);
        }
        if ( !bIncs && SE == 1 ) {
            addM.getItems ().add (incSM);
        }
        if ( !bAnalys && SE == 1 ) {
            addM.getItems ().add (analysM);
        }
        if ( !bStatus ) {
            addM.getItems ().add (statusM);
        }
        if ( !bHist && SE == 1 ) {
            addM.getItems ().add (histM);
        }
        if ( !bErrAnalys ) {
            addM.getItems ().add (errAnalysM);
        }
        if ( !bAddResult && SE == 1 ) {
            addM.getItems ().add (addlResultM);
        }
        if ( !bdData ) {
            addM.getItems ().add (decayDataM);
        }
        if ( !bdMon ) {
            addM.getItems ().add (decayMonM);
        }
        if ( !bCorrect ) {
            addM.getItems ().add (correctM);
        }
        //if ( !checkStringArray (subEnt, sub4Common) ) {
        if ( s1.startsWith ("NOC") ) {
            addM.getItems ().add (commActiveM);
        }
        if ( s1.startsWith ("COM") ) {
            addM.getItems ().add (commEditM);
        }
        if ( !bData ) {
            addM.getItems ().add (dataM);
        }
        if ( !bFlag ) {
            addM.getItems ().add (flagM);
        }
        addM.getItems ().add (commentM); // may be put in any block
        addM.getItems ().add (newSUBM);
    }

    public void resetMenuEditAdd(String s1, boolean ivoid, int SE) {
        boolean bibSUB = false;
        if ( s1.contains ("BIB") ||
                s1.contains ("SUBENT") ||
                s1.contains ("ENDBIB") ||
                s1.contains ("ENDSUBENT") ) {
            bibSUB = true;
        }

        chkMenuEdit.getItems ().clear ();
        editM.getItems ().clear ();
        getAddMenus (s1, SE);
        editM.getItems ().addAll (cEdit, cDelete);
        if ( !ivoid && !bibSUB ) {
            chkMenuEdit.getItems ().addAll (editM, addM);
        } else if ( !ivoid ) {
            chkMenuEdit.getItems ().addAll (addM);
        }
    }

    private void doAddEditDelete(int ri, String s1, String act) {
        String subEntS;

        if ( s1.contains ("ENDENTRY") ) {
            subEntS = Integer.toString (ri - 1);
            System.out.println (subEntS);
        } else {
            subEntS = findSubEnt (ri);
        }

        if ( !s1.isEmpty () ) {
            switch (s1) {
                case "TITLE":
                    doEditFreeText (ri, "TITLE", act, subEntS);
                    break;
                case "AUTHOR":
                    // doEditAuth (ri, "AUTHOR", act, subEntS);
                    doEditFreeText (ri, "AUTHOR", act, subEntS);
                    break;
                case "INSTITUTE":
                    doSingleCBText (ri, "INSTITUTE", institute,
                            lList.instList, act, subEntS);
                    break;
                case "REFERENCE":
                    doEditRef (ri, "REFERENCE", act, subEntS);
                    break;
                case "FACILITY":
                    doEditFacility (ri, "FACILITY", act, subEntS);
                    break;
                case "SAMPLE":
                    doEditFreeText (ri, "SAMPLE", act, subEntS);
                    break;
                case "INC-SOURCE":
                    // doEditIncSrc (ri, "INC-SOURCE", act, subEntS);
                    doSingleCBText (ri, "INC-SOURCE", incSrcCB, lList.incSrcList,
                            act,
                            subEntS);
                    break;
                case "DETECTOR":
                    doSingleCBText (ri, "DETECTOR", detectorCB,
                            lList.detectorList, act, subEntS);
                    break;
                case "METHOD":
                    //doMethod (ri, "MONITOR", methodCB,
                    doSingleCBText (ri, "METHOD", methodCB,
                            lList.methodList, act, subEntS);
                    break;
                case "MONITOR":
                    doEditReact (ri, "MONITOR", act, subEntS);
                    break;
                case "REACTION":
                    doEditReact (ri, "REACTION", act, subEntS);
                    break;
                case "MONIT-REF":
                    doMonitRef (ri, "MONIT-REF", act, subEntS);
                    break;
                case "HISTORY":
                    doEditFreeText (ri, "HISTORY", act, subEntS);
                    break;
                case "COMMENT":
                    doEditFreeText (ri, "COMMENT", act, subEntS);
                    break;
                case "CORRECTION":
                    doEditFreeText (ri, "CORRECTION", act, subEntS);
                    break;
                case "STATUS":
                    doSingleCBText (ri, "STATUS", statusCB,
                            lList.statusList, act, subEntS);
                    break;
                case "ERR-ANALYS":
                    doEditFreeText (ri, "ERR-ANALYS", act, subEntS);
                    break;
                case "ADD-RES":
                    doSingleCBText (ri, "ADD-RES", addlResultCB,
                            lList.addlResultList, act, subEntS);
                    break;
                case "DECAY-DATA":
                    doDD (ri, "DECAY-DATA", act, subEntS);
                    break;
                case "DECAY-MON":
                    doDD (ri, "DECAY-MON", act, subEntS);
                    break;
                case "NOCOMMON":
                    doCommon (ri, "COMMON", "Activate", subEntS);
                    break;
                case "COMMON":
                    doCommon (ri, "COMMON", "Activate", subEntS);
                    break;
                case "DATA":
                    doCommon (ri, "COMMON", "Activate", subEntS);
                    break;
                case "FLAG":
                    doEditFreeText (ri, "FLAG", act, subEntS);
                    break;
            }
        }
        titleM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bTitle = (act.contains ("Add")) ? true : bTitle;
            bTitle = (act.contains ("Delete")) ? false : bTitle;
            doEditFreeText (ri, "TITLE", act, subEntS);
        });
        authM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bAuth = (act.contains ("Add")) ? true : bAuth;
            bAuth = (act.contains ("Delete")) ? false : bAuth;
            // doEditAuth (ri, "AUTHOR", act, subEntS);
            doEditFreeText (ri, "AUTHOR", act, subEntS);
        });
        instM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bInst = (act.contains ("Add")) ? true : bInst;
            bInst = (act.contains ("Delete")) ? false : bInst;
            doSingleCBText (ri, "INSTITUTE", institute,
                    lList.instList, act, subEntS);
        });
        refM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bRef = (act.contains ("Add")) ? true : bRef;
            bRef = (act.contains ("Delete")) ? false : bRef;
            doEditRef (ri, "REFERENCE", act, subEntS);
        });
        facM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bFac = (act.contains ("Add")) ? true : bFac;
            bFac = (act.contains ("Delete")) ? false : bFac;
            doEditFacility (ri, "FACILITY", act, subEntS);
        });
        sampM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bSamp = (act.contains ("Add")) ? true : bSamp;
            bSamp = (act.contains ("Delete")) ? false : bSamp;
            doEditFreeText (ri, "SAMPLE", act, subEntS);
        });
        incSM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bIncs = (act.contains ("Add")) ? true : bIncs;
            bIncs = (act.contains ("Delete")) ? false : bIncs;
            // doEditIncSrc (ri, "INC-SOURCE", act, subEntS);
            doSingleCBText (ri, "INC-SOURCE", incSrcCB, lList.incSrcList, act,
                    subEntS);
        });
        detM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bDet = (act.contains ("Add")) ? true : bDet;
            bDet = (act.contains ("Delete")) ? false : bDet;
            doSingleCBText (ri, "DETECTOR", detectorCB,
                    lList.detectorList, act, subEntS);
        });
        methodM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bMethod = (act.contains ("Add")) ? true : bMethod;
            bMethod = (act.contains ("Delete")) ? false : bMethod;
            // doMethod (ri, "METHOD", methodCB,
            doSingleCBText (ri, "METHOD", methodCB,
                    lList.methodList, act, subEntS);
        });
        monitM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bMonit = (act.contains ("Add")) ? true : bMonit;
            bMonit = (act.contains ("Delete")) ? false : bMonit;
            doEditReact (ri, "MONITOR", act, subEntS);
        });
        reacM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bReac = (act.contains ("Add")) ? true : bReac;
            bReac = (act.contains ("Delete")) ? false : bReac;
            doEditReact (ri, "REACTION", act, subEntS);
        });
        monitRefM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bMonitRef = (act.contains ("Add")) ? true : bMonitRef;
            bMonitRef = (act.contains ("Delete")) ? false : bMonitRef;
            doMonitRef (ri, "MONIT-REF", act, subEntS);
        });
        histM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bHist = (act.contains ("Add")) ? true : bHist;
            bHist = (act.contains ("Delete")) ? false : bHist;
            doEditFreeText (ri, "HISTORY", act, subEntS);
        });
        newSUBM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            InsSubEnt (true, subEntS);
        });
        commentM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bComm = (act.contains ("Add")) ? true : bComm;
            bComm = (act.contains ("Delete")) ? false : bComm;
            doEditFreeText (ri, "COMMENT", act, subEntS);
        });
        correctM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bCorrect = (act.contains ("Add")) ? true : bCorrect;
            bCorrect = (act.contains ("Delete")) ? false : bCorrect;
            doEditFreeText (ri, "CORRECTION", act, subEntS);
        });
        statusM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bStatus = (act.contains ("Add")) ? true : bStatus;
            bStatus = (act.contains ("Delete")) ? false : bStatus;
            doSingleCBText (ri, "STATUS", statusCB,
                    lList.statusList, act, subEntS);
        });
        errAnalysM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bErrAnalys = (act.contains ("Add")) ? true : bErrAnalys;
            bErrAnalys = (act.contains ("Delete")) ? false : bErrAnalys;
            doEditFreeText (ri, "ERR-ANALYS", act, subEntS);
        });
        addlResultM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bAddResult = (act.contains ("Add")) ? true : bAddResult;
            bAddResult = (act.contains ("Delete")) ? false : bAddResult;
            doSingleCBText (ri, "ADD-RES", addlResultCB,
                    lList.addlResultList, act, subEntS);
        });
        decayDataM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bdData = (act.contains ("Add")) ? true : bdData;
            bdData = (act.contains ("Delete")) ? false : bdData;
            doDD (ri, "DECAY-DATA", act, subEntS);
        });
        decayMonM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bdMon = (act.contains ("Add")) ? true : bdMon;
            bdMon = (act.contains ("Delete")) ? false : bdMon;
            doDD (ri, "DECAY-MON", act, subEntS);
        });
        commActiveM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            //bCommon = (act.contains ("Add")) ? true : bCommon;
            //bCommon = (act.contains ("Delete")) ? false : bCommon;
            doCommon (ri, "COMMON", "Activate", subEntS);
        });
        commEditM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            doCommon (ri, "COMMON", "Activate", subEntS);
        });
        dataM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            doCommon (ri, "COMMON", "Activate", subEntS);
        });
        flagM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            bFlag = (act.contains ("Add")) ? true : bFlag;
            bFlag = (act.contains ("Delete")) ? false : bFlag;
            doEditFreeText (ri, "FLAG", act, subEntS);
        });
        procTree ();
    }

    //Dynamically Process Tree
    private void procTree() {
        // Update Tree after operation on Heads
        doSortBIBList ();
        fixTreeList (); /// <<--------- for testing
    }

    // Try to Sort BIBList
    private void doSortBIBList() {
        if ( !bibList.isEmpty () ) {
            FXCollections.sort (bibList, (BIBClasses b1, BIBClasses b2)
                    -> b1.getLineNum ().compareTo (b2.getLineNum ())
            );
        }
    }

    /*
     *
     */
    // private void InsSubEnt(String subN) {
    private void InsSubEnt(boolean append, String SE) {
        String subN = Integer.toString (++subentNum);
        int line = Integer.parseInt (SE);

        if ( append ) {
            ++line;
            //System.out.println (subentNum);
            subN = Integer.toString (subentNum);
            //System.out.println (subN + " " + subentNum);
            myData.add (line,
                    new editableData ("SUBENT", "",
                            exforUtil.fixString11 (getSUBENT (subentNum)) +
                            exforUtil.fixString11 (myDate0), subN));
            ++myDataN;
            myData.add (line + 1, new editableData ("BIB", "", "", subN));
            ++myDataN;
            myData.add (line + 2, new editableData ("ENDBIB", "", "", subN));
            ++myDataN;
            myData.add (line + 3, new editableData ("NOCOMMON", "",
                    exforUtil.fixString11 ("0") + exforUtil.fixString11 ("0"),
                    subN));
            ++myDataN;
            sub4Common.add (subN);
            myData.add (line + 4,
                    new editableData ("ENDSUBENT", "", "", subN));
            ++myDataN;
            ++lastLine;
        } else {
            myData.add (++myDataN,
                    new editableData ("SUBENT", "",
                            exforUtil.fixString11 (getSUBENT (subentNum)) +
                            exforUtil.fixString11 (myDate0), subN));

            myData.add (++myDataN, new editableData ("BIB", "", "", subN));
            myData.add (++myDataN, new editableData ("ENDBIB", "", "", subN));
            myData.add (++myDataN, new editableData ("NOCOMMON", "",
                    exforUtil.fixString11 ("0") + exforUtil.fixString11 ("0"),
                    subN));
            sub4Common.add (subN);
            myData.add (++myDataN,
                    new editableData ("ENDSUBENT", "", "", subN));
            ++lastLine;
            myData.add (++myDataN, new editableData ("ENDENTRY", "", "", "999"));
        }
    }

    // find which subentry does this row belongs to
    private String findSubEnt(int rowNow) {
        String s1 = "";
        int brk = 0;
        for ( int ij = rowNow; ij <= myDataN; ij++ ) {
            if ( myData.get (ij).getBibItemName ().contains ("SUBENT") ) {
                s1 = myData.get (ij).getSubEntNum ();
                brk = 1;
                break;
            }
            if ( brk == 1 ) {
                break;
            }
        }
        return s1;
    }

    /*
     * @Head this Head will be deleted in BibList
     */
    private void zapBIBEntry(String Head) {
        int thisEntry = 0;
        for ( int ii = 0; ii < bibList.size (); ii++ ) {
            if ( bibList.get (ii).getStrItem ().compareTo (Head) == 0 ) {
                thisEntry = ii;
            }
        }

        bibList.remove (thisEntry);
        --bibEntNum;
    }

    /*
     * this code is used for those menu heads who use singleCBText() @author
     * Abhijit Bhattacharyya
     */
    private void changeBoolStatus(String head, boolean cond) {
        switch (head) {
            case "TITLE":
                bTitle = cond;
                break;
            case "AUTHOR":
                bAuth = cond;
                break;
            case "INSTITUTE":
                bInst = cond;
                break;
            case "REFERENCE":
                bRef = cond;
                break;
            case "FACILITY":
                bFac = cond;
                break;
            case "SAMPLE":
                bSamp = cond;
                break;
            case "INC-SOURCE":
                bIncs = cond;
                break;
            case "DETECTOR":
                bDet = cond;
                break;
            case "METHOD":
                bMethod = cond;
                break;
            case "MONITOR":
                bMonit = cond;
                break;
            case "REACTION":
                bReac = cond;
                break;
            case "MONIT-REF":
                bMonitRef = cond;
                break;
            case "HISTORY":
                bHist = cond;
                break;
            case "COMMENT":
                bComm = cond;
                break;
            case "STATUS":
                bStatus = cond;
                break;
            case "ERR-ANALYS":
                bErrAnalys = cond;
                break;
            case "ADD-RES":
                bAddResult = cond;
                break;
            case "DECAY-DATA":
                bdData = cond;
                break;
            case "DECAY-MON":
                bdMon = cond;
                break;
            case "CORRECTION":
                bCorrect = false;
                break;
            case "COMMON":
                bComEdit = cond;
                break;
            case "NOCOMMON":
                bComActive = cond;
                break;
            case "FLAG":
                bFlag = cond;
                break;
        }
    }

    /**
     * Word wrapping feature for the Title
     *
     * @param s1 which is input
     * @return output String with added character to make it multiline
     */
    private String arrTitle(String s1) {

//        s1 = "Abhijit Bhattacharyya, Susmita Bhattacharyya, " +
//                "Tilottoma Bhattacharyya. we stay at Anushakti Nagar, " +
//                "Mumbai. We like to eat Mutton and fish better than " +
//                "chicken and vegetable. We like Chicken from KFC";
        String s2 = "";
        String spaceList = "";
        int wordNum = 0; // s3.split("\\s+").length;                
        multiLine = 1;
        String nComps[] = s1.split ("\\s+");
        int wCount = 0;

        s2 = nComps[0] + " ";
        wCount = nComps[0].length () + 1;
        for ( int ii = 1; ii < nComps.length; ii++ ) {
            wCount += nComps[ii].length ();
            if ( wCount <= cutPoint ) {
                s2 += nComps[ii] + " ";
                ++wCount;
            } else {
                ++multiLine;
                s2 += "@@" + nComps[ii] + " ";
                wCount = nComps[ii].length () + 1;
            }
        }
        return s2;
    }

    private void tooltipDelay(Tooltip tpX, Node thisNode) {
        thisNode.setOnMouseEntered (new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event) {
                Node b = (Node) event.getSource ();
                Point2D p = b.localToScreen (b.getLayoutBounds ().getMaxX (), b.
                        getLayoutBounds ().getMaxY ()); //I position the tooltip at bottom right of the node (see below for explanation)
                tpX.show (b, p.getX (), p.getY ());
            }
        });
        thisNode.setOnMouseExited (new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event) {
                tpX.hide ();
            }
        });
    }

    /**
     * Word wrapping feature for the Title
     *
     * @param s1 which is input
     * @param multi whether it accepts multi line boolean
     * @return output String with added character to make it multi line
     */
    private String arrTitle(String s1, boolean multi) {
        String s2 = "";
        String s3 = s1;
        String spaceList = "";
        int wCount = 0;
        int wordNum = 0; // s3.split("\\s+").length;                
        multiLine = 1;
        int ii = 0;
        while (ii <= s1.length ()) {
            String tmp = (s3.contains ("\n")) ? s3.substring (0, s3.indexOf (
                    "\n")) : s3;
            if ( tmp.length () > cutPoint ) {
                tmp = arrTitle (tmp);
                s2 += tmp + "@@";
                ++multiLine;
            } else {
                s2 += tmp + "@@";
                ++multiLine;
            }
            s3 = s3.substring (s3.indexOf ("\n") + 1);
            ii += tmp.length ();
        }
        s2 = s2.substring (0, s2.length () - 2);
        return s2;
    }

    private void doEditFreeText(int ri, String Head, String act, String SE) {
        String getTxtData = "";
        int tmpRICnt = 1;
        isSelect = false;

        while (myData.get (ri + tmpRICnt).getBibItemName ().equals ("")) {
            ++tmpRICnt;
        }

        if ( act.contains ("Add") ) {
            getTxtData = "";
        } else {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                getTxtData
                        += myData.get (ri + ii).getContentTxt () + " ";
            }
        }
        if ( Head.contains ("AUTHOR") && getTxtData.length () > 2 ) {
            getTxtData = getTxtData.substring (1, getTxtData.length () - 3);
        }
        if ( getTxtData != null ) {
            getTxtData = getTxtData.replaceAll ("\n", " ");
        }
        if ( !act.contains ("Add") ) {
            tmpINT = tmpRICnt;
        }

        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }
        VBox vb1 = new VBox (10);
        HBox hb1 = new HBox ();
        tf.setPrefWidth (130);
        tf.setWrapText (true);
        hb1.getChildren ().addAll (acceptEdit, cancelEdit);
        vb1.getChildren ().addAll (tf, hb1);
        if ( !act.contains ("Delete") ) {
            if ( act.contains ("Edit") ) {
                tf.setText (getTxtData);

            }

            tp1.getStyleClass ().add ("ttip");
            if ( Head.contains ("TITLE") ) {
                tp1.setText (titleTip);
            } else if ( Head.contains ("AUTHOR") ) {
                tp1.setText (authorTip);
            } else if ( Head.contains ("SAMPLE") ) {
                tp1.setText (sampleTip);
            } else if ( Head.contains ("HISTORY") ) {
                tp1.setText (historyTip);
            } else if ( Head.contains ("COMMENT") ) {
                tp1.setText (commentTip);
            } else if ( Head.contains ("ERR-ANALYS") ) {
                tp1.setText (errAnalysTip);
            } else if ( Head.contains ("CORRECTION") ) {
                tp1.setText (corrTip);
            }
            tf.setTooltip (tp1);
            tooltipDelay (tp1, tf);

            myDialogScene = new Scene (vb1, 400, 150);
            myDialogScene.getStylesheets ().add (getClass ().getResource (
                    "CSS/mainscreen.css").toExternalForm ());
            myDialog.setScene (myDialogScene);
            myDialog.show ();
            acceptEdit.setOnAction ((ActionEvent event) -> {
                String parseStr = "";
                String strEntered = "";
                String tmpStr1 = Head;
                String tmpStr2 = "";
                String tmpHead = "";

                if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox (
                            " Blank Entry! To Cancel, please press \"CANCEL\"",
                            "ATTENTION: BLANK ENTRY!");
                } else {

                    if ( Head.contains ("AUTHOR") ) {
                        parseStr = "(" + tf.getText ().toString () + ")";
                    } else {
                        parseStr = tf.getText ().toString ();
                    }

                    parseStr = (Head.contains ("ERR-ANALYS")) ? arrTitle (
                            parseStr,
                            true) : arrTitle (parseStr);
                    multiLine = (parseStr.split ("\\@@")).length;

                    if ( act.contains ("Edit") ) {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0, parseStr.indexOf ("@@"));
                            } else {
                                strEntered = parseStr;
                            }

                            if ( tmpINT == multiLine ) {
                                myData.set (ri + ii,
                                        new editableData (tmpHead, "",
                                                strEntered,
                                                SE));
                            } else {
                                myData.add (ri + ii,
                                        new editableData (tmpHead, "",
                                                strEntered,
                                                SE));
                                ++myDataN;

                            }
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (2 + parseStr.
                                        indexOf ("@@"));
                            }
                        }
                    } else {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0, parseStr.indexOf ("@@"));
                            } else {
                                strEntered = parseStr;
                            }
                            myData.add (ri + ii,
                                    new editableData (tmpHead, "", strEntered,
                                            SE));
                            ++myDataN;
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (2 + parseStr.
                                        indexOf ("@@"));
                            }
                        }

                        thisEntryLine = Integer.toString (doGetSELineNum (Head));
                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine,
                                        Integer.toString (bibEntNum),
                                        SE, Head));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    subEntTree.refresh ();
                    tf.clear ();
                    tp1.setText ("");
                    myDialog.close ();
                }
                System.gc ();
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                myData.remove (ri);
                --myDataN;
            }
            editTable.refresh ();
            zapBIBEntry (Head);
            changeBoolStatus (Head, false);
        }
        procTree ();
        System.gc ();
    }

    private void doSingleCBText(
            int ri, String Head,
            ComboBox thisCB, ObservableList oList,
            String act, String SE
    ) {
        String getTxtData = "";
        String temp1;
        int tmpRICnt = 1;
        isSelect = false;

        while (myData.get (ri + tmpRICnt).getBibItemName () == "") {
            ++tmpRICnt;
        }
        if ( !act.contains ("Add") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                temp1 = myData.get (ri + ii).getContentTxt () + " ";
                if ( !temp1.isEmpty () ) {
                    getTxtData += temp1;// + "\n";
                } else {
                    getTxtData = "";
                }
            }
            tmpINT = tmpRICnt;
        } else {
            getTxtData = "";
        }

        if ( getTxtData.length () == 1 && getTxtData.contains ("\n") ) {
            getTxtData = "";
        }
        if ( !getTxtData.isEmpty () ) {
            demoStr0 = getTxtData.substring (1, getTxtData.indexOf (")"));
        }

        if ( act.contains ("Add") ) {
            getTxtData = null;
            putTxt = "";
            if ( !Head.contains ("METHOD") || !Head.contains ("INC-SOURCE") ) {
                addLReacCnt = 0;
            } else {
                lineCount = 1;
            }
        } else if ( act.contains ("Edit") ) {
            putTxt = getTxtData.substring (getTxtData.indexOf (")") + 1);
            if ( !Head.contains ("METHOD") || !Head.contains ("INC-SOURCE") ) {
                addLReacCnt = 1;
            } else {
                lineCount = 1;
            }
        }
        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }
        VBox vb1 = new VBox (10);
        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        tf.setPrefWidth (130);
        tf.setWrapText (true);

        hb1.getChildren ().addAll (
                selectIt, acceptEdit, cancelEdit
        );
        if ( Head.contains ("METHOD") || Head.contains ("INC-SOURCE") ) {
            hb2.getChildren ().addAll (thisCB, addRCB);
        } else {
            hb2.getChildren ().addAll (thisCB);
        }

        hb1.setSpacing (10);
        hb2.setSpacing (10);
        vb1.getChildren ().addAll (tf, hb2, hb1);

        entryChoice (oList, thisCB);

        if ( !act.contains ("Delete") ) {
            if ( act.contains ("Edit") && getTxtData.length () > 1 ) {
                tf.setText (getTxtData);
            }

            tp1.getStyleClass ().add ("ttip");
            if ( Head.contains ("INSTITUTE") ) {
                tp1.setText (instTip);
            } else if ( Head.contains ("INC-SOURCE") ) {
                tp1.setText (incSrcTip);
            }
            tf.setTooltip (tp1);
            tooltipDelay (tp1, tf);

            myDialogScene = new Scene (vb1, 700, 150);
            myDialogScene.getStylesheets ().add (getClass ().getResource (
                    "CSS/mainscreen.css").toExternalForm ());
            myDialog.setScene (myDialogScene);
            myDialog.show ();

            addRCB.setOnAction ((ActionEvent event1) -> {
                String tmp = tf.getText ();
                tf.clear ();
                //++lineCount;
                if ( !tmp.isEmpty () ) {
                    if ( !exforUtil.isNumeric (tmp.substring (0, 1)) ) {
                        tmp = Integer.toString (lineCount) + tmp;
                    }
                    tf.setText (tmp);
                    addRCBB = true;
                }
            });

            selectIt.setOnAction ((ActionEvent event1) -> {
                String tmpB = "";
                String tmp = (!thisCB.getValue ().toString ().isEmpty ())
                        ? (String) thisCB.getValue () : "";
                tmp = (!tmp.isEmpty ()) ? tmp.substring (0, tmp.indexOf (" "))
                        : "";
                tmpB = tf.getText ().toString ();
                int cnt = 1;

                if ( Head.contains ("METHOD") || Head.contains ("INC-SOURCE") ) {
                    if ( !addRCBB ) {
                        if ( lineCount == 1 ) {
                            if ( !tmpB.isEmpty () ) {
                                demoStr0 = tmpB.substring (
                                        0, tmpB.indexOf (")")) +
                                        "," + tmp + ")" +
                                        tmpB.substring (tmpB.indexOf (")") + 1);;
                            } else {
                                demoStr0 = "(" + tmp + ")";
                            }
                        } else if ( !tmpB.isEmpty () ) {
                            demoStr0 = tmpB.substring (0, tmpB.indexOf (")")) +
                                    "," + tmp;// + ")";
                        } else {
                            demoStr0 = tmpB.substring (0, tmpB.indexOf (")")) +
                                    "," + tmp + ")" +
                                    tmpB.substring (tmpB.indexOf (")"));
                        }
                    } else {
                        demoStr0 = tmpB + "\n" +
                                (++lineCount) + "(" + tmp + ")";
                        ++cnt;
                        addRCBB = false;
                    }
                } else {
                    if ( (addLReacCnt > 0) ) {
                        demoStr0 += "," + tmp;
                    } else {
                        demoStr0 = tmp;
                    }
                    tmpB = tmpB.substring (tmpB.indexOf (")") + 1);
                }

                tf.clear ();
                if ( Head.contains ("METHOD") || Head.contains ("INC-SOURCE") ) {
                    putTxt = demoStr0;
                } else {
                    putTxt = "(" + demoStr0 + ")";
                }

                tf.setText (putTxt);
                ++addLReacCnt;
                multiLine = lineCount;
                isSelect = true;
            });
            acceptEdit.setOnAction ((ActionEvent event) -> {
                String parseStr;
                String tmpHead;
                String strEntered = "";
                String str1 = Head;
                String str2 = "";
                String labelS;
                demoStr0 = "";
                if ( !isSelect ) {
                    popupMsg.warnBox ("Please \"SELECT\" first",
                            "Attentio!: No Selection.");
                } else if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox (
                            "Please add entry, then press \"Select\" first",
                            "Attention! ENTRY NULL.");
                } else {
                    parseStr = tf.getText ().toString ();
                    if ( Head.contains ("METHOD") || Head.
                            contains ("INC-SOURCE") ) {
                        reacStrLen = parseStr.split ("\\s+");
                    } else {
                        parseStr = arrTitle (parseStr);
                    }

                    if ( act.contains ("Edit") ) {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? str1 : str2;
                            if ( ii <= multiLine - 2 ) {
                                if ( Head.contains ("METHOD") || Head.contains (
                                        "INC-SOURCE") ) {
                                    strEntered = parseStr.substring (
                                            1, Integer.valueOf (reacStrLen[ii].
                                                    length ())
                                    );
                                } else {
                                    strEntered = parseStr.substring (
                                            0, parseStr.indexOf ("@@"));
                                }
                            } else {
                                strEntered = parseStr;
                            }
                            labelS = (multiLine > 1) ? Integer.toString (ii + 1)
                                    : "";
                            if ( tmpINT == multiLine ) {
                                if ( Head.contains ("METHOD") || Head.contains (
                                        "INC-SOURCE") ) {
                                    myData.set (ri + ii,
                                            new editableData (
                                                    tmpHead, labelS, strEntered,
                                                    SE));
                                } else {
                                    myData.set (ri + ii,
                                            new editableData (tmpHead, "",
                                                    strEntered,
                                                    SE));
                                }
                            } else {
                                if ( Head.contains ("METHOD") || Head.contains (
                                        "INC-SOURCE") ) {
                                    myData.add (ri + ii,
                                            new editableData (
                                                    tmpHead, labelS, strEntered,
                                                    SE));
                                } else {
                                    myData.add (ri + ii,
                                            new editableData (tmpHead, "",
                                                    strEntered, SE));
                                }
                            }
                            if ( ii <= multiLine - 2 ) {
                                if ( Head.contains ("METHOD") || Head.contains (
                                        "INC-SOURCE") ) {
                                    parseStr = parseStr.substring (
                                            parseStr.indexOf ("),") + 3);
                                } else {
                                    parseStr = parseStr.substring (2 + parseStr.
                                            indexOf (" "));
                                }
                            }
                        }
                    } else {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? str1 : str2;
                            if ( ii <= multiLine - 2 ) {
                                if ( Head.contains ("METHOD") || Head.contains (
                                        "INC-SOURCE") ) {
                                    strEntered = parseStr.substring (
                                            1, parseStr.indexOf ("\n")
                                    );
                                } else {
                                    strEntered = parseStr.substring (
                                            0, parseStr.indexOf ("@@"));
                                }
                            } else {
                                strEntered = parseStr;
                            }

                            labelS = (multiLine > 1) ? Integer.toString (ii + 1)
                                    : "";
                            if ( Head.contains ("METHOD") || Head.contains (
                                    "INC-SOURCE") ) {
                                myData.add (ri + ii,
                                        new editableData (
                                                tmpHead, labelS, strEntered, SE));
                            } else {
                                myData.add (ri + ii,
                                        new editableData (
                                                tmpHead, "", strEntered, SE));
                            }
                            ++myDataN;
                            if ( ii <= multiLine - 2 ) {
                                if ( Head.contains ("METHOD") || Head.contains (
                                        "INC-SOURCE") ) {
                                    parseStr = parseStr.substring (
                                            parseStr.indexOf ("\n") + 1);
                                    parseStr = parseStr.substring (
                                            parseStr.indexOf ("("));
                                } else {
                                    parseStr = parseStr.substring (2 + parseStr.
                                            indexOf ("@@"));
                                }
                            }
                        }
                        thisEntryLine = Integer.toString (doGetSELineNum (Head));

                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine, Integer.
                                        toString (
                                                bibEntNum), SE, str1)); //Head));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    tf.clear ();
                    myDialog.close ();
                    addRCBB = false;
                    lineCount = 1;
                }
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }

        if ( act.contains ("Delete") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                myData.remove (ri);
                --myDataN;
            }
            editTable.refresh ();
            changeBoolStatus (Head, false);
            zapBIBEntry (Head);
        }
        procTree ();
        System.gc ();
    }

    private void refComboSetup(String checkIt) {
        //entryChoice (jTypeList, jType);
        if ( checkIt.contains ("JOUR") ) {
            entryChoice (lList.jourList, journals);
        }
        if ( checkIt.contains ("CONF") ) {
            entryChoice (lList.confList, journals);
        }
        if ( (checkIt.contains ("REPT")) || (checkIt.contains ("THES")) ||
                (checkIt.contains ("PRIV")) || (checkIt.contains ("PREP")) ||
                (checkIt.contains ("PROG")) ) {
            entryChoice (lList.reportList, journals);
        }
        if ( checkIt.contains ("BOOK") ) {
            entryChoice (lList.bookList, journals);
        }
        if ( checkIt.contains ("DATA") ) {
            entryChoice (lList.dataList, journals);
        }
    }

    private void doEditRef(int ri, String Head, String act, String SE) {
        String getTxtData;
        String temp1;
        isSelect = false;

        if ( act.contains ("Add") ) {
            getTxtData = null;
            putTxt = "";
        } else {
            getTxtData = myData.get (ri).getContentTxt ().toString ();
            if ( getTxtData.startsWith ("(") ) {
                getTxtData = getTxtData.replace ("(", "");
            }
            if ( getTxtData.endsWith (")") ) {
                getTxtData = getTxtData.replace (")", "");
            }
            putTxt = getTxtData;
        }
        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }
        VBox vb1 = new VBox (10);
        VBox vb2 = new VBox (10);
        VBox vb3 = new VBox (10);
        VBox vb4 = new VBox (10);
        VBox vb5 = new VBox (10);
        VBox vb6 = new VBox (10);
        VBox vb7 = new VBox (10);
        VBox vb8 = new VBox (10);
        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        tf.setPrefWidth (130);
        tf.setWrapText (true);
        hb1.getChildren ().addAll (
                selectIt, acceptEdit, cancelEdit
        );
        vb2.getChildren ().addAll (jtypeL, jType);
        vb3.getChildren ().addAll (refSrcL, journals);
        vb4.getChildren ().addAll (volL, volTxt);
        vb5.getChildren ().addAll (pgL, pgTxt);
        vb6.getChildren ().addAll (reptL, reptNumTxt);
        vb7.getChildren ().addAll (reptMML, MMTxt);
        vb8.getChildren ().addAll (yrL, yrTxt);
        hb2.getChildren ().addAll (
                vb2, vb3, vb4, vb5, vb6, vb7, vb8
        );
        volTxt.setMinWidth (50);
        pgTxt.setMinWidth (50);
        yrTxt.setMinWidth (70);
        reptNumTxt.setMinWidth (70);
        MMTxt.setMinWidth (70);
        MMTxt.setMaxWidth (90);
        reptNumTxt.setMaxWidth (90);
        yrTxt.setMaxWidth (90);
        volTxt.setMaxWidth (50);
        pgTxt.setMaxWidth (50);

        journals.setMaxWidth (300);
        jType.setMaxWidth (300);
        hb1.setSpacing (10);
        hb2.setSpacing (10);
        vb1.getChildren ().addAll (tf, hb2, hb1);
        entryChoice (lList.jTypeList, jType);

        if ( !act.contains ("Delete") ) {
            tf.setText (putTxt);
            myDialogScene = new Scene (vb1, 1200, 250);
            myDialog.setScene (myDialogScene);
            myDialog.show ();
            selectIt.setOnAction ((ActionEvent event1) -> {
                String temp = "";
                String tmp = "";
                int selUpto;
                temp = jType.getValue ();
                if ( !temp.contains ("Please") && (temp != null) ) {
                    if ( !putTxt.endsWith (", ") &&
                            putTxt != null &&
                            putTxt != "" &&
                            putTxt.length () > 1 ) {
                        putTxt += ",\n";
                    }
                    temp = temp.substring (1, 2);
                    putTxt += temp + ", ";
                    tmp = journals.getValue ();
                    tmp = tmp.substring (0, tmp.indexOf (" "));
                    if ( !tmp.contains ("Please") ) {
                        putTxt += tmp;
                        switch (temp) {
                            case "0":
                            case "3":
                            case "4":
                                break;
                            case "A":
                            case "B":
                            case "C":
                                if ( volTxt.getText () != null ) {
                                    putTxt += "," + volTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                if ( pgTxt.getText () != null ) {
                                    putTxt += "," + pgTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                if ( yrTxt.getText () != null ) {
                                    putTxt += "," + yrTxt.getText ();
                                }
                                break;
                            case "J":
                            case "K":
                                if ( volTxt.getText () != null ) {
                                    putTxt += "," + volTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                if ( pgTxt.getText () != null ) {
                                    putTxt += "," + pgTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                if ( yrTxt.getText () != null ) {
                                    putTxt += "," + yrTxt.getText ();
                                }
                                break;
                            case "P":
                            case "R":
                            case "S":
                            case "X":
                                if ( reptNumTxt.getText () != null ) {
                                    putTxt += reptNumTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                if ( MMTxt.getText () != null ) {
                                    putTxt += "," + MMTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                if ( yrTxt.getText () != null ) {
                                    putTxt += "," + yrTxt.getText ();
                                } else {
                                    putTxt += ",";
                                }
                                break;
                            case "T":
                            case "W":
                                break;
                        }
                        tf.setText (putTxt);
                    }
                }
                isSelect = true;
            });
            acceptEdit.setOnAction ((ActionEvent event) -> {
                String parseStr;
                if ( !isSelect ) {
                    popupMsg.warnBox ("Please \"Select\" first",
                            "Attention! Select first");
                } else if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox (
                            "Text Area blank. Use \"Select\" to select data",
                            "Attention! No Data");
                } else {
                    popupMsg.
                            warnBox ("Please enter some entry using \"Select\"",
                                    "Attention! Select data");
                    parseStr = tf.getText ().toString ();
                    if ( parseStr.contains ("(") ) {
                        parseStr = parseStr;
                    } else {
                        parseStr = "(" + parseStr + ")";
                    }
                    myDataN = (act.contains ("Add")) ? myDataN + 1 : myDataN;
                    if ( act.contains ("Edit") ) {
                        myData.set (ri,
                                new editableData (Head, "", parseStr, SE));
                    } else {
                        myData.add (ri,
                                new editableData (Head, "", parseStr, SE));
                        thisEntryLine = Integer.toString (doGetSELineNum (Head));
                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine, Integer.
                                        toString (
                                                bibEntNum),
                                        SE, Head));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    tf.clear ();
                    myDialog.close ();
                }
                System.gc ();
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            changeBoolStatus (Head, false);
            myData.remove (ri);
            editTable.refresh ();
            --myDataN;
            zapBIBEntry (Head);
        }
        procTree ();
        System.gc ();
    }

    private void doEditFacility(int ri, String Head, String act, String SE) {
        String getTxtData = "";
        String temp1;
        int tmpRICnt = 1;
        isSelect = false;

        while (myData.get (ri + tmpRICnt).getBibItemName ().toString () == "") {
            ++tmpRICnt;
        }

        if ( act.contains ("Add") ) {
            getTxtData = "";
            putTxt = "";
        } else {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                getTxtData
                        += myData.get (ri + ii).getContentTxt ().toString () +
                        ",\n";
            }
        }

        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }

        VBox vb1 = new VBox (10);
        VBox vb2 = new VBox (10);
        VBox vb3 = new VBox (10);
        VBox vb4 = new VBox (10);

        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        tf.setPrefWidth (130);
        tf.setWrapText (true);
        hb1.getChildren ().addAll (
                selectIt, acceptEdit, cancelEdit
        );
        hb1.setSpacing (10);
        vb2.getChildren ().addAll (faciL, facilCB);
        vb3.getChildren ().addAll (instL, institute);
        //vb4.getChildren ().addAll (addL);
        hb2.getChildren ().addAll (vb2, vb3);   // , vb4);
        vb1.getChildren ().addAll (tf, hb2, hb1);

        entryChoice (lList.facilList, facilCB);
        entryChoice (lList.instList, institute);

        if ( !act.contains ("Delete") ) {
            tf.setText (getTxtData);
            myDialogScene = new Scene (vb1, 800, 250);
            myDialog.setScene (myDialogScene);
            myDialog.show ();
            addLReacCnt = 0;
            selectIt.setOnAction ((ActionEvent event1) -> {
                String tempA = "";
                String tempB = "";
                String tempC = "";
                tempA = facilCB.getValue ();
                tempA = tempA.substring (0, tempA.indexOf (" "));
                tempB = institute.getValue ();
                tempB = tempB.substring (0, tempB.indexOf (" "));

                tempC = (addLReacCnt == 0) ? "" : tf.getText () + ",\n";
                if ( !putTxt.endsWith (", ") &&
                        putTxt != null &&
                        putTxt != "" &&
                        putTxt.length () > 1 ) {
                    putTxt += ",\n";
                }

                putTxt = tempA + ", " + tempB;
                putTxt = "(" + putTxt + ")";
                tempC = (addLReacCnt == 0) ? putTxt : tempC + putTxt;
                tf.setText (tempC);
                ++addLReacCnt;
                isSelect = true;
            });
            acceptEdit.setOnAction (new EventHandler<ActionEvent> () {
                @Override
                public void handle(ActionEvent event1) {
                    String parseStr;
                    String strEntered = "";
                    String tmpStr1 = Head;
                    String tmpStr2 = "";
                    String tmpHead = "";
                    int lCnt1 = 0;
                    int lCnt2 = 0;

                    if ( !isSelect ) {
                        popupMsg.warnBox ("Use \"Select\" to select data",
                                "Attention! Select not used");
                    } else if ( tf.getText ().isEmpty () ) {
                        popupMsg.warnBox (
                                "Text Area blank. Please select data first",
                                "Attention! No Data");
                    } else {
                        parseStr = tf.getText ().toString ();
                        for ( int ii = 0; ii < parseStr.length (); ii++ ) {
                            if ( parseStr.charAt (ii) == '\n' ) {
                                ++lCnt1;
                            }
                        }
                        tmp1 = "";
                        tmp2 = parseStr;
                        for ( int ii = 0; ii < lCnt1; ii++ ) {
                            tmp3 = tmp2.substring (0, tmp2.indexOf ('\n'));
                            tmp3 = tmp3.substring (0, tmp3.lastIndexOf (","));
                            if ( tmp3.length () >= cutPoint ) {
                                tmp3 = arrTitle (tmp3);
                                lCnt2 += multiLine;
                            } else {
                                ++lCnt2;
                            }
                            multiLine = lCnt2;
                            tmp1 += tmp3 + "@@";  // -- CHECK THIS and for problem replace
                            tmp2 = tmp2.substring (tmp2.indexOf ('\n') + 1);
                        }
                        tmp1 += tmp2;
                        parseStr = tmp1;
                        //parseStr = arrTitle (parseStr);

                        if ( act.contains ("Edit") ) {
                            for ( int ii = 0; ii < multiLine; ii++ ) {
                                tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                                if ( ii <= multiLine - 2 ) {
                                    strEntered = parseStr.substring (
                                            0, parseStr.indexOf ("@@"));
                                } else {
                                    strEntered = parseStr;
                                }
                                myData.set (ri + ii,
                                        new editableData (tmpHead, "",
                                                strEntered,
                                                SE));
                                if ( ii <= multiLine - 2 ) {
                                    parseStr = parseStr.substring (2 + parseStr.
                                            indexOf ("@@"));
                                }
                            }
                        } else {
                            for ( int ii = 0; ii < multiLine; ii++ ) {
                                tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                                if ( ii < multiLine - 1 ) {
                                    strEntered = parseStr.substring (
                                            0, parseStr.indexOf ("@@"));
                                } else {
                                    strEntered = parseStr;
                                }
                                myData.add (ri + ii,
                                        new editableData (tmpHead, "",
                                                strEntered,
                                                SE));
                                ++myDataN;
                                if ( ii < multiLine - 1 ) {
                                    parseStr = parseStr.substring (2 + parseStr.
                                            indexOf ("@@"));
                                }
                            }

                            thisEntryLine = Integer.toString (doGetSELineNum (
                                    Head));
                            bibList.add (
                                    bibEntNum,
                                    new BIBClasses (thisEntryLine,
                                            Integer.toString (bibEntNum),
                                            SE, Head));
                            ++bibEntNum;
                        }
                        editTable.refresh ();
                        subEntTree.refresh ();
                        tf.clear ();
                        myDialog.close ();
                    }
                }
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                myData.remove (ri);
                --myDataN;
            }
            editTable.refresh ();
            --myDataN;
            zapBIBEntry (Head);
            changeBoolStatus (Head, false);
        }
        procTree ();
        System.gc ();
    }

    private void doEditIncSrc(int ri, String Head, String act, String SE) {
        String getTxtData = "";
        int tmpRICnt = 1;
        String temp1;
        isSelect = false;

        while (myData.get (ri + tmpRICnt).getBibItemName ().toString () ==
                "") {
            ++tmpRICnt;
        }

        if ( act.contains ("Add") ) {
            getTxtData = "";
        } else if ( act.contains ("Edit") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                getTxtData
                        += myData.get (ri + ii).getContentTxt ().toString () +
                        " ";
            }
        }
        if ( getTxtData != null ) {
            getTxtData = getTxtData.replaceAll ("\n", " ");
        }

        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }
        VBox vb1 = new VBox (10);
        VBox vb2 = new VBox (10);
        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        tf.setPrefWidth (130);
        tf.setWrapText (true);
        hb1.getChildren ().addAll (
                selectIt, acceptEdit, cancelEdit
        );
        hb1.setSpacing (10);
        vb2.getChildren ().addAll (incsrcL, incSrcCB);
        vb1.getChildren ().addAll (tf, vb2, hb1);

        entryChoice (lList.incSrcList, incSrcCB);

        if ( !act.contains ("DELETE") ) {
            if ( act.contains ("Edit") ) {
                tf.setText (getTxtData);
            }
            putTxt = getTxtData;
            myDialogScene = new Scene (vb1, 800, 250);
            myDialog.setScene (myDialogScene);
            myDialog.show ();
            selectIt.setOnAction ((ActionEvent event1) -> {
                String tempA;
                String tempB;
                tempA = incSrcCB.getValue ();

                tempA = tempA.substring (0, tempA.indexOf (" "));
                tempA = "(" + tempA + ")";
                if ( !putTxt.endsWith (", ") &&
                        putTxt != null &&
                        putTxt != "" &&
                        putTxt.length () > 1 ) {
                    putTxt += ",\n";
                }
                tempB = tf.getText ();
                putTxt = (!tempB.isEmpty ()) ? tempB + ",\n" + tempA
                        : tempA;
                tf.clear ();
                tf.setText (putTxt);
                isSelect = true;
            });
            acceptEdit.setOnAction ((ActionEvent event1) -> {
                String parseStr = "";
                String strEntered = "";
                String tmpStr1 = Head;
                String tmpStr2 = "";
                String tmpHead = "";

                if ( !isSelect ) {
                    popupMsg.warnBox ("Use \"Select\" first",
                            "Attention! use Select");
                } else if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox ("Text Area is blank. Select data first",
                            "Attention! No Data");
                } else {

                    parseStr = tf.getText ().toString ();
                    parseStr = arrTitle (parseStr);

                    if ( act.contains ("Edit") ) {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0, parseStr.indexOf ("@@"));
                            } else {
                                strEntered = parseStr;
                            }
                            myData.set (ri + ii,
                                    new editableData (tmpHead, "", strEntered,
                                            SE));
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (2 + parseStr.
                                        indexOf ("@@"));
                            }
                        }
                    } else {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0, parseStr.indexOf ("@@"));
                            } else {
                                strEntered = parseStr;
                            }
                            myData.add (ri + ii, new editableData (tmpHead, "",
                                    strEntered, SE));
                            ++myDataN;
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (2 + parseStr.
                                        indexOf ("@@"));
                            }
                        }
                        thisEntryLine = Integer.toString (doGetSELineNum (Head));
                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine,
                                        Integer.toString (bibEntNum),
                                        SE, Head));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    subEntTree.refresh ();
                    tf.clear ();
                    myDialog.close ();
                }
                System.gc ();
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                myData.remove (ri);
                --myDataN;
            }
            editTable.refresh ();
            changeBoolStatus (Head, false);
            --myDataN;
            zapBIBEntry (Head);
        }
        procTree ();
        System.gc ();
    }

    private void doEditReact(int ri, String Head, String act, String SE) {
        String getTxtData = "";
        String temp1;
        int tmpRICnt = 1;
        isSelect = false;
        addLReacCnt = 0;
        multiLine = 0; // check here for multiline problem

        while (myData.get (ri + tmpRICnt).getBibItemName ().toString () == "") {
            ++tmpRICnt;
        }

        if ( act.contains ("Add") ) {
            getTxtData = "";
        } else {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                getTxtData
                        += myData.get (ri + ii).getContentTxt ().toString () +
                        " ";
            }
        }
        if ( getTxtData != null ) {
            getTxtData = getTxtData.replaceAll ("\n", " ");
        }
        if ( act.contains ("Add") ) {
            getTxtData = null;
            putTxt = "";
        } else {
            // getTxtData = myData.get (ri).getcontentTxt ().toString ();
            putTxt = getTxtData;
            addLReacCnt = 0;
        }

        //****
        //entryChoice (compoundNucArr, compNucCB);    // SF1
        //entryChoice (incPList, incPCB);        
        entryChoice (lList.mixedSF1List, targetNCB);      // SF1
        entryChoice (lList.mixedSF2List, incPCB);         // SF2
        entryChoice (lList.mixedSF3List, procCB);         // SF3        
        entryChoice (lList.mixedSF4List, prodCB);         // SF4
        entryChoice (lList.branchList, branchCB);         // SF5
        entryChoice (lList.paramSF6List, paramSF6CB);     // SF6
        entryChoice (lList.paramSF7List, paramSF7CB);     // SF7
        entryChoice (lList.modifierList, modifierCB);     // SF8
        entryChoice (lList.dataTypeList, dataTypeCB);     // SF9

        if ( !putTxt.isEmpty () ) {
            String[] tmp = putTxt.split ("\\(|\\,|\\)");
            for ( String item : tmp ) {
                if ( !tmp[1].isEmpty () ) {
                    targetNCB.setValue (tmp[1]);
                }
                if ( !tmp[2].isEmpty () ) {
                    incPCB.setValue (tmp[2]);
                }
                if ( !tmp[3].isEmpty () ) {
                    procCB.setValue (tmp[3]);
                }
                if ( !tmp[4].isEmpty () ) {
                    prodCB.setValue (tmp[4]);
                }
            }

        }
        myDialog.setTitle ("Enter Reaction Details:");
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }

        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        HBox hb3 = new HBox ();
        HBox hb4 = new HBox ();
        HBox hb5 = new HBox ();
        HBox hb6 = new HBox ();
        VBox vb1 = new VBox ();
        VBox vb2 = new VBox ();
        VBox vb3 = new VBox ();
        VBox vb4 = new VBox ();
        VBox vb5 = new VBox ();
        VBox vb6 = new VBox ();
        VBox vb7 = new VBox ();
        VBox vb8 = new VBox ();
        VBox vb9 = new VBox ();
        VBox vb10 = new VBox ();
        VBox vb11 = new VBox ();

        tf.setPrefWidth (130);
        tf.setWrapText (true);

        hb1.getChildren ().addAll (
                selectIt, acceptEdit, cancelEdit
        );
        vb1.getChildren ().addAll (sf1L, targetNCB); // SF1
        vb2.getChildren ().addAll (sf2L, incPCB); // SF2
        hb6.getChildren ().addAll (procCB, addL); // Add button beside SF3
        vb3.getChildren ().addAll (sf3L, hb6); // SF3
        vb4.getChildren ().addAll (sf4L, prodCB); // SF4
        vb5.getChildren ().addAll (sf5L, branchCB); // SF5
        vb6.getChildren ().addAll (sf6L, paramSF6CB); // SF6
        vb7.getChildren ().addAll (sf7L, paramSF7CB); // SF7
        vb8.getChildren ().addAll (sf8L, modifierCB); // SF8
        vb9.getChildren ().addAll (sf9L, dataTypeCB); // SF9

        hb3.getChildren ().addAll (vb1, vb2, vb3);
        hb4.getChildren ().addAll (vb4, vb5, vb6);
        hb5.getChildren ().addAll (vb7, vb8, vb9);
        hb1.setSpacing (10);
        vb11.getChildren ().addAll (tf, hb3, hb4, hb5, hb1);
        targetNCB.setMaxWidth (180);
        incPCB.setMaxWidth (180);
        procCB.setMaxWidth (180);
        prodCB.setMaxWidth (180);
        branchCB.setMaxWidth (180);
        paramSF6CB.setMaxWidth (180);
        paramSF7CB.setMaxWidth (180);
        modifierCB.setMaxWidth (180);
        dataTypeCB.setMaxWidth (180);

        

        tp1.getStyleClass ().add ("ttip");
        tp2.getStyleClass ().add ("ttip");
        tp3.getStyleClass ().add ("ttip");
        tp4.getStyleClass ().add ("ttip");
        tp5.getStyleClass ().add ("ttip");
        tp6.getStyleClass ().add ("ttip");
        tp7.getStyleClass ().add ("ttip");
        tp8.getStyleClass ().add ("ttip");
        tp9.getStyleClass ().add ("ttip");
        tp10.getStyleClass ().add ("ttip");

        tp1.setText (SF1RTip);
        tp2.setText (SF2RTip);
        tp3.setText (SF3RTip);
        tp4.setText (SF4RTip);
        tp5.setText (SF5RTip);
        tp6.setText (SF6RTip);
        tp7.setText (SF7RTip);
        tp8.setText (SF8RTip);
        tp9.setText (SF9RTip);
        tp10.setText (SF3Add);
        targetNCB.setTooltip (tp1);
        incPCB.setTooltip (tp2);
        procCB.setTooltip (tp3);
        addL.setTooltip (tp10);
        prodCB.setTooltip (tp4);
        branchCB.setTooltip (tp5);
        paramSF6CB.setTooltip (tp6);
        paramSF7CB.setTooltip (tp7);
        modifierCB.setTooltip (tp8);
        dataTypeCB.setTooltip (tp9);
        tooltipDelay (tp1, targetNCB);
        tooltipDelay (tp2, incPCB);
        tooltipDelay (tp3, procCB);
        tooltipDelay (tp4, prodCB);
        tooltipDelay (tp5, branchCB);
        tooltipDelay (tp6, paramSF6CB);
        tooltipDelay (tp7, paramSF7CB);
        tooltipDelay (tp8, modifierCB);
        tooltipDelay (tp9, dataTypeCB);
        tooltipDelay (tp10, addL);

        //*****
        if ( !act.contains ("Delete") ) {
            if ( act.contains ("Edit") ) {
                tf.setText (getTxtData);
            }
            myDialogScene = new Scene (vb11, 800, 300);
            myDialogScene.getStylesheets ().add (getClass ().getResource (
                    "CSS/mainscreen.css").toExternalForm ());
            myDialog.setScene (myDialogScene);
            myDialog.show ();

            addL.setOnAction (e -> {
                if ( addLReacCnt > 0 ) {
                    demoStr0 += "+" + exforUtil.fixGet (procCB.getValue ()); // SF3
                } else {
                    demoStr0 += exforUtil.fixGet (procCB.getValue ()); // SF3
                }
                addLReac = (addLReac == false) ? true : addLReac;
                ++addLReacCnt;
            });
            selectIt.setOnAction ((ActionEvent event1) -> {
                String tmpA = "";
                String tmpB = "";

                String s1 = (!targetNCB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (targetNCB.getValue ()) : "";
                String s2 = (!incPCB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (incPCB.getValue ()) : "";
                String s3 = demoStr0;
                String s4 = (!prodCB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (prodCB.getValue ()) : "";
                String s5 = (!branchCB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (branchCB.getValue ()) : "";        // SF5
                String s6 = (!paramSF6CB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (paramSF6CB.getValue ()) : "";      // SF6
                String s7 = (!paramSF7CB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (paramSF7CB.getValue ()) : "";      // SF7
                String s8 = (!modifierCB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (modifierCB.getValue ()) : "";      // SF8
                String s9 = (!dataTypeCB.getValue ().isEmpty ()) ? exforUtil.
                        fixGet (dataTypeCB.getValue ()) : "";      // SF9
                Reaction.Reaction (ri, s1, s2, s3, s4, s5, s6, s7, s8, s9);

                tmpA = "(" + s1;
                tmpA += "(" + s2;
                tmpA += "," + s3;
                tmpA += ")" + s4;
                tmpA += "," + s5;
                tmpA += "," + s6;

                tmpA += (!s7.isEmpty ()) ? "," + s7 : "";
                tmpA += (!s8.isEmpty ()) ? "," + s8 : "";
                tmpA += (!s9.isEmpty ()) ? "," + s9 : "";
                tmpA += ")";
                demoStr1 = Integer.toString (tmpA.length ()) + " ";

                if ( !putTxt.endsWith (", ") &&
                        putTxt != null &&
                        putTxt != "" &&
                        putTxt.length () > 1 ) {
                    putTxt += ",\n";
                }
                putTxt += tmpA;
                tf.setText (putTxt);
                ++multiLine;
                addLReacCnt = 0;
                demoStr0 = "";
                isSelect = true;
            });

            acceptEdit.setOnAction ((ActionEvent event1) -> {
                String parseStr;
                String strEntered = "";
                String tmpStr1 = "";
                String tmpStr2 = "";
                String tmpHead = "";
                String labelS;
                parseStr = tf.getText ().toString ();
                reacStrLen = parseStr.split ("\\s+");
                //(demoStr1.split (" ");

                if ( !isSelect ) {
                    popupMsg.warnBox ("Please press \"Select\" first",
                            "Attention! Select first");
                } else if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox ("Text Area is blank.",
                            "Attention! no Data");
                } else {
                    tmpStr1 = Head;
                    if ( act.contains ("Edit") ) {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0, Integer.valueOf (reacStrLen[ii]));
                            } else {
                                strEntered = parseStr;
                            }
                            labelS = (multiLine > 1) ? Integer.toString (ii + 1)
                                    : "";
                            myData.set (ri + ii,
                                    new editableData (
                                            tmpHead,
                                            labelS,
                                            strEntered, SE)
                            );
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (
                                        parseStr.indexOf ("),") + 3);
                            }
                        }
                    } else {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0,
                                        Integer.valueOf (reacStrLen[ii].
                                                length ())
                                );
                                strEntered = strEntered.substring (
                                        0, strEntered.lastIndexOf (",")
                                );
                            } else {
                                strEntered = parseStr;
                            }
                            labelS = (multiLine > 1) ? Integer.toString (ii + 1)
                                    : "";
                            myData.add (ri + ii,
                                    new editableData (
                                            tmpHead,
                                            labelS,
                                            strEntered,
                                            SE
                                    ));
                            ++myDataN;
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (
                                        parseStr.indexOf ("),") + 3);
                            }
                        }
                        thisEntryLine = Integer.toString (doGetSELineNum (Head));
                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine, Integer.
                                        toString (
                                                bibEntNum),
                                        SE, tmpStr1));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    tf.clear ();
                    myDialog.close ();
                }
                System.gc ();
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                editTable.refresh ();
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                myData.remove (ri);
                --myDataN;
            }
            editTable.refresh ();
            zapBIBEntry (Head);
            changeBoolStatus (Head, false);
        }
        procTree ();
        System.gc ();
    }

    private void doMonitRef(int ri, String Head, String act, String SE) {
        String getTxtData;
        String headingTxt;

        isSelect = false;
        if ( act.contains ("Add") ) {
            getTxtData = null;
            putTxt = "";
        } else {
            getTxtData = myData.get (ri).getContentTxt ().toString ();
            if ( getTxtData.startsWith ("(") ) {
                getTxtData = getTxtData.replace ("(", "");
            }
            if ( getTxtData.endsWith (")") ) {
                getTxtData = getTxtData.replace (")", "");
            }
            putTxt = getTxtData;
        }
        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }
        VBox vb1 = new VBox (10);
        VBox vb2 = new VBox (10);
        VBox vb3 = new VBox (10);
        VBox vb4 = new VBox (10);
        VBox vb5 = new VBox (10);
        VBox vb6 = new VBox (10);
        VBox vb7 = new VBox (10);
        VBox vb8 = new VBox (10);
        VBox vb9 = new VBox (10);
        VBox vb10 = new VBox (10);
        VBox vb11 = new VBox (10);
        VBox vb12 = new VBox (10);
        VBox vb13 = new VBox (10);
        VBox vb14 = new VBox (10);
        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        HBox hb3 = new HBox ();
        HBox hb4 = new HBox ();
        HBox hb5 = new HBox ();
        tf.setPrefWidth (130);
        tf.setPrefHeight (120);
        tf.setWrapText (true);
        volTxt.setMaxWidth (50);
        pgTxt.setMaxWidth (50);
        yrTxt.setMaxWidth (50);
        headingL.setStyle ("-fx-font-size: 10pt;");
        subAccL.setStyle ("-fx-font-size: 10pt;");
        authL.setStyle ("-fx-font-size: 10pt;");
        monitRefL.setStyle ("-fx-font-size: 10pt;");
        volL.setStyle ("-fx-font-size: 10pt;");
        pgL.setStyle ("-fx-font-size: 10pt;");
        yrL.setStyle ("-fx-font-size: 10pt;");

        tab1.setText ("Standard Reference");
        tab2.setText ("Data Library");
        tab1.setClosable (false);
        tab2.setClosable (false);
        tab1.setStyle ("-fx-font-size: 9pt;");
        tab2.setStyle ("-fx-font-size: 9pt;");

        headingC.setStyle (
                "-fx-border-color: lightblue; " +
                "-fx-font-size: 15;" +
                "-fx-border-insets: -5; " +
                "-fx-border-radius: 5;" +
                "-fx-border-style: dotted;" +
                "-fx-border-width: 2;"
        );
        hb1.getChildren ().addAll (headingL, headingC,
                subAccL, subAcc, authL, authT, addRCB);
        hb3.getChildren ().addAll (selectIt, acceptEdit, cancelEdit);

        // ******------>  TAB1 : standard reference setting
        vb2.getChildren ().addAll (jtypeL, jType);
        vb3.getChildren ().addAll (refSrcL, journals);
        vb4.getChildren ().addAll (volL, volTxt);
        vb5.getChildren ().addAll (pgL, pgTxt);
        vb6.getChildren ().addAll (reptL, reptNumTxt);
        vb7.getChildren ().addAll (reptMML, MMTxt);
        vb8.getChildren ().addAll (yrL, yrTxt);
        volTxt.setMinWidth (50);
        pgTxt.setMinWidth (50);
        yrTxt.setMinWidth (70);
        reptNumTxt.setMinWidth (70);
        MMTxt.setMinWidth (70);
        MMTxt.setMaxWidth (90);
        reptNumTxt.setMaxWidth (90);
        yrTxt.setMaxWidth (90);
        volTxt.setMaxWidth (50);
        pgTxt.setMaxWidth (50);
        journals.setMinWidth (200);
        jType.setMinWidth (200);
        monitRefCB.setMinWidth (300);
        hb2.getChildren ().addAll (vb2, vb3, vb4, vb5, vb6, vb7, vb8);

        vb9.getChildren ().addAll (hb2);
        winPane1.setPrefWidth (1190);
        winPane1.getChildren ().addAll (vb9);
        tab1.setContent (winPane1);

        // ******------>  TAB2 :: Data Library setting
        volMTxt.clear ();
        volMTxt.clear ();
        pgMTxt.clear ();
        yrMTxt.clear ();
        vb10.getChildren ().addAll (monitRefL, monitRefCB);
        vb11.getChildren ().addAll (volML, volMTxt);
        vb12.getChildren ().addAll (matML, pgMTxt);
        vb13.getChildren ().addAll (yrML, yrMTxt);
        hb4.getChildren ().addAll (vb10, vb11, vb12, vb13);
        vb14.getChildren ().addAll (hb4);
        winPane2.setPrefWidth (1190);
        winPane2.getChildren ().addAll (vb14);
        volMTxt.setMinWidth (50);
        pgMTxt.setMinWidth (50);
        yrMTxt.setMinWidth (50);
        tab2.setContent (winPane2);

        hb1.setSpacing (10);
        hb2.setSpacing (10);
        hb3.setSpacing (10);
        hb4.setSpacing (10);
        hb5.setSpacing (10);

        myTabPane.getTabs ().addAll (tab1, tab2);
        myTabPane.setSide (Side.LEFT);
        myTabPane.getSelectionModel ().select (tab1);

        //myTabPane.setSide (Side.TOP);
        vb1.getChildren ().addAll (tf, hb1, myTabPane, hb3);
        rootGr.getChildren ().addAll (vb1);
        winPane.getChildren ().add (rootGr);
        entryChoice (lList.monitRefList, monitRefCB);
        entryChoice (lList.jTypeList, jType);
        entryChoice (lList.jourList, journals);
        if ( !act.contains ("Delete") ) {
            if ( act.contains ("Edit") ) {
                tf.setText (putTxt);
            }
            myDialogScene = new Scene (winPane, 1200, 450);
            myDialog.setScene (myDialogScene);
            myDialog.show ();

            addRCB.setOnAction (new EventHandler<ActionEvent> () {
                String tmp1;

                @Override
                public void handle(ActionEvent event1) {
                    headingC.selectedProperty ().addListener (
                            new ChangeListener<Boolean> () {
                        public void changed(
                                ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                            headingCB = new_val; //headingC.isSelected ();
                        }
                    });
                    putTxt = (act.contains ("Add")) ? "" : putTxt;
                    if ( headingCB ) {
                        ++headingNum;
                        tmp1 = String.format ("(MONIT%s",
                                Integer.toString (headingNum));
                        if ( headingCB ) {
                            tmp1 += ")";
                        }
                    } else {
                        tmp1 = "";
                    }
                    tmp1 += ",";
                    if ( !subAcc.getText ().isEmpty () ) {
                        tmp1 += subAcc.getText () + ",";
                    }
                    if ( !authT.getText ().isEmpty () ) {
                        tmp1 += authT.getText () + ",";
                    }
                    putTxt += tmp1;
                    tmp1 = "";
                }
            });

            selectIt.setOnAction ((ActionEvent event1) -> {
                String tmp1 = "";
                String tmp = "";
                String[] sComps;
                String date = "";
                // TAB1 : General             
                if ( tab1B && !tabSelected ) {
                    tmp = (!jType.getValue ().isEmpty ())
                            ? jType.getValue () : "";
                    tmp1 = tmp.substring (1, 2);
                    putTxt += tmp1 + ",";
                    tmp = journals.getValue ();
                    tmp = tmp.substring (0, tmp.indexOf (" "));
                    putTxt += tmp;
                    switch (tmp1) {
                        case "0":
                        case "3":
                        case "4":
                            break;
                        case "A":
                        case "B":
                        case "C":
                            if ( volTxt.getText () != null ) {
                                putTxt += "," + volTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            if ( pgTxt.getText () != null ) {
                                putTxt += "," + pgTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            if ( yrTxt.getText () != null ) {
                                putTxt += "," + yrTxt.getText ();
                            }
                            break;
                        case "J":
                        case "K":
                            if ( volTxt.getText () != null ) {
                                putTxt += "," + volTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            if ( pgTxt.getText () != null ) {
                                putTxt += "," + pgTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            if ( yrTxt.getText () != null ) {
                                putTxt += "," + yrTxt.getText ();
                            }
                            break;
                        case "P":
                        case "R":
                        case "S":
                        case "X":
                            if ( reptNumTxt.getText () != null ) {
                                putTxt += "-" + reptNumTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            if ( MMTxt.getText () != null ) {
                                putTxt += "," + MMTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            if ( yrTxt.getText () != null ) {
                                putTxt += "," + yrTxt.getText ();
                            } else {
                                putTxt += ",";
                            }
                            break;
                        case "T":
                        case "W":
                            break;
                    }
                    putTxt = "(" + putTxt + ")";
                    tf.setText (putTxt);
                    tmp1 = "";
                    tmp = "";
                    tabSelected = true;
                }
                if ( tab2B && !tabSelected ) {
                    if ( monitRefCB.getValue () != null ) {
                        tmp1 = monitRefCB.getValue ();
                        tmp1 = tmp1.substring (0, tmp1.indexOf (" "));
                        putTxt += "3," + tmp1 + ",";
                    }
                    if ( volMTxt.getText () != null ) {
                        putTxt += volMTxt.getText ().trim () + ",";
                    }
                    if ( pgMTxt.getText () != null ) {
                        putTxt += pgMTxt.getText ().trim () + ",";
                    }
                    if ( yrMTxt.getText () != null ) {
                        putTxt += yrMTxt.getText ().trim ();
                    }
                    tmp1 = "";
                    tabSelected = true;
                }
                //putTxt = "(" + putTxt + ")";
                //putTxt = putTxt;
                tf.setText (putTxt);
                isSelect = true;
            });
            acceptEdit.setOnAction ((ActionEvent event1) -> {
                String parseStr;
                if ( !isSelect ) {
                    popupMsg.warnBox ("Please ress \"Select\" to select data",
                            "Attention! Select first");
                } else if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox ("No Data is selected.",
                            "Attention! No Data");
                } else {

                    parseStr = tf.getText ().toString ();
                    if ( parseStr.contains ("(") ) {
                        parseStr = parseStr;
                    } else {
                        parseStr = "(" + parseStr + ")";
                    }
                    myDataN = (act.contains ("Add")) ? myDataN + 1 : myDataN;
                    if ( act.contains ("Edit") ) {
                        myData.set (ri,
                                new editableData (Head, "", parseStr, SE));
                    } else {
                        myData.add (ri,
                                new editableData (Head, "", parseStr, SE));
                        thisEntryLine = Integer.toString (doGetSELineNum (Head));
                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine,
                                        Integer.toString (bibEntNum),
                                        SE, Head));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    subEntTree.refresh ();
                    tf.clear ();
                    winPane.getChildren ().clear ();
                    myDialog.close ();
                }
                System.gc ();
            });
            cancelEdit.setOnAction ((ActionEvent event1) -> {
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                winPane.getChildren ().clear ();
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            changeBoolStatus (Head, false);
            myData.remove (ri);
            editTable.refresh ();
            --myDataN;
            zapBIBEntry (Head);
        }
        procTree ();
        System.gc ();
    }

    /*
     * This is a new common procedure for better matrix manipulation including
     * copy and paste as suggested by Rema
     */
    private void doCommon(int ri, String Head, String act, String SE) {
        if ( act.contains ("Edit") ) {
            if ( Head.contains ("COMMON") ) {
                myData.set (ri, new editableData ("NOCOMMON", "", "", SE));
                int ii = ri + 1;
                while (!myData.get (ii).getBibItemName ().contains ("ENDCOMMON")) {
                    myData.remove (ii);
                    --myDataN;
                    editTable.refresh ();
                }

                myData.remove (ii);
                --myDataN;
                editTable.refresh ();
                zapBIBEntry (Head);
                procTree ();
                changeBoolStatus (Head, false);
                System.gc ();
                return;
            } else if ( Head.contains ("DATA") ) {
                int ii = ri;
                while (!myData.get (ii).getBibItemName ().contains ("ENDDATA")) {
                    myData.remove (ii);
                    --myDataN;
                    editTable.refresh ();
                }
                myData.remove (ii);
                --myDataN;
                editTable.refresh ();
                zapBIBEntry (Head);
                procTree ();
                changeBoolStatus (Head, false);
                System.gc ();
                return;
            }
        }

        entryChoice (lList.dataHeadingList, dataHeadingCB);
        entryChoice (lList.dataHeadingList, dHCB1, "Head");
        entryChoice (lList.dataHeadingList, dHCB2, "Head");
        entryChoice (lList.dataHeadingList, dHCB3, "Head");
        entryChoice (lList.dataHeadingList, dHCB4, "Head");
        entryChoice (lList.dataHeadingList, dHCB5, "Head");
        entryChoice (lList.dataHeadingList, dHCB6, "Head");
        entryChoice (lList.dataUnitsList, dUCB1, "Units");
        entryChoice (lList.dataUnitsList, dUCB2, "Units");
        entryChoice (lList.dataUnitsList, dUCB3, "Units");
        entryChoice (lList.dataUnitsList, dUCB4, "Units");
        entryChoice (lList.dataUnitsList, dUCB5, "Units");
        entryChoice (lList.dataUnitsList, dUCB6, "Units");
        myDialog.setTitle ("Enter " + Head);
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }

        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        HBox hb3 = new HBox ();
        HBox hb4 = new HBox ();
        VBox vb1 = new VBox ();
        VBox vb2 = new VBox ();
        hb1.getChildren ().addAll (dHCB1, dHCB2, dHCB3, dHCB4, dHCB5, dHCB6);
        hb2.getChildren ().addAll (dUCB1, dUCB2, dUCB3, dUCB4, dUCB5, dUCB6);
        vb1.getChildren ().addAll (hb1, hb2, matrixData);
        hb3.getChildren ().addAll (acceptEdit, cancelEdit);
        vb2.getChildren ().addAll (vb1, hb3);
        hb4.getChildren ().addAll (vb2, numNumL, numColL, numCol, chngCol);

        numCol.setMaxWidth (40);

        hb1.setSpacing (0);

        dHCB1.setMaxWidth (maxW);
        dHCB2.setMaxWidth (maxW);
        dHCB3.setMaxWidth (maxW);
        dHCB4.setMaxWidth (maxW);
        dHCB5.setMaxWidth (maxW);
        dHCB6.setMaxWidth (maxW);
        dUCB1.setMaxWidth (maxW);
        dUCB2.setMaxWidth (maxW);
        dUCB3.setMaxWidth (maxW);
        dUCB4.setMaxWidth (maxW);
        dUCB5.setMaxWidth (maxW);
        dUCB6.setMaxWidth (maxW);

        ////////////////////////////////
        matrixData.getSelectionModel ().setCellSelectionEnabled (true);
        matrixData.getSelectionModel ().
                setSelectionMode (SelectionMode.MULTIPLE);
        /////////////////////////////////
        myDialogScene = new Scene (hb4, 1200, 300);
        myDialog.setScene (myDialogScene);

        myDialog.show ();

        acceptEdit.setOnAction ((ActionEvent ev) -> {
            String parseStr = "";
            String strEntered = "";
            int rowN = 0;
            int colN = 0;
            int lineCnt = 0;
            String[] strCOL = {"", "", "", "", "", ""};
            String col11 = "";

            rowN = maxRowNum + 1;
            parseStr = exforUtil.fixString11 (Integer.toString (numC));
            parseStr += exforUtil.fixString11 (Integer.toString (rowN));
            if ( Head.contains ("COMMON") ) {
                myData.set (ri, new editableData (Head, "", parseStr, SE));
            } else if ( Head.contains ("DATA") ) {
                myData.add (ri, new editableData (Head, "", parseStr, SE));
            }
            ++myDataN;
            thisEntryLine = Integer.toString (doGetSELineNum (Head));
            bibList.add (bibEntNum, new BIBClasses (thisEntryLine, Integer.
                    toString (bibEntNum), SE, Head));
            ++bibEntNum;
            ++lineCnt;
            parseStr = "";

            // Here we are fixing Heading
            strCOL[0] = (!dHCB1.getValue ().isEmpty ()) ? exforUtil.
                    fixString11 (dHCB1.
                            getValue ().substring (0, 10).trim (), 10, true)
                    : "";

            strCOL[1] = (dHCB2.isVisible () && !dHCB2.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dHCB2.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[1] = (strCOL[1].length () > 2 && strCOL[1].contains (" "))
                    ? strCOL[1].substring (0, 10) : strCOL[1];
            parseStr = (!strCOL[1].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[1], 10,
                    true) : "";

            strCOL[2] = (dHCB3.isVisible () && !dHCB3.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dHCB3.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[2] = (strCOL[2].length () > 2 && strCOL[2].contains (" "))
                    ? strCOL[2].substring (0, 10) : strCOL[2];
            parseStr += (!strCOL[2].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[2], 10,
                    true) : "";

            strCOL[3] = (dHCB4.isVisible () && !dHCB4.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dHCB4.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[3] = (strCOL[3].length () > 2 && strCOL[3].contains (" "))
                    ? strCOL[3].substring (0, 10) : strCOL[3];
            parseStr += (!strCOL[3].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[3], 10,
                    true) : "";

            strCOL[4] = (dHCB5.isVisible () && !dHCB5.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dHCB5.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[4] = (strCOL[4].length () > 2 && strCOL[4].contains (" "))
                    ? strCOL[4].substring (0, 10) : strCOL[4];
            parseStr += (!strCOL[4].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[4], 10,
                    true) : "";

            strCOL[5] = (dHCB6.isVisible () && !dHCB6.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dHCB6.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[5] = (strCOL[5].length () > 2 && strCOL[5].contains (" "))
                    ? strCOL[5].substring (0, 10) : strCOL[5];
            parseStr += (!strCOL[5].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[5], 10,
                    true) : "";

            myData.add (ri + lineCnt, new editableData (strCOL[0], "", parseStr,
                    SE));
            ++myDataN;
            parseStr = "";
            ++lineCnt;

            // Here we are fixing Units
            strCOL[0] = (!dUCB1.getValue ().isEmpty ()) ? exforUtil.
                    fixString11 (dUCB1.
                            getValue ().substring (0, 10).trim (), 10, true)
                    : "";

            strCOL[1] = (dUCB2.isVisible () && !dUCB2.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dUCB2.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[1] = (strCOL[1].length () > 2 && strCOL[1].contains (" "))
                    ? strCOL[1].substring (0, 10) : strCOL[1];
            parseStr = (!strCOL[1].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[1], 10,
                    true) : "";

            strCOL[2] = (dUCB3.isVisible () && !dUCB3.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dUCB3.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[2] = (strCOL[2].length () > 2 && strCOL[2].contains (" "))
                    ? strCOL[2].substring (0, 10) : strCOL[2];
            parseStr += (!strCOL[2].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[2], 10,
                    true) : "";

            strCOL[3] = (dUCB4.isVisible () && !dUCB4.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dHCB4.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[3] = (strCOL[3].length () > 2 && strCOL[3].contains (" "))
                    ? strCOL[3].substring (0, 10) : strCOL[3];
            parseStr += (!strCOL[3].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[3], 10,
                    true) : "";

            strCOL[4] = (dUCB5.isVisible () && !dUCB5.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dUCB5.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[4] = (strCOL[4].length () > 2 && strCOL[4].contains (" "))
                    ? strCOL[4].substring (0, 10) : strCOL[4];
            parseStr += (!strCOL[4].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[4], 10,
                    true) : "";

            strCOL[5] = (dUCB6.isVisible () && !dUCB6.getValue ().isEmpty ())
                    ? exforUtil.fixString11 (
                            dUCB6.getValue ().substring (0, 10).trim (),
                            true) : "";
            strCOL[5] = (strCOL[5].length () > 2 && strCOL[5].contains (" "))
                    ? strCOL[5].substring (0, 10) : strCOL[5];
            parseStr += (!strCOL[5].isEmpty ()) ? exforUtil.fixString11 (
                    strCOL[5], 10,
                    true) : "";

            myData.add (ri + lineCnt, new editableData (strCOL[0], "", parseStr,
                    SE));
            ++myDataN;
            parseStr = "";

            for ( int jj = 0; jj < rowN; jj++ ) {
                ++lineCnt;
                strCOL[0] = matrixData.getItems ().get (jj).getMyD1 ().trim ();
                strCOL[1] = matrixData.getItems ().get (jj).getMyD2 ().trim ();
                strCOL[2] = matrixData.getItems ().get (jj).getMyD3 ().trim ();
                strCOL[3] = matrixData.getItems ().get (jj).getMyD4 ().trim ();
                strCOL[4] = matrixData.getItems ().get (jj).getMyD5 ().trim ();
                strCOL[5] = matrixData.getItems ().get (jj).getMyD6 ().trim ();

                if ( strCOL[0].length () > 11 ) {
                    strCOL[0] = strCOL[0].substring (0, 11); /// concatenation for length > 11
                }

                col11 = (strCOL[0].length () > 2) ? strCOL[0].substring (
                        strCOL[0].length () - 1) : "";
                strCOL[0] = (strCOL[0].length () > 1)
                        ? strCOL[0].substring (0, strCOL[0].length () - 1)
                        : strCOL[0];

                strCOL[0] = exforUtil.fixString11 (strCOL[0], 10);
                strCOL[1] = exforUtil.fixString11 (strCOL[1]);
                strCOL[2] = exforUtil.fixString11 (strCOL[2]);
                strCOL[3] = exforUtil.fixString11 (strCOL[3]);
                strCOL[4] = exforUtil.fixString11 (strCOL[4]);
                strCOL[5] = exforUtil.fixString11 (strCOL[5]);
                parseStr = strCOL[1] + strCOL[2] + strCOL[3] + strCOL[4] +
                        strCOL[5];
                myData.add (ri + lineCnt, new editableData (strCOL[0], col11,
                        parseStr, SE));
                ++myDataN;
            }
            parseStr = exforUtil.fixString11 (Integer.toString (numC));
            parseStr += exforUtil.fixString11 (Integer.toString (0));
            if ( Head.contains ("COMMON") ) {
                myData.add (ri + lineCnt + 1, new editableData ("ENDCOMMON", "",
                        parseStr, SE));
            } else if ( Head.contains ("DATA") ) {
                myData.add (ri, new editableData ("ENDDATA", "", parseStr, SE));
            }
            ++myDataN;
            editTable.refresh ();
            parseStr = "";

            editTable.refresh ();
            myDialog.close ();
        });

        cancelEdit.setOnAction ((ActionEvent event) -> {
            editTable.refresh ();
            if ( act.contains ("Add") ) {
                changeBoolStatus (Head, false);
            }
            myDialog.close ();
        });
        procTree ();
        System.gc ();
    }

    // Decay-Data and DECAY-MON
    private void doDD(int ri, String Head, String act, String SE) {
        String getTxtData = "";
        String headingTxt;
        int tmpRICnt = 1;
        String tmp = "";
        isSelect = false;

        multiLine = 0;

        while (myData.get (ri + tmpRICnt).getBibItemName ().toString () == "") {
            ++tmpRICnt;
        }
        if ( act.contains ("Add") ) {
            getTxtData = "";
        } else {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                tmp = myData.get (ri + ii).getContentTxt ().toString ();
                tmp = tmp.substring (1, tmp.length () - 1);
                getTxtData
                        += tmp +
                        "\n";
            }
        }

        if ( act.contains ("Add") ) {
            getTxtData = null;
            putTxt = "";
        } else {
            putTxt = getTxtData;
            addLReacCnt = 0;
        }
        entryChoice (lList.targetNList, nuclideDDCB);
        entryChoice (lList.dataUnitsList, dataUnitsCB);
        entryChoice (lList.targetNList, radSF1DDCB);
        myDialog.setTitle ("Enter Reaction Details:");
        if ( !myDialog.getModality ().toString ().contains ("MODAL") ) {
            myDialog.initModality (Modality.WINDOW_MODAL);
            myDialog.initOwner (myStage);
        }
        HBox hb1 = new HBox ();
        HBox hb2 = new HBox ();
        HBox hb3 = new HBox ();
        HBox hb4 = new HBox ();
        HBox hb5 = new HBox ();
        VBox vb1 = new VBox ();

        tf.setPrefWidth (130);
        tf.setWrapText (true);

        hb1.getChildren ().addAll (flagL, DDFlagC);
        hb2.getChildren ().addAll (nuclL, nuclideDDCB);
        hb3.getChildren ().addAll (HalfL, THalfDDT, dataUnitL, dataUnitsCB);
        hb4.getChildren ().addAll (
                SF1DDL, radSF1DDCB, SF2DDL, SF2EnergyDDT, SF3DDL, SF3abandDDT
        );
        hb5.getChildren ().addAll (
                selectIt, acceptEdit, cancelEdit
        );
        vb1.getChildren ().addAll (tf, hb1, hb2, hb3, hb4, hb5);
        DDFlagC.setStyle (
                "-fx-border-color: lightblue; " +
                "-fx-font-size: 12;" +
                "-fx-border-insets: -5; " +
                "-fx-border-radius: 5;" +
                "-fx-border-style: dotted;" +
                "-fx-border-width: 2;"
        );
        nuclideDDCB.setMaxWidth (130);
        THalfDDT.setMaxWidth (maxW);
        radSF1DDCB.setMaxWidth (130);
        SF2EnergyDDT.setMaxWidth (maxW);
        SF3abandDDT.setMaxWidth (maxW);

        if ( !act.contains ("Delete") ) {
            if ( act.contains ("Edit") ) {
                tf.setText (getTxtData);
            }

            tp1.getStyleClass ().add ("ttip");
            tp2.getStyleClass ().add ("ttip");
            tp3.getStyleClass ().add ("ttip");
            tp4.getStyleClass ().add ("ttip");
            tp5.getStyleClass ().add ("ttip");
            tp1.setText (nucDDTip);
            tp2.setText (halfLifeTip);
            tp3.setText (radSF1Tip);
            tp4.setText (radSF2Tip);
            tp5.setText (radSF3Tip);
            nuclideDDCB.setTooltip (tp1);
            THalfDDT.setTooltip (tp2);
            radSF1DDCB.setTooltip (tp3);
            SF2EnergyDDT.setTooltip (tp4);
            SF3abandDDT.setTooltip (tp5);

            myDialogScene = new Scene (vb1, 800, 300);
            myDialogScene.getStylesheets ().add (getClass ().getResource (
                    "CSS/mainscreen.css").toExternalForm ());
            myDialog.setScene (myDialogScene);
            myDialog.show ();

            selectIt.setOnAction ((ActionEvent event1) -> {
                String tmp1 = "";
                String tmp2 = "";

                // --> putTxt = (act.contains ("Add")) ? "" : putTxt;
                if ( DDFlagB ) {
                    ++DDFlagNum;
                    tmp1 = String.format ("(%s",
                            Integer.toString (DDFlagNum)
                    );
                    if ( DDFlagB ) {
                        tmp1 += ".)";
                    }
                } else {
                    tmp1 = "";
                }
                tmp2 = tmp1;

                tmp1 = nuclideDDCB.getValue ().toString ();
                sComp = tmp1.split ("\\s+");
                tmp1 = sComp[1];
                tmp1 = (!tmp1.isEmpty ()) ? tmp1 : "";
                tmp2 += tmp1 + ",";

                tmp1 = THalfDDT.getText ();
                sComp = tmp1.split ("\\s+");
                tmp1 = sComp[1];

                tmp1 = (!tmp1.isEmpty ()) ? tmp1 : "";
                tmp2 += tmp1;

                tmp1 = dataUnitsCB.getValue ().toString ();
                tmp1 = tmp1.substring (0, tmp1.indexOf (" "));
                tmp1 = (!tmp1.isEmpty ()) ? tmp1 : "";
                tmp2 += tmp1 + ",";

                tmp1 = radSF1DDCB.getValue ().toString ();
                sComp = tmp1.split ("\\s+");
                tmp1 = sComp[1];
                tmp1 = tmp1.substring (
                        tmp1.indexOf ("-") + 1, tmp1.lastIndexOf ("-"));
                tmp1 = (!tmp1.isEmpty ()) ? ("D" + tmp1) : "";
                tmp2 += tmp1 + ",";

                tmp1 = SF2EnergyDDT.getText ();
                tmp1 = (!tmp1.isEmpty ()) ? tmp1 : "";
                tmp2 += tmp1;

                tmp1 = SF3abandDDT.getText ();
                tmp1 = (!tmp1.isEmpty ()) ? tmp1 : "";
                if ( !tmp1.isEmpty () ) {
                    tmp2 += "," + tmp1;
                }

                if ( !putTxt.endsWith (", ") &&
                        putTxt != null &&
                        putTxt != "" &&
                        putTxt.length () > 1 ) {
                    putTxt += ",\n";
                }
                putTxt += tmp2;
                tf.setText (putTxt);
                ++multiLine;
                tmp2 = "";
                tmp1 = "";
                isSelect = true;
            });
            acceptEdit.setOnAction ((ActionEvent event1) -> {
                String parseStr;
                String strEntered = "";
                String tmpStr1 = "";
                String tmpStr2 = "";
                String tmpHead = "";
                String labelS;

                if ( !isSelect ) {
                    popupMsg.warnBox ("Please press \"Select\" first",
                            "Attention! Select First");
                } else if ( tf.getText ().isEmpty () ) {
                    popupMsg.warnBox ("Please enter data first",
                            "Attention! No Data");
                } else {

                    parseStr = tf.getText ().toString ();
                    sComp = parseStr.split ("\\s+");

                    tmpStr1 = Head;
                    if ( act.contains ("Edit") ) {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;
                            if ( ii <= multiLine - 2 ) {
                                strEntered = parseStr.substring (
                                        0, Integer.valueOf (sComp[ii]));
                            } else {
                                strEntered = parseStr;
                            }
                            labelS = (multiLine > 1) ? Integer.toString (ii + 1)
                                    : "";
                            strEntered = "(" + strEntered + ")";
                            myData.set (ri + ii,
                                    new editableData (
                                            tmpHead,
                                            labelS,
                                            strEntered, SE)
                            );
                            if ( ii <= multiLine - 2 ) {
                                parseStr = parseStr.substring (
                                        parseStr.indexOf ("),") + 3);
                            }
                        }
                    } else {
                        for ( int ii = 0; ii < multiLine; ii++ ) {
                            tmpHead = (ii == 0) ? tmpStr1 : tmpStr2;

                            strEntered = (ii == multiLine - 1) ? sComp[ii]
                                    : sComp[ii].substring (
                                            0,
                                            sComp[ii].lastIndexOf (","));
                            labelS = (multiLine > 1) ? Integer.toString (ii + 1)
                                    : "";
                            strEntered = "(" + strEntered + ")";
                            myData.add (ri + ii,
                                    new editableData (
                                            tmpHead,
                                            labelS,
                                            strEntered,
                                            SE
                                    ));
                            ++myDataN;
                        }
                        thisEntryLine = Integer.toString (doGetSELineNum (Head));
                        bibList.add (
                                bibEntNum,
                                new BIBClasses (thisEntryLine, Integer.
                                        toString (
                                                bibEntNum),
                                        SE, tmpStr1));
                        ++bibEntNum;
                    }
                    editTable.refresh ();
                    tf.clear ();
                    tp1.setText ("");
                    tp2.setText ("");
                    tp3.setText ("");
                    tp4.setText ("");
                    tp5.setText ("");

                    myDialog.close ();
                }
                System.gc ();
            });
            cancelEdit.setOnAction ((ActionEvent event) -> {
                editTable.refresh ();
                tf.clear ();
                if ( act.contains ("Add") ) {
                    changeBoolStatus (Head, false);
                }
                myDialog.close ();
            });
        }
        if ( act.contains ("Delete") ) {
            for ( int ii = 0; ii < tmpRICnt; ii++ ) {
                myData.remove (ri);
                --myDataN;
            }
            editTable.refresh ();
            zapBIBEntry (Head);
            changeBoolStatus (Head, false);
        }
        procTree ();
        System.gc ();
    }

    /*
     * Takes input String and checks with input Array to return bool @author
     * @author Abhijit Bhattacharyya
     */
    private boolean checkStringArray(String sIn, ArrayList<String> strArrIn) {
        boolean check = false;
        for ( String item : strArrIn ) {
            if ( item.contains (sIn) ) {
                check = true;
                break;
            }
        }
        return check;
    }

    /*
     * Get the index of a string inside the ComboBox @author Abhijit
     * Bhattacharyya
     */
    public int getIndexOfStringInCB(String sIn, ComboBox cb) {
        int i = 0;

        ObservableList<String> items = cb.getItems ();
        for ( String item : items ) {
            if ( item.contains (sIn) ) {
                break;
            }
            ++i;
        }
        return i;
    }

    /*
     * sets the comboBox with lists @author Abhijit Bhattacharyya
     */
    public void setInstCombo(
            ComboBox<String> cmb,
            ObservableList<String> list
    ) {
        cmb.getItems ().clear ();
        for ( String l1 : list ) {
            cmb.getItems ().add (l1);  // Add elements to the ComboBox
        }
    }


    /*
     * kernel for the user to associate dictionary with associate ComboBox
     * @author Abhijit Bhattacharyya
     */
    public void entryChoice(
            ObservableList<String> l1,
            ComboBox<String> cmb,
            String str1
    ) {
        cmb.setTooltip (new Tooltip ());
        new comboBoxAutoComplete<String> (cmb);
        setInstCombo (cmb, l1);
        cmb.setValue (str1);
    }

    public void entryChoice(
            ObservableList<String> l1,
            ComboBox<String> cmb
    ) {
        cmb.setTooltip (new Tooltip ());
        new comboBoxAutoComplete<String> (cmb);
        setInstCombo (cmb, l1);
        cmb.setValue (selectionStr);
    }

    private void viewHeader(int numC) {
        switch (numC) {
            case 1:
                dHCB1.setVisible (true);
                dUCB1.setVisible (true); // 1                
                dHCB2.setVisible (false);
                dUCB2.setVisible (false);
                dHCB3.setVisible (false);
                dUCB3.setVisible (false);
                dHCB4.setVisible (false);
                dUCB4.setVisible (false);
                dHCB5.setVisible (false);
                dUCB5.setVisible (false);
                dHCB6.setVisible (false);
                dUCB6.setVisible (false);
                break;
            case 2:
                dHCB1.setVisible (true);
                dUCB1.setVisible (true);    // 1
                dHCB2.setVisible (true);
                dUCB2.setVisible (true);    // 2
                dHCB3.setVisible (false);
                dUCB3.setVisible (false);
                dHCB4.setVisible (false);
                dUCB4.setVisible (false);
                dHCB5.setVisible (false);
                dUCB5.setVisible (false);
                dHCB6.setVisible (false);
                dUCB6.setVisible (false);
                break;
            case 3:
                dHCB1.setVisible (true);
                dUCB1.setVisible (true);   // 1
                dHCB2.setVisible (true);
                dUCB2.setVisible (true);   // 2
                dHCB3.setVisible (true);
                dUCB3.setVisible (true);   // 3
                dHCB4.setVisible (false);
                dUCB4.setVisible (false);
                dHCB5.setVisible (false);
                dUCB5.setVisible (false);
                dHCB6.setVisible (false);
                dUCB6.setVisible (false);
                break;
            case 4:
                dHCB1.setVisible (true);
                dUCB1.setVisible (true);    // 1
                dHCB2.setVisible (true);
                dUCB2.setVisible (true);    // 2
                dHCB3.setVisible (true);
                dUCB3.setVisible (true);    // 3
                dHCB4.setVisible (true);
                dUCB4.setVisible (true);    // 4
                dHCB5.setVisible (false);
                dUCB5.setVisible (false);
                dHCB6.setVisible (false);
                dUCB6.setVisible (false);
                break;
            case 5:
                dHCB1.setVisible (true);
                dUCB1.setVisible (true);    // 1
                dHCB2.setVisible (true);
                dUCB2.setVisible (true);    // 2
                dHCB3.setVisible (true);
                dUCB3.setVisible (true);    // 3
                dHCB4.setVisible (true);
                dUCB4.setVisible (true);    // 4
                dHCB5.setVisible (true);
                dUCB5.setVisible (true);    // 5
                dHCB6.setVisible (false);
                dUCB6.setVisible (false);
                break;
            case 6:
                dHCB1.setVisible (true);
                dUCB1.setVisible (true);    // 1
                dHCB2.setVisible (true);
                dUCB2.setVisible (true);    // 2
                dHCB3.setVisible (true);
                dUCB3.setVisible (true);    // 3
                dHCB4.setVisible (true);
                dUCB4.setVisible (true);    // 4
                dHCB5.setVisible (true);
                dUCB5.setVisible (true);    // 5
                dHCB6.setVisible (true);
                dUCB6.setVisible (true);    // 6
                break;
        }
    }

    private void addRowBelow() {
        matData.add (new CommonDataClass (
                TC1.getText (), TC2.getText (), TC3.getText (),
                TC4.getText (), TC5.getText (), TC6.getText ()
        ));
    }

    private void editCommit(int ii) {
        matrixDataColumns[ii].setOnEditCommit (
                new EventHandler<CellEditEvent<CommonDataClass, String>> () {
            @Override
            public void handle(CellEditEvent<CommonDataClass, String> event) {
                int colNum = event.getTablePosition ().getColumn ();
                maxRowNum = Math.max (maxRowNum, event.getTablePosition ().
                        getRow ());
                switch (colNum) {
                    case 0:
                        event.getTableView ().getItems ().get (event.
                                getTablePosition ().getRow ()).setMyD1 (event.
                                        getNewValue ());
                        break;
                    case 1:
                        event.getTableView ().getItems ().get (event.
                                getTablePosition ().getRow ()).setMyD2 (event.
                                        getNewValue ());
                        break;
                    case 2:
                        event.getTableView ().getItems ().get (event.
                                getTablePosition ().getRow ()).setMyD3 (event.
                                        getNewValue ());
                        break;
                    case 3:
                        event.getTableView ().getItems ().get (event.
                                getTablePosition ().getRow ()).setMyD4 (event.
                                        getNewValue ());
                        break;
                    case 4:
                        event.getTableView ().getItems ().get (event.
                                getTablePosition ().getRow ()).setMyD5 (event.
                                        getNewValue ());
                        break;
                    case 5:
                        event.getTableView ().getItems ().get (event.
                                getTablePosition ().getRow ()).setMyD6 (event.
                                        getNewValue ());
                        break;
                }
                matrixData.setItems (matData);
                matrixData.refresh ();
                if ( !event.getNewValue ().isEmpty () ) {
                    addRowBelow ();
                }
            }
        });
    }

    // this generates column for common and DATA section
    private TableColumn<CommonDataClass, String> createCol(int icol) {
        TableColumn<CommonDataClass, String> column = new TableColumn<> ();
        column.setMinWidth (maxW);
        column.setMaxWidth (maxW);
        matrixData.getStylesheets ().add (getClass ().getResource (
                "CSS/mainscreen.css").toExternalForm ()
        );
        column.getStyleClass ().add ("colStyle");
        column.setText ("C" + (icol + 1));
        String nameC = "myD" + (icol + 1);
        //column.setCellValueFactory (new PropertyValueFactory<CommonDataClass, String> (nameC));

        column.
                setCellValueFactory (cd -> cd.getValue ().
                dataNameProperty (icol));
        column.setCellFactory (
                new DragSelectionCellFactory<CommonDataClass, String> (
                        TextFieldTableCell.forTableColumn ()
                )
        );

        column.setMinWidth (120);
        return column;
    }

    private void manageTables() {
        matrixData.getStylesheets ().add (getClass ().getResource ("CSS/mainscreen.css").toExternalForm ());        
        matrixData.setEditable (true);
        matrixData.setVisible (true);
        matrixData.getSelectionModel ().setCellSelectionEnabled (true);
        matrixData.getSelectionModel ().
                setSelectionMode (SelectionMode.MULTIPLE);
        // matrixData.addEventHandler(KeyEvent.KEY_RELEASED,
        //        new ControlDownSelectionEventHandler());

        // enabling copy/paste
        TableUtils.doCopyPasteHandler (matrixData, matData);
        
        editTable.getStylesheets ().add (getClass ().getResource ("CSS/mainscreen.css").toExternalForm ());
        bibHead.getStyleClass ().add ("bibHeadStyle1");
        bibPtr.getStyleClass ().add ("bibTextStyle");
        bibText.getStyleClass ().add ("bibTextStyle");
        bibLines.getStyleClass ().add ("bibLinesStyle");
        editTable.getSelectionModel ().setCellSelectionEnabled (true);
        editTable.getSelectionModel ().setSelectionMode (SelectionMode.MULTIPLE);
    }

    private void headStyle() {
        for ( int ii = 0; ii < myDataN; ii++ ) {
            String sHead = myData.get (ii).getBibItemName ().toString ();
            bibHead.getStyleClass ().remove ("bibHeadStyle1");
            bibHead.getStyleClass ().remove ("bibHeadStyle2");

            if ( !FixedHeadList.contains (sHead) ) {
                //bibHead.getStyleClass().add("bibHeadStyle2");
                bibHead.getStyleClass ().clear ();
                bibHead.getStyleClass ().setAll (
                        "table-row-cell:selected.table-cell");
            } else {
                bibHead.getStyleClass ().add ("bibHeadStyle1");
            }
        }
    }

    /*
     * Start the process
     */
//@SuppressWarnings ("unchecked")
    @SuppressWarnings (
            "unchecked")
    public void startProcess() {
        //setDefaultDirExt (DICTPathDir);   // Setting the directory and extension by default
        // editor part is actually TableView and should not 
        // have sorting property        
        // lList.loadAllDict (brW);   // testing with progressbar

        // loadAllDict ();  // Load all List for ComboBoxes
        manageTables ();   // Add properties to Tables
        // setDefaultDirExt (EXFPathDir);

        // Institute related
        entryChoice (lList.instList, institute);

        // COMMON and DATA heading and UNIT related
        entryChoice (lList.dataHeadingList, dHCB1, "Head");
        entryChoice (lList.dataHeadingList, dHCB2, "Head");
        entryChoice (lList.dataHeadingList, dHCB3, "Head");
        entryChoice (lList.dataHeadingList, dHCB4, "Head");
        entryChoice (lList.dataHeadingList, dHCB5, "Head");
        entryChoice (lList.dataHeadingList, dHCB6, "Head");
        entryChoice (lList.dataUnitsList, dUCB1, "Units");
        entryChoice (lList.dataUnitsList, dUCB2, "Units");
        entryChoice (lList.dataUnitsList, dUCB3, "Units");
        entryChoice (lList.dataUnitsList, dUCB4, "Units");
        entryChoice (lList.dataUnitsList, dUCB5, "Units");
        entryChoice (lList.dataUnitsList, dUCB6, "Units");

        // for COMMON and DATA section only to change number of columns
        chngCol.setOnAction (e -> {
            numColStatus = (numCol.getText ().isEmpty ())
                    ? false : true;
            numC = (numColStatus) ? Integer.parseInt (numCol.getText ()) : 1;
            numC = numC > 6 ? 6 : numC;
            numC = (numC <= 0) ? 1 : numC;

            if ( numColStatus ) {
                viewHeader (numC);
                addRowBelow ();
                matrixData.getColumns ().clear ();
                for ( int ii = 0; ii < numC; ii++ ) {
                    matrixDataColumns[ii] = createCol (ii);
                    editCommit (ii);
                }
                matrixData.setItems (matData);
                if ( numColStatus ) {
                    for ( int ii = 0; ii < numC; ii++ ) {
                        matrixData.getColumns ().addAll (matrixDataColumns[ii]);
                    }
                }
            }
            matrixData.refresh ();
            //  TableUtils.installCopyPasteHandler(matrixData);
        });

        headingC.selectedProperty ().addListener (
                new ChangeListener<Boolean> () {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                headingCB = new_val; //headingC.isSelected ();                   
            }
        });

        DDFlagC.selectedProperty ().addListener (
                new ChangeListener<Boolean> () {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                DDFlagB = new_val; //headingC.isSelected ();                   
            }
        });

        myTabPane.getSelectionModel ().selectedItemProperty ().
                addListener (new ChangeListener<Tab> () {
                    String tmp = "";

                    @Override
                    public void changed(
                            ObservableValue<? extends Tab> observable,
                            Tab oldTab, Tab newTab) {
                        if ( newTab == tab1 ) {
                            tab1B = true;
                            tab2B = false;
                        } else if ( newTab == tab2 ) {
                            tab1B = false;
                            tab2B = true;
                        }
                    }
                });

        // Journal related
        entryChoice (lList.jTypeList, jType); // journal type   

        nuclideDDCB.getSelectionModel ().selectedItemProperty ()
                .addListener (new ChangeListener () {
                    @Override
                    public void changed(
                            ObservableValue observable,
                            Object oldV,
                            Object newV) {
                        if ( newV != null ) {
                            String halfLife = newV.toString ();
                            halfLife = halfLife.substring (
                                    halfLife.lastIndexOf (" ")
                            );
                            if ( halfLife.length () > 0 ) {
                                THalfDDT.setText (halfLife);
                            }
                        }
                    }

                });

        jType.getSelectionModel () // Select Journal type
                .selectedItemProperty ()
                .addListener (new ChangeListener () {
                    @Override
                    public void changed(
                            ObservableValue ov,
                            Object o1,
                            Object o2
                    ) {
                        if ( o2 != null ) {
                            String oo = o2.toString ().substring (1, 2);
                            switch (oo) {
                                case "0":
                                case "3":
                                case "4":
                                    entryChoice (lList.dataList, journals);
                                    break;
                                case "B":
                                    entryChoice (lList.bookList, journals);
                                    break;
                                case "A":
                                case "C":
                                    entryChoice (lList.confList, journals);
                                    break;
                                case "J":
                                case "K":
                                    entryChoice (lList.jourList, journals);
                                    break;
                                case "P":
                                case "R":
                                case "S":
                                case "X":
                                    entryChoice (lList.reportList, journals);
                                    break;
                                case "T":
                                case "W":
                                    break;
                            }
                        }
                    }
                }
                );

        journals.getSelectionModel () // journals
                .selectedItemProperty ()
                .addListener (new ChangeListener () {
                    @Override
                    public void changed(
                            ObservableValue ov,
                            Object jo1,
                            Object jo2
                    ) {
                        // Thinking about this part
                        // will do later
                    }
                });

        entryChoice (lList.facilList, facilCB); // Facilities ComboBox

        entryChoice (lList.incSrcList, incSrcCB); // INC-SRC ComboBox

        entryChoice (lList.detectorList, detectorCB); // detector list

        entryChoice (lList.methodList, methodCB);  // Methods

        entryChoice (lList.mixedSF1List, targetNCB);  // for nuclide and compound

        entryChoice (lList.mixedSF2List, incPCB);  // in place of only IncP

        entryChoice (lList.mixedSF3List, procCB);  // in place of only procList

        entryChoice (lList.mixedSF4List, prodCB);  // in place of prodList

        entryChoice (lList.branchList, branchCB);

        entryChoice (lList.paramSF6List, paramSF6CB);

        entryChoice (lList.paramSF7List, paramSF7CB);

        entryChoice (lList.modifierList, modifierCB);

        entryChoice (lList.dataTypeList, dataTypeCB);

        entryChoice (lList.statusList, statusCB);

        entryChoice (lList.addlResultList, addlResultCB);

        entryChoice (lList.dataHeadingList, dataHeadingCB);

        entryChoice (lList.dataUnitsList, dataUnitsCB);

        entryChoice (lList.monitRefList, monitRefCB);

        entryChoice (lList.targetNList, nuclideDDCB);

        entryChoice (lList.dataUnitsList, dataUnitsCB);

        entryChoice (lList.targetNList, radSF1DDCB);

        editTable.setEditable (true);
        editTable.autosize ();

        editTable.setMinWidth (633.0);
        editTable.getSelectionModel ()
                .setSelectionMode (SelectionMode.MULTIPLE);
        editTable.setFixedCellSize (Region.USE_COMPUTED_SIZE);

        // set cell Value Factories
        setCellValueFactories ();

        // sets the row factory of the table
        setRowFactory ();

        // sets the cell factory
        setCellFactory ();

        // set dummy data to the Table
        editTable.setItems (myData);
        headStyle ();

        lastLine = 0;

        String chckDone = setupCheckBox ();

        // Setting cell properties for the TreeView
        setTreeSelection ();

        subEntTree.setEditable (true);
    }

    private int chkSF4(String sIN) {
        int toReturn = 0;
        String chkSF3A = "TOT ABS NON TCC";
        String chkSF3B = "PAI";
        String chkSF3C = "SCT EL INL THS";
        String chkSF3D = " F FUS";

        if ( chkSF3A.contains (sIN) ) {
            toReturn = 1;
        } else if ( chkSF3B.contains (sIN) ) {
            toReturn = 2;
        } else if ( chkSF3C.contains (sIN) ) {
            toReturn = 3;
        } else if ( chkSF3D.contains (sIN) ) {
            toReturn = 4;
        }

        return toReturn;
    }

    /**
     * Initialize check boxes
     *
     * @return
     */
    private String setupCheckBox() {
        String returnS = "OK";
        titleC.setIndeterminate (false);
        authC.setIndeterminate (false);
        instC.setIndeterminate (false);
        refC.setIndeterminate (false);
        faciC.setIndeterminate (false);
        sampC.setIndeterminate (false);
        detC.setIndeterminate (false);
        methC.setIndeterminate (false);
        monitC.setIndeterminate (false);
        incSrcC.setIndeterminate (false);
        analC.setIndeterminate (false);
        errAnalC.setIndeterminate (false);
        partDetC.setIndeterminate (false);
        halfLC.setIndeterminate (false);
        commentC.setIndeterminate (false);
        commActC.setIndeterminate (false);
        dataC.setIndeterminate (false);
        reacC.setIndeterminate (false);
        flagC.setIndeterminate (false);
        statusC.setIndeterminate (false);
        addresC.setIndeterminate (false);
        corrC.setIndeterminate (false);

        return returnS;
    }


    private String getLine(int llN, int seN) {
        String finalStr = null;
        finalStr = entryNum + exforUtil.getI2S (seN, 1) + exforUtil.getI2S (llN,
                1);
        return finalStr;
    }

    private String getSUBENT(int seN) {
        String finalStr = null;
        finalStr = entryNum + exforUtil.getI2S (seN, 0);
        return finalStr;
    }

    // creating new .exf file using entryNum and fName
    public void doNewFile() {
        if ( setB1Count < 2 ) {
            String tmpS = "";
            tmpS = exforUtil.fixString11 (entryNum) + exforUtil.fixString11 (
                    myDate0);
            myData.add (myDataN,
                    new editableData ("ENTRY", "", tmpS, ""));

            ++subentNum;
            subEnt = Integer.toString (subentNum);
            lastLine = lineN;
            myData.add (++myDataN,
                    new editableData ("SUBENT", "",
                            exforUtil.fixString11 (getSUBENT (subentNum)) +
                            exforUtil.fixString11 (myDate0), subEnt));
            ++lastLine;
            myData.add (++myDataN, new editableData ("BIB", "", "", subEnt));
            ++lastLine;
            myData.add (++myDataN, new editableData ("HISTORY", "",
                    ("(" + myDate0) + "C)", subEnt));
            bHist = true;
            ++lastLine;
            myData.add (++myDataN, new editableData ("ENDBIB", "", "", subEnt));
            ++lastLine;
            myData.
                    add (++myDataN,
                            new editableData ("NOCOMMON", "",
                                    exforUtil.fixString11 ("0") + exforUtil.
                                    fixString11 ("0"),
                                    subEnt));
            sub4Common.add (subEnt);
            ++lastLine;
            myData.add (++myDataN,
                    new editableData ("ENDSUBENT", "", "", subEnt));

            //++subentNum;
            subEnt = Integer.toString (subentNum);
            InsSubEnt (false, subEnt);

            //++lastLine;
            //subentNum = 999;              // Do not do this as this will add 999 entries in Tree
            //subEnt = Integer.toString(subentNum);
            // myData.add(++myDataN, new editableData("ENDENTRY", "", "", "999"));
            //  chooseSubENT();
            editTable.refresh ();

            startTree ();  // required as there is no tree now
            procTree ();
        }
    }

    // Try to load OLD file
    private void doLoadEXFFile() {
        // BufferedReader br;        
        try {
            //br = new BufferedReader (new FileReader (fName));
            BufferedReader br = new BufferedReader (new FileReader (fName));
            brW.write ("Trying to load an Existing file->" + fName);
            //String line;
            myDataN = 0;
            bibEntNum = 0;
            int rowNumber = 0;
            int nCnt = 0;
            String line;
            while ((line = br.readLine ()) != null) {
                int i = 0;
                String s1 = "";
                String s2 = "";
                String s3 = "";
                String s4 = "";
                String s5 = "";
                String oldHead = "";
                //++rowNumber;

                s1 = line.substring (0, 10).trim ();

                oldHead = (!s1.isEmpty () && FixedHeadList.contains (s1)) ? s1
                        : oldHead;

                if ( line.length () > 10 ) {
                    s2 = line.substring (10, 11);
                }
                if ( !line.substring (11).isEmpty () ) {
                    s3 = line.substring (11, 66);
                    if ( !fNameSet ) {
                        String s31 = s3.trim ();
                        entryNum = s31.substring (0, 5);
                        s31 = s31.substring (5).trim ();
                        myDateOldFile = s31;
                        entDateT.setText (myDateOldFile);
                        fNameSet = true;
                        startTree ();
                    }
                }
                if ( line.length () > 68 ) {
                    s4 = line.substring (66, line.length ());
                    s5 = line.substring (71, 74).trim ();
                } else if ( line.length () == 67 ) {
                    s4 = line.substring (66).trim ();
                    s5 = line.substring (66).trim ();
                }
                if ( (s1.contains ("BIB")) || (s1.contains ("ENDBIB")) || (s1.
                        contains ("COMMON")) ||
                        (s1.contains ("ENDCOMMON")) || (s1.contains (
                        "ENDSUBENT")) ||
                        (s1.contains ("ENDSUBENT")) //|| (s1.contains ("ENDENTRY")) ) {
                        ) {
                    s3 = "";
                }
                if ( s1.contains ("ENTRY") ) {
                    s3 = exforUtil.fixString11 (entryNum, 11) +
                            exforUtil.fixString11 (myDateOldFile, 11);
                }
                if ( s1.contains ("SUBENT") ) {
                    ++subentNum;
                    subEnt = Integer.toString (subentNum);
                    s3 = exforUtil.fixString11 (getSUBENT (subentNum), 11) +
                            exforUtil.fixString11 (
                                    myDateOldFile, 11);
                    //s4 = subEnt;
                }
                if ( exforUtil.isNumeric (s1) ) {
                    s1 = exforUtil.fixString11 (s1, 10);
                }

                myData.add (myDataN++, new editableData (s1, s2, s3, s4));
                if ( FixedHeadList.contains (s1) && !(s1.contains ("DATA") &&
                        !s2.isEmpty ()) ) {

                    thisEntryLine = (!s1.isEmpty ())
                            ? getLineNum (line, false)
                            : getLineNum (oldHead, true);

                    if ( line.length () > 68 ) {
                        int tmpINT = Integer.parseInt (s4.substring (5, 8).
                                trim ());
                        rowNumber = (tmpINT < 999) ? tmpINT : rowNumber;
                    } else {
                        rowNumber = Integer.parseInt (s4.trim ());
                    }
                    bibList.add (bibEntNum++,
                            new BIBClasses (thisEntryLine, Integer.
                                    toString (bibEntNum), s5, s1));
                }

                changeBoolStatus (s1, true);
                subEntTree.refresh ();
                editTable.refresh ();
                if ( s1.contains ("ENDENTRY") ) {
                    break;
                }
            }
            fixTreeList (rowNumber);
            BLoadOldFile = true;
            br.close ();
            editTable.refresh ();
            //setDefaultDirExt (DICTPathDir);
            isOrdered = false;
        } catch (Exception e) {
            System.out.println (" We found an error to read the file " +
                    fName);
            System.out.println (" Please Check...........");
            try {
                brW.write ("Error in reading and parsing the file-> " + fName +
                        "in " + brW);

            } catch (IOException ex) {
                Logger.getLogger (MainScreenController.class
                        .getName ()).log (
                                Level.SEVERE, null, ex);
            }
            System.exit (89);
        }
    }

}
