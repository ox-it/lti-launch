package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.service.SimpleToolConsumer;
import edu.ksu.lti.launch.service.ToolConsumer;
import org.junit.Test;

import java.io.*;

public class LtiPrincipalTest {

    @Test
    public void testSerializableNoToolConsumer() throws IOException {
        LtiPrincipal principal = new LtiPrincipal(null, "principal");
        OutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(principal);
        oos.close();
    }

    @Test
    public void testSerializableSimpleToolConsumer() throws IOException {
        ToolConsumer toolConsumer = new SimpleToolConsumer("instance", "Instance Name", "http://example.com");
        LtiPrincipal principal = new LtiPrincipal(toolConsumer, "principal");
        OutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(principal);
        oos.close();
    }

    @Test(expected = NotSerializableException.class)
    public void testSerializableNotSerializable() throws IOException {
        ToolConsumer toolConsumer = new ToolConsumer() {
            @Override
            public String getInstance() {
                return "instance";
            }

            @Override
            public String getName() {
                return "Name";
            }

            @Override
            public String getUrl() {
                return "url";
            }
        };
        LtiPrincipal principal = new LtiPrincipal(toolConsumer, "principal");
        OutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(principal);
        oos.close();
    }


}
