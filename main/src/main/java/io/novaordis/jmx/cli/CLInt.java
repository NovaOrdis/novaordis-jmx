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

import io.novaordis.jmx.tree.nodes.JmxContainer;
import io.novaordis.jmx.tree.nodes.JmxNode;
import io.novaordis.jmx.tree.JmxTree;
import io.novaordis.utilities.UserErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class CLInt {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(CLInt.class);

    // Static ----------------------------------------------------------------------------------------------------------

    public static void printHelp() {

        System.out.println("> ls cd");
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private JmxTree jmxTree;

    // Constructors ----------------------------------------------------------------------------------------------------

    public CLInt(JmxTree jmxTree) {

        this.jmxTree = jmxTree;
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

                System.err.println("> " + e.getMessage());
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

        line = line.trim();

        //
        // if the line is empty, just loop
        //
        if (!line.isEmpty()) {

            try {

                if ("exit".equalsIgnoreCase(line)) {

                    return false;
                }
                else if ("help".equalsIgnoreCase(line)) {

                    printHelp();
                }
                else if ("ls".equals(line) || line.startsWith("ls ")) {

                    String args = line.substring("ls".length()).trim();
                    String s = ls(args);
                    System.out.println(s);
                }
                else if ("pwd".equalsIgnoreCase(line)) {

                    String s = pwd();
                    System.out.println(s);
                }
                else if ("cd".equals(line) || line.startsWith("cd ")) {

                    String args = line.substring("cd".length()).trim();
                    cd(args);
                }
                else {

                    throw new UserErrorException("unknown command '" + line + "'");
                }
            }
            catch(IOException e) {

                String msg = "failed to query the MBeanServer";

                if (e.getMessage() != null) {

                    msg = msg + ": " + e.getMessage();
                }

                throw new UserErrorException(msg);
            }
        }

        return true;
    }

    /**
     * List the current node of the JMX tree.
     *
     * @param args command line options/arguments.
     */
    String ls(String args) throws IOException, UserErrorException {

        String result = "";

        args = args.trim();

        if (!args.isEmpty()) {

            throw new UserErrorException("unknown option(s): " + args);
        }

        JmxNode n = jmxTree.getCurrent();

        if (!n.isContainer()) {

            //
            // we are not supposed to be in this situation, the navigation logic failed somehow
            //

            throw new IllegalStateException("cannot be in a non-container node, let alone list it from inside");
        }

        JmxContainer c = (JmxContainer)n;
        List<String> cns = c.getChildrenNames();

        //
        // sort alphanumerically
        //

        Collections.sort(cns);

        for(Iterator<String> i = cns.iterator(); i.hasNext(); ) {

            result += i.next();

            if (i.hasNext()) {

                result += "\n";
            }
        }

        return result;
    }

    /**
     * @return the current position in tree, which is the name of the current node.
     */
    String pwd() throws IOException {

        JmxNode n = jmxTree.getCurrent();
        return n.getName();
    }

    /**
     * Resets the current position in the tree.
     *
     * @param args command line options/arguments.
     */
    void cd(String args) throws IOException, UserErrorException {

        String newLocation = args.trim();

        try {

            jmxTree.setCurrent(newLocation);
        }
        catch(UserErrorException e) {

            throw new UserErrorException("cd: " + e.getMessage());
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}