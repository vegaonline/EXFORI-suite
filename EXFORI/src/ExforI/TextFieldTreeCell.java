/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javafx.scene.control.*;

/**
 *
 * @author vega
 */
public final class TextFieldTreeCell extends TreeCell<String> {

    private final ContextMenu chkTreeMenu = new ContextMenu ();
    final MenuItem cEdit = new MenuItem ("Edit");
    final MenuItem cDelete = new MenuItem ("Delete");
    final MenuItem titleM = new MenuItem ("Title");
    final MenuItem authM = new MenuItem ("Author");
    final MenuItem instM = new MenuItem ("Institute");
    final MenuItem refM = new MenuItem ("Reference");
    final MenuItem facM = new MenuItem ("Facility");
    final MenuItem sampM = new MenuItem ("Sample");
    final MenuItem incSM = new MenuItem ("Inc-Source");
    final MenuItem detM = new MenuItem ("Detector");
    final MenuItem monitM = new MenuItem ("Monitor");
    final MenuItem newSUBM = new MenuItem ("New SUBENTRY");
    final Menu addM = new Menu ("Add Items");
    final Menu editM = new Menu ("Edit Contents");
    boolean bTitle = false, bAuth = false, bInst = false, bRef = false;
    boolean bFac = false, bSamp = false, bIncs = false, bDet = false;
    boolean bMonit = false;

    public TextFieldTreeCell() {
        
    }

    private void getAddMenus() {
        addM.getItems ().clear ();
        if ( !bTitle ) {
            addM.getItems ().add (titleM);
        }
        if ( !bAuth ) {
            addM.getItems ().add (authM);
        }
        if ( !bInst ) {
            addM.getItems ().add (instM);
        }
        if ( !bRef ) {
            addM.getItems ().add (refM);
        }
        if ( !bFac ) {
            addM.getItems ().add (facM);
        }
        if ( !bSamp ) {
            addM.getItems ().add (sampM);
        }
        if ( !bIncs ) {
            addM.getItems ().add (incSM);
        }
        if ( !bDet ) {
            addM.getItems ().add (detM);
        }
        if ( !bMonit ) {
            addM.getItems ().add (monitM);
        }
        addM.getItems ().add (newSUBM);
    }

    private void resetMenuEditAdd(String s1, boolean ivoid) {
        boolean bibSUB = false;
        if ( s1.contains ("BIB") ||
                s1.contains ("SUBENT") ||
                s1.contains ("ENDBIB") ||
                s1.contains ("ENDSUBENT") ) {
            bibSUB = true;
        }
        chkTreeMenu.getItems ().clear ();
        editM.getItems ().clear ();
        getAddMenus ();
        editM.getItems ().addAll (cEdit, cDelete);
        if ( !ivoid && !bibSUB ) {
            chkTreeMenu.getItems ().addAll (editM, addM);
        } else {
            chkTreeMenu.getItems ().addAll (addM);
        }
    }
/*
     private void doAddEditDelete(int ri, String s1, String act) {
        if ( !s1.isEmpty () ) {
            switch (s1) {
                case "TITLE":
                    doEditTitle (ri, act);
                    break;
                case "AUTHOR":
                    doEditAuth (ri, act);
                    break;
                case "INSTITUTE":
                    doEditInst (ri, act);
                    break;
                case "REFERENCE":
                    doEditRef (ri, act);
            }
        }
        titleM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            doEditTitle (ri, act);
        });
        authM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            doEditAuth (ri, act);
        });
        instM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            doEditInst (ri, act);
        });
        refM.setOnAction ((ActionEvent event1) -> {
            editTable.refresh ();
            doEditRef (ri, act);
        });
        newSUBM.setOnAction ((ActionEvent event1) -> {
            myData.add (++myDataN,
                    new editableData ("SUBENT", "",
                            getSUBENT (++subentNum) + "   " +
                            myDate0, ""));
            myData.add (++myDataN, new editableData ("BIB", "", "", ""));
            myData.add (++myDataN, new editableData ("ENDBIB", "", "", ""));
            myData.add (++myDataN,
                    new editableData ("ENDSUBENT", "", "", ""));
        });
        updateBIBTreeData ();
     }
     */
}
