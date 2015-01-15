package gui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import parking.api.business.contract.Vehicle;
import parking.api.business.contract.VehiculeFactory;
import parking.api.exceptions.UnknowVehiculeException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by loick on 14/01/15.
 */
public class VehiculeStage extends Stage {

    private Collection<String> vehicules = new ArrayList<>();

    private Label title;
    private Label label;
    private ChoiceBox vehicule;
    private TextField plate;
    private TextField brand;
    private TextField model;
    private Button submit;
    private Button cancel;

    private void createTitle() {
        title = new Label("New Vehicule");
        title.setFont(Font.font("Arial", 30));
        title.setTextFill(Color.BLACK);
        title.alignmentProperty().setValue(Pos.CENTER);
    }

    private void createLabel() {
        label = new Label();
        label.setTextFill(Color.RED);
        label.alignmentProperty().setValue(Pos.CENTER);
    }

    private void createChoiceBoxVehicule() {
        vehicule = new ChoiceBox();
        vehicule.getItems().addAll(this.vehicules);
        vehicule.setValue(this.vehicules.iterator().next());
    }

    private void createTextFieldPlate() {
        plate = new TextField();
        plate.setPromptText("Plate");

        //style
        plate.alignmentProperty().setValue(Pos.CENTER);
    }

    private void createTextFieldBrand() {
        brand = new TextField();
        brand.setPromptText("Brand");

        //style
        brand.alignmentProperty().setValue(Pos.CENTER);
    }

    private void createTextFieldModel() {
        model = new TextField();
        model.setPromptText("Model");

        //style
        model.alignmentProperty().setValue(Pos.CENTER);
    }

    private void createButtonCreate() {
        submit = new Button();
        submit.setText("Create");

        //add action
        submit.setOnAction(createSubmitEventHandler());

        //style
        submit.setStyle("-fx-background-color: green");
        submit.setTextFill(Color.WHITE);
    }

    private void createButtonCancel() {
        cancel = new Button();
        cancel.setText("Cancel");

        //add action
        cancel.setOnAction(createCancelEventHandler());

        //style
        cancel.setStyle("-fx-background-color: red");
        cancel.setTextFill(Color.WHITE);
    }

    private EventHandler createSubmitEventHandler() {
        return event -> {
            if(!plate.getText().isEmpty()
                    && !brand.getText().isEmpty()
                    && !model.getText().isEmpty()
                    ) {
                this.close();
            }else {
                label.setText("Tous les champs ne sont pas renseignés");
            }
        };
    }

    private EventHandler createCancelEventHandler() {
        return event -> this.close();
    }

    private void init() {
        this.vehicules.add("Voiture");
        this.vehicules.add("Moto");
        this.vehicules.add("Camion");

        createTitle();
        createLabel();
        createChoiceBoxVehicule();
        createTextFieldPlate();
        createTextFieldBrand();
        createTextFieldModel();
        createButtonCreate();
        createButtonCancel();
    }

    public VehiculeStage(Window owner) {
        this.initOwner(owner);

        init();

        BorderPane borderPane = new BorderPane();
        FlowPane flowPane = new FlowPane();

        //add Nodes to FlowPane
        flowPane.getChildren().addAll(
                title,
                vehicule,
                plate,
                brand,
                model,
                submit,
                cancel
        );

        flowPane.setMaxSize(200, 400);

        //add FlowPane
        flowPane.alignmentProperty().setValue(Pos.CENTER);
        borderPane.setCenter(flowPane);
        //add Label error
        borderPane.setBottom(label);
        borderPane.setAlignment(label, Pos.CENTER);

        //create scene
        Scene scene = new Scene(borderPane, 300, 200);

        this.setResizable(false);
        this.setScene(scene);
        this.setTitle("New Vehicule");
    }

    public Vehicle getVehicule() throws UnknowVehiculeException {
        VehiculeFactory vehiculeFactory = new parking.implementation.VehiculeFactory();

        Vehicle vehicule = vehiculeFactory.createVehicule(this.vehicule.getValue().toString());
        vehicule.setPlate(plate.getText());
        vehicule.setBrand(brand.getText());
        vehicule.setModel(model.getText());

        return vehicule;
    }
}
