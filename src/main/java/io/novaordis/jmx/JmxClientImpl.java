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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class JmxClientImpl implements JmxClient {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JmxClientImpl.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JmxAddress address;
    private JMXServiceURL jmxServiceURL;
    private volatile JMXConnector jmxConnector;

    //
    // needed when testing, JBoss JMX support works without it.
    //
    private String protocolProviderPackage;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @throws IllegalArgumentException on invalid address (null host, port, malformed address etc.)
     */
    public JmxClientImpl(JmxAddress address) throws IllegalArgumentException {

        this.address = address;

        if (address == null) {

            //
            // allows for creation of partially initialized instances
            //

            log.debug("partially initialized JmxClientImpl instance created");

            return;
        }

        try {

            String jmxServiceUrlProtocol = address.getJmxServiceUrlProtocol();

            String host = address.getHost();

            if (host == null) {

                throw new IllegalArgumentException("null host in JMX address " + address);
            }

            Integer port = address.getPort();

            if (port == null) {

                throw new IllegalArgumentException("null port in JMX address " + address);
            }

            this.jmxServiceURL = new JMXServiceURL(jmxServiceUrlProtocol, address.getHost(), address.getPort());
        }
        catch(MalformedURLException e) {

            throw new IllegalArgumentException(e);
        }
    }

    // JmxClient implementation ----------------------------------------------------------------------------------------

    @Override
    public JmxAddress getAddress() {

        return address;
    }

    @Override
    public void setProtocolProviderPackage(String s) {

        this.protocolProviderPackage = s;
    }

    @Override
    public synchronized void connect() throws JmxException {

        if (jmxConnector != null) {

            log.debug(this + " is already connected");
            return;
        }

        if (address == null) {

            throw new IllegalStateException(this + " was improperly initialized: address not set");
        }

        Map<String, String> environment = null;

        if (protocolProviderPackage != null) {

            environment = new HashMap<>();
            environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, protocolProviderPackage);
            log.debug("using protocol provider package " + protocolProviderPackage);
        }

        log.debug("the jmx client instance attempting to connect to " + jmxServiceURL);

        try {

            if (environment == null) {

                this.jmxConnector = JMXConnectorFactory.connect(jmxServiceURL);
            }
            else {

                this.jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, environment);
            }
        }
        catch(Exception e) {

            //
            // in case a JBoss 6 JMX server is not up, we get java.net.ConnectException "Connection refused" here.
            //

            throw new JmxException("failed to create the underlying JMX remoting connector", e);
        }
    }

    @Override
    public synchronized void disconnect() {

        if (jmxConnector == null) {

            log.debug(this + " is already disconnected");
            return;
        }

        try {

            jmxConnector.close();
            jmxConnector = null;
        }
        catch(Exception e) {

            log.warn("failed to close connector", e);
        }
    }

    @Override
    public synchronized boolean isConnected() {

        return jmxConnector != null;
    }

    @Override
    public MBeanServerConnection getMBeanServerConnection() throws JmxException {

        if (!isConnected()) {

            throw new IllegalStateException(this + " not connected to server");
        }

        try {

            return jmxConnector.getMBeanServerConnection();
        }
        catch(Exception e) {

            throw new JmxException("failed to get the MBeanServerConnection", e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        if (jmxServiceURL != null) {

            return jmxServiceURL.toString();
        }

        if (address != null) {

            return address.toString();
        }

        return "UNINITIALIZED";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
