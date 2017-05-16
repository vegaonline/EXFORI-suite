/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExforI;

import java.io.*;
import java.util.logging.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

/**
 * EXFOR-I : an Indian initiative for EXFOR Compiler NDPCI, BARC, MUMBAI - 400
 * 085, INDIA
 *
 * @author Abhijit Bhattacharyya EMAIL : abhihere@protonmail.com
 */
public class EXFORI extends Application {

    public libList lList = new libList ();
    public ProgressBar bar;
    public BufferedWriter brW;
    public Label loadingL = new Label ("Loading!   Please wait....");
    public Stage myStage1 = new Stage ();
    public Stage primStage;
    public BorderPane mainLayout;
    public Scene mainScene;

    /**
     *
     * @throws Exception
     */
    @Override
    public void start(Stage pStage) throws Exception {
         loadLogo ();
         
        PauseTransition delay1 = new PauseTransition (Duration.seconds (3));   
        
        
        //****************************************************************
        
          System.out.println ("IN callMain-> " + lList.targetNList.size ());
        this.primStage = pStage;
        primStage.setTitle (
                "EXFOR-I :: INDIAN EXFOR :: Tool by Dr. Abhijit Bhattacharyya, (NPD :: NDPCI), BARC, Mumbai, INDIA");
<<<<<<< HEAD
        FXMLLoader loader = new FXMLLoader(EXFORI.class.getResource(
                "fxml/MainScreen.fxml"));
        mainLayout = (BorderPane) loader.load();

        mainScene = new Scene(mainLayout);

        primStage.setScene(mainScene);
        primStage.setMinWidth(1000);
        primStage.setMinHeight(500);
        MainScreenController controller = loader.getController();
        controller.setMyStage(pStage);
        controller.setMyScene(mainScene);
        controller.setbrW(brW);
        controller.setlList(lList);

        PauseTransition delay1 = new PauseTransition(Duration.seconds(15));
        // delay1.setOnFinished((ActionEvent event) -> {
        delay1.setOnFinished(event -> {
            myStage1.close();
            primStage.show();
=======


        FXMLLoader loader = new FXMLLoader (EXFORI.class.getResource ("fxml/MainScreen.fxml"));

        System.out.println("***->"+loader.getController ().toString ());
        
        // mainLayout = (BorderPane) loader.load ();
        mainScene = new Scene (loader.load ());

        primStage.setScene (mainScene);

        //primStage.setScene (mainScene);
        primStage.setMinWidth (1000);
        primStage.setMinHeight (500);
        MainScreenController controller = loader.getController ();
        controller.setMyStage (primStage);
        controller.setMyScene (mainScene);

        controller.setbrW (brW);
        System.out.println ("SEQ:->  In EXFORI B4 controller setting-> " +
                lList.targetNList.size ());
        controller.setlibList (lList);
        System.out.
                println ("SEQ:->  In EXFORI After controller setting");
        
        //****************************************************************
        

        System.out.println ("Target size->" + lList.targetNList.size ());
        delay1.setOnFinished (event -> {
            myStage1.close ();         
>>>>>>> 60df3235e758f52e1f55469ad79dd9936171267f
        });
        delay1.play ();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch (args);
    }

    public void callMain() throws IOException {
        System.out.println ("IN callMain-> " + lList.targetNList.size ());
        this.primStage = new Stage (); //pStage;
        primStage.setTitle (
                "EXFOR-I :: INDIAN EXFOR :: Tool by Dr. Abhijit Bhattacharyya, (NPD :: NDPCI), BARC, Mumbai, INDIA");

         primStage.initModality (Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader (EXFORI.class.getResource ("fxml/MainScreen.fxml"));

        System.out.println("***->"+loader.getController ().toString ());
        
        // mainLayout = (BorderPane) loader.load ();
        mainScene = new Scene (loader.load ());

        primStage.setScene (mainScene);

        //primStage.setScene (mainScene);
        primStage.setMinWidth (1000);
        primStage.setMinHeight (500);
        MainScreenController controller = loader.getController ();
        controller.setMyStage (primStage);
        controller.setMyScene (mainScene);

        controller.setbrW (brW);
        System.out.println ("SEQ:->  In EXFORI B4 controller setting-> " +
                lList.targetNList.size ());
        controller.setlibList (lList);
        System.out.
                println ("SEQ:->  In EXFORI After controller setting");
    }

    private boolean loadLogo() throws InterruptedException {
        try {
            brW = new BufferedWriter (new FileWriter ("logFile.txt"));
        } catch (IOException ex) {
            Logger.getLogger (MainScreenController.class
                    .getName ()).
                    log (Level.SEVERE, null, ex);
        }

        //myStage1 = new Stage();
        BorderPane bp1 = new BorderPane ();
        VBox vb1 = new VBox ();
        HBox hb1 = new HBox ();
        Group iRoot = new Group ();
        iRoot.getChildren ().add (bp1);
        Scene thisScene = new Scene (iRoot, 485, 325, Color.ANTIQUEWHITE);
        myStage1.initStyle (StageStyle.UNDECORATED);

        InputStream inStreamLogo = this.getClass ().getResourceAsStream (
                "EXFORI_logo.png");
        Image imageLogo = new Image (inStreamLogo);
        ImageView imageLogoView = new ImageView (imageLogo);

        ColoredProgressBar barC = new ColoredProgressBar ("progress-barA", 0); // new ProgressBar ();
        //bar.setStyle ("-fx-accent:red;");
        //bar.setProgress (0);
        barC.progressProperty ().bind (task.progressProperty ());
        new Thread (task).start ();

        hb1.getChildren ().addAll (barC, loadingL);
        vb1.getChildren ().addAll (imageLogoView, hb1);
        bp1.setCenter (vb1);

        thisScene.getStylesheets ().add (getClass ().getResource (
                "CSS/mainscreen.css").toExternalForm ());
        //thisScene.getStylesheets().add("progresss-barA");
        myStage1.setScene (thisScene);
        // myStage1.show ();

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
            super (progress);
            getStyleClass ().add (styleClass);
        }
    }
}
