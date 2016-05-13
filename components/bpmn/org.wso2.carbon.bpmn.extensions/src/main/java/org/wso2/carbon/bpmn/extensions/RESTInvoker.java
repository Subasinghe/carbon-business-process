/*
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.bpmn.extensions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.Constants;
import org.wso2.carbon.messaging.DefaultCarbonMessage;
import org.wso2.carbon.messaging.MessageProcessorException;
import org.wso2.carbon.transport.http.netty.config.SenderConfiguration;
import org.wso2.carbon.transport.http.netty.sender.NettySender;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for invoking HTTP endpoints.
 */
public class RESTInvoker {

    private static final Logger log = LoggerFactory.getLogger(RESTInvoker.class);

    //private final CloseableHttpClient client;

    /*public RESTInvoker() throws IOException, XMLStreamException {

        int maxTotal = 100;
        int maxTotalPerRoute = 100;

        String activitiConfigPath = org.wso2.carbon.kernel.utils.Utils.getCarbonConfigHome()
                .resolve(BPMNConstants.ACTIVITI_CONFIGURATION_FILE_NAME).toString();
        File configFile = new File(activitiConfigPath);
        String configContent = FileUtils.readFileToString(configFile);
        OMElement configElement = AXIOMUtil.stringToOM(configContent);
        Iterator beans = configElement.getChildrenWithName(
                new QName("http://www.springframework.org/schema/beans", "bean"));
        while (beans.hasNext()) {
            OMElement bean = (OMElement) beans.next();
            String beanId = bean.getAttributeValue(new QName(null, "id"));
            if (beanId.equals(BPMNConstants.REST_CLIENT_CONFIG_ELEMENT)) {
                Iterator beanProps = bean.getChildrenWithName(
                        new QName("http://www.springframework.org/schema/beans", "property"));
                while (beanProps.hasNext()) {
                    OMElement beanProp = (OMElement) beanProps.next();
                    if (beanProp.getAttributeValue(new QName(null, "name"))
                            .equals(BPMNConstants.REST_CLIENT_MAX_TOTAL_CONNECTIONS)) {
                        String value = beanProp.getAttributeValue(new QName(null, "value"));
                        maxTotal = Integer.parseInt(value);
                    } else if (beanProp.getAttributeValue(new QName(null, "name"))
                            .equals(BPMNConstants.REST_CLIENT_MAX_CONNECTIONS_PER_ROUTE)) {
                        String value = beanProp.getAttributeValue(new QName(null, "value"));
                        maxTotalPerRoute = Integer.parseInt(value);
                    }
                }
            }
        }

        /*PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(maxTotalPerRoute);
        cm.setMaxTotal(maxTotal);
        client = HttpClients.custom().setConnectionManager(cm).build();

        if (log.isDebugEnabled()) {
            log.debug("BPMN REST client initialized with maxTotalConnection = " + maxTotal +
                    " and maxConnectionsPerRoute = " + maxTotalPerRoute);
        }

    }*/

    public RESTInvoker() {

    }

    /*public String invokeGET(URI uri, String headerList[], String username, String password)
            throws Exception {

        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        BufferedReader rd = null;
        String output = "";
        try {
            httpGet = new HttpGet(uri);
            if (username != null && password != null) {
                String combinedCredentials = username + ":" + password;
                byte[] encodedCredentials = Base64.encodeBase64(combinedCredentials.getBytes(Charset.defaultCharset()));
                httpGet.addHeader("Authorization", "Basic " + Arrays.toString(encodedCredentials));
            }
            if (headerList != null) {
                for (String header : headerList) {
                    String pair[] = header.split(":");
                    if (pair.length == 1) {
                        httpGet.addHeader(pair[0], "");
                    } else {
                        httpGet.addHeader(pair[0], pair[1]);
                    }
                }
            }
            response = client.execute(httpGet);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.defaultCharset()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            output = result.toString();
            if (log.isTraceEnabled()) {
                log.trace("Invoked GET " + uri.toString() + " - Response message: " + output);
            }
            EntityUtils.consume(response.getEntity());

        } finally {
            if (rd != null) {
                rd.close();
            }

            if (response != null) {
                response.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return output;
    }

    public String invokePOST(URI uri, String headerList[], String username, String password,
                             String payload) throws Exception {

        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        BufferedReader rd = null;
        String output = "";
        try {
            httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(payload));
            if (username != null && password != null) {
                String combinedCredentials = username + ":" + password;
                String encodedCredentials =
                        new String(Base64.encodeBase64(combinedCredentials.getBytes(Charset.defaultCharset())),
                                Charset.defaultCharset());
                httpPost.addHeader("Authorization", "Basic " + encodedCredentials);
            }
            if (headerList != null) {
                for (String header : headerList) {
                    String pair[] = header.split(":");
                    if (pair.length == 1) {
                        httpPost.addHeader(pair[0], "");
                    } else {
                        httpPost.addHeader(pair[0], pair[1]);
                    }
                }
            }
            response = client.execute(httpPost);

            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.defaultCharset()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            output = result.toString();
            if (log.isTraceEnabled()) {
                log.trace("Invoked POST " + uri.toString() + " - Input payload: " + payload +
                        " - Response message: " + output);
            }
            EntityUtils.consume(response.getEntity());

        } finally {
            if (rd != null) {
                rd.close();
            }

            if (response != null) {
                response.close();
            }

            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return output;
    }*/

    public String invokeRequest(URI uri, String headerList[], String username, String password,
                                String inputContent) {

        SenderConfiguration senderConfiguration = new SenderConfiguration("netty-gw");
        NettySender nettySender = new NettySender(senderConfiguration);

        DefaultCarbonMessage carbonMessage = new DefaultCarbonMessage();
        carbonMessage.setProperty(Constants.HOST, uri.getHost());
        carbonMessage.setProperty(Constants.PORT, uri.getPort());
        carbonMessage.setProperty(Constants.TO, uri.getPath());
        carbonMessage.setProperty(org.wso2.carbon.transport.http.netty.common.Constants.
                IS_DISRUPTOR_ENABLE, "true");

        if (inputContent != null) {
            String payload = inputContent;
            carbonMessage.setStringMessageBody(payload);
        }

        //byte[] errorMessageBytes = payload.getBytes(Charset.defaultCharset());

        Map<String, String> transportHeaders = new HashMap();
        transportHeaders.put(Constants.HTTP_CONNECTION, Constants.KEEP_ALIVE);
        transportHeaders.put(Constants.HTTP_CONTENT_TYPE, Constants.TEXT_XML);
        //transportHeaders.put(Constants.HTTP_CONTENT_LENGTH, (String.valueOf(errorMessageBytes.length)));
        transportHeaders.put(Constants.TO, uri.getPath());
        carbonMessage.setHeaders(transportHeaders);

        CarbonCallback callback = new RESTCallback();

        try {
            nettySender.send(carbonMessage, callback);
        } catch (MessageProcessorException e) {
            log.error(e.toString());
        }

        return null;
    }

}

