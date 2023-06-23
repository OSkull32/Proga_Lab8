package client.gui;

import client.Client;
import client.utility.OutputerUI;
import client.App;
import common.data.Flat;
import common.data.Furnish;
import common.data.House;
import common.data.View;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainWindow {
    public static final String LOGIN_COMMAND_NAME = "login";
    public static final String REGISTER_COMMAND_NAME = "register";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String INFO_COMMAND_NAME = "info";
    public static final String INSERT_COMMAND_NAME = "insert";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_KEY_COMMAND_NAME = "remove_key";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String EXIT_COMMAND_NAME = "exit";
    public static final String REMOVE_GREATER_KEY_COMMAND_NAME = "remove_greater_key";
    public static final String REMOVE_LOWER_KEY_COMMAND_NAME = "remove_lower_key";
    public static final String REMOVE_ALL_BY_VIEW_COMMAND_NAME = "remove_all_by_view";
    public static final String HISTORY_COMMAND_NAME = "history";
    public static final String FILTER_LESS_THEN_HOUSE_COMMAND_NAME = "filter_less_than_house";
    public static final String PRINT_FIELD_ASCENDING_HOUSE_COMMAND_NAME = "print_field_ascending_house";

    private final long RANDOM_SEED = 1821L;
    private final Duration ANIMATION_DURATION = Duration.millis(800);
    private final double MAX_SIZE = 150;

    @FXML
    private TableView<Flat> flatTable;
    @FXML
    private TableColumn<Flat, Integer> idColumn;
    @FXML
    private TableColumn<Flat, String> ownerColumn;
    @FXML
    private TableColumn<Flat, String> creationDateColumn;
    @FXML
    private TableColumn<Flat, String> nameColumn;
    @FXML
    private TableColumn<Flat, Integer> areaColumn;
    @FXML
    private TableColumn<Flat, Long> numberOfRoomsColumn;
    @FXML
    private TableColumn<Flat, Long> numberOfBathroomsColumn;
    @FXML
    private TableColumn<Flat, Integer> coordinatesXColumn;
    @FXML
    private TableColumn<Flat, Integer> coordinatesYColumn;
    @FXML
    private TableColumn<Flat, View> viewColumn;
    @FXML
    private TableColumn<Flat, Furnish> furnishColumn;
    @FXML
    private TableColumn<Flat, String> houseNameColumn;
    @FXML
    private TableColumn<Flat, Integer> houseYearColumn;
    @FXML
    private TableColumn<Flat, Long> houseNumberOfFloorsColumn;
    @FXML
    private TableColumn<Flat, Long> houseNumberOfFlatsOnFloorColumn;
    @FXML
    private TableColumn<Flat, Long> houseNumberOfLiftsColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;
    @FXML
    private Button infoButton;
    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeKeyButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button executeScriptButton;
    @FXML
    private Button removeGreaterKeyButton;
    @FXML
    private Button removeAllByViewButton;
    @FXML
    private Button removeLowerKeyButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button filterLessThanHouseButton;
    @FXML
    private Button printFieldAscendingButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Tooltip insertButtonTooltip;
    @FXML
    private Tooltip removeAllByViewTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeKeyButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Tooltip executeScriptButtonTooltip;
    @FXML
    private Tooltip removeGreaterKeyButtonTooltip;
    @FXML
    private Tooltip removeLowerKeyButtonTooltip;
    @FXML
    private Tooltip historyButtonTooltip;
    @FXML
    private Tooltip filterLessThanHouseButtonTooltip;
    @FXML
    private Tooltip printFieldAscendingButtonTooltip;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private Stage askHouseStage;
    private FileChooser fileChooser;
    private AskWindow askWindow;
    private AskHouseWindow askHouseWindow;
    private Map<String, Color> userColorMap;
    private Map<Shape, Long> shapeMap;
    private Map<Long, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    public void initialize() {
        initializeTable();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = ResourceFactory.LOCALE_MAP;
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOwner().getUsername()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        areaColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getArea()));
        numberOfRoomsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getNumberOfRooms()));
        numberOfBathroomsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getNumberOfBathrooms()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        furnishColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getFurnish()));
        viewColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getView()));
        houseNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHouse() == null ? null : cellData.getValue().getHouse().getName()));
        houseYearColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHouse() == null ? null : cellData.getValue().getHouse().getYear()));
        houseNumberOfFloorsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHouse() == null ? null : cellData.getValue().getHouse().getNumberOfFloors()));
        houseNumberOfFlatsOnFloorColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHouse() == null ? null : cellData.getValue().getHouse().getNumberOfFlatsOnFloor()));
        houseNumberOfLiftsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHouse() == null ? null : cellData.getValue().getHouse().getNumberOfLifts()));
    }

    private void bindGuiLanguage() {
        client.setLanguage(languageComboBox.getSelectionModel().getSelectedItem());
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        areaColumn.textProperty().bind(resourceFactory.getStringBinding("AreaColumn"));
        numberOfRoomsColumn.textProperty().bind(resourceFactory.getStringBinding("NumberOfRoomsColumn"));
        numberOfBathroomsColumn.textProperty().bind(resourceFactory.getStringBinding("NumberOfBathroomsColumn"));
        furnishColumn.textProperty().bind(resourceFactory.getStringBinding("FurnishColumn"));
        viewColumn.textProperty().bind(resourceFactory.getStringBinding("ViewColumn"));
        houseNameColumn.textProperty().bind(resourceFactory.getStringBinding("HouseNameColumn"));
        houseYearColumn.textProperty().bind(resourceFactory.getStringBinding("HouseYearColumn"));
        houseNumberOfFloorsColumn.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfFloorsColumn"));
        houseNumberOfFlatsOnFloorColumn.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfFlatsOnFloorColumn"));
        houseNumberOfLiftsColumn.textProperty().bind(resourceFactory.getStringBinding("HouseNumberOfLiftsColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        insertButton.textProperty().bind(resourceFactory.getStringBinding("InsertButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeKeyButton.textProperty().bind(resourceFactory.getStringBinding("RemoveKeyButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        executeScriptButton.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButton"));
        filterLessThanHouseButton.textProperty().bind(resourceFactory.getStringBinding("FilterLessThanHouseButton"));
        removeGreaterKeyButton.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterKeyButton"));
        removeLowerKeyButton.textProperty().bind(resourceFactory.getStringBinding("RemoveLowerKeyButton"));
        historyButton.textProperty().bind(resourceFactory.getStringBinding("HistoryButton"));
        printFieldAscendingButton.textProperty().bind(resourceFactory.getStringBinding("PrintFieldAscendingButton"));
        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));
        removeAllByViewButton.textProperty().bind(resourceFactory.getStringBinding("RemoveAllByViewButton"));


        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        insertButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InsertButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeKeyButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveKeyButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        executeScriptButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButtonTooltip"));
        filterLessThanHouseButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("FilterLessThanHouseButtonTooltip"));
        removeGreaterKeyButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterKeyButtonTooltip"));
        removeLowerKeyButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveLowerKeyButtonTooltip"));
        historyButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("HistoryButtonTooltip"));
        printFieldAscendingButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("PrintFieldAscendingButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
        removeAllByViewTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveAllByViewTooltip"));

    }

    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        Hashtable<Integer, Flat> responsedFlat = client.processRequestToServer(commandName, commandStringArgument,
                commandObjectArgument);
        if (responsedFlat != null) {
            Set<Flat> flatValues = new HashSet<>(responsedFlat.values());
            ObservableList<Flat> flatList = FXCollections.observableArrayList(flatValues);
            flatTable.setItems(flatList);
            flatTable.getSelectionModel().clearSelection();
            refreshCanvas();
        }
    }

    /**
     * Binds request action.
     */
    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }

    /**
     * Refresh button on action.
     */
    @FXML
    public void refreshButtonOnAction() {
        requestAction(REFRESH_COMMAND_NAME);
    }

    /**
     * Info button on action.
     */
    @FXML
    private void infoButtonOnAction() {
        requestAction(INFO_COMMAND_NAME);
    }

    /**
     * Add button on action.
     */
    @FXML
    private void insertButtonOnAction() {
        askWindow.clearFlat();
        askStage.showAndWait();
        Flat flat = askWindow.getAndClear();
        if (flat != null) requestAction(INSERT_COMMAND_NAME, "", flat);
    }

    @FXML
    private void updateButtonOnAction() {
        if (!flatTable.getSelectionModel().isEmpty()) {
            int id = flatTable.getSelectionModel().getSelectedItem().getId();
            askWindow.setFlat(flatTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            Flat flat = askWindow.getAndClear();
            if (flat != null){
                flat.setId(id);
                requestAction(UPDATE_COMMAND_NAME, id + "", flat);
            }
        } else OutputerUI.error("UpdateButtonSelectionException");

    }


    @FXML
    private void removeButtonOnAction() {
        if (!flatTable.getSelectionModel().isEmpty())
            requestAction(REMOVE_KEY_COMMAND_NAME,
                    flatTable.getSelectionModel().getSelectedItem().getIdL().toString(), null);
        else OutputerUI.error("RemoveKeyButtonSelectionException");
    }

    @FXML
    private void clearButtonOnAction() {
        requestAction(CLEAR_COMMAND_NAME);
    }

    @FXML
    private void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return;
        if (client.processScriptToServer(selectedFile)) Platform.exit();
        else refreshButtonOnAction();
    }

    @FXML
    private void removeGreaterKeyButtonOnAction() {
        if (!flatTable.getSelectionModel().isEmpty()) {
            Flat flatFromTable = flatTable.getSelectionModel().getSelectedItem();
            Flat flat = new Flat(
                    flatFromTable.getName(),
                    flatFromTable.getCoordinates(),
                    flatFromTable.getArea(),
                    flatFromTable.getNumberOfRooms(),
                    flatFromTable.getNumberOfBathrooms(),
                    flatFromTable.getFurnish(),
                    flatFromTable.getView(),
                    flatFromTable.getHouse()
            );
            requestAction(REMOVE_GREATER_KEY_COMMAND_NAME, flatTable.getSelectionModel().getSelectedItem().getIdL().toString(), flat);
        } else OutputerUI.error("RemoveGreaterKeyButtonSelectionException");
    }

    @FXML
    private void removeAllByViewButtonOnAction() {
        if (!flatTable.getSelectionModel().isEmpty()) {
            Flat flatFromTable = flatTable.getSelectionModel().getSelectedItem();
            Flat flat = new Flat(
                    flatFromTable.getName(),
                    flatFromTable.getCoordinates(),
                    flatFromTable.getArea(),
                    flatFromTable.getNumberOfRooms(),
                    flatFromTable.getNumberOfBathrooms(),
                    flatFromTable.getFurnish(),
                    flatFromTable.getView(),
                    flatFromTable.getHouse()
            );
            requestAction(REMOVE_ALL_BY_VIEW_COMMAND_NAME, flatTable.getSelectionModel().getSelectedItem().getView().toString(), flat);
        } else OutputerUI.error("RemoveAllByViewButtonSelectionException");
    }


    @FXML
    private void removeLowerKeyButtonOnAction() {
        if (!flatTable.getSelectionModel().isEmpty()) {
            Flat flatFromTable = flatTable.getSelectionModel().getSelectedItem();
            Flat flat = new Flat(
                    flatFromTable.getName(),
                    flatFromTable.getCoordinates(),
                    flatFromTable.getArea(),
                    flatFromTable.getNumberOfRooms(),
                    flatFromTable.getNumberOfBathrooms(),
                    flatFromTable.getFurnish(),
                    flatFromTable.getView(),
                    flatFromTable.getHouse()
            );
            requestAction(REMOVE_LOWER_KEY_COMMAND_NAME, flatTable.getSelectionModel().getSelectedItem().getIdL().toString(), flat);
        } else OutputerUI.error("RemoveLowerKeyButtonSelectionException");
    }

    /**
     * History button on action.
     */
    @FXML
    private void historyButtonOnAction() {
        OutputerUI.tryError(client.getHistoryList().toString());
        client.addToHistory("history");
    }

    @FXML
    private void filterLessThanHouseButtonOnAction() {
        askHouseWindow.clearHouse();
        askHouseStage.showAndWait();
        House house = askHouseWindow.getAndClear();
        if (house != null) requestAction(FILTER_LESS_THEN_HOUSE_COMMAND_NAME, "", house);
    }

    @FXML
    private void printFieldAscendingButtonOnAction() {
        requestAction(PRINT_FIELD_ASCENDING_HOUSE_COMMAND_NAME);
    }

    private void refreshCanvas() {
        shapeMap.keySet().forEach(s -> canvasPane.getChildren().remove(s));
        shapeMap.clear();
        textMap.values().forEach(s -> canvasPane.getChildren().remove(s));
        textMap.clear();
        for (Flat flat : flatTable.getItems()) {
            if (!userColorMap.containsKey(flat.getOwner().getUsername()))
                userColorMap.put(flat.getOwner().getUsername(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));

            double size = Math.min(flat.getArea(), MAX_SIZE);

            Shape circleObject = new Circle(size, userColorMap.get(flat.getOwner().getUsername()));
            circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
            circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(flat.getCoordinates().getX()));
            circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(flat.getCoordinates().getY()));

            Text textObject = new Text(flat.getIdL().toString());
            textObject.setOnMouseClicked(circleObject::fireEvent);
            textObject.setFont(Font.font(size / 3));
            textObject.setFill(userColorMap.get(flat.getOwner().getUsername()).darker());
            textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
            textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

            canvasPane.getChildren().add(circleObject);
            canvasPane.getChildren().add(textObject);
            shapeMap.put(circleObject, (long) flat.getId());
            textMap.put((long) flat.getId(), textObject);


            ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
            ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
            circleAnimation.setFromX(0);
            circleAnimation.setToX(1);
            circleAnimation.setFromY(0);
            circleAnimation.setToY(1);
            textAnimation.setFromX(0);
            textAnimation.setToX(1);
            textAnimation.setFromY(0);
            textAnimation.setToY(1);
            circleAnimation.play();
            textAnimation.play();
        }
    }

    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        long id = shapeMap.get(shape);
        for (Flat flat : flatTable.getItems()) {
            if (flat.getId() == id) {
                flatTable.getSelectionModel().select(flat);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setAskHouseStage(Stage askHouseStage) {
        this.askHouseStage = askHouseStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindow(AskWindow askWindow) {
        this.askWindow = askWindow;
    }

    public void setAskHouseWindow(AskHouseWindow askHouseWindow) {
        this.askHouseWindow = askHouseWindow;
    }

    public void initLangs(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty())
            languageComboBox.getSelectionModel().selectFirst();
//        languageComboBox.setOnAction((event) ->
//                resourceFactory.setResources(ResourceBundle.getBundle
//                        (App.BUNDLE, localeMap.get(languageComboBox.getValue()))));
        bindGuiLanguage();
    }

    @FXML
    void updateLanguages(ActionEvent event) {
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, ResourceFactory.LOCALE_MAP.get(languageComboBox.getValue())));
        bindGuiLanguage();
    }


}
