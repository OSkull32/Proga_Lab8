package client.gui;

import client.utility.OutputerUI;
import common.data.*;
import common.exceptions.InvalidValueException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Устанавливает окно
 */
public class AskWindow {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label areaLabel;
    @FXML
    private Label furnishLabel;
    @FXML
    private Label numberOfRoomsLabel;
    @FXML
    private Label numberOfBathroomsLabel;
    @FXML
    private Label viewLabel;
    @FXML
    private Label houseYearLabel;
    @FXML
    private Label houseNameLabel;
    @FXML
    private Label houseNumberOfFloorsLabel;
    @FXML
    private Label houseNumberOfFlatsOnFloorLabel;
    @FXML
    private Label houseNumberOfLiftsLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField areaField;
    @FXML
    private TextField numberOfRoomsField;
    @FXML
    private TextField numberOfBathroomsField;
    @FXML
    private TextField houseYearField;
    @FXML
    private TextField houseNameField;
    @FXML
    private TextField houseNumberOfFloorsField;
    @FXML
    private TextField houseNumberOfFlatsOnFloorField;
    @FXML
    private TextField houseNumberOfLiftsField;
    @FXML
    private ComboBox<View> viewBox;
    @FXML
    private ComboBox<Furnish> furnishBox;
    @FXML
    private Button enterButton;

    private Stage askStage;
    private Flat resultFlat;
    private ResourceFactory resourceFactory;

    /**
     * Инициализировать окно запроса.
     */
    public void initialize() {
        viewBox.setItems(FXCollections.observableArrayList(View.values()));
        furnishBox.setItems(FXCollections.observableArrayList(Furnish.values()));
    }

    /**
     * Кнопка ввода в действии.
     */
    @FXML
    private void enterButtonOnAction() {
        try {
            resultFlat = new Flat(
                                convertName(),
                                new Coordinates(
                                        convertCoordinatesX(),
                                        convertCoordinatesY()
                                ),
                                convertArea(),
                                convertNumberOfRooms(),
                                convertNumberOfBathrooms(),
                    furnishBox.getValue(),
                    viewBox.getValue(),
                                new House(
                                        convertHouseName(),
                                        convertHouseYear(),
                                        convertHouseNumberOfFloors(),
                                        convertHouseNumberOfFlatsOnFloor(),
                                        convertHouseNumberOfLifts()
                                )
                        );
            askStage.close();
        } catch (IllegalArgumentException exception) { /* ? */ }
    }

    private String convertName() throws IllegalArgumentException {
        String name;
        try {
            name = nameField.getText();
            if (name.equals("")) throw new InvalidValueException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }
        return name;
    }

    private int convertCoordinatesX() throws IllegalArgumentException {
        String strX;
        int x;
        try {
            strX = coordinatesXField.getText();
            x = Integer.parseInt(strX);
            if (x > 713) throw new InvalidValueException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesXFormatException");
            throw new IllegalArgumentException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("CoordinatesXLimitsException", new String[]{String.valueOf(713)});
            throw new IllegalArgumentException();
        }
        return x;
    }

    private int convertCoordinatesY() throws IllegalArgumentException {
        String strY;
        Integer y;
        try {
            strY = coordinatesYField.getText();
            y = Integer.parseInt(strY);
            if (y <= -397) throw new InvalidValueException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesYFormatException");
            throw new IllegalArgumentException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("CoordinatesYLimitsException", new String[]{String.valueOf(-396)});
            throw new IllegalArgumentException();
        }
        return y;
    }

    private int convertArea() throws IllegalArgumentException {
        String strArea;
        int area;
        try {
            strArea = areaField.getText();
            area = Integer.parseInt(strArea);
            if (area <= 0) throw new InvalidValueException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("AreaFormatException");
            throw new IllegalArgumentException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("AreaLimitsException");
            throw new IllegalArgumentException();
        }
        return area;
    }

    private long convertNumberOfRooms() throws IllegalArgumentException {
        String strNumberOfRooms;
        long numberOfRooms;
        try {
            strNumberOfRooms = numberOfRoomsField.getText();
            numberOfRooms = Long.parseLong(strNumberOfRooms);
            if (numberOfRooms <= 0 || numberOfRooms > 14) throw new InvalidValueException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("NumberOfRoomsFormatException");
            throw new IllegalArgumentException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("NumberOfRoomsLimitsException");
            throw new IllegalArgumentException();
        }
        return numberOfRooms;
    }

