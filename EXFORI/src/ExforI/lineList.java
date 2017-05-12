/*
 * INDEXFOR - Indian EXFOR Compiler
 * Developed by Abhijit Bhattacharyya,
 * Nuclear Data Physics Centre of India (NDPCI),
 *  Bhabha Atomic Research Centre, Mumbai, 400 085, INDIA
 */
package ExforI;

import javafx.beans.property.*;

/**
 *
 * @author vega
 */
public class lineList {

    private final SimpleStringProperty Heading;
    private final SimpleIntegerProperty lineNum;

    public lineList(String head, int line) {
        this.Heading = new SimpleStringProperty (head);
        this.lineNum = new SimpleIntegerProperty (line);
    }

    public String getHeading() {
        return this.Heading.get ();
    }

    public int getLine() {
        return this.lineNum.getValue ();
    }

    public void setHeading(String heading) {
        this.Heading.set (heading);
    }

    public void setLine(int line) {
        this.lineNum.set (line);
    }
}
