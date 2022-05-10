/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
//     02/01/2022: Tomas Kraus
//       - Issue 1442: Implement New JPA API 3.1.0 Features
package org.eclipse.persistence.jpa.test.jpql;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.eclipse.persistence.jpa.test.criteria.model.DateTimeEntity;
import org.eclipse.persistence.jpa.test.framework.DDLGen;
import org.eclipse.persistence.jpa.test.framework.Emf;
import org.eclipse.persistence.jpa.test.framework.EmfRunner;
import org.eclipse.persistence.jpa.test.framework.Property;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EmfRunner.class)
public class TestDateTimeFunctions {

    @Emf(createTables = DDLGen.DROP_CREATE,
            classes = {
                    DateTimeEntity.class
            },
            properties = {
                    @Property(name = "eclipselink.cache.shared.default", value = "false"),
                    // This property remaps String from VARCHAR->NVARCHAR(or db equivalent)
                    @Property(name = "eclipselink.target-database-properties",
                            value = "UseNationalCharacterVaryingTypeForString=true"),
            })
    private EntityManagerFactory emf;

    private final LocalDateTime[] TS = {
            LocalDateTime.of(1970, 1, 1, 1, 11, 11),
            LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0)
    };

    private final DateTimeEntity[] ENTITY = {
            new DateTimeEntity(1, TS[0].toLocalTime(), TS[0].toLocalDate(), TS[0]), // reserved for testCriteriaUpdateLocalTime
            new DateTimeEntity(2, TS[0].toLocalTime(), TS[0].toLocalDate(), TS[0]), // reserved for testCriteriaUpdateLocalDate
            new DateTimeEntity(3, TS[0].toLocalTime(), TS[0].toLocalDate(), TS[0]), // reserved for testCriteriaUpdateLocalDateTime
            new DateTimeEntity(4, TS[1].toLocalTime(), TS[1].toLocalDate(), TS[1])  // reserved for testCriteriaQueryWhereLocalTime
            //              testCriteriaQueryWhereLocalTimeReturnsEmpty
            //              testCriteriaQueryWhereLocalDate
            //              testCriteriaQueryWhereLocalDateReturnsEmpty
            //              testCriteriaQueryWhereLocalDateTime
            //              testCriteriaQueryWhereLocalDateTimeReturnsEmpty
            //              testCriteriaQuerySelectLocalTime
            //              testCriteriaQuerySelectLocalDate
            //              testCriteriaQuerySelectLocalDateTime
    };

    // Database vs. Java timezone offset in seconds. Must be applied to LocalDateTime calculations.
    private long dbOffset = 0;

    // Update database vs. Java timezone offset using current database time.
    private void updateDbOffset() {
        final EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<LocalTime> cq = cb.createQuery(LocalTime.class);
            cq.select(cb.localTime());
            Root<DateTimeEntity> entity = cq.from(DateTimeEntity.class);
            cq.where(cb.equal(entity.get("id"), 1));
            LocalTime dbTime = em.createQuery(cq).getSingleResult();
            LocalTime javaTime = LocalTime.now();
            this.dbOffset = dbTime.truncatedTo(ChronoUnit.SECONDS).toSecondOfDay() - javaTime.truncatedTo(ChronoUnit.SECONDS).toSecondOfDay();
        } catch (Throwable t) {
            AbstractSessionLog.getLog().log(SessionLog.WARNING, "Can't update DB offset: " + t.getMessage());
            t.printStackTrace();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Before
    public void setup() {
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (DateTimeEntity e : ENTITY) {
                em.persist(e);
            }
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
        updateDbOffset();
    }

    @After
    public void cleanup() {
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM DateTimeEntity e").executeUpdate();
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    // Test JPQL with localTime in WHERE condition.
    @Test
    public void testCriteriaQueryWhereLocalTime() {
        final EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT e.id FROM DateTimeEntity e WHERE e.time < LOCAL_TIME AND e.id = :id", Integer.class);
            query.setParameter("id", 4);
            em.getTransaction().begin();
            query.getSingleResult();
            em.getTransaction().commit();
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    // Test JPQL with localDate in WHERE condition.
    @Test
    public void testCriteriaQueryWhereLocalDate() {
        final EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT e.id FROM DateTimeEntity e WHERE e.date < LOCAL_DATE AND e.id = :id", Integer.class);
            query.setParameter("id", 4);
            em.getTransaction().begin();
            query.getSingleResult();
            em.getTransaction().commit();
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    // Test JPQL with localDateTime in WHERE condition.
    @Test
    public void testCriteriaQueryWhereLocalDateTime() {
        final EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT e.id FROM DateTimeEntity e WHERE e.datetime < LOCAL_DATETIME AND e.id = :id", Integer.class);
            query.setParameter("id", 4);
            em.getTransaction().begin();
            query.getSingleResult();
            em.getTransaction().commit();
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

}
