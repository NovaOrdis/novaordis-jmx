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

import io.novaordis.jmx.tree.JmxTree;
import io.novaordis.utilities.UserErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class CLInt {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(CLInt.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public CLInt(JmxTree jmxTree) {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Reads commands from stdin on the thread that invokes it and exits when an explicit exit command was issued.
     *
     * Not supposed to throw exceptions.
     */
    public void loop() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {

            try {

                System.out.print("> ");

                String line = br.readLine();

                boolean keepLooping = processLine(line);

                if (!keepLooping) {

                    break;
                }
            }
            catch(UserErrorException e) {

                System.err.print("> " + e.getMessage());
            }
            catch(Throwable t) {

                log.warn("internal error", t);
            }
        }

        try {

            br.close();
        }
        catch(Exception e) {

            log.warn("failed to close the input stream", e);

        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @return true if the loop is supposed to keep processing input, false if the input just processed means we must
     *          exit.
     * @throws UserErrorException
     */
    boolean processLine(String line) throws UserErrorException {

        System.out.println(line);

        return true;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
