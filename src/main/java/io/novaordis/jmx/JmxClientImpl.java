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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class JmxClientImpl implements JmxClient {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxClientImpl(JmxAddress address) {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // JmxClient implementation ----------------------------------------------------------------------------------------

    @Override
    public JmxAddress getAddress() {
        throw new RuntimeException("getAddress() NOT YET IMPLEMENTED");
    }

    @Override
    public void connect() throws JmxException {
        throw new RuntimeException("connect() NOT YET IMPLEMENTED");
    }

    @Override
    public void disconnect() {
        throw new RuntimeException("disconnect() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isConnected() {
        throw new RuntimeException("isConnected() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
