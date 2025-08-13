package co.com.nequi.api.mapper;

import co.com.nequi.api.dto.response.UserDetails;
import co.com.nequi.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserDetailsMapper {

    UserDetailsMapper MAPPER = Mappers.getMapper(UserDetailsMapper.class);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "photoUrl", source = "photoUrl")
    @Mapping(target = "email", source = "email")
    UserDetails userToUserDetails(User user);

}
