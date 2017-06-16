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

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxAddress() {

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
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public JmxAddress copy() {

        return (JmxAddress)super.copy();
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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxAddress createBlankInstance() {

        return new JmxAddress();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
