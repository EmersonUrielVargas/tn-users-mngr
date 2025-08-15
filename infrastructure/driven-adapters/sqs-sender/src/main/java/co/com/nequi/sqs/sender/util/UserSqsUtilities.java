package co.com.nequi.sqs.sender.util;

import co.com.nequi.sqs.sender.constants.SqsConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@UtilityClass
public class UserSqsUtilities {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String parserToStringSQS(Object object) {
        String parserToString = null;
        try {
            parserToString = mapper.writeValueAsString(object);
        } catch (Exception exception) {
            log.info(SqsConstants.ERROR_PARSING_BODY_MESSAGE, kv(SqsConstants.LOG_PARSING_BODY_KEY, exception));
        }
        return parserToString;
    }
}
