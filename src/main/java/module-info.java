module lk.ijse.therapycenter {


    requires javafx.controls;
    requires javafx.fxml;


    requires org.hibernate.orm.core;
    requires jakarta.persistence;


    requires jbcrypt;


    requires java.naming;
    requires java.sql;


    opens lk.ijse.therapycenter to javafx.fxml;
    opens lk.ijse.therapycenter.controller to javafx.fxml;
    opens lk.ijse.therapycenter.entity to org.hibernate.orm.core, jakarta.persistence;
    opens lk.ijse.therapycenter.dto to javafx.base;

    exports lk.ijse.therapycenter;
    exports lk.ijse.therapycenter.controller;
    exports lk.ijse.therapycenter.bo;
    exports lk.ijse.therapycenter.dto;
}