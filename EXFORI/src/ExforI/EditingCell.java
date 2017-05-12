/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

/**
 *
 * @author vega
 */
public class EditingCell extends TableCell<CommonDataClass, String> {

    private TextField textField;

    /**
     *
     */
    public EditingCell() {
    }

    @Override
    public void startEdit() {
        super.startEdit ();

        if ( textField == null ) {
            createTextField ();
        }
        setText (null);
        setGraphic (textField);
        textField.selectAll ();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit ();
        setText ((String) getItem ());
        setGraphic (null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem (item, empty);
        if ( empty ) {
            setText (null);
            setGraphic(textField);
            //setGraphic (null);
        } else if ( isEditing () ) {
            if ( textField != null ) {
                textField.setText (getString ());
            }
            setText (null);
            setGraphic (textField);
            setContentDisplay (ContentDisplay.GRAPHIC_ONLY);
        } else {
            setText (getString ());
            setContentDisplay (ContentDisplay.TEXT_ONLY);
            setGraphic (null);
        }
    }

    private void createTextField() {
        textField = new TextField (getString ());
        textField.setMinWidth (this.getWidth () - this.getGraphicTextGap () * 2);
        /*
        textField.focusedProperty ().addListener (
                new ChangeListener<Boolean> () {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                    Boolean arg1, Boolean arg2) {
                if ( !arg2 ) {
                    commitEdit (textField.getText ());
                }
            }
        });
*/
        textField.setOnKeyReleased (t-> {
            System.out.println ("key->" + t.getCode ().toString ());
            if ( t.getCode () == KeyCode.ENTER ||
                    t.getCode () == KeyCode.DOWN ) {
                commitEdit (textField.getText ());                    
            } else if ( t.getCode () == KeyCode.ESCAPE ) {
                cancelEdit ();
            }
        });
    }

    private String getString() {
        return getItem () == null ? "" : getItem ().toString ();
    }
}
