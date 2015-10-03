package org.mule.modules.mandrillmail.model.transformer;

import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import org.mule.modules.mandrillmail.model.MandrillAttachment;

import java.util.ArrayList;
import java.util.List;

public class AttachmentTransformer {

    public static List<MandrillMessage.MessageContent> getMessageContentList(List<MandrillAttachment> attachmentList){
        List<MandrillMessage.MessageContent> messageContentList = new ArrayList<>();
        if(attachmentList == null){
            return messageContentList;
        }
        for(MandrillAttachment attachment : attachmentList){
            MandrillMessage.MessageContent content = new MandrillMessage.MessageContent();
            content.setContent(attachment.getBody());
            content.setName(attachment.getName());
            content.setType(attachment.getType());
            messageContentList.add(content);
        }
        return messageContentList;
    }

}
