package View;

import Model.Entities.Data;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class MainScene {
    private static Scene scene;
    private static Menu applicationMenu;
    private static Menu detailsMenu;
    private static MenuBar menuBar;
    private static BorderPane mainLayout;
    private static VBox right_vbox;
    private static VBox left_vbox;
    private static GridPane center;
    private static HBox statusbar;
    private static TextField idField;
    private static Button submitButton;
    private static Label formLabel;
    private static Label total_fee;
    private static Data data ;
    private static MenuItem load;



    public static void initialize(){

        mainLayout = new BorderPane();
        left_vbox = new VBox(13);
        right_vbox = new VBox(13);
        center= new GridPane();

        statusbar = new HBox();
        formLabel = new Label("Give Flight ID ");
        data = new Data();
        load = new MenuItem("Load");

        formLabel.setTranslateX(-25);
        formLabel.setTranslateY(30);

        idField=new TextField();
        idField.setPromptText("Give Flight ID");
        idField.setMaxWidth(100);
        idField.setTranslateX(-30);
        idField.setTranslateY(30);
        idField.setAlignment(Pos.BASELINE_RIGHT);

        submitButton=new Button("Submit");
        submitButton.setTranslateX(-20);
        submitButton.setTranslateY(30);
        submitButton.setMaxWidth(80);

        total_fee = new Label();
        total_fee.setTranslateY(30);
        total_fee.setTranslateX(15);
        total_fee.setText("Total Amount Collected:\n" + data.getTotal_fee()+ "$");




        menuBar = new MenuBar();
        applicationMenu = new Menu("Application");
        applicationMenu.getItems().add(load);
        detailsMenu = new Menu("Details");

        menuBar.getMenus().addAll(applicationMenu,detailsMenu);
        right_vbox.getChildren().addAll(formLabel,idField,submitButton);
        left_vbox.getChildren().addAll(total_fee);
        mainLayout.setTop(menuBar);
        mainLayout.setCenter(center);
        mainLayout.setLeft(left_vbox);
        mainLayout.setRight(right_vbox);
        mainLayout.setBottom(statusbar);



        scene = new Scene(mainLayout,600,600);
    }

    public static Scene getScene() { return scene; }
    public static MenuItem getLoad(){ return load; }
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
    public static GridPane getCenter() { return center; }
}
