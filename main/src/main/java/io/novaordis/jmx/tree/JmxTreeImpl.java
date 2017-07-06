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

import io.novaordis.jmx.JmxAddress;

import javax.management.MBeanServerConnection;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/6/17
 */
public class JmxTreeImpl implements JmxTree {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    public JmxTreeImpl(MBeanServerConnection c) {
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

    //        int count = mBeanServerConnection.getMBeanCount();
//
//        System.out.println(count);
//
//        ObjectName on = new ObjectName("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ");
//
//        Object o = mBeanServerConnection.getAttribute(on, "messageCount");
//
//        System.out.println(o);
//
//        ObjectName on = new ObjectName("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ");
//
//        AttributeList al = mBeanServerConnection.getAttributes(on, new String[]{"messageCount", "nosuchatt"});
//
//        for(Attribute a: al.asList()) {
//
//            System.out.println(a.getName() + ": " + a.getValue());
//        }



}
