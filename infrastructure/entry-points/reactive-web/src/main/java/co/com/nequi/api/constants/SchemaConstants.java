package co.com.nequi.api.constants;

public class SchemaConstants {
    public static final String API_USER_TAG = "user";
    public static final String OPERATION_ID_FIND_USER_BY_ID = "getUserById";
    public static final String SUMMARY_FIND_USER_BY_ID = "Get a user in API by id";

    public static final String OPERATION_ID_CREATE_USER = "postCreateUser";
    public static final String SUMMARY_CREATE_USER = "Register in API a user by id in external service";

    public static final String OPERATION_ID_GET_ALL_USERS = "getAllUsers";
    public static final String SUMMARY_GET_ALL_USERS = "List all users or filter by name";

    public static final String DESCRIPTION_PARAM_ID = "Unique identifier of the user";
    public static final String DESCRIPTION_QUERY_PARAM_NAME = "Filter users by first name";
    public static final String DESCRIPTION_RESPONSE_SUCCESSFUL = "Successful operation";
    public static final String DESCRIPTION_RESPONSE_400 = "Invalid request parameters";
    public static final String DESCRIPTION_RESPONSE_404 = "User not found";
    public static final String DESCRIPTION_RESPONSE_500 = "Internal server error";


    public static final String USER_DETAILS_ID_DESC = DESCRIPTION_PARAM_ID;
    public static final String USER_DETAILS_NAME_DESC = "First name of the user";
    public static final String USER_DETAILS_LASTNAME_DESC = "Last name of the user";
    public static final String USER_DETAILS_EMAIL_DESC = "Email of the user";
    public static final String USER_DETAILS_PHOTO_URL_DESC = "Photo URL of the user";
}
