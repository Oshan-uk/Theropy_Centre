package lk.ijse.therapycenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.dao.custom.impl.UserDAOImpl;
import lk.ijse.therapycenter.entity.User;
import lk.ijse.therapycenter.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        seedDefaultUsers(); // create admin + receptionist on first run

        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Serenity Mental Health Therapy Center");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        FactoryConfiguration.shutdown();
    }

    private void seedDefaultUsers() {
        try (Session session = FactoryConfiguration.getSession()) {
            Transaction tx = session.beginTransaction();
            Long count = session.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.username = 'admin'", Long.class)
                    .uniqueResult();

            if (count ==
                    0
            ) {
                session.persist(new User("admin",
                        PasswordUtil.hashPassword("Admin@123"), "ADMIN", "admin@serenity.lk"));
                session.persist(new User("receptionist",
                        PasswordUtil.hashPassword("Recep@123"), "RECEPTIONIST", "recep@serenity.lk"));

                System.out.println("✅ Default users created:");
                System.out.println("   admin / Admin@123  (ADMIN)");
                System.out.println("   receptionist / Recep@123  (RECEPTIONIST)");
            }
            tx.commit();
        } catch (Exception e) {
            System.err.println("User seed failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) { launch(args); }
}