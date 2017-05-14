/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExforI;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 * EXFOR-I : an Indian initiative for EXFOR Compiler
 * NDPCI, BARC, MUMBAI - 400 085, INDIA
 * @author Abhijit Bhattacharyya
 *         EMAIL : abhihere@protonmail.com
 */
public class EXFORI extends Application {
    
    private ProgressBar bar;
    private Stage stage1;
    
    private Stage primStage;
    private BorderPane mainLayout;
    public Scene mainScene;
    JFileChooser myFileChooser = new JFileChooser();
    File recordsDir;
    String path="";
    String cssName = "";
    
    
    @Override
    public void start(Stage pStage) throws Exception {
        this.primStage = pStage;
        primStage.setTitle ("EXFOR-I :: INDIAN EXFOR :: Tool by Dr. Abhijit Bhattacharyya, (NPD :: NDPCI), BARC, Mumbai, INDIA");
        FXMLLoader loader = new FXMLLoader(EXFORI.class.getResource("fxml/MainScreen.fxml"));
        mainLayout = (BorderPane) loader.load();
        
        mainScene = new Scene(mainLayout);

        primStage.setScene (mainScene);
        primStage.setMinWidth (1000);
        primStage.setMinHeight (500);
        MainScreenController controller = loader.getController ();
        controller.setMyStage(pStage);
        controller.setMyScene (mainScene);
        //controller.setCSS(cssName);
        primStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch (args);
    } 
    
}
