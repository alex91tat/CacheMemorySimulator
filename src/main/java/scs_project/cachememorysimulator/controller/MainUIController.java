package scs_project.cachememorysimulator.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import scs_project.cachememorysimulator.model.*;

public class MainUIController {
    @FXML
    private TextField cacheSizeField;
    @FXML
    private TextField blockSizeField;
    @FXML
    private TextField associativityField;
    @FXML
    private ComboBox<String> mappingCombo;
    @FXML
    private ComboBox<String> replacementCombo;
    @FXML
    private ComboBox<String> writePolicyCombo;
    @FXML
    private TableView<CacheRow> cacheTable;
    @FXML
    private TableView<MemoryRow> memoryTable;

    @FXML
    private Label totalAccessLabel;
    @FXML
    private Label hitLabel;
    @FXML
    private Label missLabel;
    @FXML
    private Label hitRateLabel;
    @FXML
    private Label missRateLabel;
    @FXML
    private TextField addressField;
    @FXML
    private TextField valueField;

    /* ===================== BACKEND ===================== */

    private MainSimulationController simulationController;
    private MainMemory mainMemory;
    private int blockSize = 64;

    /* ===================== INIT ===================== */

    @FXML
    public void initialize() {
        cacheTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        memoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initializeCombos();
        initializeMainMemory();
        setupMainMemoryTable();
        refreshMainMemoryTable();
        updateStatisticsView();

        mappingCombo.setOnAction(e -> updateAssociativityField());
        updateAssociativityField();
    }

    /* ===================== COMBOS ===================== */

    private void initializeCombos() {
        mappingCombo.setItems(FXCollections.observableArrayList(
                "Direct Mapping", "Fully Associative", "Set Associative"));
        mappingCombo.getSelectionModel().selectFirst();

        replacementCombo.setItems(FXCollections.observableArrayList(
                "Random", "FIFO", "LRU"));
        replacementCombo.getSelectionModel().selectFirst();

        writePolicyCombo.setItems(FXCollections.observableArrayList(
                "Write Through", "Write Back"));
        writePolicyCombo.getSelectionModel().selectFirst();
    }

    private void updateAssociativityField() {
        boolean setAssoc = "Set Associative".equals(mappingCombo.getValue());
        associativityField.setDisable(!setAssoc);
        if (!setAssoc) associativityField.setText("1");
    }

    /* ===================== MAIN MEMORY ===================== */

    private void initializeMainMemory() {
        mainMemory = new MainMemory(2048);
        for (int i = 0; i < 2000; i += 64) {
            mainMemory.write(i, "D" + i);
        }
    }

    private void setupMainMemoryTable() {
        TableColumn<MemoryRow, String> addr = new TableColumn<>("Address");
        addr.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<MemoryRow, String> data = new TableColumn<>("Data");
        data.setCellValueFactory(new PropertyValueFactory<>("data"));

        memoryTable.getColumns().setAll(addr, data);
    }

    private void refreshMainMemoryTable() {
        var rows = FXCollections.<MemoryRow>observableArrayList();
        for (int a = 0; a < 2000; a += blockSize) {
            rows.add(new MemoryRow(
                    String.format("0x%04X (%d)", a, a),
                    mainMemory.read(a, blockSize)
            ));
        }
        memoryTable.setItems(rows);
    }

    /* ===================== CONFIG ===================== */

