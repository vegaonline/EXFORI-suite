/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javax.swing.JOptionPane;

/**
 *
 * @author vega
 */
public class popupMsg {

    /**
     * @param infoMesg : Message to be displayed
     *
     * @param titleBar : The titleBar
     *
     */
    public static void infoBox(String infoMesg, String titleBar) {
        {
            JOptionPane.showMessageDialog (null, infoMesg, titleBar,
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
            JOptionPane.showMessageDialog (null, warnMesg, "Warning !!! " +
                    titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
