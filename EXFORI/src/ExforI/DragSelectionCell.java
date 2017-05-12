/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.input.*;

/**
 *
 * @author vega
 */
public class DragSelectionCell extends TableCell<CommonDataClass, String> {

    public DragSelectionCell() {
        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startFullDrag();
                getTableColumn().getTableView().getSelectionModel().select(getIndex(),getTableColumn());                
            }            
        });
        setOnMouseDragEntered(new EventHandler<MouseDragEvent>(){
            @Override
            public void handle(MouseDragEvent event) {
                getTableColumn().getTableView().getSelectionModel().select(getIndex(),getTableColumn());
            }            
        });
    }
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty){
            setText(null);
        } else {
            setText(item);
        }
    }
}
