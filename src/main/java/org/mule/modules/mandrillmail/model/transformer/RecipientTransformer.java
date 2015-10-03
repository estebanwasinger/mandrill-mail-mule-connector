package org.mule.modules.mandrillmail.model.transformer;

import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import org.mule.modules.mandrillmail.exception.MandrillMailException;
import org.mule.modules.mandrillmail.model.Recipient;

import java.util.ArrayList;
import java.util.List;

public class RecipientTransformer {

    public static List<MandrillMessage.Recipient> getRecipientList(List<Recipient> recipientList) {
        List<MandrillMessage.Recipient> mandrillRecipientList = new ArrayList<>();
        if (recipientList == null) {
            throw new MandrillMailException("The recipient list is Null. To send a Email is required at least one recipient");
        }
        if (recipientList.isEmpty()) {
            throw new MandrillMailException("The recipient list is empty. To send a Email is required at least one recipient.");
        }
        for (Recipient recipient : recipientList) {
            MandrillMessage.Recipient newRecipient = new MandrillMessage.Recipient();
            newRecipient.setType(MandrillMessage.Recipient.Type.valueOf(recipient.getType().toString()));
            newRecipient.setName(recipient.getName());
            newRecipient.setEmail(recipient.getEmail());
            mandrillRecipientList.add(newRecipient);
        }
        return mandrillRecipientList;
    }
}
