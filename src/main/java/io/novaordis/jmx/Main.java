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

package io.novaordis.jmx;

import javax.management.MBeanServerConnection;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/17/17
 */
public class Main {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {

            throw new Exception("the address of the JMX server is needed in format jmx://<host>:<port>");
        }

        String addressAsString = args[0];

        JmxAddress address = new JmxAddress(addressAsString);

        address.setJmxServiceUrlProtocol("remoting-jmx");

        JmxClient client = new JmxClientImpl(address);

        client.connect();

        MBeanServerConnection mBeanServerConnection = client.getMBeanServerConnection();

        int count = mBeanServerConnection.getMBeanCount();

        System.out.println(count);

        client.disconnect();
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
