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

import io.novaordis.utilities.UserErrorException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/7/17
 */
public class CommandLineArgumentsTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void noArguments() throws Exception {

        CommandLineArguments c = new CommandLineArguments(new String[0]);

        assertNull(c.getUri());
    }

    @Test
    public void uri() throws Exception {

        String s = "jmx://blah:blah123!@localhost:4447";

        CommandLineArguments c = new CommandLineArguments(new String[] {s});

        assertEquals("jmx://blah:blah123!@localhost:4447", c.getUri());

    }

    @Test
    public void uri2() throws Exception {

        String s = "jmx://localhost:4447";

        CommandLineArguments c = new CommandLineArguments(new String[] {s});

        assertEquals("jmx://localhost:4447", c.getUri());
    }

    @Test
    public void uri3() throws Exception {

        String s = "localhost:9999";

        CommandLineArguments c = new CommandLineArguments(new String[] {s});

        assertEquals("localhost:9999", c.getUri());
    }

    // username --------------------------------------------------------------------------------------------------------

    @Test
    public void username() throws Exception {

        String s = "--username=test-user";

        CommandLineArguments c = new CommandLineArguments(new String[] {s});

        assertNull(c.getUri());

        assertEquals("test-user", c.getUsernameFromOption());
    }

    @Test
    public void username_InvalidSyntax() throws Exception {

        String s = "--username";

        try {

            new CommandLineArguments(new String[]{s});
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("invalid --username syntax, use --username=<username>", msg);
        }
    }

    @Test
    public void username_InvalidSyntax2() throws Exception {

        String s = "--username&something";

        try {

            new CommandLineArguments(new String[]{s});
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("invalid --username syntax, use --username=<username>", msg);
        }
    }

    @Test
    public void username_MissingUsername() throws Exception {

        String s = "--username=";

        try {

            new CommandLineArguments(new String[]{s});
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("missing username", msg);
        }
    }

    // password --------------------------------------------------------------------------------------------------------

    @Test
    public void password() throws Exception {

        String s = "--password=test-password";

        CommandLineArguments c = new CommandLineArguments(new String[] {s});

        assertNull(c.getUri());

        assertEquals("test-password", new String(c.getPasswordFromOption()));
    }

    @Test
    public void password_InvalidSyntax() throws Exception {

        String s = "--password";

        try {

            new CommandLineArguments(new String[]{s});
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("invalid --password syntax, use --password=<password>", msg);
        }
    }

    @Test
    public void password_InvalidSyntax2() throws Exception {

        String s = "--password&something";

        try {

            new CommandLineArguments(new String[]{s});
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("invalid --password syntax, use --password=<password>", msg);
        }
    }

    @Test
    public void password_MissingUsername() throws Exception {

        String s = "--password=";

        try {

            new CommandLineArguments(new String[]{s});
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("missing password", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
