package org.mule.modules.mandrillmail.config;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.components.Configuration;

@Configuration(friendlyName = "API Token Configuration")
public class ConnectorConfig {

    MandrillApi mandrillApi;

    @Configurable
    String apiKey;

    @TestConnectivity
    public void connect() throws ConnectionException {
        MandrillApi mandrillApi = new MandrillApi(apiKey);
        try {
            mandrillApi.users().ping();
        } catch (Exception e) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, e.getMessage(), e.getMessage());
        }
    }

    public MandrillApi getClient() {
        return mandrillApi;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        mandrillApi = new MandrillApi(apiKey);
        this.apiKey = apiKey;
    }

}