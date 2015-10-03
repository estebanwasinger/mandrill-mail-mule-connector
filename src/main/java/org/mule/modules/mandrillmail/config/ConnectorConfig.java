package org.mule.modules.mandrillmail.config;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.components.Configuration;

import java.io.IOException;

@Configuration(friendlyName = "API Token Configuration")
public class ConnectorConfig {

    private String NULL_API_KEY_ERROR_MSG = "API Key is null, please fill it with a valid value";

    private MandrillApi mandrillApi;

    @Configurable
    String apiKey;

    @TestConnectivity
    public void connect() throws ConnectionException {
        if (apiKey == null) {
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, NULL_API_KEY_ERROR_MSG, NULL_API_KEY_ERROR_MSG);
        }

        MandrillApi mandrillApi = new MandrillApi(apiKey);

        try {
            mandrillApi.users().ping();
        } catch (MandrillApiError e) {
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, e.getMandrillErrorMessage(), e.getMandrillErrorMessage());
        } catch (IOException e) {
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