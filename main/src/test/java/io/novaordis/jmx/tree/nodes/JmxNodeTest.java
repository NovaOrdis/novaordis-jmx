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
import org.junit.After;
import org.junit.Test;

import javax.management.MBeanServerConnection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public abstract class JmxNodeTest {

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

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxNode n = getJmxNodeToTest(mc);

        if (n instanceof JmxRoot) {

            assertNull(n.getParent());
        }
        else {

            assertNotNull(n.getParent());
        }

        assertNotNull(n.getName());

        boolean container = n.isContainer();

        if (container) {

            JmxContainer c = (JmxContainer)n;
            List<String> cns = c.getChildrenNames();
            assertNotNull(cns);
        }

        JmxTree tree = n.getTree();
        assertNotNull(tree);

        assertEquals(mc, tree.getMBeanServerConnection());
    }

    // getTree() -------------------------------------------------------------------------------------------------------

    @Test
    public void getTree() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();

        JmxNode n = getJmxNodeToTest(mc);

        JmxTree t = n.getTree();
        assertNotNull(t);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JmxNode getJmxNodeToTest(MBeanServerConnection c) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
