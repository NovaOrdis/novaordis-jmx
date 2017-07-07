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

import io.novaordis.jmx.tree.JmxTree;

/**
 * A location in the JMX tree.
 *
 * A node does not cache data locally, except its name and its parent.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public interface JmxNode {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The JMX tree we belong to.
     */
    JmxTree getTree();

    /**
     * The node name. It can be "/" if the node is the root of the JMX tree, a domain, an MBeanName, an attribute name,
     * etc.
     */
    String getName();

    /**
     * May return null if this node is the root.
     */
    JmxContainer getParent();

    /**
     * Whether this node is a container or not. If the method returns true, the instance can be safely cast to
     * JmxContainer.

     * @see JmxContainer
     */
    boolean isContainer();



}
