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

import io.novaordis.jmx.tree.nodes.JmxContainer;
import io.novaordis.jmx.tree.nodes.JmxNode;
import io.novaordis.jmx.tree.nodes.JmxRoot;
import io.novaordis.utilities.UserErrorException;

import javax.management.MBeanServerConnection;
import java.io.IOException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxTreeImpl implements JmxTree {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private MBeanServerConnection c;

    private JmxNode current;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    public JmxTreeImpl(MBeanServerConnection c) {

        this.c = c;
        current = new JmxRoot(this);
    }

    @Override
    public JmxNode getCurrent() throws IOException {

        return current;
    }

    @Override
    public void setCurrent(String location) throws IOException, UserErrorException {

        JmxNode c = getCurrent();

        if (!(c instanceof JmxContainer)) {

            throw new IllegalStateException("the current node cannot be a non-container");

        }

        JmxContainer cnt = (JmxContainer)c;

        JmxNode target = cnt.getRelative(location);

        if (!target.isContainer()) {

            throw new UserErrorException(target.getName() + ": not a location to be navigating into");
        }

        this.current = target;
    }

    @Override
    public MBeanServerConnection getMBeanServerConnection() {

        return c;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------


}
