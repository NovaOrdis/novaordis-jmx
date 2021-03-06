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

import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.AddressImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class JmxAddressTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_default() throws Exception {

        JmxAddress a = new JmxAddress();

        assertEquals(JmxAddress.PROTOCOL, a.getProtocol());
    }

    @Test
    public void constructor_String() throws Exception {

        JmxAddress a = new JmxAddress("jmx://admin:adminpasswd@1.2.3.4:700/");

        assertEquals(JmxAddress.PROTOCOL, a.getProtocol());
        assertEquals("admin", a.getUsername());
        assertEquals("adminpasswd", new String(a.getPassword()));
        assertEquals("1.2.3.4", a.getHost());
        assertEquals(700, a.getPort().intValue());
        assertEquals("jmx://admin@1.2.3.4:700", a.getLiteral());
    }

    @Test
    public void constructor_String_InvalidProtocol() throws Exception {

        try {

            new JmxAddress("something://1.2.3.4:700");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid protocol"));
        }
    }

    @Test
    public void constructor_String_2() throws Exception {

        JmxAddress a = new JmxAddress("1.2.3.4:700");

        assertEquals(JmxAddress.PROTOCOL, a.getProtocol());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4", a.getHost());
        assertEquals(700, a.getPort().intValue());
        assertEquals("jmx://1.2.3.4:700", a.getLiteral());
    }

    @Test
    public void constructor_String_InvalidPort() throws Exception {

        try {

            new JmxAddress("1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_String_NoPort() throws Exception {

        try {

            new JmxAddress("1.2.3.4");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("missing port value"));
        }
    }

    @Test
    public void constructor_Username_Password_Host_Port() throws Exception {

        JmxAddress a = new JmxAddress("test-user", "test-password", "test-host", 1000);

        assertEquals(JmxAddress.PROTOCOL, a.getProtocol());
        assertEquals("test-user", a.getUsername());
        assertEquals("test-password", new String(a.getPassword()));
        assertEquals("test-host", a.getHost());
        assertEquals(1000, a.getPort().intValue());
    }

    @Test
    public void constructor_Username_Password_Host_Port_NoPort() throws Exception {

        try {

            new JmxAddress("test-user", "test-password", "test-host", null);
            fail("should have thrown exception");
        }
        catch(AddressException a) {

            String msg = a.getMessage();
            assertTrue(msg.contains("missing port value"));
        }
    }

    // copy() ----------------------------------------------------------------------------------------------------------

    @Test
    public void copy() throws Exception {

        JmxAddress a = new JmxAddress("jmx://admin:password@example.com:700/");

        JmxAddress a2 = a.copy();

        assertTrue(a.equals(a2));
        assertTrue(a2.equals(a));

        //
        // mutate stuff
        //

        a.setPort(701);

        assertEquals(701, a.getPort().intValue());
        assertEquals(700, a2.getPort().intValue());
    }

    @Test
    public void copy_WithJmxServiceUrlProtocol() throws Exception {

        JmxAddress a = new JmxAddress("jmx://admin:password@example.com:700/");

        a.setJmxServiceUrlProtocol("test-jmx-service-url-protocol");

        JmxAddress a2 = a.copy();

        assertTrue(a.equals(a2));
        assertTrue(a2.equals(a));

        assertEquals("test-jmx-service-url-protocol", a2.getJmxServiceUrlProtocol());

        //
        // mutate stuff
        //

        a.setJmxServiceUrlProtocol("test-jmx-service-url-protocol-2");

        assertEquals("test-jmx-service-url-protocol-2", a.getJmxServiceUrlProtocol());
        assertEquals("test-jmx-service-url-protocol", a2.getJmxServiceUrlProtocol());
    }

    // equals() --------------------------------------------------------------------------------------------------------

    /**
     * @see JmxAddress#equals(Object) for a justification for why we do that.
     */
    @Test
    public void equals_DifferentTypesButSameContent() throws Exception {

        String s = "jmx://admin:adminpasswd@1.2.3.4:700";

        JmxAddress a = new JmxAddress(s);

        AddressImpl a2 = new AddressImpl(s);

        assertTrue(a.equals(a2));
        assertTrue(a2.equals(a));
    }

    // setProtocol() ---------------------------------------------------------------------------------------------------

    @Test
    public void setProtocol_CanNotChangeFromJmx() throws Exception {

        JmxAddress a = new JmxAddress("jmx://example:80");

        try {

            a.setProtocol("something");
            fail("should have thrown exception");
        }
        catch (IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid protocol"));
        }
    }

    @Test
    public void setProtocol_CanNotChangeFromJmx_Null() throws Exception {

        JmxAddress a = new JmxAddress("jmx://example:80");

        try {

            a.setProtocol(null);
            fail("should have thrown exception");
        }
        catch (IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid protocol"));
        }
    }


    @Test
    public void setProtocol_JmxIsNoop() throws Exception {

        JmxAddress a = new JmxAddress("jmx://example:80");

        // noop
        a.setProtocol("jmx");

        assertEquals("jmx", a.getProtocol());
    }

    // getJmxServiceUrlProtocol()/setJmxServiceUrlProtocol() -----------------------------------------------------------

    @Test
    public void setJmxServiceUrlProtocol() throws Exception {

        JmxAddress a = new JmxAddress("jmx://test:1000");

        assertNull(a.getJmxServiceUrlProtocol());

        a.setJmxServiceUrlProtocol("test-jmx-service-url-protocol");

        assertEquals("test-jmx-service-url-protocol", a.getJmxServiceUrlProtocol());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
