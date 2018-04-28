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
import domain.Poze;
import domain.PozeStiri;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author oracle
 */
public class PozeStiriJpaController implements Serializable {

    public PozeStiriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PozeStiri pozeStiri) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Stiri idStire = pozeStiri.getIdStire();
            if (idStire != null) {
                idStire = em.getReference(idStire.getClass(), idStire.getIdStire());
                pozeStiri.setIdStire(idStire);
            }
            Poze idPoza = pozeStiri.getIdPoza();
            if (idPoza != null) {
                idPoza = em.getReference(idPoza.getClass(), idPoza.getIdPoza());
                pozeStiri.setIdPoza(idPoza);
            }
            em.persist(pozeStiri);
            if (idStire != null) {
                idStire.getPozeStiriCollection().add(pozeStiri);
                idStire = em.merge(idStire);
            }
            if (idPoza != null) {
                idPoza.getPozeStiriCollection().add(pozeStiri);
                idPoza = em.merge(idPoza);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPozeStiri(pozeStiri.getIdPozaStire()) != null) {
                throw new PreexistingEntityException("PozeStiri " + pozeStiri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PozeStiri pozeStiri) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PozeStiri persistentPozeStiri = em.find(PozeStiri.class, pozeStiri.getIdPozaStire());
            Stiri idStireOld = persistentPozeStiri.getIdStire();
            Stiri idStireNew = pozeStiri.getIdStire();
            Poze idPozaOld = persistentPozeStiri.getIdPoza();
            Poze idPozaNew = pozeStiri.getIdPoza();
            if (idStireNew != null) {
                idStireNew = em.getReference(idStireNew.getClass(), idStireNew.getIdStire());
                pozeStiri.setIdStire(idStireNew);
            }
            if (idPozaNew != null) {
                idPozaNew = em.getReference(idPozaNew.getClass(), idPozaNew.getIdPoza());
                pozeStiri.setIdPoza(idPozaNew);
            }
            pozeStiri = em.merge(pozeStiri);
            if (idStireOld != null && !idStireOld.equals(idStireNew)) {
                idStireOld.getPozeStiriCollection().remove(pozeStiri);
                idStireOld = em.merge(idStireOld);
            }
            if (idStireNew != null && !idStireNew.equals(idStireOld)) {
                idStireNew.getPozeStiriCollection().add(pozeStiri);
                idStireNew = em.merge(idStireNew);
            }
            if (idPozaOld != null && !idPozaOld.equals(idPozaNew)) {
                idPozaOld.getPozeStiriCollection().remove(pozeStiri);
                idPozaOld = em.merge(idPozaOld);
            }
            if (idPozaNew != null && !idPozaNew.equals(idPozaOld)) {
                idPozaNew.getPozeStiriCollection().add(pozeStiri);
                idPozaNew = em.merge(idPozaNew);
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
                Short id = pozeStiri.getIdPozaStire();
                if (findPozeStiri(id) == null) {
                    throw new NonexistentEntityException("The pozeStiri with id " + id + " no longer exists.");
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
            PozeStiri pozeStiri;
            try {
                pozeStiri = em.getReference(PozeStiri.class, id);
                pozeStiri.getIdPozaStire();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pozeStiri with id " + id + " no longer exists.", enfe);
            }
            Stiri idStire = pozeStiri.getIdStire();
            if (idStire != null) {
                idStire.getPozeStiriCollection().remove(pozeStiri);
                idStire = em.merge(idStire);
            }
            Poze idPoza = pozeStiri.getIdPoza();
            if (idPoza != null) {
                idPoza.getPozeStiriCollection().remove(pozeStiri);
                idPoza = em.merge(idPoza);
            }
            em.remove(pozeStiri);
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

    public List<PozeStiri> findPozeStiriEntities() {
        return findPozeStiriEntities(true, -1, -1);
    }

    public List<PozeStiri> findPozeStiriEntities(int maxResults, int firstResult) {
        return findPozeStiriEntities(false, maxResults, firstResult);
    }

    private List<PozeStiri> findPozeStiriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from PozeStiri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PozeStiri findPozeStiri(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PozeStiri.class, id);
        } finally {
            em.close();
        }
    }

    public int getPozeStiriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from PozeStiri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
