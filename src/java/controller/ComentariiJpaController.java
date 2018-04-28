/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import domain.Comentarii;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import domain.Useri;
import domain.Stiri;
import domain.Guests;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author oracle
 */
public class ComentariiJpaController implements Serializable {

    public ComentariiJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comentarii comentarii) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Useri idUser = comentarii.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getIdUser());
                comentarii.setIdUser(idUser);
            }
            Stiri idStire = comentarii.getIdStire();
            if (idStire != null) {
                idStire = em.getReference(idStire.getClass(), idStire.getIdStire());
                comentarii.setIdStire(idStire);
            }
            Guests idGuest = comentarii.getIdGuest();
            if (idGuest != null) {
                idGuest = em.getReference(idGuest.getClass(), idGuest.getIdGuest());
                comentarii.setIdGuest(idGuest);
            }
            em.persist(comentarii);
            if (idUser != null) {
                idUser.getComentariiCollection().add(comentarii);
                idUser = em.merge(idUser);
            }
            if (idStire != null) {
                idStire.getComentariiCollection().add(comentarii);
                idStire = em.merge(idStire);
            }
            if (idGuest != null) {
                idGuest.getComentariiCollection().add(comentarii);
                idGuest = em.merge(idGuest);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComentarii(comentarii.getIdComentariu()) != null) {
                throw new PreexistingEntityException("Comentarii " + comentarii + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comentarii comentarii) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comentarii persistentComentarii = em.find(Comentarii.class, comentarii.getIdComentariu());
            Useri idUserOld = persistentComentarii.getIdUser();
            Useri idUserNew = comentarii.getIdUser();
            Stiri idStireOld = persistentComentarii.getIdStire();
            Stiri idStireNew = comentarii.getIdStire();
            Guests idGuestOld = persistentComentarii.getIdGuest();
            Guests idGuestNew = comentarii.getIdGuest();
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getIdUser());
                comentarii.setIdUser(idUserNew);
            }
            if (idStireNew != null) {
                idStireNew = em.getReference(idStireNew.getClass(), idStireNew.getIdStire());
                comentarii.setIdStire(idStireNew);
            }
            if (idGuestNew != null) {
                idGuestNew = em.getReference(idGuestNew.getClass(), idGuestNew.getIdGuest());
                comentarii.setIdGuest(idGuestNew);
            }
            comentarii = em.merge(comentarii);
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getComentariiCollection().remove(comentarii);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getComentariiCollection().add(comentarii);
                idUserNew = em.merge(idUserNew);
            }
            if (idStireOld != null && !idStireOld.equals(idStireNew)) {
                idStireOld.getComentariiCollection().remove(comentarii);
                idStireOld = em.merge(idStireOld);
            }
            if (idStireNew != null && !idStireNew.equals(idStireOld)) {
                idStireNew.getComentariiCollection().add(comentarii);
                idStireNew = em.merge(idStireNew);
            }
            if (idGuestOld != null && !idGuestOld.equals(idGuestNew)) {
                idGuestOld.getComentariiCollection().remove(comentarii);
                idGuestOld = em.merge(idGuestOld);
            }
            if (idGuestNew != null && !idGuestNew.equals(idGuestOld)) {
                idGuestNew.getComentariiCollection().add(comentarii);
                idGuestNew = em.merge(idGuestNew);
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
                Short id = comentarii.getIdComentariu();
                if (findComentarii(id) == null) {
                    throw new NonexistentEntityException("The comentarii with id " + id + " no longer exists.");
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
            Comentarii comentarii;
            try {
                comentarii = em.getReference(Comentarii.class, id);
                comentarii.getIdComentariu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comentarii with id " + id + " no longer exists.", enfe);
            }
            Useri idUser = comentarii.getIdUser();
            if (idUser != null) {
                idUser.getComentariiCollection().remove(comentarii);
                idUser = em.merge(idUser);
            }
            Stiri idStire = comentarii.getIdStire();
            if (idStire != null) {
                idStire.getComentariiCollection().remove(comentarii);
                idStire = em.merge(idStire);
            }
            Guests idGuest = comentarii.getIdGuest();
            if (idGuest != null) {
                idGuest.getComentariiCollection().remove(comentarii);
                idGuest = em.merge(idGuest);
            }
            em.remove(comentarii);
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

    public List<Comentarii> findComentariiEntities() {
        return findComentariiEntities(true, -1, -1);
    }

    public List<Comentarii> findComentariiEntities(int maxResults, int firstResult) {
        return findComentariiEntities(false, maxResults, firstResult);
    }

    private List<Comentarii> findComentariiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Comentarii as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Comentarii findComentarii(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comentarii.class, id);
        } finally {
            em.close();
        }
    }

    public int getComentariiCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Comentarii as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
