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
import io.novaordis.utilities.UserErrorException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxRoot extends JmxContainer {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JmxTree tree;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxRoot(JmxTree tree) {

        super("/", null);

        if (tree == null) {

            throw new IllegalArgumentException("null tree");
        }

        this.tree = tree;
    }

    // JmxNode overrides -----------------------------------------------------------------------------------------------

    @Override
    public JmxTree getTree() {

        return tree;
    }

    @Override
    public List<String> getChildrenNames() throws IOException {

        String[] domains = tree.getMBeanServerConnection().getDomains();
        return Arrays.asList(domains);
    }

    @Override
    public JmxNode getRelative(String relativeLocation) throws IOException, UserErrorException {

        if (relativeLocation.startsWith("..")) {

            throw new RuntimeException("up location support NOT YET IMPLEMENTED");
        }

        if (relativeLocation.contains(":")) {

            throw new RuntimeException("compound relative locations NOT YET IMPLEMENTED");
        }

        String[] domains = tree.getMBeanServerConnection().getDomains();

        for(String s: domains) {

            if (s.equals(relativeLocation)) {

                return new JmxDomain(s, this);
            }
        }

        //
        // we did not find the domain
        //

        throw new UserErrorException(relativeLocation + ": no such location");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
