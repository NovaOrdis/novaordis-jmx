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

import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class MockJMXConnector implements JMXConnector {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockJMXConnector.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JMXServiceURL serviceUrl;

    private boolean connected;

    private MockMBeanServerConnection mockMBeanServerProxy;

    private Map<String, ?> environmentCopyInConstructor;
    private Map<String, ?> environmentCopyInConnect;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockJMXConnector(JMXServiceURL serviceURL, Map<String, ?> environment) {

        this.serviceUrl = serviceURL;

        //
        // make a shallow copy of the environment
        //
        environmentCopyInConstructor = new HashMap<>(environment);
    }

    // JMXConnector implementation -------------------------------------------------------------------------------------

    @Override
    public void connect() throws IOException {

        throw new RuntimeException("connect() NOT YET IMPLEMENTED");
    }

    @Override
    public void connect(Map<String, ?> env) throws IOException {

        log.info(this + " connected to the mock remote JMX server");

        mockMBeanServerProxy = new MockMBeanServerConnection();

        connected = true;

        //
        // make a shallow copy of the environment
        //
        environmentCopyInConnect = new HashMap<>(env);
    }

    @Override
    public MBeanServerConnection getMBeanServerConnection() throws IOException {

        if (!connected) {

            throw new IllegalStateException(this + " was not connected to the remote server");
        }

        if (ClientProvider.isRemoteJmxFailsToProduceAMBeanServerConnection()) {

            throw new IOException("the remote server failed to provide a MBeanServerConnection");
        }

        return mockMBeanServerProxy;
    }

    @Override
    public MBeanServerConnection getMBeanServerConnection(Subject delegationSubject) throws IOException {

        throw new RuntimeException("getMBeanServerConnection() NOT YET IMPLEMENTED");
    }

    @Override
    public void close() throws IOException {

        log.info(this + " disconnected from the mock remote JMX server");

        connected = false;

        mockMBeanServerProxy = null;
    }

    @Override
    public void addConnectionNotificationListener(
            NotificationListener listener, NotificationFilter filter, Object handback) {

        throw new RuntimeException("addConnectionNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void removeConnectionNotificationListener(NotificationListener listener) throws ListenerNotFoundException {

        throw new RuntimeException("removeConnectionNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void removeConnectionNotificationListener(
            NotificationListener l, NotificationFilter f, Object handback) throws ListenerNotFoundException {

        throw new RuntimeException("removeConnectionNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public String getConnectionId() throws IOException {

        throw new RuntimeException("getConnectionId() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "MockJMXConnector[" + serviceUrl + "]";
    }

    public Map<String, ?> getEnvironmentCopyInConstructor() {

        return environmentCopyInConstructor;
    }

    public Map<String, ?> getEnvironmentCopyInConnect() {

        return environmentCopyInConnect;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
