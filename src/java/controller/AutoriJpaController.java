/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import domain.Autori;
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
public class AutoriJpaController implements Serializable {

    public AutoriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autori autori) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (autori.getStiriCollection() == null) {
            autori.setStiriCollection(new ArrayList<Stiri>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Stiri> attachedStiriCollection = new ArrayList<Stiri>();
            for (Stiri stiriCollectionStiriToAttach : autori.getStiriCollection()) {
                stiriCollectionStiriToAttach = em.getReference(stiriCollectionStiriToAttach.getClass(), stiriCollectionStiriToAttach.getIdStire());
                attachedStiriCollection.add(stiriCollectionStiriToAttach);
            }
            autori.setStiriCollection(attachedStiriCollection);
            em.persist(autori);
            for (Stiri stiriCollectionStiri : autori.getStiriCollection()) {
                Autori oldIdAutorOfStiriCollectionStiri = stiriCollectionStiri.getIdAutor();
                stiriCollectionStiri.setIdAutor(autori);
                stiriCollectionStiri = em.merge(stiriCollectionStiri);
                if (oldIdAutorOfStiriCollectionStiri != null) {
                    oldIdAutorOfStiriCollectionStiri.getStiriCollection().remove(stiriCollectionStiri);
                    oldIdAutorOfStiriCollectionStiri = em.merge(oldIdAutorOfStiriCollectionStiri);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAutori(autori.getIdAutor()) != null) {
                throw new PreexistingEntityException("Autori " + autori + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autori autori) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Autori persistentAutori = em.find(Autori.class, autori.getIdAutor());
            Collection<Stiri> stiriCollectionOld = persistentAutori.getStiriCollection();
            Collection<Stiri> stiriCollectionNew = autori.getStiriCollection();
            Collection<Stiri> attachedStiriCollectionNew = new ArrayList<Stiri>();
            for (Stiri stiriCollectionNewStiriToAttach : stiriCollectionNew) {
                stiriCollectionNewStiriToAttach = em.getReference(stiriCollectionNewStiriToAttach.getClass(), stiriCollectionNewStiriToAttach.getIdStire());
                attachedStiriCollectionNew.add(stiriCollectionNewStiriToAttach);
            }
            stiriCollectionNew = attachedStiriCollectionNew;
            autori.setStiriCollection(stiriCollectionNew);
            autori = em.merge(autori);
            for (Stiri stiriCollectionOldStiri : stiriCollectionOld) {
                if (!stiriCollectionNew.contains(stiriCollectionOldStiri)) {
                    stiriCollectionOldStiri.setIdAutor(null);
                    stiriCollectionOldStiri = em.merge(stiriCollectionOldStiri);
                }
            }
            for (Stiri stiriCollectionNewStiri : stiriCollectionNew) {
                if (!stiriCollectionOld.contains(stiriCollectionNewStiri)) {
                    Autori oldIdAutorOfStiriCollectionNewStiri = stiriCollectionNewStiri.getIdAutor();
                    stiriCollectionNewStiri.setIdAutor(autori);
                    stiriCollectionNewStiri = em.merge(stiriCollectionNewStiri);
                    if (oldIdAutorOfStiriCollectionNewStiri != null && !oldIdAutorOfStiriCollectionNewStiri.equals(autori)) {
                        oldIdAutorOfStiriCollectionNewStiri.getStiriCollection().remove(stiriCollectionNewStiri);
                        oldIdAutorOfStiriCollectionNewStiri = em.merge(oldIdAutorOfStiriCollectionNewStiri);
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
                Short id = autori.getIdAutor();
                if (findAutori(id) == null) {
                    throw new NonexistentEntityException("The autori with id " + id + " no longer exists.");
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
            Autori autori;
            try {
                autori = em.getReference(Autori.class, id);
                autori.getIdAutor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autori with id " + id + " no longer exists.", enfe);
            }
            Collection<Stiri> stiriCollection = autori.getStiriCollection();
            for (Stiri stiriCollectionStiri : stiriCollection) {
                stiriCollectionStiri.setIdAutor(null);
                stiriCollectionStiri = em.merge(stiriCollectionStiri);
            }
            em.remove(autori);
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

    public List<Autori> findAutoriEntities() {
        return findAutoriEntities(true, -1, -1);
    }

    public List<Autori> findAutoriEntities(int maxResults, int firstResult) {
        return findAutoriEntities(false, maxResults, firstResult);
    }

    private List<Autori> findAutoriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Autori as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Autori findAutori(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autori.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutoriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Autori as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
