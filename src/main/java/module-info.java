module TherapyCenter {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires jakarta.persistence;
    requires static lombok;
    requires java.naming;
    requires jbcrypt;
    requires javafx.graphics;

    exports lk.ijse.therapycenter;

    opens lk.ijse.therapycenter.controller to javafx.fxml;

    opens lk.ijse.therapycenter.entity to org.hibernate.orm.core;
}