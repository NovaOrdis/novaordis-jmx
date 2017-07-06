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

import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.AddressImpl;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class JmxAddress extends AddressImpl {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PROTOCOL = "jmx";

    public static final String EAP6_JMX_SERVICE_URL_PROTOCOL = "remoting-jmx";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // the string to send to the lower Java JMX remoting API as "protocol".

    private String jmxServiceUrlProtocol;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * There is no public no-argument constructor because there are no default coordinates for a JMX endpoint. In order
     * to get a valid JMX address, we will need to specify at least a host and a port. This constructor returns a
     * partially initialized instance.
     */
    protected JmxAddress() {

        super();

        setProtocol(PROTOCOL);
    }

    /**
     * @throws AddressException
     * @throws IllegalArgumentException>
     */
    public JmxAddress(String s) throws AddressException {

        super(s);

        String protocol = getProtocol();

        if (protocol == null) {

            setProtocol(PROTOCOL);
        }

        if (getPort() == null) {

            throw new AddressException("missing port value");
        }
    }

    public JmxAddress(String username, String password, String host, Integer port) throws AddressException {

        super(PROTOCOL, username, password, host, port);

        if (getPort() == null) {

            throw new AddressException("missing port value");
        }
    }

    /**
     * Used for testing.
     */
    JmxAddress(String protocol, String host, Integer port) throws AddressException {

        super(protocol, null, null, host, port);
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public JmxAddress copy() {

        JmxAddress copy = (JmxAddress)super.copy();
        copy.setJmxServiceUrlProtocol(jmxServiceUrlProtocol);
        return copy;
    }

    /**
     * Extra semantic protection.
     */
    @Override
    public void setProtocol(String s) {

        if (!PROTOCOL.equals(s)) {

            throw new IllegalArgumentException("invalid protocol " + (s == null ? "null" : "\"" + s + "\""));
        }

        super.setProtocol(s);
    }

    /**
     * We want the superclass behavior, a JmxAddress with a content identical to an AddressImpl is equals to the
     * AddressImpl instance. This is because we want to support the intuitive behavior of creating new
     * AddressImpl("jmx://...") and using it to search in sets and maps.
     */
    @Override
    public boolean equals(Object o) {

        return super.equals(o);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The "protocol" part from a JMXServiceURL address "service:jmx:<protocol>://<host>:<port>" corresponding to this
     * JMX address:
     * <ul>
     *     <li>"remoting-jmx" for JBoss 6</li>
     *     <li>"mock-jmx" for testing</li>
     * </ul>
     *
     * @see JmxAddress#EAP6_JMX_SERVICE_URL_PROTOCOL
     */
    public String getJmxServiceUrlProtocol() {

        return jmxServiceUrlProtocol;
    }

    public void setJmxServiceUrlProtocol(String s) {

        this.jmxServiceUrlProtocol = s;
    }

    @Override
    public String toString() {

        String s = super.toString();

        String jp = getJmxServiceUrlProtocol();

        if (jp != null) {

            s += ", jmxServiceUrlProtocol=" + jp;
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxAddress createBlankInstance() {

        return new JmxAddress();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
