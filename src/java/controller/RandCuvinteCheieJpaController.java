/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import domain.Stiri;
import domain.CuvinteCheie;
import domain.RandCuvinteCheie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author oracle
 */
public class RandCuvinteCheieJpaController implements Serializable {

    public RandCuvinteCheieJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RandCuvinteCheie randCuvinteCheie) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Stiri idStire = randCuvinteCheie.getIdStire();
            if (idStire != null) {
                idStire = em.getReference(idStire.getClass(), idStire.getIdStire());
                randCuvinteCheie.setIdStire(idStire);
            }
            CuvinteCheie numeCuvantCheie = randCuvinteCheie.getNumeCuvantCheie();
            if (numeCuvantCheie != null) {
                numeCuvantCheie = em.getReference(numeCuvantCheie.getClass(), numeCuvantCheie.getNumeCuvantCheie());
                randCuvinteCheie.setNumeCuvantCheie(numeCuvantCheie);
            }
            em.persist(randCuvinteCheie);
            if (idStire != null) {
                idStire.getRandCuvinteCheieCollection().add(randCuvinteCheie);
                idStire = em.merge(idStire);
            }
            if (numeCuvantCheie != null) {
                numeCuvantCheie.getRandCuvinteCheieCollection().add(randCuvinteCheie);
                numeCuvantCheie = em.merge(numeCuvantCheie);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRandCuvinteCheie(randCuvinteCheie.getIdRandCuvantCheie()) != null) {
                throw new PreexistingEntityException("RandCuvinteCheie " + randCuvinteCheie + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RandCuvinteCheie randCuvinteCheie) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RandCuvinteCheie persistentRandCuvinteCheie = em.find(RandCuvinteCheie.class, randCuvinteCheie.getIdRandCuvantCheie());
            Stiri idStireOld = persistentRandCuvinteCheie.getIdStire();
            Stiri idStireNew = randCuvinteCheie.getIdStire();
            CuvinteCheie numeCuvantCheieOld = persistentRandCuvinteCheie.getNumeCuvantCheie();
            CuvinteCheie numeCuvantCheieNew = randCuvinteCheie.getNumeCuvantCheie();
            if (idStireNew != null) {
                idStireNew = em.getReference(idStireNew.getClass(), idStireNew.getIdStire());
                randCuvinteCheie.setIdStire(idStireNew);
            }
            if (numeCuvantCheieNew != null) {
                numeCuvantCheieNew = em.getReference(numeCuvantCheieNew.getClass(), numeCuvantCheieNew.getNumeCuvantCheie());
                randCuvinteCheie.setNumeCuvantCheie(numeCuvantCheieNew);
            }
            randCuvinteCheie = em.merge(randCuvinteCheie);
            if (idStireOld != null && !idStireOld.equals(idStireNew)) {
                idStireOld.getRandCuvinteCheieCollection().remove(randCuvinteCheie);
                idStireOld = em.merge(idStireOld);
            }
            if (idStireNew != null && !idStireNew.equals(idStireOld)) {
                idStireNew.getRandCuvinteCheieCollection().add(randCuvinteCheie);
                idStireNew = em.merge(idStireNew);
            }
            if (numeCuvantCheieOld != null && !numeCuvantCheieOld.equals(numeCuvantCheieNew)) {
                numeCuvantCheieOld.getRandCuvinteCheieCollection().remove(randCuvinteCheie);
                numeCuvantCheieOld = em.merge(numeCuvantCheieOld);
            }
            if (numeCuvantCheieNew != null && !numeCuvantCheieNew.equals(numeCuvantCheieOld)) {
                numeCuvantCheieNew.getRandCuvinteCheieCollection().add(randCuvinteCheie);
                numeCuvantCheieNew = em.merge(numeCuvantCheieNew);
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
                Short id = randCuvinteCheie.getIdRandCuvantCheie();
                if (findRandCuvinteCheie(id) == null) {
                    throw new NonexistentEntityException("The randCuvinteCheie with id " + id + " no longer exists.");
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
            RandCuvinteCheie randCuvinteCheie;
            try {
                randCuvinteCheie = em.getReference(RandCuvinteCheie.class, id);
                randCuvinteCheie.getIdRandCuvantCheie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The randCuvinteCheie with id " + id + " no longer exists.", enfe);
            }
            Stiri idStire = randCuvinteCheie.getIdStire();
            if (idStire != null) {
                idStire.getRandCuvinteCheieCollection().remove(randCuvinteCheie);
                idStire = em.merge(idStire);
            }
            CuvinteCheie numeCuvantCheie = randCuvinteCheie.getNumeCuvantCheie();
            if (numeCuvantCheie != null) {
                numeCuvantCheie.getRandCuvinteCheieCollection().remove(randCuvinteCheie);
                numeCuvantCheie = em.merge(numeCuvantCheie);
            }
            em.remove(randCuvinteCheie);
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

    public List<RandCuvinteCheie> findRandCuvinteCheieEntities() {
        return findRandCuvinteCheieEntities(true, -1, -1);
    }

    public List<RandCuvinteCheie> findRandCuvinteCheieEntities(int maxResults, int firstResult) {
        return findRandCuvinteCheieEntities(false, maxResults, firstResult);
    }

    private List<RandCuvinteCheie> findRandCuvinteCheieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from RandCuvinteCheie as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RandCuvinteCheie findRandCuvinteCheie(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RandCuvinteCheie.class, id);
        } finally {
            em.close();
        }
    }

    public int getRandCuvinteCheieCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from RandCuvinteCheie as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
