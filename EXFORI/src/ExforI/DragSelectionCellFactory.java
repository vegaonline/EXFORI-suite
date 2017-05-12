/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import javafx.scene.control.*;
import javafx.util.Callback;

/**
 *
 * @author vega
 */
//public class DragSelectionCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
public class DragSelectionCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private final Callback<TableColumn<S, T>, TableCell<S, T>> factory;

    public DragSelectionCellFactory(
            Callback<TableColumn<S, T>, TableCell<S, T>> factory) {
        this.factory = factory;
    }

    public DragSelectionCellFactory() {
        this (col -> new TableCell<S, T> () {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem (item, empty);
                if ( empty || item == null ) {
                    setText (null);
                } else {
                    setText (item.toString ());
                }
            }
        });
    }

    @Override
    public TableCell<S, T> call(
            final TableColumn<S,T> col) {
        TableCell<S, T> cell = factory.call (col);
        cell.setOnDragDetected (event -> {
            cell.startFullDrag ();
            col.getTableView ().getSelectionModel ().select (cell.getIndex (),
                    col);
        });
        cell.setOnMouseDragEntered (event -> {
            col.getTableView ().getSelectionModel ().select (cell.getIndex (),
                    col);
        });

        /*
        cell.setOnMouseEntered (e -> {
            T item = cell.getItem ();
            if ( item != null ) {
               //  System.out.println ("ITEM---->" + item);  future modification for precision
            }
        });
*/
        return cell;
    }

}

/*
 * public class DragSelectionCellFactory implements
 * Callback<TableColumn<CommonDataClass, String>, TableCell<CommonDataClass,
 * String>> { @Override public TableCell<CommonDataClass, String> call(final
 * TableColumn<CommonDataClass, String> col) { return new DragSelectionCell(); }
 * }
 */
