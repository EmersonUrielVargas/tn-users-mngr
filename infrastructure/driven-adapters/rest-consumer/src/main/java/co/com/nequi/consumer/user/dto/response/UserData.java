package co.com.nequi.consumer.user.dto.response;

import lombok.Data;

@Data
public class UserData {
    private Long id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}