
/*
 * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.bpel.core.ode.integration.jmx;

import org.apache.ode.bpel.evt.ActivityFailureEvent;
import org.apache.ode.bpel.evt.BpelEvent;
import org.apache.ode.bpel.evt.ScopeFaultEvent;
import org.apache.ode.bpel.iapi.BpelEventListener;

import java.util.Properties;

/**
 * JMX BPEL Event Listener.
 */
public class JmxBpelEventListener implements BpelEventListener {

    @Override
    public void onEvent(BpelEvent bpelEvent) {

        if (bpelEvent instanceof ActivityFailureEvent) {
            String message = ((ActivityFailureEvent) bpelEvent).getProcessId() + "  has failed in  " + (
                    (ActivityFailureEvent) bpelEvent).getActivityName() + " Activity";
            InstanceStatusMonitor statusMonitor = InstanceStatusMonitor.getInstanceStatusMonitor();
            statusMonitor.setLastFailedProcessInfo(message);
        } else if (bpelEvent instanceof ScopeFaultEvent) {
            String message = ((ScopeFaultEvent) bpelEvent).getProcessId() + " scope id " + ((ScopeFaultEvent)
                    bpelEvent).getScopeId() + " has failed " + ((ScopeFaultEvent) bpelEvent).getFaultType();
            InstanceStatusMonitor statusMonitor = InstanceStatusMonitor.getInstanceStatusMonitor();
            statusMonitor.setLastFailedProcessInfo(message);
        }
    }

    @Override
    public void startup(Properties properties) {
    }

    @Override
    public void shutdown() {
    }
}
