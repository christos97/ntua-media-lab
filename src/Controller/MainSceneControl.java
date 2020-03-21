package Controller;

import Model.Entities.AirportParking;
import Model.Entities.Category;
import Model.Entities.Data;
import Model.Query.AirportParkingQuery;
import Model.Query.CategoryQuery;
import Model.Query.FlightQuery;
import Model.Query.ServicesQuery;
import View.MainScene;
import View.ParkingPopup;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static java.lang.Double.parseDouble;


public class MainSceneControl {

    //fields
    private static BorderPane main;
    private static Button submitButton;
    private static MenuItem load;
    private static TextField idField;
    private static String id;
    private static int aircraftTypeId;
    //private static Rectangle[] rect;
    private static Alert statusAlert;
    private static String status;
    private static Alert existAlert;
    private static boolean exist;
    private static String prev;
    private static Label total_fee;
    private static int total_slots;
    private static GridPane center;
    private static  int[] categoryId;
    private static Rectangle rect;
    private static int[] park;
    private static FillTransition fill;
    private static ObservableList<Rectangle> rectangles;
    //initialize
    public static void initialize(){
        fill = new FillTransition();
        fill.setCycleCount(1);
        fill.setDuration(Duration.millis(2000));
        fill.setFromValue(Color.GREEN);
        fill.setToValue(Color.RED);
        main = MainScene.getMainLayout();
        //username field
        idField = MainScene.getIdField();
        statusAlert = new Alert(Alert.AlertType.ERROR);
        statusAlert.setContentText("Flight has already been scheduled.");
        existAlert = new Alert(Alert.AlertType.ERROR);
        existAlert.setContentText("Flight does not exist.");

        //submitButton
        total_fee = MainScene.getTotalFee();
        prev= total_fee.getText();
        System.out.println((prev));
        submitButton = MainScene.getSubmitButton();
        submitButton.setDefaultButton(true);

        load = MainScene.getLoad();
        center = MainScene.getCenter();
        rectangles = FXCollections.observableArrayList();
        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                var q = AirportParkingQuery.getAirportData();
                int[] cat = new int[q.size()];
                park = new int[q.size()];
                for (int i=0; i<q.size(); i++){
                    park[i] = q.get(i).getParkingSlots();
                    cat[i] = q.get(i).getCategory();
                }
                for (int i=0; i<cat.length; i++) {
                    rect = new Rectangle();
                    center.setHgap(15);
                    center.setVgap(15);
                    center.setTranslateX(50);
                    var q1 = CategoryQuery.getCategoryTitle(cat[i]);
                    String title = q1.get(0).getTitle();
                    Label label = new Label(title);
                    label.setTranslateY(130);
                    label.setTranslateX(0);
                    center.add(label,0,i);
                    for (int j=0; j<park[i]; j++){
                        rect = new Rectangle();
                        rect.setTranslateX(0);
                        rect.setTranslateY(130);
                        rect.setWidth(15);
                        rect.setHeight(15);
                        rect.setArcWidth(4);
                        rect.setArcHeight(4);
                        rect.setFill(Color.GREEN);
                        rectangles.add(rect);
                        center.add(rect,j+1,i);
                    }

                    //System.out.println(center.getChildren().remove(rect[i][0]));
                }
                center.setTranslateY(50);
            }
        };

        load.setOnAction(eventHandler);

        submitButton.setOnAction(e->{   // Submit Flight id actions
            id = idField.getText();
            idField.clear();
            Data data = handleSubmitButton();
            if (data != null) {
                ParkingPopup.display(data);
            }
            else {
                if (!exist) existAlert.show();
                else if (status.equals("Parked")) statusAlert.show();
                return;
            };
            if (ParkingPopup.isIsCancelled()) return;
            prev= total_fee.getText().replaceAll("[^-.?0-9]+", "");
            MainScene.setTotalFee(String.valueOf(data.getTotal_fee() + parseDouble(prev)));
            System.out.println("Total Amount Updated");
            int passed =0;
            if (data.getCategId() != 1) {
                for (int i = 0; i < data.getCategId()-1; i++) passed += park[i];
            }
            System.out.println(passed);
            for (int j=0; j < park[data.getCategId()-1]; j++) {
                System.out.println(park[data.getCategId()-1]);
                if(rectangles.get(passed + j).getFill() == Color.GREEN) {
                    fill.setShape(rectangles.get(passed + j));
                    fill.play();
                    FlightQuery.updateById(id);
                    System.out.println("status=Parked @flightID:" + id );
                }
                else if (j == park[data.getCategId()-1] -1) {
                    Alert noSlot = new Alert(Alert.AlertType.ERROR);
                    noSlot.setContentText("No parking slot available at \n" + data.getCateg());
                    noSlot.show();
                }
            }
        });

    }



    //handle submit button
    public static Data handleSubmitButton(){
        exist = true;
        var q1 = FlightQuery.findById(id);  // GET Flight data
        if (q1.size() == 0){
            exist = false;
            return null;
        } ;
        String city = q1.get(0).getCity();
        int flightTypeId = q1.get(0).getFlightType();
        aircraftTypeId = q1.get(0).getAircraftType();
        int departsIn = q1.get(0).getDepartsIn();
        status = q1.get(0).getStatus();
        System.out.println("status="+status+" @flightID:"+id);
        if (status.equals("Parked")) return null;
        var q2 = AirportParkingQuery.getAirportData(); // GET Airport data
        assert q2 != null;
        int size = q2.size();
        categoryId = new int[size];
        int[] parkingSlots = new int[size];
        int[] price = new int[size];
        String[] suffix = new String[size];


        for (int i =0; i < size; i++) {
            categoryId[i] = q2.get(i).getCategory();
            parkingSlots[i] = q2.get(i).getParkingSlots();
            System.out.println(categoryId[i] + "    " + parkingSlots[i]);
            price[i] = q2.get(i).getPrice();
            suffix[i] = q2.get(i).getSuffix();
        }

        total_slots=0;
        for (int i =0; i < size; i++) {
            total_slots += parkingSlots[i];
        }

            //CHECK

        //  Gate
        for (int i =0; i < size; i++) {
            if (categoryId[i] == 1 && flightTypeId == 1 && (aircraftTypeId == 2 || aircraftTypeId == 3) && departsIn <= 45 && parkingSlots[i] > 0)  {
                var cq = CategoryQuery.getCategoryTitle(categoryId[i]); //Category Title
                String title = cq.get(0).getTitle();
                var sq = ServicesQuery.findById(categoryId[i],aircraftTypeId); //Services available at category
                String[] services = new String[sq.size()];
                Double[] pricing = new Double[sq.size()];
                for (int j =0; j < sq.size(); j++){
                    services[j] = sq.get(j).getTitle();
                    pricing[j] = sq.get(j).getPricing();
                }
                Data data = new Data(title,city,flightTypeId,price[i],suffix[i],services,pricing,categoryId[i]);
                return data;
            }
        }

        //  Cargo Gate
        for (int i =0; i < size; i++) {
            if (categoryId[i] == 2 && flightTypeId == 2 && (aircraftTypeId == 2 || aircraftTypeId == 3) && departsIn <= 90 && parkingSlots[i] > 0)  {
                var cq = CategoryQuery.getCategoryTitle(categoryId[i]); //Category Title
                String title = cq.get(0).getTitle();
                var sq = ServicesQuery.findById(categoryId[i],aircraftTypeId); //Services available at category
                String[] services = new String[sq.size()];
                Double[] pricing = new Double[sq.size()];
                for (int j =0; j < sq.size(); j++){
                    services[j] = sq.get(j).getTitle();
                    pricing[j] = sq.get(j).getPricing();
                }
                Data data = new Data(title,city,flightTypeId,price[i],suffix[i],services,pricing,categoryId[i]);
                return  data;
            }
        }
        return null;
    }


}




