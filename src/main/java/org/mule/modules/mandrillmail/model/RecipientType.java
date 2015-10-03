package org.mule.modules.mandrillmail.model;

public enum RecipientType {

    TO("TO"),CC("CC"),BCC("BCC");

    private String value;

    public String toString(){
        return value;
    }

    RecipientType(String value){
        this.value = value;
    }
}
