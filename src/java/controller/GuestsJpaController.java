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
import domain.Comentarii;
import domain.Guests;
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
public class GuestsJpaController implements Serializable {

    public GuestsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Guests guests) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (guests.getComentariiCollection() == null) {
            guests.setComentariiCollection(new ArrayList<Comentarii>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Comentarii> attachedComentariiCollection = new ArrayList<Comentarii>();
            for (Comentarii comentariiCollectionComentariiToAttach : guests.getComentariiCollection()) {
                comentariiCollectionComentariiToAttach = em.getReference(comentariiCollectionComentariiToAttach.getClass(), comentariiCollectionComentariiToAttach.getIdComentariu());
                attachedComentariiCollection.add(comentariiCollectionComentariiToAttach);
            }
            guests.setComentariiCollection(attachedComentariiCollection);
            em.persist(guests);
            for (Comentarii comentariiCollectionComentarii : guests.getComentariiCollection()) {
                Guests oldIdGuestOfComentariiCollectionComentarii = comentariiCollectionComentarii.getIdGuest();
                comentariiCollectionComentarii.setIdGuest(guests);
                comentariiCollectionComentarii = em.merge(comentariiCollectionComentarii);
                if (oldIdGuestOfComentariiCollectionComentarii != null) {
                    oldIdGuestOfComentariiCollectionComentarii.getComentariiCollection().remove(comentariiCollectionComentarii);
                    oldIdGuestOfComentariiCollectionComentarii = em.merge(oldIdGuestOfComentariiCollectionComentarii);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGuests(guests.getIdGuest()) != null) {
                throw new PreexistingEntityException("Guests " + guests + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Guests guests) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Guests persistentGuests = em.find(Guests.class, guests.getIdGuest());
            Collection<Comentarii> comentariiCollectionOld = persistentGuests.getComentariiCollection();
            Collection<Comentarii> comentariiCollectionNew = guests.getComentariiCollection();
            Collection<Comentarii> attachedComentariiCollectionNew = new ArrayList<Comentarii>();
            for (Comentarii comentariiCollectionNewComentariiToAttach : comentariiCollectionNew) {
                comentariiCollectionNewComentariiToAttach = em.getReference(comentariiCollectionNewComentariiToAttach.getClass(), comentariiCollectionNewComentariiToAttach.getIdComentariu());
                attachedComentariiCollectionNew.add(comentariiCollectionNewComentariiToAttach);
            }
            comentariiCollectionNew = attachedComentariiCollectionNew;
            guests.setComentariiCollection(comentariiCollectionNew);
            guests = em.merge(guests);
            for (Comentarii comentariiCollectionOldComentarii : comentariiCollectionOld) {
                if (!comentariiCollectionNew.contains(comentariiCollectionOldComentarii)) {
                    comentariiCollectionOldComentarii.setIdGuest(null);
                    comentariiCollectionOldComentarii = em.merge(comentariiCollectionOldComentarii);
                }
            }
            for (Comentarii comentariiCollectionNewComentarii : comentariiCollectionNew) {
                if (!comentariiCollectionOld.contains(comentariiCollectionNewComentarii)) {
                    Guests oldIdGuestOfComentariiCollectionNewComentarii = comentariiCollectionNewComentarii.getIdGuest();
                    comentariiCollectionNewComentarii.setIdGuest(guests);
                    comentariiCollectionNewComentarii = em.merge(comentariiCollectionNewComentarii);
                    if (oldIdGuestOfComentariiCollectionNewComentarii != null && !oldIdGuestOfComentariiCollectionNewComentarii.equals(guests)) {
                        oldIdGuestOfComentariiCollectionNewComentarii.getComentariiCollection().remove(comentariiCollectionNewComentarii);
                        oldIdGuestOfComentariiCollectionNewComentarii = em.merge(oldIdGuestOfComentariiCollectionNewComentarii);
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
                String id = guests.getIdGuest();
                if (findGuests(id) == null) {
                    throw new NonexistentEntityException("The guests with id " + id + " no longer exists.");
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
            Guests guests;
            try {
                guests = em.getReference(Guests.class, id);
                guests.getIdGuest();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The guests with id " + id + " no longer exists.", enfe);
            }
            Collection<Comentarii> comentariiCollection = guests.getComentariiCollection();
            for (Comentarii comentariiCollectionComentarii : comentariiCollection) {
                comentariiCollectionComentarii.setIdGuest(null);
                comentariiCollectionComentarii = em.merge(comentariiCollectionComentarii);
            }
            em.remove(guests);
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

    public List<Guests> findGuestsEntities() {
        return findGuestsEntities(true, -1, -1);
    }

    public List<Guests> findGuestsEntities(int maxResults, int firstResult) {
        return findGuestsEntities(false, maxResults, firstResult);
    }

    private List<Guests> findGuestsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Guests as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Guests findGuests(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Guests.class, id);
        } finally {
            em.close();
        }
    }

    public int getGuestsCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Guests as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

//    public void edit() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
