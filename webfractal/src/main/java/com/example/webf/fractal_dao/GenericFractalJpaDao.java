package com.example.webf.fractal_dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.example.webf.fractal.FractalBaseEntity;

/**
 * Базовая реализация DAO для работы с фракталами.
 */

@Transactional
public abstract class GenericFractalJpaDao <T, FID extends Serializable> implements GenericFractalDao<T, FID>{
	

    private Class<T> persistentClass;
    
    private EntityManager entityManager;
    
    public GenericFractalJpaDao(Class<T> persistentClass) {
            this.persistentClass = persistentClass;
    }

    protected EntityManager getEntityManager() {
            return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
    }

    public Class<T> getPersistentClass() {
            return persistentClass;
    }
    
    @Transactional(readOnly=true)
    public T findByFID(FID fid) {
            T entity = (T) getEntityManager().find(getPersistentClass(), fid);
            
            //T entity = (T) getEntityManager().
            return entity;
    }
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly=true)
    public List<T> findAll() {
            return getEntityManager()
                    .createQuery("select x from " + getPersistentClass().getSimpleName() + " x")
                    .getResultList();
    }
    
    public T save(T entity) {
            getEntityManager().persist(entity);
            return entity;
    }
    
    public T update(T entity) {
            T mergedEntity = getEntityManager().merge(entity);
            return mergedEntity;
    }
    
    public void delete(T entity) {
            if (FractalBaseEntity.class.isAssignableFrom(persistentClass)) {
                    getEntityManager().remove(
                                    getEntityManager().getReference(entity.getClass(), 
                                                    ((FractalBaseEntity)entity).getFid())); //getFid
            } else {
                    entity = getEntityManager().merge(entity);
                    getEntityManager().remove(entity);
            }
    }
    
    public void flush() {
            getEntityManager().flush();
    }
    

}
