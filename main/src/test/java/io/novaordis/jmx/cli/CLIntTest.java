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

import javax.management.ObjectName;

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

    @Test
    public void cd_UpOneLevel() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        mc.setDomains("mock-domain");

        JmxTree mt = new JmxTreeImpl(mc);

        CLInt c = new CLInt(mt);

        String pwd = c.pwd();
        assertEquals("/", pwd);

        c.cd("mock-domain");

        pwd = c.pwd() ;
        assertEquals("/mock-domain:", pwd);

        c.cd("..");

        pwd = c.pwd() ;
        assertEquals("/", pwd);
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

    // pwd() -----------------------------------------------------------------------------------------------------------

    @Test
    public void pwd_Root_Domain_MBean() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();
        MockMBeanServerConnection.addAttribute(
                new ObjectName("mock-domain:service=Mock"), "MockAttribute", "something");

        JmxTree t = new JmxTreeImpl(mc);
        CLInt c = new CLInt(t);

        String s = c.pwd();
        assertEquals("/", s);

        c.cd("mock-domain");
        s = c.pwd();
        assertEquals("/mock-domain:", s);

        c.cd("service=Mock");
        s = c.pwd();
        assertEquals("/mock-domain:service=Mock", s);
    }

    // get() -----------------------------------------------------------------------------------------------------------

    @Test
    public void get_Root() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();
        MockMBeanServerConnection.addAttribute(new ObjectName("mock-domain:service=Mock"), "MockAttribute", 1);

        JmxTree t = new JmxTreeImpl(mc);
        CLInt c = new CLInt(t);

        String s = c.pwd();
        assertEquals("/", s);

        try {
            //
            // TODO: this needs to be adjusted when functionality is implemented
            //
            c.get("something");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("NOT YET IMPLEMENTED", msg);
        }
    }

    @Test
    public void get_Domain() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();
        MockMBeanServerConnection.addAttribute(new ObjectName("mock-domain:service=Mock"), "MockAttribute", 1);

        JmxTree t = new JmxTreeImpl(mc);
        CLInt c = new CLInt(t);

        c.cd("mock-domain");
        String s = c.pwd();
        assertEquals("/mock-domain:", s);

        try {
            //
            // TODO: this needs to be adjusted when functionality is implemented
            //
            c.get("something");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("NOT YET IMPLEMENTED", msg);
        }
    }

    @Test
    public void get_MBean_ValidAttribute() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();
        MockMBeanServerConnection.addAttribute(new ObjectName("mock-domain:service=Mock"), "MockAttribute", 1);

        JmxTree t = new JmxTreeImpl(mc);
        CLInt c = new CLInt(t);

        c.cd("mock-domain");
        c.cd("service=Mock");
        String s = c.pwd();
        assertEquals("/mock-domain:service=Mock", s);

        s = c.get("MockAttribute");
        assertEquals("1", s);
    }

    @Test
    public void get_MBean_NoSuchAttribute() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();
        MockMBeanServerConnection.addAttribute(new ObjectName("mock-domain:service=Mock"), "MockAttribute", 1);

        JmxTree t = new JmxTreeImpl(mc);
        CLInt c = new CLInt(t);

        c.cd("mock-domain");
        c.cd("service=Mock");
        String s = c.pwd();
        assertEquals("/mock-domain:service=Mock", s);

        try {

            c.get("NoSuchAttribute");
            fail("should have thrown exception");
        }
        catch (UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("NoSuchAttribute: no such attribute", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
