/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExforI;

import java.io.*;
import java.util.logging.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 * EXFOR-I : an Indian initiative for EXFOR Compiler NDPCI, BARC, MUMBAI - 400
 * 085, INDIA
 *
 * @author Abhijit Bhattacharyya EMAIL : abhihere@protonmail.com
 */
public class EXFORI extends Application {

    libList lList = new libList ();
    ProgressBar bar;
    public BufferedWriter brW;

    private Stage primStage;
    private BorderPane mainLayout;
    public Scene mainScene;
    JFileChooser myFileChooser = new JFileChooser ();
    File recordsDir;
    String path = "";
    String cssName = "";

    @Override
    public void start(Stage pStage) throws Exception {
        /*
         * Task mytask1 = new taskStart(); Thread th = new Thread(mytask1);
         * th.setDaemon (true); mytask1.setOnSucceeded (new
         * EventHandler<WorkerStateEvent>() { @Override public void
         * handle(WorkerStateEvent event) { throw new
         * UnsupportedOperationException ("Not supported yet."); //To change
         * body of generated methods, choose Tools | Templates. } });
         */
        if (!loadLogo ()){
            Thread.sleep (10000);
        }

        this.primStage = pStage;
        primStage.setTitle (
                "EXFOR-I :: INDIAN EXFOR :: Tool by Dr. Abhijit Bhattacharyya, (NPD :: NDPCI), BARC, Mumbai, INDIA");
        FXMLLoader loader = new FXMLLoader (EXFORI.class.getResource (
                "fxml/MainScreen.fxml"));
        mainLayout = (BorderPane) loader.load ();

        mainScene = new Scene (mainLayout);

        primStage.setScene (mainScene);
        primStage.setMinWidth (1000);
        primStage.setMinHeight (500);
        MainScreenController controller = loader.getController ();
        controller.setMyStage (pStage);
        controller.setMyScene (mainScene);
        controller.setbrW (brW);
        //controller.setCSS(cssName);
        primStage.show ();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch (args);
    }

    private boolean loadLogo() throws InterruptedException {
        try {
            brW = new BufferedWriter (new FileWriter ("logFile.txt"));
        } catch (IOException ex) {
            Logger.getLogger (MainScreenController.class
                    .getName ()).
                    log (Level.SEVERE, null, ex);
        }

        Stage myStage1 = new Stage ();
        BorderPane bp1 = new BorderPane ();
        Group iRoot = new Group ();
        iRoot.getChildren ().add (bp1);
        Scene thisScene = new Scene (iRoot, 800, 510, Color.ANTIQUEWHITE);

        InputStream inStreamLogo = this.getClass ().getResourceAsStream (
                "EXFORI_logo.png");
        Image imageLogo = new Image (inStreamLogo);
        ImageView imageLogoView = new ImageView (imageLogo);

        ColoredProgressBar barC = new ColoredProgressBar("progress-barA", 0) ; // new ProgressBar ();
        //bar.setStyle ("-fx-accent:red;");
        //bar.setProgress (0);
        barC.progressProperty ().bind (task.progressProperty ());
        new Thread (task).start ();

        bp1.setCenter (imageLogoView);
        bp1.setBottom (barC);

        thisScene.getStylesheets ().add (getClass ().getResource (
                "CSS/mainscreen.css").toExternalForm ());
        thisScene.getStylesheets ().add ("progresss-barA");
        myStage1.setScene (thisScene);
        myStage1.show ();
        //Thread.sleep (10000);

//         lList.loadAllDict (brW);
        return true;
    }

    Task task = new Task<Void> () {
        @Override
        public Void call() throws InterruptedException {
            lList.loadAllDict (brW);
            updateProgress (lList.rptCount, lList.totCount);
            return null;
        }
    };
        
    class ColoredProgressBar extends ProgressBar {
        ColoredProgressBar(String styleClass, double progress) {
            super(progress);
            getStyleClass().add(styleClass);
        }
    }
}
