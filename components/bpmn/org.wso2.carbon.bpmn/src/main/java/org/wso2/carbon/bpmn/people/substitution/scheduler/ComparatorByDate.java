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
package org.wso2.carbon.bpmn.people.substitution.scheduler;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorByDate implements Comparator<ScheduledTask>, Serializable {

    public int compare(ScheduledTask o1, ScheduledTask o2) {
        long diff = o1.schedDate - o2.schedDate;
        if (diff < 0) return -1;
        if (diff > 0) return 1;
        return 0;
    }

}
