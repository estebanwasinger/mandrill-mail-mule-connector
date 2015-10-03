package org.mule.modules.mandrillmail.model;

/**
 * Created by estebanwasinger on 9/29/15.
 */
public class MandrillAttachment {

    private String name;
    private String body;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }
}
