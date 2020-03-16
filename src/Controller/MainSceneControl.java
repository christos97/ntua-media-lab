package Controller;

import Model.Entities.Data;
import Model.Query.AirportParkingQuery;
import Model.Query.CategoryQuery;
import Model.Query.FlightQuery;
import Model.Query.ServicesQuery;
import View.MainScene;
import View.ParkingPopup;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import static java.lang.Double.parseDouble;


public class MainSceneControl {

    //fields
    private static Button submitButton;
    private static TextField idField;
    private static String id;
    private static Rectangle[] rect;
    private static Alert alert;
    private static String prev;
    private static Label total_fee;
    private static int total_slots;
    //initialize
    public static void initialize(){
        //username field
        idField = MainScene.getIdField();
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Flight does not exist \nor has already been scheduled.");
        //submitButton
        total_fee = MainScene.getTotalFee();
        prev= total_fee.getText();
        System.out.println((prev));
        submitButton = MainScene.getSubmitButton();
        submitButton.setDefaultButton(true);
        submitButton.setOnAction(e->{
            id = idField.getText();
            idField.clear();
            Data data = handleSubmitButton();
            if (data != null) {
                ParkingPopup.display(data);
            }
            else {
                alert.show();
                return;
            };
            if (ParkingPopup.isIsCancelled()) return;
            prev= total_fee.getText().replaceAll("[^-.?0-9]+", "");
            System.out.println(prev);
            AirportParkingQuery.newArrival(data.getCateg());
            System.out.println("parking slot: "+ data.getSuf());
            System.out.println("updated @category: " + data.getCateg());
            FlightQuery.updateById(id);
            System.out.println("status=Parked @flightID:" + id );
            MainScene.setTotalFee(String.valueOf(data.getTotal_fee() + parseDouble(prev)));
            System.out.println("Total Amount Updated");

        });

    }



    //handle submit button
    public static Data handleSubmitButton(){
        var q1 = FlightQuery.findById(id);  // GET Flight data
        if (q1.size() == 0){
            return null;
        } ;
        String city = q1.get(0).getCity();
        int flightTypeId = q1.get(0).getFlightType();
        int aircraftTypeId = q1.get(0).getAircraftType();
        int departsIn = q1.get(0).getDepartsIn();
        String status = q1.get(0).getStatus();
        System.out.println("status="+status+" @flightID:"+id);
        if (status.equals("Parked")) return null;
        var q2 = AirportParkingQuery.getAirportData(); // GET Airport data
        assert q2 != null;
        int size = q2.size();
        int[] categoryId = new int[size];
        int[] parkingSlots = new int[size];
        int[] price = new int[size];
        String[] suffix = new String[size];


        for (int i =0; i < size; i++) {
            categoryId[i] = q2.get(i).getCategory();
            parkingSlots[i] = q2.get(i).getParkingSlots();
            System.out.println("1"+parkingSlots[i]);
            price[i] = q2.get(i).getPrice();
            suffix[i] = q2.get(i).getSuffix();
        }
        System.out.println(parkingSlots[0]);

        for (int i =0; i < size; i++) {
            total_slots += parkingSlots[i];
        }
        System.out.println("Total parking slots: " + total_slots);
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
                Data data = new Data(title,city,flightTypeId,price[i],suffix[i],services,pricing);
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
                Data data = new Data(title,city,flightTypeId,price[i],suffix[i],services,pricing);
                return  data;
            }
        }
        return null;
    }


}




