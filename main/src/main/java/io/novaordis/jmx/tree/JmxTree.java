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

import io.novaordis.jmx.tree.nodes.JmxNode;
import io.novaordis.utilities.UserErrorException;

import javax.management.MBeanServerConnection;
import java.io.IOException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public interface JmxTree {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the current node of the tree. If no set() method was used on the tree, the current node is the tree's
     * root
     *
     * @exception IOException if communication with the underlying MBean server fails in some way.
     */
    JmxNode getCurrent() throws IOException;

    /**
     * Sets the new location relative to the current one.
     *
     * @exception IOException if communication with the underlying MBean server fails in some way.
     *
     * @exception UserErrorException if the operation cannot be performed because it does not make sense.
     */
    void setCurrent(String location) throws IOException, UserErrorException;

    MBeanServerConnection getMBeanServerConnection();

}
