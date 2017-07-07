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
import org.junit.Test;

import javax.management.MBeanServerConnection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxRootTest extends JmxContainerTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void nullArgumentConstructor() throws Exception {

        try {

            new JmxRoot(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null"));
        }
    }

    @Test
    public void jmxRootConstructor() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        mc.setDomains("mock-domain1");

        JmxTree t = new JmxTreeImpl(mc);

        JmxRoot r = new JmxRoot(t);

        List<String> cn = r.getChildrenNames();
        assertEquals(1, cn.size());
        assertEquals("mock-domain1", cn.get(0));
    }

    @Test
    public void getParent() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxRoot r = getJmxContainerToTest(mc);

        assertNull(r.getParent());
    }

    @Test
    public void getName() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxRoot r = getJmxContainerToTest(mc);

        assertEquals("/", r.getName());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxRoot getJmxContainerToTest(MBeanServerConnection c) throws Exception {

        JmxTree t = new JmxTreeImpl(c);
        return new JmxRoot(t);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
