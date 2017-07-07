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

package io.novaordis.jmx.tree;

import io.novaordis.jmx.mockpackage.mockprotocol.MockMBeanServerConnection;
import io.novaordis.utilities.UserErrorException;
import org.junit.After;
import org.junit.Test;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxTreeImplTest extends JmxTreeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @After
    public void cleanup() {

        MockMBeanServerConnection.clear();
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void setCurrent_TargetIsNotAContainer() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection();
        JmxTree t = getJmxTreeToTest(mc);

        MockMBeanServerConnection.addAttribute(
                new ObjectName("mock-domain:service=Mock"), "MockAttribute", "something");

        t.setCurrent("mock-domain");
        t.setCurrent("service=Mock");

        try {

            t.setCurrent("MockAttribute");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("MockAttribute: not a location to be navigating into", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxTree getJmxTreeToTest(MBeanServerConnection c) throws Exception {

        return new JmxTreeImpl(c);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
