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
import org.junit.After;
import org.junit.Test;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxMBeanTest extends JmxContainerTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    public void getChildren() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxMBean b = getJmxContainerToTest(mc);

        MockMBeanServerConnection.clear();

        //
        // populate the MBeanServer with a matching ObjectName
        //

        ObjectName o = new ObjectName(b.getParent().getName() + ":"  + b.getName());
        MockMBeanServerConnection.addAttribute(o, "test-attribute-1", 1);
        MockMBeanServerConnection.addAttribute(o, "test-attribute-2", "something");

        List<String> childrenNames = b.getChildrenNames();

        assertEquals(2, childrenNames.size());

        //
        // TODO this test will fail when I fix JmxMBean.getChildren() and it will need adjusting; currently it does
        // not test the right thing
        //
        assertTrue(childrenNames.contains("test-attribute-1 (java.lang.Integer)"));
        assertTrue(childrenNames.contains("test-attribute-2 (java.lang.String)"));
    }

    @Test
    public void getRelative_NoMatchingName() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxMBean b = getJmxContainerToTest(mc);


        //
        // populate the MBeanServer with a matching ObjectName
        //

        ObjectName o = new ObjectName(b.getParent().getName() + ":"  + b.getName());
        MockMBeanServerConnection.addAttribute(o, "test-attribute-1", 1);
        MockMBeanServerConnection.addAttribute(o, "test-attribute-2", "something");

        try {

            b.getRelative("no-such-attribute");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no such attribute"));
            assertTrue(msg.contains("no-such-attribute"));
        }
    }

    @Test
    public void getRelative() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxMBean b = getJmxContainerToTest(mc);


        //
        // populate the MBeanServer with a matching ObjectName
        //

        ObjectName o = new ObjectName(b.getParent().getName() + ":"  + b.getName());
        MockMBeanServerConnection.addAttribute(o, "test-attribute-1", 1);
        MockMBeanServerConnection.addAttribute(o, "test-attribute-2", "something");

        JmxAttribute a = (JmxAttribute)b.getRelative("test-attribute-1");
        assertEquals(b, a.getParent());
        assertEquals("test-attribute-1", a.getName());
    }

    // getTree() -------------------------------------------------------------------------------------------------------

    @Test
    public void getTree() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxMBean n = getJmxContainerToTest(mc);

        JmxTree t = n.getTree();

        assertNotNull(t);

        JmxDomain d = (JmxDomain)n.getParent();
        assertNotNull(d);

        JmxRoot r = (JmxRoot)d.getParent();
        assertNotNull(r);

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxMBean getJmxContainerToTest(MBeanServerConnection c) throws Exception {

        JmxRoot r = new JmxRoot(new JmxTreeImpl(c));
        JmxDomain d = new JmxDomain("mock-domain", r);
        String name = "service=Mock,color=Blue";

        //
        // "populate" the MBeanServer with the corresponding instance, so getChildrenNames() won't fail
        //

        MockMBeanServerConnection.addAttribute(
                new ObjectName("mock-domain:service=Mock,color=Blue"), "some-attribute", 1);

        return new JmxMBean(name, d);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
