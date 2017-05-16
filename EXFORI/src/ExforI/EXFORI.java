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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JFileChooser;

/**
 * EXFOR-I : an Indian initiative for EXFOR Compiler NDPCI, BARC, MUMBAI - 400
 * 085, INDIA
 *
 * @author Abhijit Bhattacharyya EMAIL : abhihere@protonmail.com
 */
public class EXFORI extends Application {

    libList lList = new libList();
    ProgressBar bar;
    ColoredProgressBar barC;
    public BufferedWriter brW;
    public Label loadingL = new Label("Loading IAEA Dictionaries!   Please wait....");
    Stage myStage1 = new Stage();
    private Stage primStage;
    private BorderPane mainLayout;
    public Scene mainScene;
    JFileChooser myFileChooser = new JFileChooser();
    File recordsDir;
    String path = "";
    String cssName = "";
    double lSize = 0.0;

    @Override
    public void start(Stage pStage) throws Exception {
        loadLogo();

        this.primStage = pStage;
        primStage.setTitle(
                "EXFOR-I :: INDIAN EXFOR :: Tool by Dr. Abhijit Bhattacharyya, (NPD :: NDPCI), BARC, Mumbai, INDIA");
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

        PauseTransition delay1 = new PauseTransition(Duration.seconds(lSize));
        delay1.setOnFinished(event -> {
            myStage1.close();
            primStage.show();
        });
        delay1.play();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private boolean loadLogo() throws InterruptedException {
        try {
            brW = new BufferedWriter(new FileWriter("logFile.txt"));
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class
                    .getName()).
                    log(Level.SEVERE, null, ex);
        }

        //myStage1 = new Stage();
        BorderPane bp1 = new BorderPane();
        VBox vb1 = new VBox();
        HBox hb1 = new HBox();
        Group iRoot = new Group();
        iRoot.getChildren().add(bp1);
        Scene thisScene = new Scene(iRoot, 485, 325, Color.ANTIQUEWHITE);
        myStage1.initStyle(StageStyle.UNDECORATED);

        InputStream inStreamLogo = this.getClass().getResourceAsStream(
                "EXFORI_logo.png");
        Image imageLogo = new Image(inStreamLogo);
        ImageView imageLogoView = new ImageView(imageLogo);

        barC = new ColoredProgressBar("progress-barA", 0); // new ProgressBar ();

        new Thread(task).start();

        hb1.getChildren().addAll(barC, loadingL);
        vb1.getChildren().addAll(imageLogoView, hb1);
        bp1.setCenter(vb1);

        thisScene.getStylesheets().add(getClass().getResource(
                "CSS/mainscreen.css").toExternalForm());

        myStage1.setScene(thisScene);
        myStage1.show();
        return true;
    }

    Task task = new Task<Void>() {
        @Override
        public Void call() throws InterruptedException {
            lList.loadAllDict(brW);
            //updateProgress(lList.rptCount, lList.totCount);
            lSize = (double) (lList.totCount / 3.0);
            lSize = Math.round(lSize);
            new Thread() {
                public void run() {
                    for (double i = 0.0; i <= lSize; i++) {
                        final double step = i;
                        Platform.runLater(() -> barC.setProgress(step / lSize));

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }.start();

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
