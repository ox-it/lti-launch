package edu.ksu.lti.launch.model;

import java.util.HashMap;
import java.util.Map;

// List of possible roles is from section A.2.2 at http://www.imsglobal.org/lti/blti/bltiv1p0/ltiBLTIimgv1p0.html
// Going to be honest here. Pretty sure there needs to be some more intelligence added here. Especially with context sub-roles

public enum InstitutionRole {
    Sysadmin, SysSupport, Creator, AccountAdmin, User, ContentDeveloper, Manager,
    Student, Faculty, Member, Learner, Instructor, TeachingAssistant, Mentor, Staff, Alumni,
    ProspectiveStudent,Guest, Other, Administrator, Observer, None;

    private static Map<String, InstitutionRole> roleMap = new HashMap<>();

    static {
        //system roles
        roleMap.put("urn:lti:sysrole:ims/lis/SysAdmin", Sysadmin);
        roleMap.put("SysAdmin", Sysadmin);
        roleMap.put("urn:lti:sysrole:ims/lis/SysSupport", SysSupport);
        roleMap.put("SysSupport", SysSupport);
        roleMap.put("urn:lti:sysrole:ims/lis/Creator", Creator);
        roleMap.put("Creator", Creator);
        roleMap.put("urn:lti:sysrole:ims/lis/AccountAdmin", AccountAdmin);
        roleMap.put("AccountAdmin", AccountAdmin);
        roleMap.put("urn:lti:sysrole:ims/lis/User", User);
        roleMap.put("User", User);
        roleMap.put("urn:lti:sysrole:ims/lis/Administrator", Administrator);
        roleMap.put("Administrator", Administrator);
        roleMap.put("urn:lti:sysrole:ims/lis/None", None);
        roleMap.put("None", None);

        //institution roles
        roleMap.put("urn:lti:instrole:ims/lis/Student", Student);
        roleMap.put("Student", Student);
        roleMap.put("urn:lti:instrole:ims/lis/Faculty", Faculty);
        roleMap.put("Faculty", Faculty);
        roleMap.put("urn:lti:instrole:ims/lis/Member", Member);
        roleMap.put("Member", Member);
        roleMap.put("urn:lti:instrole:ims/lis/Learner", Learner);
        roleMap.put("Learner", Learner);
        roleMap.put("urn:lti:instrole:ims/lis/Instructor", Instructor);
        roleMap.put("Instructor", Instructor);
        roleMap.put("urn:lti:instrole:ims/lis/Mentor", Mentor);
        roleMap.put("Mentor", Mentor);
        roleMap.put("urn:lti:instrole:ims/lis/Staff", Staff);
        roleMap.put("Staff", Staff);
        roleMap.put("urn:lti:instrole:ims/lis/Alumni", Alumni);
        roleMap.put("Alumni", Alumni);
        roleMap.put("urn:lti:instrole:ims/lis/ProspectiveStudent",ProspectiveStudent);
        roleMap.put("ProspectiveStudent", ProspectiveStudent);
        roleMap.put("urn:lti:instrole:ims/lis/Guest", Guest);
        roleMap.put("Guest", Guest);
        roleMap.put("urn:lti:instrole:ims/lis/Other", Other);
        roleMap.put("Other", Other);
        roleMap.put("urn:lti:instrole:ims/lis/Administrator", Administrator);
        //short version of Administrator already done up above
        roleMap.put("urn:lti:instrole:ims/lis/Observer", Observer);
        roleMap.put("Observer", Observer);
        roleMap.put("urn:lti:instrole:ims/lis/None", None);
        //short version of None already done up above

        //context roles - does not include subroles yet
        roleMap.put("urn:lti:role:ims/lis/Learner", Learner);
        roleMap.put("urn:lti:role:ims/lis/Instructor", Instructor);
        roleMap.put("urn:lti:role:ims/lis/ContentDeveloper", ContentDeveloper);
        roleMap.put("ContentDeveloper", ContentDeveloper);
        roleMap.put("urn:lti:role:ims/lis/Member", Member);
        roleMap.put("urn:lti:role:ims/lis/Manager", Manager);
        roleMap.put("Manager", Manager);
        roleMap.put("urn:lti:role:ims/lis/Mentor", Mentor);
        roleMap.put("urn:lti:role:ims/lis/Administrator", Administrator);
        roleMap.put("urn:lti:role:ims/lis/TeachingAssistant", TeachingAssistant);
        roleMap.put("TeachingAssistant", TeachingAssistant);
    }

    /**
     * @param roleStr The role to be looked up.
     * @return The matched role or <code>null</code> if one isn't found.
     */
    public static InstitutionRole fromString(String roleStr) {
        return roleMap.get(roleStr);
    }
}
