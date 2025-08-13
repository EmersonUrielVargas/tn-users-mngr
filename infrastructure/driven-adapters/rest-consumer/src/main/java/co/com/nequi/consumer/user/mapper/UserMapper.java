package co.com.nequi.consumer.user.mapper;

import co.com.nequi.consumer.user.dto.response.UserData;
import co.com.nequi.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "name", source = "first_name")
    @Mapping(target = "lastname", source = "last_name")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "photoUrl", source = "avatar")
    @Mapping(target = "email", source = "email")
    User userDataToUser(UserData userData);

}
