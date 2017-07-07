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

import java.io.IOException;
import java.util.List;

/**
 * A JMX node that may have children: the JMX root, JMX domains, JMX MBeans, etc.
 *
 * The container node does not cache data, except its name and its parent, but it retrieves it from the MBean server
 * every time it is queried.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public abstract class JmxContainer extends JmxNodeBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    protected JmxContainer(String name, JmxContainer parent) {

        super(name, parent);
    }

    // JmxNode overrides -----------------------------------------------------------------------------------------------

    @Override
    public boolean isContainer() {

        return  true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the JMX node situated in the given relative position to the current node. A valid node is returned
     * or an UserErrorException is thrown.
     *
     * Only container nodes can have "relatives" because we can never set a non-container as a current node.
     *
     * The method is not supposed to cache data locally, but exercise the underlying MBean server connection every time
     * it is invoked.
     *
     * This method handles common cases and sub-class specific situation are delegated to getRelativeNode().
     *
     * @exception io.novaordis.utilities.UserErrorException in case the relative location does not map on a valid node.
     * @exception java.io.IOException on communication failure.
     *
     * @see JmxContainer#getRelativeNode(String)
     */
    public final JmxNode getRelative(String relativeLocation) throws IOException, UserErrorException {

        //
        // handle standard situation and message the subclass only if not in one of these standard situations.
        //

        if ("..".equals(relativeLocation)) {

            //
            // we go to parent, unless we're root, in which case we stay at root
            //

            JmxNode parent = getParent();

            if (parent == null) {

                return this;
            }

            return parent;
        }
        else if ("/".equals(relativeLocation)) {

            //
            // go to root
            //

            JmxNode root = getParent();

            if (root == null) {

                return this;
            }

            while(root.getParent() != null) {

                root = root.getParent();
            }

            return root;
        }

        return getRelativeNode(relativeLocation);
    }

    /**
     * Return the names of its children, if any. If the node has not children, and empty list should be returned.
     *
     * The method is not supposed to cache data locally, but exercise the underlying MBean server connection every time
     * it is invoked.
     */
    public abstract List<String> getChildrenNames() throws IOException;

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * A method to be implemented by subclasses. The entry point is getRelative(), which handles common cases and
     * it was made final.
     *
     * @return the JMX node situated in the given relative position to the current node. A valid node is returned
     * or an UserErrorException is thrown.
     *
     * Only container nodes can have "relatives" because we can never set a non-container as a current node.
     *
     * The method is not supposed to cache data locally, but exercise the underlying MBean server connection every time
     * it is invoked.
     *
     * @exception io.novaordis.utilities.UserErrorException in case the relative location does not map on a valid node.
     * @exception java.io.IOException on communication failure.
     *
     * @see JmxContainer#getRelative(String)
     */
    protected abstract JmxNode getRelativeNode(String relativeLocation) throws IOException, UserErrorException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
