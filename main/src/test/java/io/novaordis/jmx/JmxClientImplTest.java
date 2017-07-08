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

import io.novaordis.jmx.mockpackage.mockprotocol.MockJMXConnector;
import org.junit.Test;

import javax.management.remote.JMXConnector;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class JmxClientImplTest extends JmxClientTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        JmxAddress a = new JmxAddress("jmx://mock-host:1000");

        JmxClientImpl c = new JmxClientImpl(a);

        assertEquals(a, c.getAddress());
    }

    @Test
    public void connect() throws Exception {

        MockJmxAddress a = new MockJmxAddress();

        JmxClientImpl c = new JmxClientImpl(a);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        assertTrue(c.isConnected());
    }

    @Test
    public void testThatAuthenticationCredentialsArePassedInEnvironment() throws Exception {

        MockJmxAddress a = new MockJmxAddress();

        a.setUsername("test-username");
        a.setPassword("test-password".toCharArray());

        JmxClientImpl c = new JmxClientImpl(a);

        //
        // configure jmx client for testing, io.novaordis.jmx.mockpackage.mockprotcol.ClientProvider
        // builds a test JMXConnector
        //

        c.setProtocolProviderPackage("io.novaordis.jmx.mockpackage");

        c.connect();

        //
        // Make sure the environment that was provided to the MockJMXConnector constructor by the ClientProvider
        // contains authentication credentials.
        //

        MockJMXConnector mc = (MockJMXConnector)c.getJmxConnector();

        Map<String, ?> environmentCopyInConstructor = mc.getEnvironmentCopyInConstructor();

        Object o = environmentCopyInConstructor.get(JMXConnector.CREDENTIALS);
        assertNotNull(o);
        String[] credentials = (String[]) o;
        assertEquals(2, credentials.length);
        assertEquals("test-username", credentials[0]);
        assertEquals("test-password", credentials[1]);

        //
        // Also make the environment content was also provided to connect()
        //

        Map<String, ?> environmentCopyInConnect = mc.getEnvironmentCopyInConnect();

        Object o2 = environmentCopyInConnect.get(JMXConnector.CREDENTIALS);
        assertNotNull(o2);
        String[] credentials2 = (String[]) o;
        assertEquals(2, credentials2.length);
        assertEquals("test-username", credentials2[0]);
        assertEquals("test-password", credentials2[1]);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxClientImpl getJmxClientToTest(JmxAddress address) throws Exception {

        return new JmxClientImpl(address);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
