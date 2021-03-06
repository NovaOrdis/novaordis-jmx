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

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxMBean extends JmxContainer {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private ObjectName objectName;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected JmxMBean(String name, JmxContainer parent) throws MalformedObjectNameException {

        super(name, parent);

        //
        // we also set the ObjectName here
        //

        this.objectName = new ObjectName(parent.getName() + ":" + name);
    }

    // JmxNode overrides -----------------------------------------------------------------------------------------------

    /**
     * The current implementation only returns attribute names.
     */
    @Override
    public List<String> getChildrenNames() throws IOException {

        //
        // we get the MBeanInfo
        //

        MBeanServerConnection c = getTree().getMBeanServerConnection();

        try {

            MBeanInfo i = c.getMBeanInfo(objectName);

            //
            // so far, we only list attributes as children
            //

            List<String> result = new ArrayList<>();

            MBeanAttributeInfo[] attributeInfos = i.getAttributes();

            for(MBeanAttributeInfo ai: attributeInfos) {

                //
                // TODO this is a hack, it will only work for attributes; we need another accessor that
                // exposes more information about the children
                //

                result.add(ai.getName() + " (" + ai.getType() + ")");

            }

            return result;
        }
        catch(Exception e) {

            //
            // this is abnormal, we could not have built the instance is we did not exist
            //

            throw new IllegalStateException("internal failure", e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public ObjectName getObjectName() {

        return objectName;
    }

    /**
     * @return the value of the JMX attribute whose name was specified (which may be null).
     *
     * @exception AttributeNotFoundException if the MBean does not have such an attribute.
     */
    public Object get(String jmxAttributeName) throws IOException, AttributeNotFoundException {

        MBeanServerConnection c = getTree().getMBeanServerConnection();

        try {

            return c.getAttribute(objectName, jmxAttributeName);
        }
        catch (ReflectionException | MBeanException | InstanceNotFoundException e) {

            //
            // not expected here
            //
            throw new IllegalStateException(e);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxNode getRelativeNode(String relativeLocation) throws IOException, UserErrorException {

        //
        // the relative can only be an attribute, for the time being
        //

        List<String> attributeNames = getChildrenNames();

        for(String an: attributeNames) {

            //
            // TODO: currently we hackishly put the type in the attribute name, this needs to be fixed
            //

            if (an.startsWith(relativeLocation + " (")) {

                return new JmxAttribute(relativeLocation, this);
            }
        }

        throw new UserErrorException(relativeLocation + ": no such attribute");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
