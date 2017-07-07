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
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public abstract class JmxNodeBase implements JmxNode {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JmxContainer parent;
    private String name;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected JmxNodeBase(String name, JmxContainer parent) {

        this.name = name;
        this.parent = parent;
    }

    // JmxNode implementation ------------------------------------------------------------------------------------------

    @Override
    public JmxTree getTree() {

        while(parent.getParent() != null) {

            parent = parent.getParent();
        }

        return parent.getTree();
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public JmxContainer getParent() {

        return parent;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return name == null ? "UNINITIALIZED" : name;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
