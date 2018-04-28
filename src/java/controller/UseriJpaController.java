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
import domain.Useri;
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
public class UseriJpaController implements Serializable {

    public UseriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Useri useri) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (useri.getComentariiCollection() == null) {
            useri.setComentariiCollection(new ArrayList<Comentarii>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Comentarii> attachedComentariiCollection = new ArrayList<Comentarii>();
            for (Comentarii comentariiCollectionComentariiToAttach : useri.getComentariiCollection()) {
                comentariiCollectionComentariiToAttach = em.getReference(comentariiCollectionComentariiToAttach.getClass(), comentariiCollectionComentariiToAttach.getIdComentariu());
                attachedComentariiCollection.add(comentariiCollectionComentariiToAttach);
            }
            useri.setComentariiCollection(attachedComentariiCollection);
            em.persist(useri);
            for (Comentarii comentariiCollectionComentarii : useri.getComentariiCollection()) {
                Useri oldIdUserOfComentariiCollectionComentarii = comentariiCollectionComentarii.getIdUser();
                comentariiCollectionComentarii.setIdUser(useri);
                comentariiCollectionComentarii = em.merge(comentariiCollectionComentarii);
                if (oldIdUserOfComentariiCollectionComentarii != null) {
                    oldIdUserOfComentariiCollectionComentarii.getComentariiCollection().remove(comentariiCollectionComentarii);
                    oldIdUserOfComentariiCollectionComentarii = em.merge(oldIdUserOfComentariiCollectionComentarii);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUseri(useri.getIdUser()) != null) {
                throw new PreexistingEntityException("Useri " + useri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Useri useri) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Useri persistentUseri = em.find(Useri.class, useri.getIdUser());
            Collection<Comentarii> comentariiCollectionOld = persistentUseri.getComentariiCollection();
            Collection<Comentarii> comentariiCollectionNew = useri.getComentariiCollection();
            Collection<Comentarii> attachedComentariiCollectionNew = new ArrayList<Comentarii>();
            for (Comentarii comentariiCollectionNewComentariiToAttach : comentariiCollectionNew) {
                comentariiCollectionNewComentariiToAttach = em.getReference(comentariiCollectionNewComentariiToAttach.getClass(), comentariiCollectionNewComentariiToAttach.getIdComentariu());
                attachedComentariiCollectionNew.add(comentariiCollectionNewComentariiToAttach);
            }
            comentariiCollectionNew = attachedComentariiCollectionNew;
            useri.setComentariiCollection(comentariiCollectionNew);
            useri = em.merge(useri);
            for (Comentarii comentariiCollectionOldComentarii : comentariiCollectionOld) {
                if (!comentariiCollectionNew.contains(comentariiCollectionOldComentarii)) {
                    comentariiCollectionOldComentarii.setIdUser(null);
                    comentariiCollectionOldComentarii = em.merge(comentariiCollectionOldComentarii);
                }
            }
            for (Comentarii comentariiCollectionNewComentarii : comentariiCollectionNew) {
                if (!comentariiCollectionOld.contains(comentariiCollectionNewComentarii)) {
                    Useri oldIdUserOfComentariiCollectionNewComentarii = comentariiCollectionNewComentarii.getIdUser();
                    comentariiCollectionNewComentarii.setIdUser(useri);
                    comentariiCollectionNewComentarii = em.merge(comentariiCollectionNewComentarii);
                    if (oldIdUserOfComentariiCollectionNewComentarii != null && !oldIdUserOfComentariiCollectionNewComentarii.equals(useri)) {
                        oldIdUserOfComentariiCollectionNewComentarii.getComentariiCollection().remove(comentariiCollectionNewComentarii);
                        oldIdUserOfComentariiCollectionNewComentarii = em.merge(oldIdUserOfComentariiCollectionNewComentarii);
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
                Short id = useri.getIdUser();
                if (findUseri(id) == null) {
                    throw new NonexistentEntityException("The useri with id " + id + " no longer exists.");
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
            Useri useri;
            try {
                useri = em.getReference(Useri.class, id);
                useri.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The useri with id " + id + " no longer exists.", enfe);
            }
            Collection<Comentarii> comentariiCollection = useri.getComentariiCollection();
            for (Comentarii comentariiCollectionComentarii : comentariiCollection) {
                comentariiCollectionComentarii.setIdUser(null);
                comentariiCollectionComentarii = em.merge(comentariiCollectionComentarii);
            }
            em.remove(useri);
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

    public List<Useri> findUseriEntities() {
        return findUseriEntities(true, -1, -1);
    }

    public List<Useri> findUseriEntities(int maxResults, int firstResult) {
        return findUseriEntities(false, maxResults, firstResult);
    }

    private List<Useri> findUseriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Useri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Useri findUseri(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Useri.class, id);
        } finally {
            em.close();
        }
    }

    public int getUseriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Useri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
