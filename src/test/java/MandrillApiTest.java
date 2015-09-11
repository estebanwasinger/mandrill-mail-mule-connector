import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillTemplate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by estebanwasinger on 9/11/15.
 */
public class MandrillApiTest {

    MandrillApi mandrillApi;
    MandrillTemplate template;
    @Before
    public void setup() throws IOException, MandrillApiError {
        mandrillApi = new MandrillApi("Q6n5lFTiJCX51XPtbcT-ig");
        template = mandrillApi.templates().info("Welcome Anypoint Platform");
    }

    @Test
    public void test() throws IOException, MandrillApiError {
        Map<String,String> map = new HashMap<>();
        map.put("link", "http://google.com");

        MandrillMessage message = new MandrillMessage();
        message.setSubject("Hi");
        message.setAutoText(true);
        message.setFromEmail("esteban.wasinger@mulesoft.com");
        message.setFromName("Esteban Wasinger");
        // add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail("esteban.wasinger@mulesoft.com");
        recipient.setName("Esteban");
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(true);
        List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();
        mergeVars.add(new MandrillMessage.MergeVar("link", "http://google.com"));
        message.setGlobalMergeVars(mergeVars);
        mandrillApi.messages().sendTemplate(template.getName(),map,message,false);

//        for (MandrillTemplate mandrillTemplate : mandrillApi.templates().list()) {
//            System.out.println(mandrillTemplate.getName());
//        }

    }

    @Test
    public void testParseTemplate(){
       // System.out.println(template.getCode());
        for (String s : getHolders(template.getCode())) {
            System.out.println(s);
        }
        ;
    }

    private Set<String> getHolders(String template){
        Set<String> holders = new HashSet<>();
        Pattern pattern = Pattern.compile("\\*\\|(.*)\\|\\*");
        Matcher matcher = pattern.matcher(template);
        while(matcher.find()){
            holders.add(matcher.group(1));
        }
        return holders;
    }

    private String cleanHolder(String holder) {
        return holder.replaceAll("(&lt;%|%&gt;|<%|%>)","");
    }


}
