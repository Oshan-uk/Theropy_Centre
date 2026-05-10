module TherapyCenter {
    requires jakarta.persistence;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jbcrypt;

    opens lk.ijse.therapycenter.entity to org.hibernate.orm.core;
}