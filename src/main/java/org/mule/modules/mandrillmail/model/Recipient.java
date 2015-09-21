package org.mule.modules.mandrillmail.model;

public class Recipient {
    private String email;
    private String name;
    private Type type;

    public Recipient() {
        this.type = Type.TO;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static enum Type {
        TO,
        BCC,
        CC;

        private Type() {
        }
    }
}
