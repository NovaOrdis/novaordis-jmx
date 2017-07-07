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

import io.novaordis.utilities.UserErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxDomain extends JmxContainer {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JmxDomain.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxDomain(String name, JmxRoot root) {

        super(name, root);
    }

    // JmxNode overrides -----------------------------------------------------------------------------------------------

    @Override
    public List<String> getChildrenNames() throws IOException {

        //
        // we perform a JMX query
        //

        List<String> result = new ArrayList<>();

        MBeanServerConnection c = getTree().getMBeanServerConnection();

        try {

            ObjectName on = new ObjectName(getName() + ":*");
            Set<ObjectName> qr = c.queryNames(on, null);

            for(ObjectName n: qr) {

                String s = n.getCanonicalKeyPropertyListString();
                result.add(s);
            }
        }
        catch(MalformedObjectNameException e) {

            throw new IOException(e);
        }

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param relativeLocation we expect a partial ObjectName, the key/value pair sequence without the domain name
     *                         and ":"
     */
    @Override
    protected JmxNode getRelativeNode(String relativeLocation) throws IOException, UserErrorException {

        //
        // we interpret the relative location as the key/value pair of an ObjectName for this domain and we query
        //

        String s = getName() + ":" + relativeLocation;

        ObjectName on;

        try {

            on = new ObjectName(s);

        }
        catch(MalformedObjectNameException e) {

            throw new UserErrorException(s + " is not a valid ObjectName: " + e.getMessage());
        }

        MBeanServerConnection c = getTree().getMBeanServerConnection();

        try {

            MBeanInfo i = c.getMBeanInfo(on);

            log.debug("" + i);

            try {

                return new JmxMBean(relativeLocation, this);
            }
            catch(MalformedObjectNameException e) {

                //
                // we already performed this test, so it would be odd to fail now
                //

                throw new IllegalStateException(e);
            }
        }
        catch(InstanceNotFoundException e) {

            throw new UserErrorException(relativeLocation + ": no such MBean in domain '" + getName() + "'");

        }
        catch(ReflectionException | IntrospectionException e) {

            throw new UserErrorException("failed to query " + on.getCanonicalName());
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
