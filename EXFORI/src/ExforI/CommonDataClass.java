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
public class CommonDataClass {

    private final SimpleStringProperty myD1;
    private final SimpleStringProperty myD2;
    private final SimpleStringProperty myD3;
    private final SimpleStringProperty myD4;
    private final SimpleStringProperty myD5;
    private final SimpleStringProperty myD6;    

    
    public CommonDataClass(String myStr1, String myStr2, String myStr3, String myStr4, String myStr5, String myStr6) {        
        this.myD1 = new SimpleStringProperty (myStr1);
        this.myD2 = new SimpleStringProperty (myStr2);
        this.myD3 = new SimpleStringProperty (myStr3);
        this.myD4 = new SimpleStringProperty (myStr4);
        this.myD5 = new SimpleStringProperty (myStr5);
        this.myD6 = new SimpleStringProperty (myStr6);        
    }

    public String getMyD1(){
        return myD1.get();
    }
    public String getMyD2(){
        return myD2.get();
    }
    public String getMyD3(){
        return myD3.get();
    }
    public String getMyD4(){
        return myD4.get();
    }
    public String getMyD5(){
        return myD5.get();
    }
    public String getMyD6(){
        return myD6.get();
    }
    public void setMyD1(String myStr){
        myD1.set(myStr);
    }
    public void setMyD2(String myStr){
        myD2.set(myStr);
    }
    public void setMyD3(String myStr){
        myD3.set(myStr);
    }
    public void setMyD4(String myStr){
        myD4.set(myStr);
    }
    public void setMyD5(String myStr){
        myD5.set(myStr);
    }
    public void setMyD6(String myStr){
        myD6.set(myStr);
    }

    
    
    
    public StringProperty dataNameProperty(int index) {
        StringProperty strProp = null;

        switch (index) {
            case 0: strProp = myD1;
                break;
            case 1: strProp = myD2;
                break;
            case 2: strProp = myD3;
                break;
            case 3: strProp = myD4;
                break;
            case 4: strProp = myD5;
                break;
            case 5: strProp = myD6;
                break;
        }
        return strProp;
    }
}
