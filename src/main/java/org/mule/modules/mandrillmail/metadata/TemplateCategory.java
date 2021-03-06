
package org.mule.modules.mandrillmail.metadata;

import com.microtripit.mandrillapp.lutung.view.MandrillTemplate;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.*;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.mandrillmail.MandrillMailConnector;
import org.mule.modules.mandrillmail.model.MandrillAttachment;
import org.mule.modules.mandrillmail.model.Recipient;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaDataCategory
public class TemplateCategory {

    @Inject
    private MandrillMailConnector connector;

    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<>();
        MandrillTemplate[] templates = connector.getConfig().getClient().templates().list();
        for (MandrillTemplate template : Arrays.asList(templates)) {
            keys.add(new DefaultMetaDataKey(template.getName(), template.getName()));
        }
        return keys;
    }

    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();

        MandrillTemplate template = connector.getConfig().getClient().templates().info(key.getId());
        DynamicObjectBuilder<?> dynamicObject = builder.createDynamicObject(key.getDisplayName());

        for (String holder : getHolders(template.getCode())){
            dynamicObject.addSimpleField(holder, DataType.STRING);
        }

        DefaultMetaData defaultMetaData = new DefaultMetaData(builder.build());
        defaultMetaData.addProperty(MetaDataPropertyScope.FLOW, "attachments", new DefaultListMetaDataModel(new DefaultPojoMetaDataModel(MandrillAttachment.class)));
        defaultMetaData.addProperty(MetaDataPropertyScope.FLOW, "recipients", new DefaultListMetaDataModel(new DefaultPojoMetaDataModel(Recipient.class)));
        defaultMetaData.addProperty(MetaDataPropertyScope.FLOW, "tags", new DefaultListMetaDataModel(new DefaultSimpleMetaDataModel(DataType.STRING)));
        return defaultMetaData;
    }

    private Set<String> getHolders(String template){
        Set<String> holders = new HashSet<>();
        Pattern pattern = Pattern.compile("\\*\\|(\\S+)\\|\\*");
        Matcher matcher = pattern.matcher(template);
        while(matcher.find()){
            holders.add(matcher.group(1));
        }
        return holders;
    }

    public MandrillMailConnector getConnector() {
        return connector;
    }

    public void setConnector(MandrillMailConnector connector) {
        this.connector = connector;
    }
}