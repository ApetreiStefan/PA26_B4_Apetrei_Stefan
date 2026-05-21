package org.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.util.JpaLogger;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository<T, ID> {
    protected static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("QuizAppPU");

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            JpaLogger.error("Error creating entity: " + entity, e);
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            JpaLogger.error("Error updating entity: " + entity, e);
            throw e;
        } finally {
            em.close();
        }
    }

    public abstract T findById(ID id);

    // Generic JPQL Read Query Helper with Caching and Logging
    protected <R> List<R> runReadQuery(String jpql, Class<R> clazz, Map<String, Object> params, boolean cacheable) {
        EntityManager em = getEntityManager();
        long start = System.nanoTime();
        try {
            TypedQuery<R> query = em.createQuery(jpql, clazz);
            if (params != null) {
                params.forEach(query::setParameter);
            }
            if (cacheable) {
                // Enable query caching
                query.setHint("jakarta.persistence.cache.retrieveMode", jakarta.persistence.CacheRetrieveMode.USE);
                query.setHint("jakarta.persistence.cache.storeMode", jakarta.persistence.CacheStoreMode.USE);
                // Hibernate specific query cache hint
                query.setHint("org.hibernate.cacheable", true);
            }
            List<R> results = query.getResultList();
            long elapsedUs = (System.nanoTime() - start) / 1000;
            JpaLogger.info("JPQL Read executed in " + elapsedUs + " us: \"" + jpql + "\" [Params: " + params + ", Cacheable: " + cacheable + "]");
            return results;
        } catch (Exception e) {
            JpaLogger.error("Exception executing JPQL Read: \"" + jpql + "\"", e);
            throw e;
        } finally {
            em.close();
        }
    }

    // Generic JPQL Modifying Query Helper with Logging
    protected int runModifyingQuery(String jpql, Map<String, Object> params) {
        EntityManager em = getEntityManager();
        long start = System.nanoTime();
        try {
            em.getTransaction().begin();
            var query = em.createQuery(jpql);
            if (params != null) {
                params.forEach(query::setParameter);
            }
            int updatedCount = query.executeUpdate();
            em.getTransaction().commit();
            long elapsedUs = (System.nanoTime() - start) / 1000;
            JpaLogger.info("JPQL Modifying executed in " + elapsedUs + " us (updated " + updatedCount + " rows): \"" + jpql + "\" [Params: " + params + "]");
            return updatedCount;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            JpaLogger.error("Exception executing JPQL Modifying: \"" + jpql + "\"", e);
            throw e;
        } finally {
            em.close();
        }
    }
}
