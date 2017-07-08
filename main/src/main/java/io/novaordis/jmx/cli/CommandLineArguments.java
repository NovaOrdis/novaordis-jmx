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

import io.novaordis.utilities.UserErrorException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/7/17
 */
public class CommandLineArguments {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String uri;
    private String usernameFromOption;
    private char[] passwordFromOption;

    // Constructors ----------------------------------------------------------------------------------------------------

    public CommandLineArguments(String[] args) throws UserErrorException {

        if (args.length == 0) {

            return;
        }

        for(int i = 0; i < args.length; i ++) {

            String a = args[i];

            if (a.startsWith("--username")) {

                if (a.length() == "--username".length() || a.charAt("--username".length()) != '=') {

                    throw new UserErrorException("invalid --username syntax, use --username=<username>");
                }

                this.usernameFromOption = a.substring("--username=".length()).trim();

                if (this.usernameFromOption.isEmpty()) {

                    throw new UserErrorException("missing username");
                }
            }
            else if (a.startsWith("--password")) {

                if (a.length() == "--password".length() || a.charAt("--password".length()) != '=') {

                    throw new UserErrorException("invalid --password syntax, use --password=<password>");
                }

                String s = a.substring("--password=".length()).trim();

                if (s.isEmpty()) {

                    throw new UserErrorException("missing password");
                }

                this.passwordFromOption = s.toCharArray();
            }
            else if (uri == null) {

                uri = a;
            }
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the URI as string. May return null if no arguments were provided.
     */
    public String getUri() {

        return uri;
    }

    /**
     * May return null if no username was set with --username=
     */
    public String getUsernameFromOption() {

        return usernameFromOption;
    }

    /**
     * May return null if no username was set with --password=
     */
    public char[] getPasswordFromOption() {

        return passwordFromOption;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
