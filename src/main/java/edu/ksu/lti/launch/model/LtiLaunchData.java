package edu.ksu.lti.launch.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to hold the POST data from a Canvas LTI launch request. Uses custom property values to map from request names.
 */
public class LtiLaunchData {

    private String ltiVersion;
    private String contextLabel;
    private String resourceLinkId;
    private String lisPersonNameFamily;
    private Integer launchPresentationWidth;
    private String launchPresentationReturnUrl;
    private String toolConsumerInfoVersion;
    private String toolConsumerInstanceContactEmail;
    private String userId;
    private String ltiMessageType;
    private String toolConsumerInfoProductFamilyCode;
    private String toolConsumerInstanceGuid;
    private String userImage;
    private String launchPresentationDocumentTarget;
    private String contextTitle;
    private String toolConsumerInstanceName;
    private String lisPersonSourcedid;
    private String lisPersonNameFull;
    private Integer launchPresentationHeight;
    private String resourceLinkTitle;
    private String contextId;
    private String roles;
    private String lisPersonContactEmailPrimary;
    private String lisPersonNameGiven;
    private String launchPresentationLocale;

    private Map<String, String> custom = new HashMap<>();


    // computed from a CSV list in the "roles" string
    private List<InstitutionRole> rolesList;

    public String getLtiVersion() {
        return ltiVersion;
    }

    public void setLtiVersion(String ltiVersion) {
        this.ltiVersion = ltiVersion;
    }

    public String getContextLabel() {
        return contextLabel;
    }

    public void setContextLabel(String contextLabel) {
        this.contextLabel = contextLabel;
    }

    public String getResourceLinkId() {
        return resourceLinkId;
    }

    public void setResourceLinkId(String resourceLinkId) {
        this.resourceLinkId = resourceLinkId;
    }

    public String getLisPersonNameFamily() {
        return lisPersonNameFamily;
    }

    public void setLisPersonNameFamily(String lisPersonNameFamily) {
        this.lisPersonNameFamily = lisPersonNameFamily;
    }

    public Integer getLaunchPresentationWidth() {
        return launchPresentationWidth;
    }

    public void setLaunchPresentationWidth(Integer launchPresentationWidth) {
        this.launchPresentationWidth = launchPresentationWidth;
    }

    public String getLaunchPresentationReturnUrl() {
        return launchPresentationReturnUrl;
    }

    public void setLaunchPresentationReturnUrl(String launchPresentationReturnUrl) {
        this.launchPresentationReturnUrl = launchPresentationReturnUrl;
    }

    public String getToolConsumerInfoVersion() {
        return toolConsumerInfoVersion;
    }

    public void setToolConsumerInfoVersion(String toolConsumerInfoVersion) {
        this.toolConsumerInfoVersion = toolConsumerInfoVersion;
    }

    public String getToolConsumerInstanceContactEmail() {
        return toolConsumerInstanceContactEmail;
    }

    public void setToolConsumerInstanceContactEmail(String toolConsumerInstanceContactEmail) {
        this.toolConsumerInstanceContactEmail = toolConsumerInstanceContactEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLtiMessageType() {
        return ltiMessageType;
    }

    public void setLtiMessageType(String ltiMessageType) {
        this.ltiMessageType = ltiMessageType;
    }

    public String getToolConsumerInfoProductFamilyCode() {
        return toolConsumerInfoProductFamilyCode;
    }

    public void setToolConsumerInfoProductFamilyCode(String toolConsumerInfoProductFamilyCode) {
        this.toolConsumerInfoProductFamilyCode = toolConsumerInfoProductFamilyCode;
    }

    public String getToolConsumerInstanceGuid() {
        return toolConsumerInstanceGuid;
    }

    public void setToolConsumerInstanceGuid(String toolConsumerInstanceGuid) {
        this.toolConsumerInstanceGuid = toolConsumerInstanceGuid;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getLaunchPresentationDocumentTarget() {
        return launchPresentationDocumentTarget;
    }

    public void setLaunchPresentationDocumentTarget(String launchPresentationDocumentTarget) {
        this.launchPresentationDocumentTarget = launchPresentationDocumentTarget;
    }

    public String getContextTitle() {
        return contextTitle;
    }

    public void setContextTitle(String contextTitle) {
        this.contextTitle = contextTitle;
    }

    public String getToolConsumerInstanceName() {
        return toolConsumerInstanceName;
    }

    public void setToolConsumerInstanceName(String toolConsumerInstanceName) {
        this.toolConsumerInstanceName = toolConsumerInstanceName;
    }

    public String getLisPersonSourcedid() {
        return lisPersonSourcedid;
    }

    public void setLisPersonSourcedid(String lisPersonSourcedid) {
        this.lisPersonSourcedid = lisPersonSourcedid;
    }

    public String getLisPersonNameFull() {
        return lisPersonNameFull;
    }

    public void setLisPersonNameFull(String lisPersonNameFull) {
        this.lisPersonNameFull = lisPersonNameFull;
    }

    public Integer getLaunchPresentationHeight() {
        return launchPresentationHeight;
    }

    public void setLaunchPresentationHeight(Integer launchPresentationHeight) {
        this.launchPresentationHeight = launchPresentationHeight;
    }

    public String getResourceLinkTitle() {
        return resourceLinkTitle;
    }

    public void setResourceLinkTitle(String resourceLinkTitle) {
        this.resourceLinkTitle = resourceLinkTitle;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getLisPersonContactEmailPrimary() {
        return lisPersonContactEmailPrimary;
    }

    public void setLisPersonContactEmailPrimary(String lisPersonContactEmailPrimary) {
        this.lisPersonContactEmailPrimary = lisPersonContactEmailPrimary;
    }

    public String getLisPersonNameGiven() {
        return lisPersonNameGiven;
    }

    public void setLisPersonNameGiven(String lisPersonNameGiven) {
        this.lisPersonNameGiven = lisPersonNameGiven;
    }

    public String getLaunchPresentationLocale() {
        return launchPresentationLocale;
    }

    public void setLaunchPresentationLocale(String launchPresentationLocale) {
        this.launchPresentationLocale = launchPresentationLocale;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }

    public List<InstitutionRole> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<InstitutionRole> rolesList) {
        this.rolesList = rolesList;
    }
}
