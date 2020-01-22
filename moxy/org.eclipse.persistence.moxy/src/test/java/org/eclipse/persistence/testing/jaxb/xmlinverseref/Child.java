/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Denise Smith, February 2013
package org.eclipse.persistence.testing.jaxb.xmlinverseref;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Child extends Person {
   public int age;

   public boolean equals(Object obj){
       if(obj instanceof Child){
           if(age != ((Child)obj).age){
               return false;
           }
           return super.equals(obj);
       }
       return false;
   }

}