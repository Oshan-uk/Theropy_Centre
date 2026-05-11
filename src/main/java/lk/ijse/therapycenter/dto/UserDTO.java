package lk.ijse.therapycenter.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;

    public UserDTO() {}

    public UserDTO(int id, String username, String password, String role, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

}