module scs_project.cachememorysimulator {
    requires javafx.controls;
    requires javafx.fxml;

    // JavaFX reflection
    opens scs_project.cachememorysimulator to javafx.fxml;

    // Tests + external access
    exports scs_project.cachememorysimulator;
    exports scs_project.cachememorysimulator.model;
    exports scs_project.cachememorysimulator.controller;
}
