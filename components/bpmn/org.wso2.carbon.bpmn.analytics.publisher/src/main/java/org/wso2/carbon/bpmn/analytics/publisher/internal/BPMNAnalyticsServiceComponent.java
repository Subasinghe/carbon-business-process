/**
 * Copyright (c) 2005-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.bpmn.analytics.publisher.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.bpmn.analytics.publisher.BPMNDataPublisher;
import org.wso2.carbon.bpmn.analytics.publisher.BPMNDataPublisherException;
import org.wso2.carbon.bpmn.analytics.publisher.BPSAnalyticsServer;
import org.wso2.carbon.bpmn.analytics.publisher.BPSAnalyticsService;
import org.wso2.carbon.bpmn.core.BPMNEngineService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="org.wso2.carbon.bpmn.analytics.publisher.internal.BPMNAnalyticsServiceComponent"
 * immediate="true"
 * @scr.reference name="realm.service" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="bpmn.service" interface="org.wso2.carbon.bpmn.core.BPMNEngineService"
 * cardinality="1..1" policy="dynamic" bind="setBPMNEngineService" unbind="unsetBPMNEngineService"
 */
public class BPMNAnalyticsServiceComponent {
    private static final Log log = LogFactory.getLog(BPMNAnalyticsServiceComponent.class);

    /**
     * Activate BPMN analytics component.
     *
     * @param ctxt ComponentContext
     */
    protected void activate(ComponentContext ctxt) {

        try {
            BPMNAnalyticsHolder bpmnAnalyticsHolder = BPMNAnalyticsHolder.getInstance();
            initAnalyticsServer(bpmnAnalyticsHolder);
            BPSAnalyticsService bpsAnalyticsService = new BPSAnalyticsService();
            bpsAnalyticsService.setBPSAnalyticsServer(bpmnAnalyticsHolder.getBPSAnalyticsServer());
            // Register BPS analytics Service OSGI Service
            ctxt.getBundleContext().registerService(BPSAnalyticsService.class.getName(),
                    bpsAnalyticsService, null);
            BPMNDataPublisher BPMNDataPublisher = new BPMNDataPublisher();
            BPMNAnalyticsHolder.getInstance().setBpmnDataPublisher(BPMNDataPublisher);
            BPMNDataPublisher.configure();
            log.info("Initializing the BPMN Analytics Service component...");
        } catch (Throwable e) {
            log.error("Failed to initialize the BPMN Analytics Service component.", e);
        }
    }

    public void setBPMNEngineService(BPMNEngineService bpmnEngineService) {
        BPMNAnalyticsHolder.getInstance().setBpmnEngineService(bpmnEngineService);
    }

    public void unsetBPMNEngineService(BPMNEngineService bpmnEngineService) {
        BPMNAnalyticsHolder.getInstance().setBpmnEngineService(null);
    }

    /**
     * Set RegistryService instance when bundle get bind to OSGI runtime.
     *
     * @param registryService
     */
    public void setRegistryService(RegistryService registryService) {
        BPMNAnalyticsHolder.getInstance().setRegistryService(registryService);
    }

    /**
     * Unset RegistryService instance when bundle get unbind from OSGI runtime.
     *
     * @param registryService
     */
    public void unsetRegistryService(RegistryService registryService) {
        BPMNAnalyticsHolder.getInstance().setRegistryService(null);
    }

    /**
     * Set RealmService instance when bundle get bind to OSGI runtime.
     *
     * @param realmService
     */
    public void setRealmService(RealmService realmService) {
        BPMNAnalyticsHolder.getInstance().setRealmService(realmService);
    }

    /**
     * Unset RealmService instance when bundle get unbind from OSGI runtime.
     *
     * @param realmService
     */
    public void unsetRealmService(RealmService realmService) {
        BPMNAnalyticsHolder.getInstance().setRealmService(null);
    }

    // Initializing the analytics server .
    private void initAnalyticsServer(BPMNAnalyticsHolder bpmnAnalyticsHolder)
            throws BPMNDataPublisherException {
        bpmnAnalyticsHolder.setBPSAnalyticsServer(new BPSAnalyticsServer());
        bpmnAnalyticsHolder.getBPSAnalyticsServer().init();
    }
}
