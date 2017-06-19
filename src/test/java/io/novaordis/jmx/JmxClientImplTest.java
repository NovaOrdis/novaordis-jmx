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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxClientImpl getJmxClientToTest(JmxAddress address) throws Exception {

        return new JmxClientImpl(address);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
