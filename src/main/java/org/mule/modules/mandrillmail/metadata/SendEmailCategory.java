
package org.mule.modules.mandrillmail.metadata;

import com.microtripit.mandrillapp.lutung.view.MandrillTemplate;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.*;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.mandrillmail.MandrillMailConnector;
import org.mule.modules.mandrillmail.model.MandrillAttachment;
import org.mule.modules.mandrillmail.model.Recipient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@MetaDataCategory
public class SendEmailCategory {

    @Inject
    private MandrillMailConnector connector;

    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<>();
        MandrillTemplate[] templates = connector.getConfig().getClient().templates().list();
        keys.add(new DefaultMetaDataKey("Default","Default"));
        return keys;
    }

    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultSimpleMetaDataModel builder = new DefaultSimpleMetaDataModel(DataType.STRING);

        DefaultMetaData defaultMetaData = new DefaultMetaData(builder);
        defaultMetaData.addProperty(MetaDataPropertyScope.FLOW, "attachments", new DefaultListMetaDataModel(new DefaultPojoMetaDataModel(MandrillAttachment.class)));
        defaultMetaData.addProperty(MetaDataPropertyScope.FLOW, "recipients", new DefaultListMetaDataModel(new DefaultPojoMetaDataModel(Recipient.class)));
        defaultMetaData.addProperty(MetaDataPropertyScope.FLOW, "tags", new DefaultListMetaDataModel(new DefaultSimpleMetaDataModel(DataType.STRING)));
        return defaultMetaData;
    }

    public MandrillMailConnector getConnector() {
        return connector;
    }

    public void setConnector(MandrillMailConnector connector) {
        this.connector = connector;
    }
}