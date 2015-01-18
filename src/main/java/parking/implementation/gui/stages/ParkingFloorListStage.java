package parking.implementation.gui.stages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import parking.api.business.parking.Parking;
import parking.api.business.parking.ParkingManager;
import parking.api.business.parkingspot.ParkingSpot;
import parking.api.exceptions.ParkingNotPresentException;
import parking.implementation.business.Client;
import parking.implementation.business.logistic.simple.SimpleParkingSpotFactory;
import parking.implementation.gui.controls.ButtonSpot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loick on 14/01/15.
 */
public class ParkingFloorListStage extends Stage {
    private Collection<Client> clients;
    private Collection<GridPane> parking = new ArrayList<>();

    private Label title;
    private Label label;
    private ChoiceBox<GridPane> select;
    private Button create;
    private Button ok;

    private int maxInLine = 10;

    private ParkingManager parkingManager;
    private SimpleParkingSpotFactory parkingSpotFactory;

    public ParkingFloorListStage(Window owner, Collection<GridPane> parking, Collection<Client> clients) {
        this.initOwner(owner);
        this.parking = parking;
        this.clients = clients;

        init();

        BorderPane borderPane = new BorderPane();
        FlowPane flowPane = new FlowPane();

        //add Nodes to FlowPane
        flowPane.getChildren().addAll(
                title,
                select,
                label,
                create,
                ok
        );

        updateState();

        flowPane.setMaxSize(200, 400);

        //add FlowPane
        flowPane.alignmentProperty().setValue(Pos.CENTER);
        borderPane.setCenter(flowPane);

        //create scene
        Scene scene = new Scene(borderPane, 300, 200);

        this.setResizable(false);
        this.setScene(scene);
        this.setTitle("Parking");
    }

    private GridPane generateFloor() {
        //create GridPane
        GridPane gridPane = new GridPane();

        //ask spots number in this parking
        ParkingStage parkingStage = new ParkingStage(this);
        parkingStage.showAndWait();
        int nbCar = parkingStage.getNbCar();
        int nbCarrier = parkingStage.getNbTruck();

        //create parking
        if (nbCar != 0 || nbCarrier != 0) {
            parkingManager = ParkingManager.getInstance();
            parkingSpotFactory = new SimpleParkingSpotFactory();

            parkingManager.setCompanyName("SWAG COMPANY");
            int idParking = parkingManager.count();
            parkingManager.newParking("Parking " + ++idParking);

            Collection<String> vehicleTypes = new ArrayList<>();
            Map<String, Integer> vehicleCountByType = new HashMap<>();
            vehicleTypes.add("Car");
            vehicleCountByType.put("Car", nbCar);
            vehicleTypes.add("Carrier");
            vehicleCountByType.put("Carrier", nbCarrier);

            int x = 0;
            int y = 0;

            for (String vehicleType : vehicleTypes) {
                try {
                    Parking parking = parkingManager.getParkingById(parkingManager.count());
                    Collection<ParkingSpot> parkingSpots = parking.newParkingSpot(parkingSpotFactory, vehicleCountByType.get(vehicleType));

                    for (ParkingSpot parkingSpot : parkingSpots) {
                        if (x == maxInLine) {
                            x = 0;
                            y++;
                        }

                        ButtonSpot buttonSpot = new ButtonSpot(parkingSpot, this);
                        gridPane.add(buttonSpot, x++, y);
                    }
                } catch (ParkingNotPresentException e) {
                    e.printStackTrace();
                }
            }

            setHeight(y * 50 + 80);  //50: button height & 80: menu height
        }
        return gridPane;
    }

    private void createTitle() {
        title = new Label("Parking");
        title.setFont(Font.font("Arial", 30));
        title.setTextFill(Color.BLACK);
        title.alignmentProperty().setValue(Pos.CENTER);
    }

    private void createLabel() {
        label = new Label("Il n'y a pas d'étages.");
        label.setTextFill(Color.RED);
        label.setAlignment(Pos.CENTER);
    }

    private void createSelect() {
        select = new ChoiceBox<>();
        if (!parking.isEmpty()) {
            select.getItems().setAll(parking);
        }
    }

    private void updateState() {
        if (parking.isEmpty()) {
            this.select.setVisible(false);
            this.label.setVisible(true);
        } else {
            this.select.setVisible(true);
            this.label.setVisible(false);

            select.getItems().setAll(parking);
        }
    }

    private void createButtonCreate() {
        create = new Button();
        create.setText("Ajouter Étage");

        //add action
        create.setOnAction(event -> {
            GridPane floor = generateFloor();
            parking.add(floor);

            updateState();
        });

        //style
        create.setStyle("-fx-background-color: blue");
        create.setTextFill(Color.WHITE);
    }

    private void createButtonOK() {
        ok = new Button();
        ok.setText("OK");

        //add action
        ok.setOnAction(event -> {
            this.close();
        });

        //style
        ok.setStyle("-fx-background-color: royalblue");
        ok.setTextFill(Color.WHITE);
    }

    private void init() {
        createTitle();
        createLabel();
        createSelect();
        createButtonCreate();
        createButtonOK();
    }

    public GridPane getFloor() {
        return this.select.getValue();
    }
}
