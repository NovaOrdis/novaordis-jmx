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

package io.novaordis.jmx.tree;

import io.novaordis.jmx.mockpackage.mockprotocol.MockMBeanServerConnection;
import io.novaordis.jmx.tree.nodes.JmxRoot;
import io.novaordis.utilities.UserErrorException;
import org.junit.Test;

import javax.management.MBeanServerConnection;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public abstract class JmxTreeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        JmxTree t = getJmxTreeToTest(mc);

        assertEquals(mc, t.getMBeanServerConnection());

        JmxRoot r = (JmxRoot)t.getCurrent();
        assertEquals("/", r.getName());
        assertNull(r.getParent());

        List<String> cns = r.getChildrenNames();
        assertTrue(cns.isEmpty());
    }

    @Test
    public void getCurrent() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        mc.setDomains("mock-domain1", "mock-domain2");

        JmxTree t = getJmxTreeToTest(mc);

        JmxRoot r = (JmxRoot)t.getCurrent();

        assertEquals("/", r.getName());

        assertNull(r.getParent());

        List<String> cns = r.getChildrenNames();
        assertEquals(2, cns.size());
        assertTrue(cns.contains("mock-domain1"));
        assertTrue(cns.contains("mock-domain2"));
    }

    @Test
    public void setCurrent_NoSuchLocation() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        mc.setDomains("mock-domain1", "mock-domain2");

        JmxTree t = getJmxTreeToTest(mc);

        JmxRoot r = (JmxRoot)t.getCurrent();
        assertNotNull(r);

        try {

            t.setCurrent("does-not-exist");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertEquals("does-not-exist: no such location", msg);
        }
    }

    @Test
    public void setCurrent() throws Exception {

        MockMBeanServerConnection mc = new MockMBeanServerConnection(true);

        mc.setDomains("mock-domain1", "mock-domain2");

        JmxTree t = getJmxTreeToTest(mc);

        JmxRoot r = (JmxRoot)t.getCurrent();
        assertNotNull(r);

        t.setCurrent("mock-domain1");

        //JmxDomain d = (JmxDomain)t.getCurrent();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JmxTree getJmxTreeToTest(MBeanServerConnection c) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
