/**
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.bpmn.core.integration;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.context.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.bpmn.core.BPMNServerHolder;
import org.wso2.carbon.bpmn.people.substitution.scheduler.SubstitutionScheduler;
import org.wso2.carbon.utils.WaitBeforeShutdownObserver;

public class BPMNEngineWaitBeforeShutdownObserver implements WaitBeforeShutdownObserver{

    private static Log log = LogFactory.getLog(BPMNEngineWaitBeforeShutdownObserver.class);
    private boolean status = false;

    @Override
    public void startingShutdown() {
        log.info("Shutting down activiti process engine");
        SubstitutionScheduler scheduler = BPMNServerHolder.getInstance().getSubstitutionScheduler();
        if (scheduler != null) {
            log.info("Shutting down the BPMN Substitution Scheduler");
            scheduler.stop();
        }
        log.info("Beginning to close down the command context");
        if(Context.getCommandContext() != null){
            Context.getCommandContext().close();
            log.info("Completed the closing of command context");
        }
        ProcessEngines.destroy();
        status = true;
    }

    @Override
    public boolean isTaskComplete() {
        return status;
    }
}
