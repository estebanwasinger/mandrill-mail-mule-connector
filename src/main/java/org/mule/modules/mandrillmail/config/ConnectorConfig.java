package org.mule.modules.mandrillmail.config;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.view.MandrillUserInfo;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.param.ConnectionKey;

@ConnectionManagement(friendlyName = "Configuration")
public class ConnectorConfig {

    MandrillApi mandrillApi;

    /**
     *
     * @param apiKey
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String apiKey)
        throws ConnectionException {
        mandrillApi = new MandrillApi(apiKey);
        try {
            MandrillUserInfo info = mandrillApi.users().info();
        }catch (Exception e){
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN,e.getMessage(),e.getMessage());
        }
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        /*
         * CODE FOR CLOSING A CONNECTION GOES IN HERE
         */
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        return mandrillApi != null;
    }

    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }

    public MandrillApi getClient(){
        return mandrillApi;
    }
    
}