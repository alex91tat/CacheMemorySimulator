module scs_project.cachememorysimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens scs_project.cachememorysimulator to javafx.fxml;
    exports scs_project.cachememorysimulator;
}