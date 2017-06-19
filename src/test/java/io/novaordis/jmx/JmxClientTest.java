/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.jmx;

import io.novaordis.jmx.mockpackage.mockprotocol.ClientProvider;
import io.novaordis.jmx.mockpackage.mockprotocol.MockMBeanServerConnection;
import org.junit.After;
import org.junit.Test;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public abstract class JmxClientTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @After
    public void cleanup() {

        //
        // "clean" the mock remote MBean Server
        //

        MockMBeanServerConnection.clear();
        ClientProvider.clear();
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void defaults() throws Exception {

        JmxAddress address = new JmxAddress("jmx://mock-server:1000");

        JmxClient c = getJmxClientToTest(address);

        assertEquals(address, c.getAddress());
    }

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullHostInAddress() throws Exception {

        JmxAddress address = new JmxAddress(JmxAddress.PROTOCOL, null, 1000);

        try {

            getJmxClientToTest(address);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null host"));
        }
    }

    @Test
    public void constructor_NullPortInAddress() throws Exception {

        JmxAddress address = new JmxAddress(JmxAddress.PROTOCOL, "test-host", null);

        try {

            getJmxClientToTest(address);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null port"));
        }
    }

    // connect() -------------------------------------------------------------------------------------------------------

    @Test
    public void connect_AddressNotSet() throws Exception {

        JmxClient c = getJmxClientToTest(null);

        assertNull(c.getAddress());

        try {

            c.connect();
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("improperly initialized"));
            assertTrue(msg.contains("address not set"));
        }
    }

    @Test
    public void connect_ServerNotAvailable() throws Exception {

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        //
        // configure the mock JMX server to "not be available"
        //
        ClientProvider.setRemoteJmxServerNotAvailable(true);

        try {

            c.connect();
            fail("should have thrown exception");
        }
        catch(JmxException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("failed to create the underlying JMX remoting connector"));

            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertTrue(cause instanceof ConnectException);
        }
    }

    @Test
    public void connect_connectSuccessfulThenServerGoesDown() throws Exception {

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        assertFalse(c.isConnected());

        c.connect();

        assertTrue(c.isConnected());

        //
        // configure the mock server to "go down" after connection
        //

        ClientProvider.setRemoteJmxFailsToProduceAMBeanServerConnection(true);

        try {

            c.getMBeanServerConnection();
            fail("should have thrown Exception");
        }
        catch(JmxException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to get the MBeanServerConnection"));

            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertTrue(cause instanceof IOException);
        }
    }

    // lifecycle() -----------------------------------------------------------------------------------------------------

    @Test
    public void lifecycle() throws Exception {

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        //
        // configure the remote server with a specific attribute
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=Mock"), "TestAttribute", "blue");

        //
        // lifecycle
        //

        assertFalse(c.isConnected());

        c.connect();

        assertTrue(c.isConnected());

        //
        // idempotence
        //

        c.connect();

        assertTrue(c.isConnected());

        MBeanServerConnection sc = c.getMBeanServerConnection();

        Object o = sc.getAttribute(new ObjectName("test:service=Mock"), "TestAttribute");
        assertEquals("blue", o);

        c.disconnect();

        assertFalse(c.isConnected());

        //
        // idempotence
        //

        c.disconnect();

        assertFalse(c.isConnected());

        assertTrue(ma.equals(c.getAddress()));
    }

    // getMBeanServerConnection() --------------------------------------------------------------------------------------

    @Test
    public void getMBeanServerConnection_Unconnected() throws Exception {

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        assertFalse(c.isConnected());

        try {

            c.getMBeanServerConnection();
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not connected"));
        }
    }

    @Test
    public void getMBeanServerConnection_getAttribute() throws Exception {

        //
        // "populate" the remote server
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=MockService"), "TestAttribute", "something");

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=MockService"), "TestAttribute2", 7);

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        MBeanServerConnection mBeanServer = c.getMBeanServerConnection();

        ObjectName on = new ObjectName("test:service=MockService");

        Object o = mBeanServer.getAttribute(on, "TestAttribute");

        assertEquals("something", o);

        Object o2 = mBeanServer.getAttribute(on, "TestAttribute2");

        assertEquals(7, o2);

        c.disconnect();

        assertFalse(c.isConnected());
    }

    @Test
    public void getMBeanServerConnection_getAttributes() throws Exception {

        //
        // "populate" the remote server
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=MockService"), "TestAttribute", "something");

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=MockService"), "TestAttribute2", 7);

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        MBeanServerConnection mBeanServer = c.getMBeanServerConnection();

        ObjectName on = new ObjectName("test:service=MockService");

        AttributeList al = mBeanServer.getAttributes(on, new String[]{"TestAttribute", "TestAttribute2"});

        List<Attribute> attributes = al.asList();

        assertEquals(2, attributes.size());

        Attribute jmxAttribute = attributes.get(0);

        assertEquals("TestAttribute", jmxAttribute.getName());
        assertEquals("something", jmxAttribute.getValue());

        Attribute jmxAttribute2 = attributes.get(1);

        assertEquals("TestAttribute2", jmxAttribute2.getName());
        assertEquals(7, jmxAttribute2.getValue());

        c.disconnect();

        assertFalse(c.isConnected());
    }

    @Test
    public void getMBeanServerConnection_getAttribute_ObjectNameDoesNotExist() throws Exception {

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        MBeanServerConnection mBeanServer = c.getMBeanServerConnection();

        ObjectName on = new ObjectName("test:service=NoSuchService");

        try {

            mBeanServer.getAttribute(on, "TestAttribute");
            fail("should have thrown exception");
        }
        catch(InstanceNotFoundException e) {

            String msg = e.getMessage();
            assertEquals("test:service=NoSuchService", msg);
        }

        c.disconnect();

        assertFalse(c.isConnected());
    }

    @Test
    public void getMBeanServerConnection_getAttribute_AttributeDoesNotExist() throws Exception {

        //
        // "populate" the remote server with the correct ObjectName, but not the attribute
        //

        MockMBeanServerConnection.addAttribute(
                new ObjectName("test:service=MockService"), "TestAttribute", "something");

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        MBeanServerConnection mBeanServer = c.getMBeanServerConnection();

        ObjectName on = new ObjectName("test:service=MockService");

        try {

            mBeanServer.getAttribute(on, "SomeAttributeThatDoesNotExist");
            fail("should have thrown exception");
        }
        catch(AttributeNotFoundException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("SomeAttributeThatDoesNotExist"));
        }

        c.disconnect();

        assertFalse(c.isConnected());
    }

    @Test
    public void getMBeanServerConnection_getAttribute_JustOneAttributeFromTheListDoesNotExist() throws Exception {

        //
        // "populate" the remote server
        //

        MockMBeanServerConnection.addAttribute(
                new ObjectName("test:service=MockService"), "TestAttribute", "something");

        MockJmxAddress ma = new MockJmxAddress();

        JmxClient c = getJmxClientToTest(ma);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        MBeanServerConnection mBeanServer = c.getMBeanServerConnection();

        ObjectName on = new ObjectName("test:service=MockService");

        // the first attribute exists, but the second doesn't

        try {
            mBeanServer.getAttributes(on, new String[]{"TestAttribute", "NoSuchAttribute"});
        }
        catch(ReflectionException e) {

            AttributeNotFoundException cause = (AttributeNotFoundException)e.getCause();
            String msg = cause.getMessage();
            assertTrue(msg.contains("NoSuchAttribute"));
        }

        c.disconnect();

        assertFalse(c.isConnected());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param address may be null.
     */
    protected abstract JmxClient getJmxClientToTest(JmxAddress address) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
