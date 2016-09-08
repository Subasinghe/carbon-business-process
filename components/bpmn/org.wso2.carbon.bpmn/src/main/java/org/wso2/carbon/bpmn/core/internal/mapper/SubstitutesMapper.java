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
package org.wso2.carbon.bpmn.core.internal.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.wso2.carbon.bpmn.core.BPMNConstants;
import org.wso2.carbon.bpmn.core.mgt.model.PaginatedSubstitutesDataModel;
import org.wso2.carbon.bpmn.core.mgt.model.SubstitutesDataModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SubstitutesMapper {

    final String INSERT_SUBSTITUTE = "INSERT INTO " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            "  (USERNAME, SUBSTITUTE, SUBSTITUTION_START, SUBSTITUTION_END, ENABLED, TRANSITIVE_SUBSTITUTE, "
            + "CREATED, UPDATED, TENANT_ID, TASK_LIST) VALUES (#{user}, #{substitute}, #{substitutionStart}, "
            + "#{substitutionEnd}, #{enabled}, #{transitiveSub, jdbcType=VARCHAR}, #{created}, "
            + "#{updated, jdbcType=TIMESTAMP}, #{tenantId}, #{taskList, jdbcType=VARCHAR})";
    final String SELECT_ALL_BY_USER = "SELECT * FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            " WHERE USERNAME = #{user} AND TENANT_ID = #{tenantId}";
    final String UPDATE_ENABLED = "UPDATE " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            "  SET ENABLED = #{enabled} WHERE USERNAME = #{user} AND TENANT_ID=#{tenantId}";
    final String UPDATE_INFO = "UPDATE " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE
            + " SET SUBSTITUTE = #{substitute}, SUBSTITUTION_START = #{substitutionStart}, "
            + "SUBSTITUTION_END = #{substitutionEnd}, ENABLED = #{enabled}, "
            + "TRANSITIVE_SUBSTITUTE = #{transitiveSub, jdbcType=VARCHAR}, UPDATED = #{updated, jdbcType=TIMESTAMP}, "
            + "TASK_LIST = #{taskList, jdbcType=VARCHAR} WHERE USERNAME = #{user} AND TENANT_ID=#{tenantId}";
    final String COUNT_USER_AS_SUBSTITUTE = "SELECT COUNT(*) FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE
            + " WHERE SUBSTITUTE = #{substitute} AND TENANT_ID = #{tenantId}";
    final String SELECT_ALL_SUBSTITUTES = "SELECT USERNAME, SUBSTITUTE, SUBSTITUTION_START, SUBSTITUTION_END, ENABLED, TASK_LIST from "
            + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE + " WHERE TENANT_ID = #{tenantId}";
    final String SELECT_ACTIVE_SUBSTITUTES = "SELECT USERNAME, SUBSTITUTE, SUBSTITUTION_START, SUBSTITUTION_END, ENABLED, TASK_LIST, TENANT_ID from "
            + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE + " WHERE TENANT_ID = #{tenantId} AND ENABLED = #{enabled}  AND #{currentTime} > SUBSTITUTION_START AND #{currentTime} < SUBSTITUTION_END";
    final String UPDATE_TRANSITIVE_SUB = "UPDATE " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            "  SET TRANSITIVE_SUBSTITUTE = #{transitiveSub}, UPDATED = #{updated} WHERE USERNAME = #{user} AND TENANT_ID=#{tenantId}";
    final String DELETE_SUBSTITUTE = "DELETE FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE
            + " WHERE USERNAME = #{user} AND TENANT_ID=#{tenantId}";
    final String UPDATE_SUBSTITUTE_USER = "UPDATE " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            "  SET SUBSTITUTE = #{substitute}, UPDATED = #{updated} WHERE USERNAME = #{user} AND TENANT_ID=#{tenantId}";
    final String QUERY_SUBSTITUTES = "<script> SELECT * FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            " WHERE " +
            " <if test=\"user != null\"> USERNAME = #{user} AND </if> " +
            " <if test=\"substitute != null\"> SUBSTITUTE = #{substitute} AND </if> " +
            " <if test=\"enabled != null\"> ENABLED = #{enabled} AND </if> " +
            " TENANT_ID = #{tenantId}" +
            " ORDER BY ${sort} ${order} </script>";
    final String QUERY_SUBSTITUTES_COUNT = "<script> SELECT count(*) FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            " WHERE " +
            " <if test=\"user != null\"> USERNAME = #{user} AND </if> " +
            " <if test=\"substitute != null\"> SUBSTITUTE = #{substitute} AND </if> " +
            " <if test=\"enabled != null\"> ENABLED = #{enabled} AND </if> " +
            " TENANT_ID = #{tenantId}" +
            " </script>";
    final String QUERY_SUBSTITUTES_NO_ENABLED = "<script> SELECT * FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            " WHERE " +
            " <if test=\"user != null\"> USERNAME = #{user} AND </if> " +
            " <if test=\"substitute != null\"> SUBSTITUTE = #{substitute} AND </if> " +
            " TENANT_ID = #{tenantId}" +
            " ORDER BY ${sort} ${order} </script>";
    final String QUERY_SUBSTITUTES_NO_ENABLED_COUNT = "<script> SELECT count(*) FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE +
            " WHERE " +
            " <if test=\"user != null\"> USERNAME = #{user} AND </if> " +
            " <if test=\"substitute != null\"> SUBSTITUTE = #{substitute} AND </if> " +
            " TENANT_ID = #{tenantId}" +
            " </script>";
    final String SELECT_DISTINCT_TENANT_LIST = "SELECT DISTINCT TENANT_ID FROM " + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE;
    final String SELECT_ENABLED_EXPIRED_SUBSTITUTES = "SELECT USERNAME, SUBSTITUTE, SUBSTITUTION_START, SUBSTITUTION_END, ENABLED from "
            + BPMNConstants.ACT_BPS_SUBSTITUTES_TABLE + " WHERE TENANT_ID = #{tenantId} AND ENABLED = #{enabled}  AND #{currentTime} > SUBSTITUTION_END";

    /**
     * Insert new row in ACT_BPS_SUBSTITUTES table
     * @param substitutesDataModel
     * @return Inserted row count
     */
    @Insert(INSERT_SUBSTITUTE)
    int insertSubstitute(SubstitutesDataModel substitutesDataModel);

    /**
     * Select the SubstitutesDataModel from the given tenantId and username
     * @param user assignee that required the substitution info
     * @return the substitution info for the given user
     */
    @Select(SELECT_ALL_BY_USER)
    @Results(value = {
            @Result(property = "user", column = "USERNAME"),
            @Result(property = "substitute", column = "SUBSTITUTE"),
            @Result(property = "substitutionStart", column = "SUBSTITUTION_START"),
            @Result(property = "substitutionEnd", column = "SUBSTITUTION_END"),
            @Result(property = "enabled", column = "ENABLED"),
            @Result(property = "tenantId", column = "TENANT_ID"),
            @Result(property = "transitiveSub", column = "TRANSITIVE_SUBSTITUTE"),
            @Result(property = "created", column = "CREATED"),
            @Result(property = "updated", column = "UPDATED"),
            @Result(property = "taskList", column = "TASK_LIST")
    })
    SubstitutesDataModel selectSubstitute( @Param("user") String user, @Param("tenantId") int tenantId);

    /**
     * Update the ACT_BPS_SUBSTITUTES table.
     * @param substitutesDataModel
     * @return number of rows updated
     */
    @Update(UPDATE_INFO)
    int updateSubstitute(SubstitutesDataModel substitutesDataModel);

    /**
     * Return the row count where the given user acts as the substitute
     * @param substitute
     * @param tenantId
     * @return
     */
    @Select(COUNT_USER_AS_SUBSTITUTE)
    int countUserAsSubstitute( @Param("substitute") String substitute, @Param("tenantId") int tenantId);

    /**
     * Select all Substitute info for given tenant
     * @param tenantId
     * @return Map with key USER and value SubstitutesDataModel
     */
    @Select(SELECT_ALL_SUBSTITUTES)
    @MapKey("user")
    @Results(value = {
            @Result(property = "user", column = "USERNAME"),
            @Result(property = "substitute", column = "SUBSTITUTE"),
            @Result(property = "substitutionStart", column = "SUBSTITUTION_START"),
            @Result(property = "substitutionEnd", column = "SUBSTITUTION_END"),
            @Result(property = "enabled", column = "ENABLED"),
            @Result(property = "taskList", column = "TASK_LIST")
    })
    Map<String, SubstitutesDataModel> selectAllSubstituteInfo(@Param("tenantId") int tenantId);

    /**
     * Select active substitutes info for given tenant
     * @param tenantId
     * @return Map with key USER and value SubstitutesDataModel
     */
    @Select(SELECT_ACTIVE_SUBSTITUTES)
    @MapKey("user")
    @Results(value = {
            @Result(property = "user", column = "USERNAME"),
            @Result(property = "substitute", column = "SUBSTITUTE"),
            @Result(property = "substitutionStart", column = "SUBSTITUTION_START"),
            @Result(property = "substitutionEnd", column = "SUBSTITUTION_END"),
            @Result(property = "enabled", column = "ENABLED"),
            @Result(property = "enabled", column = "ENABLED"),
            @Result(property = "taskList", column = "TASK_LIST"),
            @Result(property = "tenantId", column = "TENANT_ID")
    })
    Map<String, SubstitutesDataModel> selectActiveSubstitutesInfo(@Param("tenantId") int tenantId, @Param("currentTime") Date currentTime, @Param("enabled") boolean enabled);

    /**
     * Update Transitive substitute for the given user and tenant
     * @param user
     * @param tenantId
     * @param transitiveSub
     * @param date
     * @return
     */
    @Update(UPDATE_TRANSITIVE_SUB)
    int updateTransitiveSub(@Param("user") String user, @Param("tenantId") int tenantId, @Param("transitiveSub") String transitiveSub, @Param("updated") Date date);

    /**
     * Remove substitute info for given user
     * @param user
     * @param tenantId
     * @return
     */
    @Delete(DELETE_SUBSTITUTE)
    int removeSubstitute(@Param("user") String user, @Param("tenantId") int tenantId);

    /**
     * Update the substitute of the given user
     * @param assignee
     * @param substitute
     * @param tenantId
     * @param updated
     * @return updated row count
     */
    @Update(UPDATE_SUBSTITUTE_USER)
    Integer updateSubstituteUser(String assignee, String substitute, int tenantId, Date updated);

    /**
     * Return the list of substitute info based on query parameters.
     * @param substitutesDataModel
     * @return List<SubstitutesDataModel> Result set of substitute info
     */
    @Select(QUERY_SUBSTITUTES)
    @Results(value = {
            @Result(property = "user", column = "USERNAME"),
            @Result(property = "substitute", column = "SUBSTITUTE"),
            @Result(property = "substitutionStart", column = "SUBSTITUTION_START"),
            @Result(property = "substitutionEnd", column = "SUBSTITUTION_END"),
            @Result(property = "enabled", column = "ENABLED"),
            @Result(property = "taskList", column = "TASK_LIST")
    })
    List<SubstitutesDataModel> querySubstitutes(RowBounds rowBounds, PaginatedSubstitutesDataModel substitutesDataModel);

    /**
     * Return the list of substitute info based on query parameters except enabled property.
     * @param substitutesDataModel
     * @return List<SubstitutesDataModel> Result set of substitute info
     */
    @Select(QUERY_SUBSTITUTES_NO_ENABLED)
    @Results(value = {
            @Result(property = "user", column = "USERNAME"),
            @Result(property = "substitute", column = "SUBSTITUTE"),
            @Result(property = "substitutionStart", column = "SUBSTITUTION_START"),
            @Result(property = "substitutionEnd", column = "SUBSTITUTION_END"),
            @Result(property = "enabled", column = "ENABLED"),
            @Result(property = "taskList", column = "TASK_LIST")
    })
    List<SubstitutesDataModel> querySubstitutesWithoutEnabled(RowBounds rowBounds, PaginatedSubstitutesDataModel substitutesDataModel);

    /**
     * Return a list of distinct tenant IDs in the substitute tables
     * @return List<Integer> List of Distinct tenant IDs
     */
    @Select(SELECT_DISTINCT_TENANT_LIST)
    List<Integer> getDistinctTenantList();

    /**
     * Enable/Disable a substitution record
     * @param enabled
     * @param user
     * @param tenantId
     * @return Updated row count
     */
    @Update(UPDATE_ENABLED)
    int enableSubstitution(@Param("enabled") boolean enabled, @Param("user") String user, @Param("tenantId") int tenantId);

    /**
     * Select enabled but date expired substitute info for given tenant
     * @param tenantId
     * @return Map with key USER and value SubstitutesDataModel
     */
    @Select(SELECT_ENABLED_EXPIRED_SUBSTITUTES)
    @MapKey("user")
    @Results(value = {
            @Result(property = "user", column = "USERNAME"),
            @Result(property = "substitute", column = "SUBSTITUTE"),
            @Result(property = "substitutionStart", column = "SUBSTITUTION_START"),
            @Result(property = "substitutionEnd", column = "SUBSTITUTION_END"),
            @Result(property = "enabled", column = "ENABLED")
    })
    Map<String, SubstitutesDataModel> selectEnabledExpiredRecords(@Param("tenantId") int tenantId, @Param("currentTime") Date currentTime, @Param("enabled") boolean enabled);

    /**
     * Return the count of substitute info based on query parameters.
     * @param substitutesDataModel
     * @return int Result set count
     */
    @Select(QUERY_SUBSTITUTES_COUNT)
    int selectQuerySubstitutesCount(PaginatedSubstitutesDataModel substitutesDataModel);

    /**
     * Return the count of substitute info based on query parameters except enabled property.
     * @param substitutesDataModel
     * @return int Result set count
     */
    @Select(QUERY_SUBSTITUTES_NO_ENABLED_COUNT)
    int selectQuerySubstitutesCountWithoutEnabled(PaginatedSubstitutesDataModel substitutesDataModel);

}