    private long convertNumberOfBathrooms() throws IllegalArgumentException {
        String strNumberOfBathrooms;
        long numberOfBathrooms;
        try {
            strNumberOfBathrooms = numberOfBathroomsField.getText();
            numberOfBathrooms = Long.parseLong(strNumberOfBathrooms);
            if (numberOfBathrooms <= 0) throw new InvalidValueException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("NumberOfBathroomsFormatException");
            throw new IllegalArgumentException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("NumberOfBathroomsLimitsException");
            throw new IllegalArgumentException();
        }
        return numberOfBathrooms;
    }

    private String convertHouseName() {
        String houseName;
            houseName = houseNameField.getText();
            if (houseName.equals("")) houseName = null;
        return houseName;
    }

    private int convertHouseYear() throws IllegalArgumentException {
        String strHouseYear;
        int houseYear;
        try {
            strHouseYear = houseYearField.getText();
            houseYear = Integer.parseInt(strHouseYear);
            if (houseYear <= 0) throw new InvalidValueException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("HouseYearLimitsException", new String[]{String.valueOf(0)});
            throw new IllegalArgumentException();
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
                if (houseNumberOfFloors <= 0 || houseNumberOfFloors > 39) throw new InvalidValueException();
            }
        } catch (InvalidValueException exception) {
            OutputerUI.error("HouseNumberOfFloorsLimitsException", new String[]{String.valueOf(0)});
            throw new IllegalArgumentException();
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
            if (houseNumberOfFlatsOnFloor <= 0) throw new InvalidValueException();
        } catch (InvalidValueException exception) {
            OutputerUI.error("HouseNumberOfFlatsOnFloorLimitsException", new String[]{String.valueOf(0)});
            throw new IllegalArgumentException();
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
                if (houseNumberOfLifts <= 0) throw new InvalidValueException();
            }
        } catch (InvalidValueException exception) {
            OutputerUI.error("HouseNumberOfLiftsLimitsException", new String[]{String.valueOf(0)});
            throw new IllegalArgumentException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("HouseNumberOfLiftsFormatException");
            throw new IllegalArgumentException();
        }
        return houseNumberOfLifts;
    }

    /**
     * Привязывает язык интерфейса.
     */
    private void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        areaLabel.textProperty().bind(resourceFactory.getStringBinding("AreaColumn"));
        numberOfRoomsLabel.textProperty().bind(resourceFactory.getStringBinding("NumberOfRoomsColumn"));
        numberOfBathroomsLabel.textProperty().bind(resourceFactory.getStringBinding("NumberOfBathroomsColumn"));
        furnishLabel.textProperty().bind(resourceFactory.getStringBinding("FurnishColumn"));
        viewLabel.textProperty().bind(resourceFactory.getStringBinding("ViewColumn"));
        houseNameLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNameColumn"));
        houseYearLabel.textProperty().bind(resourceFactory.getStringBinding("HouseYearColumn"));
        houseNumberOfFloorsLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfFloorsColumn"));
        houseNumberOfFlatsOnFloorLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfFlatsOnFloorColumn"));
        houseNumberOfLiftsLabel.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfLiftsColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    public void setFlat(Flat flat) {
        nameField.setText(flat.getName());
        coordinatesXField.setText(flat.getCoordinates().getX() + "");
        coordinatesYField.setText(flat.getCoordinates().getY() + "");
        areaField.setText(flat.getArea() + "");
        numberOfRoomsField.setText(flat.getNumberOfRooms() + "");
        numberOfBathroomsField.setText(flat.getNumberOfBathrooms() + "");
        houseNameField.setText(flat.getHouse().getName());
        houseYearField.setText(flat.getHouse().getYear() + "");
        houseNumberOfFloorsField.setText(flat.getHouse().getNumberOfFloors() + "");
        houseNumberOfFlatsOnFloorField.setText(flat.getHouse().getNumberOfFlatsOnFloor() + "");
        houseNumberOfLiftsField.setText(flat.getHouse().getNumberOfLifts() + "");
        furnishBox.setValue(flat.getFurnish());
        viewBox.setValue(flat.getView());
    }

    public void clearFlat() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        areaField.clear();
        numberOfRoomsField.clear();
        numberOfBathroomsField.clear();
        houseNameField.clear();
        houseYearField.clear();
        houseNumberOfFloorsField.clear();
        houseNumberOfFlatsOnFloorField.clear();
        houseNumberOfLiftsField.clear();
        furnishBox.setValue(Furnish.NONE);
        viewBox.setValue(View.NORMAL);
    }

    /**
     * Получить и очистить flat
     *
     * @return Flat
     */
    public Flat getAndClear() {
        Flat flatToReturn = resultFlat;
        resultFlat = null;
        return flatToReturn;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
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

}
