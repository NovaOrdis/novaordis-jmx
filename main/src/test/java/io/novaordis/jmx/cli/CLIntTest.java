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

import io.novaordis.jmx.mockpackage.mockprotocol.MockMBeanServerConnection;
import io.novaordis.jmx.tree.JmxTree;
import io.novaordis.jmx.tree.JmxTreeImpl;
import io.novaordis.utilities.UserErrorException;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class CLIntTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @After
    public void cleanup() throws Exception {

        MockMBeanServerConnection.clear();
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // cd() ------------------------------------------------------------------------------------------------------------

    @Test
    public void cd_GoingDown_ChildDoesNotExist() throws Exception {

        // no domains
        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxTree mt = new JmxTreeImpl(mc);

        CLInt c = new CLInt(mt);

        String s = c.pwd();
        assertEquals("/", s);

        try {

            c.cd("something");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("cd: something: no such location", msg);
        }
    }

    @Test
    public void cd_GoingDown_ChildDoesExist() throws Exception {

        // no domains
        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxTree mt = new JmxTreeImpl(mc);

        CLInt c = new CLInt(mt);

        String s = c.pwd();
        assertEquals("/", s);

        try {

            c.cd("something");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("cd: something: no such location", msg);
        }
    }

    // ls() ------------------------------------------------------------------------------------------------------------

    @Test
    public void ls_NamesInAlphabeticalOrder() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        mc.setDomains("kia", "jaguar", "infiniti", "honda", "gmc", "fiat", "dodge", "chrysler", "bmw", "acura");

        JmxTree mt = new JmxTreeImpl(mc);

        CLInt c = new CLInt(mt);

        String s = c.ls("");

        assertEquals("acura\nbmw\nchrysler\ndodge\nfiat\ngmc\nhonda\ninfiniti\njaguar\nkia", s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}