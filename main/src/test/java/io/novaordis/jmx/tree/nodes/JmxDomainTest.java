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

package io.novaordis.jmx.tree.nodes;

import io.novaordis.jmx.mockpackage.mockprotocol.MockMBeanServerConnection;
import io.novaordis.jmx.tree.JmxTree;
import io.novaordis.jmx.tree.JmxTreeImpl;
import io.novaordis.utilities.UserErrorException;
import org.junit.Test;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxDomainTest extends JmxContainerTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    public void getChildren() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxDomain d = getJmxContainerToTest(mc);

        String name = d.getName();

        //
        // populate the MBeanServer with matching ObjectNames
        //

        ObjectName o1 = new ObjectName(name + ":service=Mock,color=red");
        ObjectName o2 = new ObjectName(name + ":service=Mock,color=blue");

        MockMBeanServerConnection.addAttribute(o1, "something", 1);
        MockMBeanServerConnection.addAttribute(o2, "something-else", 2);


        List<String> childrenNames = d.getChildrenNames();

        assertEquals(2, childrenNames.size());

        assertTrue(childrenNames.contains(o1.getCanonicalKeyPropertyListString()));
        assertTrue(childrenNames.contains(o2.getCanonicalKeyPropertyListString()));
    }

    @Test
    public void getRelative_InvalidName() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxDomain d = getJmxContainerToTest(mc);

        String invalidKeyValuePair = "&w23h##28301";

        try {

            d.getRelative(invalidKeyValuePair);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.startsWith(d.getName() + ":" + invalidKeyValuePair + " is not a valid ObjectName"));
        }
    }

    @Test
    public void getRelative_NoMatchingName() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxDomain d = getJmxContainerToTest(mc);

        String name = d.getName();

        //
        // populate the MBeanServer with matching ObjectNames
        //

        ObjectName o1 = new ObjectName(name + ":service=Mock,color=red");
        ObjectName o2 = new ObjectName(name + ":service=Mock,color=blue");

        MockMBeanServerConnection.addAttribute(o1, "something", 1);
        MockMBeanServerConnection.addAttribute(o2, "something-else", 2);

        try {

            d.getRelative("service=Mock,color=green");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue("service=Mock,color=green: no such MBean in domain 'mock-domain'".equals(msg));
        }
    }

    @Test
    public void getRelative() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxDomain d = getJmxContainerToTest(mc);

        String name = d.getName();

        //
        // populate the MBeanServer with matching ObjectNames
        //

        ObjectName o1 = new ObjectName(name + ":service=Mock,color=red");
        ObjectName o2 = new ObjectName(name + ":service=Mock,color=blue");

        MockMBeanServerConnection.addAttribute(o1, "something", 1);
        MockMBeanServerConnection.addAttribute(o2, "something-else", 2);

        JmxMBean mbean = (JmxMBean)d.getRelative("service=Mock,color=red");

        assertEquals("service=Mock,color=red", mbean.getName());
    }

    // getTree() -------------------------------------------------------------------------------------------------------

    @Test
    public void getTree() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxDomain n = getJmxContainerToTest(mc);

        JmxTree t = n.getTree();

        assertNotNull(t);

        JmxRoot r = (JmxRoot)n.getParent();
        assertNotNull(r);
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxDomain getJmxContainerToTest(MBeanServerConnection c) throws Exception {

        JmxRoot r = new JmxRoot(new JmxTreeImpl(c));
        return new JmxDomain("mock-domain", r);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
