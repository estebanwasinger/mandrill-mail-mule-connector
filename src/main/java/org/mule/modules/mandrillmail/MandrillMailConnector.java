package org.mule.modules.mandrillmail;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.param.*;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.mandrillmail.config.ConnectorConfig;
import org.mule.modules.mandrillmail.metadata.SendEmailCategory;
import org.mule.modules.mandrillmail.metadata.TemplateCategory;
import org.mule.modules.mandrillmail.model.MandrillAttachment;
import org.mule.modules.mandrillmail.model.Recipient;
import org.mule.modules.mandrillmail.model.transformer.AttachmentTransformer;
import org.mule.modules.mandrillmail.model.transformer.RecipientTransformer;

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
     * @param recipientList
     * @param attachmentList
     * @return
     * @throws IOException
     * @throws MandrillApiError
     */
    @Processor
    @MetaDataScope(SendEmailCategory.class)
    public List<MandrillMessageStatus> sendEmail(@Placement(group = "Message") String subject, @Placement(group = "Message") @Default("#[payload]") String body, @Placement(group = "Sender") String fromEmail, @Placement(group = "Sender") @Optional String fromName,
                                                 @RefOnly @Default("#[flowVars.recipients]") @Placement(group = "Recipient") List<Recipient> recipientList, @RefOnly @Default("#[flowVars.attachments]") @Placement(group = "Attachment") List<MandrillAttachment> attachmentList,
                                                 @FriendlyName("Tag List") @RefOnly @Default("#[flowVars.tags]") @Placement(group = "Extras") List<String> tags, @Default("#[false]") @Placement(group = "Extras") Boolean sendAsync,
                                                 @Placement(group = "DataSense") @MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String key) throws IOException, MandrillApiError {
        MandrillApi mandrillApi = getConfig().getClient();
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setHtml(body);
        message.setAutoText(true);
        message.setFromEmail(fromEmail);
        message.setFromName(fromName);
        message.setTo(RecipientTransformer.getRecipientList(recipientList));
        message.setAttachments(AttachmentTransformer.getMessageContentList(attachmentList));
        if (tags != null) {
            message.setTags(tags);
        }
        message.setPreserveRecipients(true);
        MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, sendAsync);
        return Arrays.asList(messageStatusReports);
    }

    /**
     * @param templateName
     * @param subject
     * @param parameters
     * @param fromEmail
     * @param fromName
     * @param recipientList
     * @param attachments
     * @return
     * @throws IOException
     * @throws MandrillApiError
     */
    @Processor
    @MetaDataScope(TemplateCategory.class)
    public List<MandrillMessageStatus> sendTemplateEmail(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String templateName, @Placement(group = "Message") String subject, @Placement(group = "Message") @Default(value = "#[payload]") Map<String, String> parameters, @Optional @Placement(group = "Sender") String fromEmail, @Placement(group = "Sender") String fromName, @Default("#[flowVars.recipients]") @Placement(group = "Recipient") List<Recipient> recipientList, @Placement(group = "Attachments") @Summary("List of attachments to send.") @FriendlyName("Attachments") @Default("#[flowVars.attachments]") List<MandrillAttachment> attachments, @Default("#[flowVars.tags]") @Placement(group = "Extras") List<String> tags, @Placement(group = "Extras") @Default("#[false]") Boolean sendAsync) throws IOException, MandrillApiError {
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setAutoText(true);
        message.setFromEmail(fromEmail);
        message.setFromName(fromName);
        message.setTo(RecipientTransformer.getRecipientList(recipientList));
        message.setPreserveRecipients(true);
        List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();

        for (String key : parameters.keySet()) {
            mergeVars.add(new MandrillMessage.MergeVar(key, parameters.get(key)));
        }

        message.setAttachments(AttachmentTransformer.getMessageContentList(attachments));

        message.setGlobalMergeVars(mergeVars);

        if (tags != null) {
            message.setTags(tags);
        }

        MandrillMessageStatus[] mandrillMessageStatuses = getConfig().getClient().messages().sendTemplate(templateName, new HashMap<String, String>(), message, sendAsync);
        return Arrays.asList(mandrillMessageStatuses);
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

}