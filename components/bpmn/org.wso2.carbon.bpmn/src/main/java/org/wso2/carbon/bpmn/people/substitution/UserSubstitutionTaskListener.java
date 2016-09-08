/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
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
package org.wso2.carbon.bpmn.people.substitution;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.bpmn.core.BPMNConstants;
import org.wso2.carbon.bpmn.core.BPMNServerHolder;
import org.wso2.carbon.bpmn.core.mgt.dao.ActivitiDAO;
import org.wso2.carbon.bpmn.core.mgt.model.SubstitutesDataModel;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

public class UserSubstitutionTaskListener implements TaskListener{

    private static final Log log = LogFactory.getLog(UserSubstitutionTaskListener.class);

    ActivitiDAO dao = new ActivitiDAO();

    @Override
    public void notify(DelegateTask delegateTask) {

        if (!SubstitutionDataHolder.getInstance().isSubstitutionFeatureEnabled()) {
            return;
        }

        String assignee = delegateTask.getAssignee();

        if (assignee == null) {
            return;
        }
        String substitute = getSubstituteIfEnabled(assignee);
        if (substitute != null) {
            if (!BPMNConstants.TRANSITIVE_SUB_UNDEFINED.equals(substitute)) {
                delegateTask.setAssignee(substitute);
                if (log.isDebugEnabled()) {
                    log.debug("User: " + assignee + "is substituted by : " + substitute + "for the task" + delegateTask.getName());
                }
            } else {
                if (delegateTask.getOwner() != null) {
                    delegateTask.setAssignee(delegateTask.getOwner());
                    if (log.isDebugEnabled()) {
                        log.debug("User: " + assignee + "is substituted to task owner : " + delegateTask.getOwner() + "for the task" + delegateTask.getName());
                    }
                } else {
                    delegateTask.addCandidateUser(assignee);
                    BPMNServerHolder.getInstance().getEngine().getTaskService().deleteUserIdentityLink(delegateTask.getId(), assignee, IdentityLinkType.ASSIGNEE);
                    if (log.isDebugEnabled()) {
                        log.debug("Could not find an substitution assignee for the task" + delegateTask.getName() + ". Task status changed to unclaimed");
                    }
                }
            }
        }
   }

    /**
     * Return the active Substitute or return BPMNConstants.TRANSITIVE_SUB_UNDEFINED
     * @param assignee
     */
    private String getSubstituteIfEnabled (String assignee) {

        //retrieve Substitute info
        SubstitutesDataModel substitutesDataModel = getImmediateSubstitute(MultitenantUtils.getTenantAwareUsername(assignee));
        if(substitutesDataModel != null && isSubstitutionActive(substitutesDataModel)) {
            if (!SubstitutionDataHolder.getInstance().isTransitivityEnabled() || substitutesDataModel.getTransitiveSub() == null || BPMNConstants.TRANSITIVE_SUB_NOT_APPLICABLE.equals(substitutesDataModel.getTransitiveSub())) {
                return substitutesDataModel.getSubstitute();
            } else {
                return substitutesDataModel.getTransitiveSub();
            }
        } else {
            return null;
        }


    }

    private SubstitutesDataModel getImmediateSubstitute(String assignee){
        return dao.selectSubstituteInfo(assignee, PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
    }

    /**
     * Check if an active substitution available for given substitute info
     * @param substitutesDataModel
     * @return true if substitution active
     */
    private boolean isSubstitutionActive(SubstitutesDataModel substitutesDataModel) {
        long startDate = substitutesDataModel.getSubstitutionStart().getTime();
        long endDate = substitutesDataModel.getSubstitutionEnd().getTime();
        long currentTime = System.currentTimeMillis();

        if (substitutesDataModel.isEnabled() && (startDate < currentTime) && (endDate > currentTime)) {
            return true;
        }
        return false;
    }
}
