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

package io.novaordis.jmx.mockpackage.mockprotocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

/**
 * Instantiated via reflection.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
@SuppressWarnings("unused")
public class ClientProvider implements JMXConnectorProvider {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ClientProvider.class);

    // Static ----------------------------------------------------------------------------------------------------------

    private static boolean remoteJmxServerNotAvailable;
    private static boolean remoteJmxFailsToProduceAMBeanServerConnection;

    public static void setRemoteJmxServerNotAvailable(boolean b) {

        remoteJmxServerNotAvailable = b;
    }

    public static boolean isRemoteJmxServerNotAvailable() {

        return remoteJmxServerNotAvailable;
    }

    public static void setRemoteJmxFailsToProduceAMBeanServerConnection(boolean b) {

        remoteJmxFailsToProduceAMBeanServerConnection = b;
    }

    public static boolean isRemoteJmxFailsToProduceAMBeanServerConnection() {

        return remoteJmxFailsToProduceAMBeanServerConnection;
    }

    /**
     * Resets this provider to a state in which creates valid JMX connectors.
     */
    public static void clear() {

        remoteJmxServerNotAvailable = false;
        remoteJmxFailsToProduceAMBeanServerConnection = false;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // JMXConnectorProvider implementation -----------------------------------------------------------------------------

    @Override
    public JMXConnector newJMXConnector(JMXServiceURL serviceURL, Map<String, ?> environment) throws IOException {

        log.info(this + " creating a new JMXConnector instance ...");

        if (isRemoteJmxServerNotAvailable()) {

            log.info("simulating a remote JMX server that is not available");
            throw new ConnectException("Connection refused");
        }

        //noinspection UnnecessaryLocalVariable
        MockJMXConnector c = new MockJMXConnector(serviceURL, environment);

        log.info(this + " created " + c);

        return c;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "Mock Client Provider[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
