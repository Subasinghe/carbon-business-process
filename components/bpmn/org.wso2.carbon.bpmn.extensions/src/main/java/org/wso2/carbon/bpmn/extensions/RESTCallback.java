package org.wso2.carbon.bpmn.extensions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.Constants;

import java.util.Map;

/**
 * Call back implementation for REST task.
 */
public class RESTCallback implements CarbonCallback {

    private static final Logger log = LoggerFactory.getLogger(RESTCallback.class);

    public RESTCallback() {
    }

    /**
     * Invoked when the backend response arrived.
     *
     * @param responseCmsg response carbon message.
     */
    @Override
    public void done(CarbonMessage responseCmsg) {
        if (responseCmsg != null) {
            Map<String, String> transportHeaders = responseCmsg.getHeaders();
            if (transportHeaders != null) {
                transportHeaders.put(Constants.HTTP_STATUS_CODE,
                        responseCmsg.getProperty(Constants.HTTP_STATUS_CODE).toString());
            }

            Object obj = responseCmsg.getMessageBody();

            log.error("Test for response " + obj.toString());
            //invokeResponse();
        }
    }
}


