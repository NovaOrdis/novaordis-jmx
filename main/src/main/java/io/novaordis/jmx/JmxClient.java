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
 * @since 6/16/17
 */
public interface JmxClient {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the address of the corresponding remote JMX bus.
     */
    JmxAddress getAddress();

    /**
     * Configure the JmxClient to look up for the following ClientProvider class:
     *
     * <protocol-provider-package> + "." + <protocol-name> + "." + ClientProvider
     *
     * Some implementations, such a JBoss 6 remote JMX support, do not need this.
     */
    void setProtocolProviderPackage(String protocolProviderPackage);

    // Lifecycle -------------------------------------------------------------------------------------------------------

    /**
     * Connects the client the corresponding remote JMX bus.
     *
     * The client instance must be configured with a JmxAddress before attempting to connect it, either via a
     * constructor or a mutator method. If the client is improperly configured, invocation will throw
     * IllegalStateException.
     *
     * @exception IllegalStateException if the client is improperly configured (missing Address, for example) when
     * the method is invoked.
     *
     * @exception JmxException on any other exceptional condition.
     */
    void connect() throws JmxException;

    /**
     * Disconnects the client, and releases resources.
     */
    void disconnect();

    boolean isConnected();

    // Business --------------------------------------------------------------------------------------------------------

    /**
     *
     * @throws IllegalStateException if invoked on an unconnected client.
     */
    MBeanServerConnection getMBeanServerConnection() throws JmxException;

}
