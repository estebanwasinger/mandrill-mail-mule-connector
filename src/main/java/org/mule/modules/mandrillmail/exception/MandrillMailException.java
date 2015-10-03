package org.mule.modules.mandrillmail.exception;

/**
 * Created by estebanwasinger on 9/30/15.
 */
public class MandrillMailException extends RuntimeException {

    public MandrillMailException(String msg){
        super(msg);
    }

    public MandrillMailException(String msg, Throwable cause){
        super(msg,cause);
    }
}
