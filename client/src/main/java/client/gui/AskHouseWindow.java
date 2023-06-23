package client.gui;

import client.utility.OutputerUI;
import common.data.House;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AskHouseWindow {

    @FXML
    private Button enterButton;

    @FXML
    private TextField houseNumberOfFlatsOnFloorField;

    @FXML
    private Label houseNumberOfFlatsOnFloorLabel;

    @FXML
    private TextField houseNumberOfFloorsField;

    @FXML
    private Label houseNumberOfFloorsLabel;

    @FXML
    private TextField houseNumberOfLiftsField;

    @FXML
    private Label houseNumberOfLiftsLabel;

    @FXML
    private TextField houseYearField;

    @FXML
    private Label houseYearLabel;

    private Stage askHouseStage;
    private House resultHouse;
    private ResourceFactory resourceFactory;

    @FXML
    void enterButtonOnAction(ActionEvent event) {
        try {
            resultHouse = new House(
                    "",
                    convertHouseYear(),
                    convertHouseNumberOfFloors(),
                    convertHouseNumberOfFlatsOnFloor(),
                    convertHouseNumberOfLifts()
            );
            askHouseStage.close();
        } catch (IllegalArgumentException exception) { /* ? */ }
    }

    private int convertHouseYear() throws IllegalArgumentException {
        String strHouseYear;
        int houseYear;
        try {
            strHouseYear = houseYearField.getText();
            houseYear = Integer.parseInt(strHouseYear);
//            if (houseYear <= 0) throw new InvalidValueException();
//        } catch (InvalidValueException exception) {
//            OutputerUI.error("HouseYearLimitsException", new String[]{String.valueOf(0)});
//            throw new IllegalArgumentException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("HouseYearFormatException");
            throw new IllegalArgumentException();
        }
        return houseYear;
    }

    private Long convertHouseNumberOfFloors() throws IllegalArgumentException {
        String strHouseNumberOfFloors;
        Long houseNumberOfFloors;
        try {
            strHouseNumberOfFloors = houseNumberOfFloorsField.getText();
            houseNumberOfFloors = Long.parseLong(strHouseNumberOfFloors);
            if (strHouseNumberOfFloors.equals("")) houseNumberOfFloors = null;
            else {
                houseNumberOfFloors = Long.parseLong(strHouseNumberOfFloors);
//                if (houseNumberOfFloors <= 0 || houseNumberOfFloors > 39) throw new InvalidValueException();
            }
//        } catch (InvalidValueException exception) {
//            OutputerUI.error("HouseNumberOfFloorsLimitsException", new String[]{String.valueOf(0)});
//            throw new IllegalArgumentException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("HouseNumberOfFloorsFormatException");
            throw new IllegalArgumentException();
        }
        return houseNumberOfFloors;
    }

    private long convertHouseNumberOfFlatsOnFloor() throws IllegalArgumentException {
        String strHouseNumberOfFlatsOnFloor;
        long houseNumberOfFlatsOnFloor;
        try {
            strHouseNumberOfFlatsOnFloor = houseNumberOfFlatsOnFloorField.getText();
            houseNumberOfFlatsOnFloor = Long.parseLong(strHouseNumberOfFlatsOnFloor);
//            if (houseNumberOfFlatsOnFloor <= 0) throw new InvalidValueException();
//        } catch (InvalidValueException exception) {
//            OutputerUI.error("HouseNumberOfFloorsLimitsException", new String[]{String.valueOf(0)});
//            throw new IllegalArgumentException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("HouseNumberOfFlatsOnFloorFormatException");
            throw new IllegalArgumentException();
        }
        return houseNumberOfFlatsOnFloor;
    }

    private Long convertHouseNumberOfLifts() throws IllegalArgumentException {
        String strHouseNumberOfLifts;
        Long houseNumberOfLifts;
        try {
            strHouseNumberOfLifts = houseNumberOfLiftsField.getText();
            houseNumberOfLifts = Long.parseLong(strHouseNumberOfLifts);
            if (strHouseNumberOfLifts.equals("")) houseNumberOfLiftsField = null;
            else {
                houseNumberOfLifts = Long.parseLong(strHouseNumberOfLifts);
//                if (houseNumberOfLifts <= 0) throw new InvalidValueException();
            }
//        } catch (InvalidValueException exception) {
//            OutputerUI.error("HouseNumberOfFloorsLimitsException", new String[]{String.valueOf(0)});
//            throw new IllegalArgumentException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("HouseNumberOfLiftsFormatException");
            throw new IllegalArgumentException();
        }
        return houseNumberOfLifts;
    }

    private void bindGuiLanguage() {
        houseYearLabel.textProperty().bind(resourceFactory.getStringBinding("HouseYearColumn"));
        houseNumberOfFloorsLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfFloorsColumn"));
        houseNumberOfFlatsOnFloorLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfFlatsOnFloorColumn"));
        houseNumberOfLiftsLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfLiftsColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    public House getAndClear() {
        House houseToReturn = resultHouse;
        resultHouse = null;
        return houseToReturn;
    }

    public void setAskHouseStage(Stage askHouseStage) {
        this.askHouseStage = askHouseStage;
    }

    /**
     * Устанавливает языки инициализации
     *
     * @param resourceFactory Resource factory для установки
     */
    public void initLangs(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }

    public void clearHouse() {
        houseYearField.clear();
        houseNumberOfFloorsField.clear();
        houseNumberOfFlatsOnFloorField.clear();
        houseNumberOfLiftsField.clear();
    }

}
