package edu.ksu.lti.launch.oauth;

import org.junit.Before;
import org.junit.Test;

import static edu.ksu.lti.launch.model.InstitutionRole.*;
import static java.util.Collections.EMPTY_SET;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefaultLtiUserAutorityFactoryTest {

    private DefaultLtiUserAuthorityFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultLtiUserAuthorityFactory();
    }

    @Test
    public void testEmptyAuthorities() {
        assertThat(factory.getLtiUserAuthorities(""), equalTo(EMPTY_SET));
    }

    @Test
    public void testEmptyAuthority() {
        assertThat(factory.getLtiUserAuthority(""), nullValue());
    }

    @Test
    public void testSingleRole() {
        assertThat(factory.getLtiUserAuthorities("Instructor"), hasItems(new StandardLtiUserAuthority(Instructor)));
    }

    @Test
    public void testTwoRoles() {
        assertThat(factory.getLtiUserAuthorities("Instructor,Student"), hasItems(
            new StandardLtiUserAuthority(Instructor),
            new StandardLtiUserAuthority(Student)
        ));
    }

    @Test
    public void testTwoQualifiedRoles() {
        assertThat(factory.getLtiUserAuthorities("urn:lti:role:ims/lis/Member,urn:lti:instrole:ims/lis/Guest"), hasItems(
            new StandardLtiUserAuthority(Member),
            new StandardLtiUserAuthority(Guest)
        ));
    }

    @Test
    public void testDuplicatedRoles() {
        assertThat(factory.getLtiUserAuthorities("Instructor,urn:lti:instrole:ims/lis/Instructor,urn:lti:role:ims/lis/Instructor"),
            everyItem(is(new StandardLtiUserAuthority(Instructor))
        ));
    }
}
