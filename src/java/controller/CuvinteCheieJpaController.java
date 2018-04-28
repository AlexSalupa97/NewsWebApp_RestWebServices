/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import domain.CuvinteCheie;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import domain.RandCuvinteCheie;
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
public class CuvinteCheieJpaController implements Serializable {

    public CuvinteCheieJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CuvinteCheie cuvinteCheie) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cuvinteCheie.getRandCuvinteCheieCollection() == null) {
            cuvinteCheie.setRandCuvinteCheieCollection(new ArrayList<RandCuvinteCheie>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<RandCuvinteCheie> attachedRandCuvinteCheieCollection = new ArrayList<RandCuvinteCheie>();
            for (RandCuvinteCheie randCuvinteCheieCollectionRandCuvinteCheieToAttach : cuvinteCheie.getRandCuvinteCheieCollection()) {
                randCuvinteCheieCollectionRandCuvinteCheieToAttach = em.getReference(randCuvinteCheieCollectionRandCuvinteCheieToAttach.getClass(), randCuvinteCheieCollectionRandCuvinteCheieToAttach.getIdRandCuvantCheie());
                attachedRandCuvinteCheieCollection.add(randCuvinteCheieCollectionRandCuvinteCheieToAttach);
            }
            cuvinteCheie.setRandCuvinteCheieCollection(attachedRandCuvinteCheieCollection);
            em.persist(cuvinteCheie);
            for (RandCuvinteCheie randCuvinteCheieCollectionRandCuvinteCheie : cuvinteCheie.getRandCuvinteCheieCollection()) {
                CuvinteCheie oldNumeCuvantCheieOfRandCuvinteCheieCollectionRandCuvinteCheie = randCuvinteCheieCollectionRandCuvinteCheie.getNumeCuvantCheie();
                randCuvinteCheieCollectionRandCuvinteCheie.setNumeCuvantCheie(cuvinteCheie);
                randCuvinteCheieCollectionRandCuvinteCheie = em.merge(randCuvinteCheieCollectionRandCuvinteCheie);
                if (oldNumeCuvantCheieOfRandCuvinteCheieCollectionRandCuvinteCheie != null) {
                    oldNumeCuvantCheieOfRandCuvinteCheieCollectionRandCuvinteCheie.getRandCuvinteCheieCollection().remove(randCuvinteCheieCollectionRandCuvinteCheie);
                    oldNumeCuvantCheieOfRandCuvinteCheieCollectionRandCuvinteCheie = em.merge(oldNumeCuvantCheieOfRandCuvinteCheieCollectionRandCuvinteCheie);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCuvinteCheie(cuvinteCheie.getNumeCuvantCheie()) != null) {
                throw new PreexistingEntityException("CuvinteCheie " + cuvinteCheie + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CuvinteCheie cuvinteCheie) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CuvinteCheie persistentCuvinteCheie = em.find(CuvinteCheie.class, cuvinteCheie.getNumeCuvantCheie());
            Collection<RandCuvinteCheie> randCuvinteCheieCollectionOld = persistentCuvinteCheie.getRandCuvinteCheieCollection();
            Collection<RandCuvinteCheie> randCuvinteCheieCollectionNew = cuvinteCheie.getRandCuvinteCheieCollection();
            Collection<RandCuvinteCheie> attachedRandCuvinteCheieCollectionNew = new ArrayList<RandCuvinteCheie>();
            for (RandCuvinteCheie randCuvinteCheieCollectionNewRandCuvinteCheieToAttach : randCuvinteCheieCollectionNew) {
                randCuvinteCheieCollectionNewRandCuvinteCheieToAttach = em.getReference(randCuvinteCheieCollectionNewRandCuvinteCheieToAttach.getClass(), randCuvinteCheieCollectionNewRandCuvinteCheieToAttach.getIdRandCuvantCheie());
                attachedRandCuvinteCheieCollectionNew.add(randCuvinteCheieCollectionNewRandCuvinteCheieToAttach);
            }
            randCuvinteCheieCollectionNew = attachedRandCuvinteCheieCollectionNew;
            cuvinteCheie.setRandCuvinteCheieCollection(randCuvinteCheieCollectionNew);
            cuvinteCheie = em.merge(cuvinteCheie);
            for (RandCuvinteCheie randCuvinteCheieCollectionOldRandCuvinteCheie : randCuvinteCheieCollectionOld) {
                if (!randCuvinteCheieCollectionNew.contains(randCuvinteCheieCollectionOldRandCuvinteCheie)) {
                    randCuvinteCheieCollectionOldRandCuvinteCheie.setNumeCuvantCheie(null);
                    randCuvinteCheieCollectionOldRandCuvinteCheie = em.merge(randCuvinteCheieCollectionOldRandCuvinteCheie);
                }
            }
            for (RandCuvinteCheie randCuvinteCheieCollectionNewRandCuvinteCheie : randCuvinteCheieCollectionNew) {
                if (!randCuvinteCheieCollectionOld.contains(randCuvinteCheieCollectionNewRandCuvinteCheie)) {
                    CuvinteCheie oldNumeCuvantCheieOfRandCuvinteCheieCollectionNewRandCuvinteCheie = randCuvinteCheieCollectionNewRandCuvinteCheie.getNumeCuvantCheie();
                    randCuvinteCheieCollectionNewRandCuvinteCheie.setNumeCuvantCheie(cuvinteCheie);
                    randCuvinteCheieCollectionNewRandCuvinteCheie = em.merge(randCuvinteCheieCollectionNewRandCuvinteCheie);
                    if (oldNumeCuvantCheieOfRandCuvinteCheieCollectionNewRandCuvinteCheie != null && !oldNumeCuvantCheieOfRandCuvinteCheieCollectionNewRandCuvinteCheie.equals(cuvinteCheie)) {
                        oldNumeCuvantCheieOfRandCuvinteCheieCollectionNewRandCuvinteCheie.getRandCuvinteCheieCollection().remove(randCuvinteCheieCollectionNewRandCuvinteCheie);
                        oldNumeCuvantCheieOfRandCuvinteCheieCollectionNewRandCuvinteCheie = em.merge(oldNumeCuvantCheieOfRandCuvinteCheieCollectionNewRandCuvinteCheie);
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
                String id = cuvinteCheie.getNumeCuvantCheie();
                if (findCuvinteCheie(id) == null) {
                    throw new NonexistentEntityException("The cuvinteCheie with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CuvinteCheie cuvinteCheie;
            try {
                cuvinteCheie = em.getReference(CuvinteCheie.class, id);
                cuvinteCheie.getNumeCuvantCheie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuvinteCheie with id " + id + " no longer exists.", enfe);
            }
            Collection<RandCuvinteCheie> randCuvinteCheieCollection = cuvinteCheie.getRandCuvinteCheieCollection();
            for (RandCuvinteCheie randCuvinteCheieCollectionRandCuvinteCheie : randCuvinteCheieCollection) {
                randCuvinteCheieCollectionRandCuvinteCheie.setNumeCuvantCheie(null);
                randCuvinteCheieCollectionRandCuvinteCheie = em.merge(randCuvinteCheieCollectionRandCuvinteCheie);
            }
            em.remove(cuvinteCheie);
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

    public List<CuvinteCheie> findCuvinteCheieEntities() {
        return findCuvinteCheieEntities(true, -1, -1);
    }

    public List<CuvinteCheie> findCuvinteCheieEntities(int maxResults, int firstResult) {
        return findCuvinteCheieEntities(false, maxResults, firstResult);
    }

    private List<CuvinteCheie> findCuvinteCheieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from CuvinteCheie as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CuvinteCheie findCuvinteCheie(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CuvinteCheie.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuvinteCheieCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from CuvinteCheie as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
