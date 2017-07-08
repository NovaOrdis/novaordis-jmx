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

package io.novaordis.jmx.cli;

import io.novaordis.jmx.JmxAddress;
import io.novaordis.utilities.UserErrorException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/7/17
 */
public class MainTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void reconcileCredentials_NoUsernameFromOption() throws Exception {

        JmxAddress a = new JmxAddress("jmx://localhost:1000");

        Main.reconcileCredentials(a, null, null);

        assertNull(a.getUsername());
        assertNull(a.getPassword());

        JmxAddress a2 = new JmxAddress("jmx://test-user:test-password@localhost:1000");

        Main.reconcileCredentials(a2, null, null);

        assertEquals("test-user", a2.getUsername());
        assertEquals("test-password", new String(a2.getPassword()));
    }

    @Test
    public void reconcileCredentials_SameUsernameFromOption_SamePasswordFromOption() throws Exception {

        JmxAddress a = new JmxAddress("jmx://test-user:test-password@localhost:1000");

        Main.reconcileCredentials(a, "test-user", "test-password".toCharArray());

        assertEquals("test-user", a.getUsername());
        assertEquals("test-password", new String(a.getPassword()));
    }

    @Test
    public void reconcileCredentials_NoUsernameInUri() throws Exception {

        JmxAddress a = new JmxAddress("jmx://localhost:1000");

        assertNull(a.getUsername());
        assertNull(a.getPassword());

        Main.reconcileCredentials(a, "test-user", "test-password".toCharArray());

        assertEquals("test-user", a.getUsername());
        assertEquals("test-password", new String(a.getPassword()));
    }

    @Test
    public void reconcileCredentials_DifferentUsernameFromOption() throws Exception {

        JmxAddress a = new JmxAddress("jmx://test-user:test-password@localhost:1000");

        try {

            Main.reconcileCredentials(a, "other-user", "test-password".toCharArray());
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("the username specified with --username and the one specified in the URI do not match", msg);
        }
    }

    @Test
    public void reconcileCredentials_SameUsernameFromOption_DifferentPasswordFromOption() throws Exception {

        JmxAddress a = new JmxAddress("jmx://test-user:test-password@localhost:1000");

        try {

            Main.reconcileCredentials(a, "test-user", "other-password".toCharArray());
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("the password specified with --password and the one specified in the URI do not match", msg);
        }
    }

    @Test
    public void reconcileCredentials_SameUsernameFromOption_NoPasswordInUri_PasswordInOption() throws Exception {

        JmxAddress a = new JmxAddress("jmx://test-user@localhost:1000");

        Main.reconcileCredentials(a, "test-user", "test-password".toCharArray());

        assertEquals("test-user", a.getUsername());
        assertEquals("test-password", new String(a.getPassword()));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
