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
import io.novaordis.jmx.tree.JmxTreeImpl;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxAttributeTest extends JmxNodeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxAttribute getJmxNodeToTest(MBeanServerConnection c) throws Exception {

        JmxRoot r = new JmxRoot(new JmxTreeImpl(c));
        JmxDomain d = new JmxDomain("mock-domain", r);
        String name = "service=Mock,color=Blue";

        //
        // "populate" the MBeanServer with the corresponding instance
        //

        MockMBeanServerConnection.addAttribute(
                new ObjectName("mock-domain:service=Mock,color=Blue"), "mock-attribute", 1);

        JmxMBean b = new JmxMBean(name, d);
        return new JmxAttribute("mock-attribute", b);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
