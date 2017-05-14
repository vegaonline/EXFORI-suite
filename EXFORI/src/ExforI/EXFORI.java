/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExforI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import javafx.application.Application;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

    Task copyWorker;

    libList lList = new libList();
    public BufferedWriter brW;

    private ProgressBar bar;
    private Stage stage1;

    private Stage primStage;
    private BorderPane mainLayout;
    public Scene mainScene;
    JFileChooser myFileChooser = new JFileChooser();
    File recordsDir;
    String path = "";
    String cssName = "";

    @Override
    public void start(Stage pStage) throws Exception {
        runMyLoader(pStage);
        this.primStage = pStage;
        primStage.setTitle("EXFOR-I :: INDIAN EXFOR :: Tool by Dr. Abhijit Bhattacharyya, (NPD :: NDPCI), BARC, Mumbai, INDIA");
        FXMLLoader loader = new FXMLLoader(EXFORI.class.getResource("fxml/MainScreen.fxml"));
        mainLayout = (BorderPane) loader.load();

        mainScene = new Scene(mainLayout);

        primStage.setScene(mainScene);
        primStage.setMinWidth(1000);
        primStage.setMinHeight(500);
        MainScreenController controller = loader.getController();
        controller.setMyStage(pStage);
        controller.setMyScene(mainScene);
        //controller.setCSS(cssName);
        primStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void runMyLoader(Stage inStage) {
        Group root = new Group();
        this.stage1 = inStage;

        BorderPane bp = new BorderPane();
        root.getChildren().add(bp);
        bar = new ProgressBar();
        bar.setProgress(0);

        InputStream inStreamLogo = this.getClass().getResourceAsStream("EXFORI_logo.png");
        Image imageLogo = new Image(inStreamLogo);
        ImageView imageLogoView = new ImageView(imageLogo);

        VBox vb = new VBox();
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(imageLogoView, bar);
        bp.setCenter(imageLogoView);
        bp.setBottom(bar);
        Scene thisScene = new Scene(root, 800, 400, Color.ANTIQUEWHITE);
        thisScene.getStylesheets().add("progresss-bar");

        //Scene thisScene = createLoaderScene();
        copyWorker = createWorker();

        bar.progressProperty().unbind();
        bar.progressProperty().bind(copyWorker.progressProperty());
        copyWorker.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable,
                    String oldVal, String newVal) {

            }
        });
        stage1.setScene(thisScene);

        stage1.show();
        //lList.loadAllDict (brW);
        
    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                lList.loadAllDict(brW);
                updateMessage(lList.rptList);
                updateProgress(lList.rptCount, lList.totCount);
                //for (int i = 0; i < 10; i++) {
                //    Thread.sleep(2000);
                //    //updateMessage("2000 milliseconds");
                //    updateProgress(i + 1, 10);
                //}
                return true;
            }
        };
    }
}
