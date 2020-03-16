package View;

import Model.Entities.Data;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javafx.scene.layout.VBox;



public class MainScene {
    private static Scene scene;
    private static Menu applicationMenu;
    private static Menu detailsMenu;
    private static MenuBar menuBar;
    private static BorderPane mainLayout;
    private static VBox right_vbox;
    private static VBox left_vbox;
    private static HBox statusbar;
    private static TextField idField;
    private static Button submitButton;
    private static Label formLabel;
    private static Label total_fee;
    private static Data data;

    public static  void initialize(){

        mainLayout = new BorderPane();
        left_vbox = new VBox(13);
        right_vbox = new VBox(13);
        statusbar = new HBox();
        formLabel = new Label("Give Flight ID ");
        data = new Data();

        formLabel.setTranslateX(-25);
        formLabel.setTranslateY(200);

        idField=new TextField();
        idField.setPromptText("Give Flight ID");
        idField.setMaxWidth(100);
        idField.setTranslateX(-30);
        idField.setTranslateY(200);
        idField.setAlignment(Pos.BASELINE_RIGHT);

        submitButton=new Button("Submit");
        submitButton.setTranslateX(-20);
        submitButton.setTranslateY(200);
        submitButton.setMaxWidth(80);

        total_fee = new Label();
        total_fee.setTranslateY(20);
        total_fee.setText("Total Amount Collected:\n" + data.getTotal_fee()+ "$");



        menuBar = new MenuBar();
        applicationMenu = new Menu("Application");
        detailsMenu = new Menu("Details");

        menuBar.getMenus().addAll(applicationMenu,detailsMenu);
        right_vbox.getChildren().addAll(formLabel,idField,submitButton);
        left_vbox.getChildren().addAll(total_fee);
        mainLayout.setTop(menuBar);
        mainLayout.setLeft(left_vbox);
        mainLayout.setRight(right_vbox);
        mainLayout.setBottom(statusbar);



        scene = new Scene(mainLayout,650,650);
    }

    public static Scene getScene() {
        return scene;
    }
    public static BorderPane getMainLayout() {
        return mainLayout;
    }
    public static Button getSubmitButton() {
        return submitButton;
    }
    public static TextField getIdField() {
        return idField;
    }
    public static void setTotalFee(String fee) { total_fee.setText("Total Amount Collected:\n"+ fee + "$"); }
    public static Label getTotalFee(){return total_fee;}
}
