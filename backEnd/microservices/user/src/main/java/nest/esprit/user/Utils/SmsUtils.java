package nest.esprit.user.Utils;


import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsUtils {
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String FROM_NUMBER = System.getenv("TWILIO_FROM_NUMBER");
    public static void sendSMS(String to,String messagebody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(to),
                        new com.twilio.type.PhoneNumber(FROM_NUMBER),
                        messagebody)
                .create();

        System.out.println(message.getSid());

    }
}
