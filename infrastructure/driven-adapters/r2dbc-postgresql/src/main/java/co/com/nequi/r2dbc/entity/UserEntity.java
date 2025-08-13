package co.com.nequi.r2dbc.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table("tn_users_app.users")
public class UserEntity {
    @Id
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String lastname;

    @NotBlank
    @Size(min = 3, max = 100)
    private String email;

    @NotBlank
    @Size(max = 500)
    @Column("photo_url")
    private String photoUrl;
}
