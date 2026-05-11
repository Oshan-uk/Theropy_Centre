package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "therapists")
public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(nullable = false)
    private boolean available = true;


    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TherapySession> sessions = new ArrayList<>();

    public Therapist() {}

    public Therapist(String fullName, String email, String phone, String specialization) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public List<TherapySession> getSessions() { return sessions; }
    public void setSessions(List<TherapySession> sessions) { this.sessions = sessions; }
}