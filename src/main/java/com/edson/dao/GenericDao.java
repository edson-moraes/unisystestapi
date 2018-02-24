package com.edson.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.io.Serializable;
import java.util.*;


public abstract class GenericDao<T, KEY extends Serializable> {

    private static final String PERSISTENCE_UNIT_NAME = "unisysTestApi";
    private static EntityManagerFactory factory;

    private Class<T> persistedClass;

    private GenericDao() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    protected GenericDao(Class<T> persistedClass) {
        this();
        this.persistedClass = persistedClass;
    }

    protected EntityManager getEntityManger() {
        return factory.createEntityManager();
    }

    public T save(@Valid T entity) {
        EntityManager em = getEntityManger();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }

    public T update(@Valid T entity) {
        EntityManager em = getEntityManger();
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
        return entity;
    }

    public void delete(KEY id) throws NotFoundException {
        EntityManager em = getEntityManger();
        T entity = em.find(persistedClass, id);
        if (entity != null) {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } else {
            throw new NotFoundException();
        }

    }

    public List<T> findAll() {
        EntityManager em = getEntityManger();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistedClass);
        query.from(persistedClass);
        return em.createQuery(query).getResultList();
    }

    public List<T> findAll(List<Order> orderArray, List<String> propertiesOrder) {

        EntityManager em = getEntityManger();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(persistedClass);
        Root<T> root = cq.from(persistedClass);

        List<javax.persistence.criteria.Order> orders = new ArrayList<>();

        int index = 0;
        Order sortOrder;
        for (String property : propertiesOrder) {
            sortOrder = orderArray.get(index);
            if (sortOrder == null || sortOrder.isAscOrder()) {
                orders.add(cb.asc(root.get(property)));
            } else {
                orders.add(cb.desc(root.get(property)));
            }

            index = index + 1;
        }
        cq.orderBy(orders);
        return em.createQuery(cq).getResultList();
    }

    public T find(KEY id) {
        EntityManager em = getEntityManger();
        return em.find(persistedClass, id);
    }

    public List<T> find(HashMap<String, Object> propertyValueMap) {
        EntityManager em = getEntityManger();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(persistedClass);
        Root<T> root = cq.from(persistedClass);

        Iterator<Map.Entry<String, Object>> it = propertyValueMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = it.next();
            cq.where(cb.equal(root.get(pair.getKey()), pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        return em.createQuery(cq).getResultList();
    }

}