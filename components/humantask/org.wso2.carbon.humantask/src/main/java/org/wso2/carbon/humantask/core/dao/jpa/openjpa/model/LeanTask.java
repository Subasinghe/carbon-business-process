/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


package org.wso2.carbon.humantask.core.dao.jpa.openjpa.model;


import org.w3c.dom.Element;
import org.wso2.carbon.humantask.core.dao.LeanTaskDAO;
import org.wso2.carbon.humantask.core.utils.DOMUtils;
import org.xml.sax.SAXException;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "HT_LEANTASK")

public class LeanTask implements LeanTaskDAO{

   @Column(name = "LEANTASK_TENANTID", nullable = false)
    private int tenantId;

    @Column(name = "LEANTASK_NAME", nullable = false)
    private String name;

    @Id
    @Column(name = "LEANTASK_VERSION", nullable = false)
    private long version;

    @Column(name = "LEANTASK_DEF", nullable = false, columnDefinition = "CLOB")
    @Lob
    private String leanTaskDef;



    public void setTenantID(int tenantId) {
        this.tenantId=tenantId;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setVersion(long version) {
        this.version=version;
    }

    public void setLeanTask(Element leanTask) {
        this.leanTaskDef= DOMUtils.domToString(leanTask);
    }

    public int getTenantID() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public long getVersion() {
        return version;
    }

    public Element getLeanTask() throws IOException, SAXException {
        return DOMUtils.stringToDOM(leanTaskDef);
    }
}