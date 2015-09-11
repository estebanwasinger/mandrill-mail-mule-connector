package org.mule.modules.mandrillmail;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.*;
import org.mule.modules.mandrillmail.config.ConnectorConfig;
import org.mule.modules.mandrillmail.metadata.TemplateCategory;

import java.io.IOException;
import java.util.*;

@Connector(name = "mandrill-mail", friendlyName = "Mandrill Mail")
public class MandrillMailConnector {

    @Config
    ConnectorConfig config;

    /**
     * @param subject
     * @param body
     * @param fromEmail
     * @param fromName
     * @param recipientEmail
     * @param recipientName
     * @param tags
     * @return
     * @throws IOException
     * @throws MandrillApiError
     */
    @Processor
    public List<MandrillMessageStatus> sendEmail(@Placement(group = "Message") String subject, @Placement(group = "Message") @Default(value = "#[payload]") String body, @Placement(group = "Message") String fromEmail, @Placement(group = "Message") String fromName, @Placement(group = "Recipient") String recipientEmail, @Placement(group = "Recipient") String recipientName, @Placement(group = "Other") List<String> tags) throws IOException, MandrillApiError {
        MandrillApi mandrillApi = getConfig().getClient();
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setHtml(body);
        message.setAutoText(true);
        message.setFromEmail(fromEmail);
        message.setFromName(fromName);
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(recipientEmail);
        recipient.setName(recipientName);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(true);
        message.setTags(tags);
        MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);
        return Arrays.asList(messageStatusReports);
    }

    /**
     * 
     * @param templateName
     * @param subject
     * @param parameters
     * @param fromEmail
     * @param fromName
     * @param recipientEmail
     * @param recipientName
     * @param tags
     * @return
     * @throws IOException
     * @throws MandrillApiError
     */
    @Processor
    @MetaDataScope(TemplateCategory.class)
    public List<MandrillMessageStatus> sendTemplateEmail(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String templateName, @Placement(group = "Message") String subject, @Placement(group = "Message") @Default(value = "#[payload]") Map<String,Object> parameters, @Placement(group = "Message") String fromEmail, @Placement(group = "Message") String fromName, @Placement(group = "Recipient") String recipientEmail, @Placement(group = "Recipient") String recipientName, @Placement(group = "Other") @Optional List<String> tags) throws IOException, MandrillApiError {
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setAutoText(true);
        message.setFromEmail(fromEmail);
        message.setFromName(fromName);
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(recipientEmail);
        recipient.setName(recipientName);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(true);
        List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();
        for (String key : parameters.keySet()) {
            mergeVars.add(new MandrillMessage.MergeVar(key,(String) parameters.get(key)));
        }

        message.setGlobalMergeVars(mergeVars);

        MandrillMessageStatus[] mandrillMessageStatuses = getConfig().getClient().messages().sendTemplate(templateName, new HashMap<String, String>(), message, false);
        return Arrays.asList(mandrillMessageStatuses);
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

}