    @FXML
    private void applyConfigButtonOnAction(ActionEvent e) {
        try {
            int cacheSize = Integer.parseInt(cacheSizeField.getText());
            blockSize = Integer.parseInt(blockSizeField.getText());

            int associativity = "Set Associative".equals(mappingCombo.getValue())
                    ? Integer.parseInt(associativityField.getText())
                    : 1;

            if (associativity <= 0)
                throw new IllegalArgumentException("Associativity must be positive");

            AddressMappingStrategy map =
                    createMappingStrategy(cacheSize, blockSize, associativity);

            simulationController = new MainSimulationController(
                    cacheSize, blockSize, associativity,
                    map, createReplacementPolicy(),
                    createWritingPolicy(), mainMemory
            );

            setupCacheTable(map, cacheSize, blockSize, associativity);

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void readButtonOnAction(ActionEvent e) {
        handleRead();
    }

    @FXML
    private void writeButtonOnAction(ActionEvent e) {
        handleWrite();
    }

    @FXML
    private void resetButtonOnAction(ActionEvent e) {
        handleReset();
    }

    private void handleRead() {
        if (simulationController == null) {
            showError("Please configure the cache first.");
            return;
        }

        try {
            int address = Integer.parseInt(addressField.getText());

            // ⭐ capture returned value
            String data = simulationController.read(address);

            // ⭐ show result
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Read Result");
            alert.setContentText(
                    "Address: " + address +
                            "\nData: " + data
            );
            alert.showAndWait();

            refreshCacheTable();
            refreshMainMemoryTable();
            updateStatisticsView();

        } catch (NumberFormatException e) {
            showError("Invalid address.");
        }
    }


    private void handleWrite() {
        if (simulationController == null) {
            showError("Please configure the cache first.");
            return;
        }

        try {
            int address = Integer.parseInt(addressField.getText());
            String value = valueField.getText();

            if (value == null || value.isBlank()) {
                showError("Write value cannot be empty.");
                return;
            }

            simulationController.write(address, value);

            refreshCacheTable();
            refreshMainMemoryTable();
            updateStatisticsView();

        } catch (NumberFormatException e) {
            showError("Invalid address.");
        }
    }

    private void handleReset() {

        simulationController = null;

        initializeMainMemory();
        refreshMainMemoryTable();

        cacheTable.getItems().clear();

        updateStatisticsView();
    }

    private void refreshCacheTable() {

        Cache cache = simulationController.getCache();
        var rows = FXCollections.<CacheRow>observableArrayList();

        int lineNumber = 0;

        for (int setIndex = 0; setIndex < cache.getSets().length; setIndex++) {
            CacheSet set = cache.getSets()[setIndex];

            for (CacheLine line : set.getLines()) {

                rows.add(new CacheRow(
                        lineNumber,
                        (cache.getSets().length > 1 ? setIndex : -1),
                        line.isValid() ? String.format("0x%04X", line.getTag()) : "-",
                        line.isValid() ? "1" : "0",
                        line.isDirty() ? "1" : "0",
                        line.isValid() ? line.getData() : ""
                ));

                lineNumber++;
            }
        }

        cacheTable.setItems(rows);
    }


    /* ===================== CACHE TABLE ===================== */

    private void setupCacheTable(AddressMappingStrategy map,
                                 int cacheSize, int blockSize, int associativity) {

        cacheTable.getColumns().clear();
        cacheTable.getItems().clear();

        TableColumn<CacheRow, Integer> lineCol = new TableColumn<>("Line");
        lineCol.setCellValueFactory(new PropertyValueFactory<>("line"));

        TableColumn<CacheRow, Integer> setCol = new TableColumn<>("Set");
        setCol.setCellValueFactory(new PropertyValueFactory<>("set"));

        TableColumn<CacheRow, String> tagCol = new TableColumn<>("Tag");
        tagCol.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<CacheRow, String> validCol = new TableColumn<>("Valid");
        validCol.setCellValueFactory(new PropertyValueFactory<>("valid"));

        TableColumn<CacheRow, String> dirtyCol = new TableColumn<>("Dirty");
        dirtyCol.setCellValueFactory(new PropertyValueFactory<>("dirty"));

        TableColumn<CacheRow, String> dataCol = new TableColumn<>("Data");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));

        if (map instanceof SetAssociativeMappingStrategy) {
            cacheTable.getColumns().addAll(lineCol, setCol, tagCol, validCol, dirtyCol, dataCol);
        } else {
            cacheTable.getColumns().addAll(lineCol, tagCol, validCol, dirtyCol, dataCol);
        }

        int lines = cacheSize / blockSize;
        var rows = FXCollections.<CacheRow>observableArrayList();

        for (int line = 0; line < lines; line++) {
            int set = (map instanceof SetAssociativeMappingStrategy)
                    ? line / associativity
                    : -1;

            rows.add(new CacheRow(line, set, "-", "0", "0", ""));
        }

        cacheTable.setItems(rows);
    }

    /* ===================== HELPERS ===================== */

    private AddressMappingStrategy createMappingStrategy(int c, int b, int a) {
        return switch (mappingCombo.getValue()) {
            case "Direct Mapping" -> new DirectMappingStrategy(b, c / b);
            case "Fully Associative" -> new FullyAssociativeMappingStrategy(b);
            default -> new SetAssociativeMappingStrategy(b, c / b, a);
        };
    }

    private ReplacementPolicy createReplacementPolicy() {
        return switch (replacementCombo.getValue()) {
            case "FIFO" -> new FifoPolicy();
            case "LRU" -> new LruPolicy();
            default -> new RandomPolicy();
        };
    }

    private WritingPolicy createWritingPolicy() {
        return "Write Back".equals(writePolicyCombo.getValue())
                ? new WriteBackPolicy()
                : new WriteThroughPolicy();
    }

    private void updateStatisticsView() {

        if (simulationController == null) {
            totalAccessLabel.setText("0");
            hitLabel.setText("0");
            missLabel.setText("0");
            hitRateLabel.setText("0.00 %");
            missRateLabel.setText("0.00 %");
            return;
        }

        Statistics s = simulationController.getStatistics();

        totalAccessLabel.setText(String.valueOf(s.getTotalAccesses()));
        hitLabel.setText(String.valueOf(s.getHits()));
        missLabel.setText(String.valueOf(s.getMisses()));
        hitRateLabel.setText(String.format("%.2f %%", s.getHitRate() * 100));
        missRateLabel.setText(String.format("%.2f %%", s.getMissRate() * 100));
    }


    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    /* ===================== ROW CLASSES ===================== */

    public static class MemoryRow {
        private final String address, data;
        public MemoryRow(String a, String d) { address = a; data = d; }
        public String getAddress() { return address; }
        public String getData() { return data; }
    }

    public static class CacheRow {
        private final int line, set;
        private final String tag, valid, dirty, data;

        public CacheRow(int line, int set,
                        String tag, String valid,
                        String dirty, String data) {
            this.line = line;
            this.set = set;
            this.tag = tag;
            this.valid = valid;
            this.dirty = dirty;
            this.data = data;
        }

        public int getLine() { return line; }
        public int getSet() { return set; }
        public String getTag() { return tag; }
        public String getValid() { return valid; }
        public String getDirty() { return dirty; }
        public String getData() { return data; }
    }
}
