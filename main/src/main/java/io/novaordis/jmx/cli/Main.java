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

package io.novaordis.jmx.cli;

import io.novaordis.jmx.JmxAddress;
import io.novaordis.jmx.JmxClient;
import io.novaordis.jmx.JmxClientImpl;
import io.novaordis.jmx.tree.JmxTree;
import io.novaordis.jmx.tree.JmxTreeImpl;
import io.novaordis.utilities.UserErrorException;

import javax.management.MBeanServerConnection;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/17/17
 */
public class Main {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {

        try {

            CommandLineArguments cla = new CommandLineArguments(args);

            String uri = cla.getUri();

            if (uri == null) {

                CLInt.displayHelp();
                return;
            }

            JmxAddress address = new JmxAddress(uri);

            reconcileCredentials(address, cla.getUsernameFromOption(), cla.getPasswordFromOption());

            address.setJmxServiceUrlProtocol(JmxAddress.EAP6_JMX_SERVICE_URL_PROTOCOL);

            JmxClient client = new JmxClientImpl(address);

            client.connect();

            MBeanServerConnection mBeanServerConnection = client.getMBeanServerConnection();

            JmxTree jmxTree = new JmxTreeImpl(mBeanServerConnection);

            CLInt cli = new CLInt(jmxTree);

            cli.loop();

            client.disconnect();
        }
        catch(UserErrorException e) {

            System.err.println("[error]: " + e.getMessage());
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    static void reconcileCredentials(JmxAddress a, String usernameFromOption, char[] passwordFromOption)
            throws UserErrorException {

        if (usernameFromOption == null) {

            return;
        }

        String uriUsername = a.getUsername();

        if (uriUsername == null) {

            a.setUsername(usernameFromOption);

            if (passwordFromOption != null) {

                a.setPassword(passwordFromOption);
            }
        }
        else if (!uriUsername.equals(usernameFromOption)) {

            throw new UserErrorException(
                    "the username specified with --username and the one specified in the URI do not match");
        }

        //
        // user name are similar, make sure the passwords match
        //

        char[] passwordFromUri = a.getPassword();

        if (passwordFromUri != null) {

            if (passwordFromOption != null) {

                if (!new String (passwordFromUri).equals(new String(passwordFromOption))) {

                    throw new UserErrorException(
                            "the password specified with --password and the one specified in the URI do not match");

                }
            }
        }
        else {

            //
            // no password in URI, but maybe we have a password in option
            //

            a.setPassword(passwordFromOption);
        }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
