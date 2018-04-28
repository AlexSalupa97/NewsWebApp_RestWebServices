/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import domain.Categorii;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import domain.Stiri;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author oracle
 */
public class CategoriiJpaController implements Serializable {

    public CategoriiJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categorii categorii) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (categorii.getStiriCollection() == null) {
            categorii.setStiriCollection(new ArrayList<Stiri>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Stiri> attachedStiriCollection = new ArrayList<Stiri>();
            for (Stiri stiriCollectionStiriToAttach : categorii.getStiriCollection()) {
                stiriCollectionStiriToAttach = em.getReference(stiriCollectionStiriToAttach.getClass(), stiriCollectionStiriToAttach.getIdStire());
                attachedStiriCollection.add(stiriCollectionStiriToAttach);
            }
            categorii.setStiriCollection(attachedStiriCollection);
            em.persist(categorii);
            for (Stiri stiriCollectionStiri : categorii.getStiriCollection()) {
                Categorii oldIdCategorieOfStiriCollectionStiri = stiriCollectionStiri.getIdCategorie();
                stiriCollectionStiri.setIdCategorie(categorii);
                stiriCollectionStiri = em.merge(stiriCollectionStiri);
                if (oldIdCategorieOfStiriCollectionStiri != null) {
                    oldIdCategorieOfStiriCollectionStiri.getStiriCollection().remove(stiriCollectionStiri);
                    oldIdCategorieOfStiriCollectionStiri = em.merge(oldIdCategorieOfStiriCollectionStiri);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCategorii(categorii.getIdCategorie()) != null) {
                throw new PreexistingEntityException("Categorii " + categorii + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categorii categorii) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categorii persistentCategorii = em.find(Categorii.class, categorii.getIdCategorie());
            Collection<Stiri> stiriCollectionOld = persistentCategorii.getStiriCollection();
            Collection<Stiri> stiriCollectionNew = categorii.getStiriCollection();
            Collection<Stiri> attachedStiriCollectionNew = new ArrayList<Stiri>();
            for (Stiri stiriCollectionNewStiriToAttach : stiriCollectionNew) {
                stiriCollectionNewStiriToAttach = em.getReference(stiriCollectionNewStiriToAttach.getClass(), stiriCollectionNewStiriToAttach.getIdStire());
                attachedStiriCollectionNew.add(stiriCollectionNewStiriToAttach);
            }
            stiriCollectionNew = attachedStiriCollectionNew;
            categorii.setStiriCollection(stiriCollectionNew);
            categorii = em.merge(categorii);
            for (Stiri stiriCollectionOldStiri : stiriCollectionOld) {
                if (!stiriCollectionNew.contains(stiriCollectionOldStiri)) {
                    stiriCollectionOldStiri.setIdCategorie(null);
                    stiriCollectionOldStiri = em.merge(stiriCollectionOldStiri);
                }
            }
            for (Stiri stiriCollectionNewStiri : stiriCollectionNew) {
                if (!stiriCollectionOld.contains(stiriCollectionNewStiri)) {
                    Categorii oldIdCategorieOfStiriCollectionNewStiri = stiriCollectionNewStiri.getIdCategorie();
                    stiriCollectionNewStiri.setIdCategorie(categorii);
                    stiriCollectionNewStiri = em.merge(stiriCollectionNewStiri);
                    if (oldIdCategorieOfStiriCollectionNewStiri != null && !oldIdCategorieOfStiriCollectionNewStiri.equals(categorii)) {
                        oldIdCategorieOfStiriCollectionNewStiri.getStiriCollection().remove(stiriCollectionNewStiri);
                        oldIdCategorieOfStiriCollectionNewStiri = em.merge(oldIdCategorieOfStiriCollectionNewStiri);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = categorii.getIdCategorie();
                if (findCategorii(id) == null) {
                    throw new NonexistentEntityException("The categorii with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categorii categorii;
            try {
                categorii = em.getReference(Categorii.class, id);
                categorii.getIdCategorie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorii with id " + id + " no longer exists.", enfe);
            }
            Collection<Stiri> stiriCollection = categorii.getStiriCollection();
            for (Stiri stiriCollectionStiri : stiriCollection) {
                stiriCollectionStiri.setIdCategorie(null);
                stiriCollectionStiri = em.merge(stiriCollectionStiri);
            }
            em.remove(categorii);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categorii> findCategoriiEntities() {
        return findCategoriiEntities(true, -1, -1);
    }

    public List<Categorii> findCategoriiEntities(int maxResults, int firstResult) {
        return findCategoriiEntities(false, maxResults, firstResult);
    }

    private List<Categorii> findCategoriiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Categorii as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Categorii findCategorii(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categorii.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriiCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Categorii as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
