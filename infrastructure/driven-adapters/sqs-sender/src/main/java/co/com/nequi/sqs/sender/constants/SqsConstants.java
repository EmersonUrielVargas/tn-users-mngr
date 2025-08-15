package co.com.nequi.sqs.sender.constants;

public class SqsConstants {
    public static final String SQS_SENDER_ADAPTER = "SQS_SENDER_ADAPTER";
    public static final String SQS_SENDER_LOG = "SQS_SENDER_LOG";
    public static final String SQS_SENDER_METRICS = "SQS_SENDER_METRICS";
    public static final String SQS_SENDER_TRACING = "SQS_SENDER_TRACING";
    public static final String SQS_SENDER_ERROR = "SQS_SENDER_ERROR";
    public static final String LOG_SEND_NEW_USER_SUCCESSFUL = "Send notification new user Message Response";
    public static final String LOG_SEND_NEW_USER_ERROR = "Error Sending notification new user Message";
    public static final String ERROR_PARSING_BODY_MESSAGE = "Error during parser to string sqs";
    public static final String LOG_PARSING_BODY_KEY = "sqsParserToStringError";
    public static final String LOG_RESPONSE_SUCCESSFUL_KEY = "sendNewUserMessageRS";
    public static final String LOG_RESPONSE_ERROR_KEY = "sendNewUserErrorMessageRS";
}
