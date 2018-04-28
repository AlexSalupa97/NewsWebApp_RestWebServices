/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import domain.Poze;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import domain.PozeStiri;
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
public class PozeJpaController implements Serializable {

    public PozeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Poze poze) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (poze.getPozeStiriCollection() == null) {
            poze.setPozeStiriCollection(new ArrayList<PozeStiri>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PozeStiri> attachedPozeStiriCollection = new ArrayList<PozeStiri>();
            for (PozeStiri pozeStiriCollectionPozeStiriToAttach : poze.getPozeStiriCollection()) {
                pozeStiriCollectionPozeStiriToAttach = em.getReference(pozeStiriCollectionPozeStiriToAttach.getClass(), pozeStiriCollectionPozeStiriToAttach.getIdPozaStire());
                attachedPozeStiriCollection.add(pozeStiriCollectionPozeStiriToAttach);
            }
            poze.setPozeStiriCollection(attachedPozeStiriCollection);
            em.persist(poze);
            for (PozeStiri pozeStiriCollectionPozeStiri : poze.getPozeStiriCollection()) {
                Poze oldIdPozaOfPozeStiriCollectionPozeStiri = pozeStiriCollectionPozeStiri.getIdPoza();
                pozeStiriCollectionPozeStiri.setIdPoza(poze);
                pozeStiriCollectionPozeStiri = em.merge(pozeStiriCollectionPozeStiri);
                if (oldIdPozaOfPozeStiriCollectionPozeStiri != null) {
                    oldIdPozaOfPozeStiriCollectionPozeStiri.getPozeStiriCollection().remove(pozeStiriCollectionPozeStiri);
                    oldIdPozaOfPozeStiriCollectionPozeStiri = em.merge(oldIdPozaOfPozeStiriCollectionPozeStiri);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPoze(poze.getIdPoza()) != null) {
                throw new PreexistingEntityException("Poze " + poze + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Poze poze) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Poze persistentPoze = em.find(Poze.class, poze.getIdPoza());
            Collection<PozeStiri> pozeStiriCollectionOld = persistentPoze.getPozeStiriCollection();
            Collection<PozeStiri> pozeStiriCollectionNew = poze.getPozeStiriCollection();
            Collection<PozeStiri> attachedPozeStiriCollectionNew = new ArrayList<PozeStiri>();
            for (PozeStiri pozeStiriCollectionNewPozeStiriToAttach : pozeStiriCollectionNew) {
                pozeStiriCollectionNewPozeStiriToAttach = em.getReference(pozeStiriCollectionNewPozeStiriToAttach.getClass(), pozeStiriCollectionNewPozeStiriToAttach.getIdPozaStire());
                attachedPozeStiriCollectionNew.add(pozeStiriCollectionNewPozeStiriToAttach);
            }
            pozeStiriCollectionNew = attachedPozeStiriCollectionNew;
            poze.setPozeStiriCollection(pozeStiriCollectionNew);
            poze = em.merge(poze);
            for (PozeStiri pozeStiriCollectionOldPozeStiri : pozeStiriCollectionOld) {
                if (!pozeStiriCollectionNew.contains(pozeStiriCollectionOldPozeStiri)) {
                    pozeStiriCollectionOldPozeStiri.setIdPoza(null);
                    pozeStiriCollectionOldPozeStiri = em.merge(pozeStiriCollectionOldPozeStiri);
                }
            }
            for (PozeStiri pozeStiriCollectionNewPozeStiri : pozeStiriCollectionNew) {
                if (!pozeStiriCollectionOld.contains(pozeStiriCollectionNewPozeStiri)) {
                    Poze oldIdPozaOfPozeStiriCollectionNewPozeStiri = pozeStiriCollectionNewPozeStiri.getIdPoza();
                    pozeStiriCollectionNewPozeStiri.setIdPoza(poze);
                    pozeStiriCollectionNewPozeStiri = em.merge(pozeStiriCollectionNewPozeStiri);
                    if (oldIdPozaOfPozeStiriCollectionNewPozeStiri != null && !oldIdPozaOfPozeStiriCollectionNewPozeStiri.equals(poze)) {
                        oldIdPozaOfPozeStiriCollectionNewPozeStiri.getPozeStiriCollection().remove(pozeStiriCollectionNewPozeStiri);
                        oldIdPozaOfPozeStiriCollectionNewPozeStiri = em.merge(oldIdPozaOfPozeStiriCollectionNewPozeStiri);
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
                Short id = poze.getIdPoza();
                if (findPoze(id) == null) {
                    throw new NonexistentEntityException("The poze with id " + id + " no longer exists.");
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
            Poze poze;
            try {
                poze = em.getReference(Poze.class, id);
                poze.getIdPoza();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The poze with id " + id + " no longer exists.", enfe);
            }
            Collection<PozeStiri> pozeStiriCollection = poze.getPozeStiriCollection();
            for (PozeStiri pozeStiriCollectionPozeStiri : pozeStiriCollection) {
                pozeStiriCollectionPozeStiri.setIdPoza(null);
                pozeStiriCollectionPozeStiri = em.merge(pozeStiriCollectionPozeStiri);
            }
            em.remove(poze);
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

    public List<Poze> findPozeEntities() {
        return findPozeEntities(true, -1, -1);
    }

    public List<Poze> findPozeEntities(int maxResults, int firstResult) {
        return findPozeEntities(false, maxResults, firstResult);
    }

    private List<Poze> findPozeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Poze as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Poze findPoze(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Poze.class, id);
        } finally {
            em.close();
        }
    }

    public int getPozeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Poze as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
