module com.example.examendi {
    requires javafx.controls;
    requires javafx.fxml;
    // Añadido
    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires jasperreports;


    opens com.example.examendi to javafx.fxml;
    exports com.example.examendi;
    exports com.example.examendi.utlity;
    opens com.example.examendi.utlity;
    exports com.example.examendi.domain;
    opens com.example.examendi.domain;
}