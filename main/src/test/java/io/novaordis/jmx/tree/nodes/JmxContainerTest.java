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
import org.junit.Test;

import javax.management.MBeanServerConnection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public abstract class JmxContainerTest extends JmxNodeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void getChildren() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxContainer c = getJmxContainerToTest(mc);

        List<String> childrenNames = c.getChildrenNames();

        assertNotNull(childrenNames);
    }

    @Test
    public void getRelative_OneUp() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxContainer c = getJmxContainerToTest(mc);

        JmxNode p = c.getParent();

        if (p == null) {

            JmxNode relative = c.getRelative("..");
            assertEquals(c, relative);
        }
        else {

            JmxNode relative = c.getRelative("..");
            assertEquals(p, relative);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JmxContainer getJmxContainerToTest(MBeanServerConnection c) throws Exception;

    @Override
    protected JmxNode getJmxNodeToTest(MBeanServerConnection c) throws Exception {

        return getJmxContainerToTest(c);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
