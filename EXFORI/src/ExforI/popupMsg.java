/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author vega
 */
public class popupMsg {

    private TextField myTF = new TextField();

    /**
     * @param infoMesg : Message to be displayed
     *
     * @param titleBar : The titleBar
     *
     */
    public static void infoBox(String infoMesg, String titleBar) {
        {
            JOptionPane.showMessageDialog(null, infoMesg, titleBar,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * @param warnMesg : Message to be displayed
     *
     * @param titleBar : The titleBar
     *
     */
    public static void warnBox(String warnMesg, String titleBar) {
        {
            JOptionPane.showMessageDialog(null, warnMesg, "Warning !!! "
                    + titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static String showAndEditMsg(String inString) {
        //popupMsg pop = new popupMsg();
        final JTextField myTXTF = new JTextField(80);

        myTXTF.setEditable(true);

        myTXTF.setText(inString);
        System.out.println("Within popup " + inString);
        JOptionPane.showMessageDialog(null, myTXTF);
        return myTXTF.getText().toString();
    }

}
