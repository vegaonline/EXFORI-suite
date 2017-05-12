/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import java.util.stream.Stream;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;

/**
 *
 * @author vega
 */
public class TableUtils extends TableCell<CommonDataClass, String> {

    /**
     * CTRL + C = copy CTRL + V = paste
     *
     * @param table
     * @param myData
     */    

    public static void doCopyPasteHandler(TableView<CommonDataClass> table,
            ObservableList<CommonDataClass> myData) {
        table.setOnKeyPressed (new TableKeyEventHandler ());
    }

    public static class TableKeyEventHandler implements EventHandler<KeyEvent> {
        KeyCodeCombination copyCode = new KeyCodeCombination (KeyCode.C,
                KeyCodeCombination.CONTROL_ANY);
        KeyCodeCombination pasteCode = new KeyCodeCombination (KeyCode.V,
                KeyCodeCombination.CONTROL_ANY);

        //@Override
        public void handle(KeyEvent event) {
            if ( copyCode.match (event) ) {
                if ( event.getSource () instanceof TableView ) {
                    copySelection2Clipboard ((TableView<CommonDataClass>) event.
                            getSource ());  // this will copy to clipboard
                    event.consume (); // After using event please consume
                }
            } else if ( pasteCode.match (event) ) {
                if ( event.getSource () instanceof TableView ) {
                    pasteFromClipboard ((TableView<CommonDataClass>) event.
                            getSource ());
                    event.consume ();
                }
            }
        }
    }

    public static <S> void copySelection2Clipboard(TableView<S> table) {
        StringBuilder clipboardString = new StringBuilder ();
        ObservableList<TablePosition> positionList = table.getSelectionModel ().
                getSelectedCells ();
        int prevRow = -1;
        for ( TablePosition pos : positionList ) {
            int row = pos.getRow ();
            int col = pos.getColumn ();
            if ( prevRow == row ) {    // determine whether we advance in a row or col (newline)
                clipboardString.append ('\t');
            } else if ( prevRow != -1 ) {
                clipboardString.append ('\n');
            }
            String text = "";
            Object obsValue = (Object) table.getColumns ().get (col).
                    getCellObservableValue (row);
            if ( obsValue == null ) {
                text = "";
            } else if ( obsValue instanceof StringProperty ) {
                text = ((StringProperty) obsValue).get ();
            } else {
                System.out.println ("Unsupported observable value: " + obsValue);
            }
            clipboardString.append (text);
            prevRow = row;
        }
        ClipboardContent clipboardContent = new ClipboardContent ();
        clipboardContent.putString (clipboardString.toString ());
        Clipboard.getSystemClipboard ().setContent (clipboardContent);
    }

    public static <S> void pasteFromClipboard(TableView<S> table) {
        if ( table.getSelectionModel ().getSelectedCells ().isEmpty () ) {
            return;
        }
        TablePosition pasteCellPosition = table.getSelectionModel ().
                getSelectedCells ().get (0);     // get cell position at start

        Clipboard cb = Clipboard.getSystemClipboard ();
        if ( !cb.hasString () ) {
            return;
        }

        String pasteString = cb.getString ();

        String[][] values = Stream.of (pasteString.split ("\\r?\\n")).map (
                 line -> line.split ("\\s++")).toArray (String[][]::new);
             //  line -> line.split ("\t")).toArray (String[][]::new);

        final int offsetY = pasteCellPosition.getRow ();
        final int offsetX = pasteCellPosition.getColumn ();
        final int maxY = Math.min (table.getItems ().size () - offsetY,
                values.length);
        final int colMax = table.getColumns ().size () - offsetX;



        for ( int y = 0; y < maxY; y++ ) {
            String[] r = values[y];
            final int maxX = Math.min (colMax, r.length);


            S rowObject = (S) table.getItems ().get (y + offsetY);

            for ( int x = 0; x < maxX; x++ ) {
                Object property = table.getColumns ().get (x + offsetX).
                        getCellObservableValue (rowObject);
                if ( property instanceof StringProperty ) {
                    // write value using the property
                    ((StringProperty) property).set (r[x]);
                }
            }
            if (y < maxY-1){
                // wish to add blank row
            }
        }
    }
    /*
     *
     *
     * StringTokenizer rowTokenizer = new StringTokenizer (pasteString, "\n");
     *
     * int rowNum = rowTokenizer.countTokens () + 1;
     *
     * System.out.println ("Pasting into cell->" + pasteCellPosition);
     *
     * int rowCB = -1; while (rowTokenizer.hasMoreTokens ()) { rowCB++; String
     * rowString = rowTokenizer.nextToken ();
     *
     * StringTokenizer colTokenizer = new StringTokenizer (rowString, "\t");
     *
     * int colCB = -1; while (colTokenizer.hasMoreTokens ()) { colCB++; String
     * clpCellCont = colTokenizer.nextToken ();
     *
     * int rowTable = pasteCellPosition.getRow () + rowCB; int colTable =
     * pasteCellPosition.getColumn () + colCB;
     *
     * if ( rowTable >= table.getItems ().size () ) { continue; } if ( colTable
     * >= table.getColumns ().size () ) { continue; }
     *
     * //System.out.println ( // "rowTable->" + rowTable + " rowS->" +
     * rowString + // " colTable->" + colTable); TableColumn tabCol =
     * table.getVisibleLeafColumn (colTable);
     *
     * ObservableValue obsVal = tabCol. getCellObservableValue (rowTable);
     *
     * System.out.println ("SIZE->" + table.getItems ().size () + " " +
     * tabCol.getCellData (rowTable));
     *
     * System.out.println (rowTable + "/" + colTable + " : " + clpCellCont); }
     *
     * }
     * }
     */
    

}
