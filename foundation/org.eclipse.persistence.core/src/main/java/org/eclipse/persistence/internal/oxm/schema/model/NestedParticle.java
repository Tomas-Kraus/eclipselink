/*
 * Copyright (c) 1998, 2021 Oracle and/or its affiliates. All rights reserved.
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
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.internal.oxm.schema.model;

import java.util.List;

/**
 * <p><b>Purpose</b>: Interface to represent a Choice or Sequence
 * @see org.eclipse.persistence.internal.oxm.schema.model.Sequence
 * @see org.eclipse.persistence.internal.oxm.schema.model.Choice
 */
public interface NestedParticle extends TypeDefParticleOwner {
    void addSequence(Sequence sequence);

    void addChoice(Choice choice);

    void addElement(Element elem);

    void addAny(Any any);

    boolean hasAny();

    void setSequences(List<Sequence> sequences);

    void setChoices(List<Choice> choices);

    void setAnys(List<Any> anys);

    void setElements(List<Element> elements);

    void setMinOccurs(String minOccurs);

    String getMinOccurs();

    String getMaxOccurs();

    void setMaxOccurs(String maxOccurs);

    void setOwner(TypeDefParticleOwner owner);

    boolean isEmpty();
}
