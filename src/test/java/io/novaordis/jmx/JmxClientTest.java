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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public abstract class JmxClientTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // connect() -------------------------------------------------------------------------------------------------------

    @Test
    public void connect_AddressNotSet() throws Exception {

        JmxClient c = getJmxClientToTest();

        assertNull(c.getAddress());

        try {

            c.connect();
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("improperly configured"));
            assertTrue(msg.matches("not.*address"));
        }
    }

    // lifecycle() -----------------------------------------------------------------------------------------------------

    @Test
    public void lifecycle() throws Exception {

        JmxClient c = getJmxClientToTest();

        assertFalse(c.isConnected());

        c.connect();

        assertTrue(c.isConnected());

        //
        // idempotence
        //

        c.connect();

        assertTrue(c.isConnected());

        c.disconnect();

        assertFalse(c.isConnected());

        //
        // idempotence
        //

        c.disconnect();

        assertFalse(c.isConnected());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JmxClient getJmxClientToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
