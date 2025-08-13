package co.com.nequi.api.dto.response;

import co.com.nequi.api.constants.SchemaConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    @Schema(description = SchemaConstants.USER_DETAILS_ID_DESC)
    private Long id;

    @Schema(description = SchemaConstants.USER_DETAILS_NAME_DESC)
    private String name;

    @Schema(description = SchemaConstants.USER_DETAILS_EMAIL_DESC)
    private String email;

    @Schema(description = SchemaConstants.USER_DETAILS_PHOTO_URL_DESC)
    private String photoUrl;

    @Schema(description = SchemaConstants.USER_DETAILS_LASTNAME_DESC)
    private String lastname;
}
