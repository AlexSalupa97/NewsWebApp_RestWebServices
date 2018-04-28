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
import domain.Categorii;
import domain.Autori;
import domain.PozeStiri;
import java.util.ArrayList;
import java.util.Collection;
import domain.Comentarii;
import domain.RandCuvinteCheie;
import domain.Stiri;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author oracle
 */
public class StiriJpaController implements Serializable {

    public StiriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stiri stiri) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (stiri.getPozeStiriCollection() == null) {
            stiri.setPozeStiriCollection(new ArrayList<PozeStiri>());
        }
        if (stiri.getComentariiCollection() == null) {
            stiri.setComentariiCollection(new ArrayList<Comentarii>());
        }
        if (stiri.getRandCuvinteCheieCollection() == null) {
            stiri.setRandCuvinteCheieCollection(new ArrayList<RandCuvinteCheie>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categorii idCategorie = stiri.getIdCategorie();
            if (idCategorie != null) {
                idCategorie = em.getReference(idCategorie.getClass(), idCategorie.getIdCategorie());
                stiri.setIdCategorie(idCategorie);
            }
            Autori idAutor = stiri.getIdAutor();
            if (idAutor != null) {
                idAutor = em.getReference(idAutor.getClass(), idAutor.getIdAutor());
                stiri.setIdAutor(idAutor);
            }
            Collection<PozeStiri> attachedPozeStiriCollection = new ArrayList<PozeStiri>();
            for (PozeStiri pozeStiriCollectionPozeStiriToAttach : stiri.getPozeStiriCollection()) {
                pozeStiriCollectionPozeStiriToAttach = em.getReference(pozeStiriCollectionPozeStiriToAttach.getClass(), pozeStiriCollectionPozeStiriToAttach.getIdPozaStire());
                attachedPozeStiriCollection.add(pozeStiriCollectionPozeStiriToAttach);
            }
            stiri.setPozeStiriCollection(attachedPozeStiriCollection);
            Collection<Comentarii> attachedComentariiCollection = new ArrayList<Comentarii>();
            for (Comentarii comentariiCollectionComentariiToAttach : stiri.getComentariiCollection()) {
                comentariiCollectionComentariiToAttach = em.getReference(comentariiCollectionComentariiToAttach.getClass(), comentariiCollectionComentariiToAttach.getIdComentariu());
                attachedComentariiCollection.add(comentariiCollectionComentariiToAttach);
            }
            stiri.setComentariiCollection(attachedComentariiCollection);
            Collection<RandCuvinteCheie> attachedRandCuvinteCheieCollection = new ArrayList<RandCuvinteCheie>();
            for (RandCuvinteCheie randCuvinteCheieCollectionRandCuvinteCheieToAttach : stiri.getRandCuvinteCheieCollection()) {
                randCuvinteCheieCollectionRandCuvinteCheieToAttach = em.getReference(randCuvinteCheieCollectionRandCuvinteCheieToAttach.getClass(), randCuvinteCheieCollectionRandCuvinteCheieToAttach.getIdRandCuvantCheie());
                attachedRandCuvinteCheieCollection.add(randCuvinteCheieCollectionRandCuvinteCheieToAttach);
            }
            stiri.setRandCuvinteCheieCollection(attachedRandCuvinteCheieCollection);
            em.persist(stiri);
            if (idCategorie != null) {
                idCategorie.getStiriCollection().add(stiri);
                idCategorie = em.merge(idCategorie);
            }
            if (idAutor != null) {
                idAutor.getStiriCollection().add(stiri);
                idAutor = em.merge(idAutor);
            }
            for (PozeStiri pozeStiriCollectionPozeStiri : stiri.getPozeStiriCollection()) {
                Stiri oldIdStireOfPozeStiriCollectionPozeStiri = pozeStiriCollectionPozeStiri.getIdStire();
                pozeStiriCollectionPozeStiri.setIdStire(stiri);
                pozeStiriCollectionPozeStiri = em.merge(pozeStiriCollectionPozeStiri);
                if (oldIdStireOfPozeStiriCollectionPozeStiri != null) {
                    oldIdStireOfPozeStiriCollectionPozeStiri.getPozeStiriCollection().remove(pozeStiriCollectionPozeStiri);
                    oldIdStireOfPozeStiriCollectionPozeStiri = em.merge(oldIdStireOfPozeStiriCollectionPozeStiri);
                }
            }
            for (Comentarii comentariiCollectionComentarii : stiri.getComentariiCollection()) {
                Stiri oldIdStireOfComentariiCollectionComentarii = comentariiCollectionComentarii.getIdStire();
                comentariiCollectionComentarii.setIdStire(stiri);
                comentariiCollectionComentarii = em.merge(comentariiCollectionComentarii);
                if (oldIdStireOfComentariiCollectionComentarii != null) {
                    oldIdStireOfComentariiCollectionComentarii.getComentariiCollection().remove(comentariiCollectionComentarii);
                    oldIdStireOfComentariiCollectionComentarii = em.merge(oldIdStireOfComentariiCollectionComentarii);
                }
            }
            for (RandCuvinteCheie randCuvinteCheieCollectionRandCuvinteCheie : stiri.getRandCuvinteCheieCollection()) {
                Stiri oldIdStireOfRandCuvinteCheieCollectionRandCuvinteCheie = randCuvinteCheieCollectionRandCuvinteCheie.getIdStire();
                randCuvinteCheieCollectionRandCuvinteCheie.setIdStire(stiri);
                randCuvinteCheieCollectionRandCuvinteCheie = em.merge(randCuvinteCheieCollectionRandCuvinteCheie);
                if (oldIdStireOfRandCuvinteCheieCollectionRandCuvinteCheie != null) {
                    oldIdStireOfRandCuvinteCheieCollectionRandCuvinteCheie.getRandCuvinteCheieCollection().remove(randCuvinteCheieCollectionRandCuvinteCheie);
                    oldIdStireOfRandCuvinteCheieCollectionRandCuvinteCheie = em.merge(oldIdStireOfRandCuvinteCheieCollectionRandCuvinteCheie);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findStiri(stiri.getIdStire()) != null) {
                throw new PreexistingEntityException("Stiri " + stiri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stiri stiri) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Stiri persistentStiri = em.find(Stiri.class, stiri.getIdStire());
            Categorii idCategorieOld = persistentStiri.getIdCategorie();
            Categorii idCategorieNew = stiri.getIdCategorie();
            Autori idAutorOld = persistentStiri.getIdAutor();
            Autori idAutorNew = stiri.getIdAutor();
            Collection<PozeStiri> pozeStiriCollectionOld = persistentStiri.getPozeStiriCollection();
            Collection<PozeStiri> pozeStiriCollectionNew = stiri.getPozeStiriCollection();
            Collection<Comentarii> comentariiCollectionOld = persistentStiri.getComentariiCollection();
            Collection<Comentarii> comentariiCollectionNew = stiri.getComentariiCollection();
            Collection<RandCuvinteCheie> randCuvinteCheieCollectionOld = persistentStiri.getRandCuvinteCheieCollection();
            Collection<RandCuvinteCheie> randCuvinteCheieCollectionNew = stiri.getRandCuvinteCheieCollection();
            if (idCategorieNew != null) {
                idCategorieNew = em.getReference(idCategorieNew.getClass(), idCategorieNew.getIdCategorie());
                stiri.setIdCategorie(idCategorieNew);
            }
            if (idAutorNew != null) {
                idAutorNew = em.getReference(idAutorNew.getClass(), idAutorNew.getIdAutor());
                stiri.setIdAutor(idAutorNew);
            }
            Collection<PozeStiri> attachedPozeStiriCollectionNew = new ArrayList<PozeStiri>();
            for (PozeStiri pozeStiriCollectionNewPozeStiriToAttach : pozeStiriCollectionNew) {
                pozeStiriCollectionNewPozeStiriToAttach = em.getReference(pozeStiriCollectionNewPozeStiriToAttach.getClass(), pozeStiriCollectionNewPozeStiriToAttach.getIdPozaStire());
                attachedPozeStiriCollectionNew.add(pozeStiriCollectionNewPozeStiriToAttach);
            }
            pozeStiriCollectionNew = attachedPozeStiriCollectionNew;
            stiri.setPozeStiriCollection(pozeStiriCollectionNew);
            Collection<Comentarii> attachedComentariiCollectionNew = new ArrayList<Comentarii>();
            for (Comentarii comentariiCollectionNewComentariiToAttach : comentariiCollectionNew) {
                comentariiCollectionNewComentariiToAttach = em.getReference(comentariiCollectionNewComentariiToAttach.getClass(), comentariiCollectionNewComentariiToAttach.getIdComentariu());
                attachedComentariiCollectionNew.add(comentariiCollectionNewComentariiToAttach);
            }
            comentariiCollectionNew = attachedComentariiCollectionNew;
            stiri.setComentariiCollection(comentariiCollectionNew);
            Collection<RandCuvinteCheie> attachedRandCuvinteCheieCollectionNew = new ArrayList<RandCuvinteCheie>();
            for (RandCuvinteCheie randCuvinteCheieCollectionNewRandCuvinteCheieToAttach : randCuvinteCheieCollectionNew) {
                randCuvinteCheieCollectionNewRandCuvinteCheieToAttach = em.getReference(randCuvinteCheieCollectionNewRandCuvinteCheieToAttach.getClass(), randCuvinteCheieCollectionNewRandCuvinteCheieToAttach.getIdRandCuvantCheie());
                attachedRandCuvinteCheieCollectionNew.add(randCuvinteCheieCollectionNewRandCuvinteCheieToAttach);
            }
            randCuvinteCheieCollectionNew = attachedRandCuvinteCheieCollectionNew;
            stiri.setRandCuvinteCheieCollection(randCuvinteCheieCollectionNew);
            stiri = em.merge(stiri);
            if (idCategorieOld != null && !idCategorieOld.equals(idCategorieNew)) {
                idCategorieOld.getStiriCollection().remove(stiri);
                idCategorieOld = em.merge(idCategorieOld);
            }
            if (idCategorieNew != null && !idCategorieNew.equals(idCategorieOld)) {
                idCategorieNew.getStiriCollection().add(stiri);
                idCategorieNew = em.merge(idCategorieNew);
            }
            if (idAutorOld != null && !idAutorOld.equals(idAutorNew)) {
                idAutorOld.getStiriCollection().remove(stiri);
                idAutorOld = em.merge(idAutorOld);
            }
            if (idAutorNew != null && !idAutorNew.equals(idAutorOld)) {
                idAutorNew.getStiriCollection().add(stiri);
                idAutorNew = em.merge(idAutorNew);
            }
            for (PozeStiri pozeStiriCollectionOldPozeStiri : pozeStiriCollectionOld) {
                if (!pozeStiriCollectionNew.contains(pozeStiriCollectionOldPozeStiri)) {
                    pozeStiriCollectionOldPozeStiri.setIdStire(null);
                    pozeStiriCollectionOldPozeStiri = em.merge(pozeStiriCollectionOldPozeStiri);
                }
            }
            for (PozeStiri pozeStiriCollectionNewPozeStiri : pozeStiriCollectionNew) {
                if (!pozeStiriCollectionOld.contains(pozeStiriCollectionNewPozeStiri)) {
                    Stiri oldIdStireOfPozeStiriCollectionNewPozeStiri = pozeStiriCollectionNewPozeStiri.getIdStire();
                    pozeStiriCollectionNewPozeStiri.setIdStire(stiri);
                    pozeStiriCollectionNewPozeStiri = em.merge(pozeStiriCollectionNewPozeStiri);
                    if (oldIdStireOfPozeStiriCollectionNewPozeStiri != null && !oldIdStireOfPozeStiriCollectionNewPozeStiri.equals(stiri)) {
                        oldIdStireOfPozeStiriCollectionNewPozeStiri.getPozeStiriCollection().remove(pozeStiriCollectionNewPozeStiri);
                        oldIdStireOfPozeStiriCollectionNewPozeStiri = em.merge(oldIdStireOfPozeStiriCollectionNewPozeStiri);
                    }
                }
            }
            for (Comentarii comentariiCollectionOldComentarii : comentariiCollectionOld) {
                if (!comentariiCollectionNew.contains(comentariiCollectionOldComentarii)) {
                    comentariiCollectionOldComentarii.setIdStire(null);
                    comentariiCollectionOldComentarii = em.merge(comentariiCollectionOldComentarii);
                }
            }
            for (Comentarii comentariiCollectionNewComentarii : comentariiCollectionNew) {
                if (!comentariiCollectionOld.contains(comentariiCollectionNewComentarii)) {
                    Stiri oldIdStireOfComentariiCollectionNewComentarii = comentariiCollectionNewComentarii.getIdStire();
                    comentariiCollectionNewComentarii.setIdStire(stiri);
                    comentariiCollectionNewComentarii = em.merge(comentariiCollectionNewComentarii);
                    if (oldIdStireOfComentariiCollectionNewComentarii != null && !oldIdStireOfComentariiCollectionNewComentarii.equals(stiri)) {
                        oldIdStireOfComentariiCollectionNewComentarii.getComentariiCollection().remove(comentariiCollectionNewComentarii);
                        oldIdStireOfComentariiCollectionNewComentarii = em.merge(oldIdStireOfComentariiCollectionNewComentarii);
                    }
                }
            }
            for (RandCuvinteCheie randCuvinteCheieCollectionOldRandCuvinteCheie : randCuvinteCheieCollectionOld) {
                if (!randCuvinteCheieCollectionNew.contains(randCuvinteCheieCollectionOldRandCuvinteCheie)) {
                    randCuvinteCheieCollectionOldRandCuvinteCheie.setIdStire(null);
                    randCuvinteCheieCollectionOldRandCuvinteCheie = em.merge(randCuvinteCheieCollectionOldRandCuvinteCheie);
                }
            }
            for (RandCuvinteCheie randCuvinteCheieCollectionNewRandCuvinteCheie : randCuvinteCheieCollectionNew) {
                if (!randCuvinteCheieCollectionOld.contains(randCuvinteCheieCollectionNewRandCuvinteCheie)) {
                    Stiri oldIdStireOfRandCuvinteCheieCollectionNewRandCuvinteCheie = randCuvinteCheieCollectionNewRandCuvinteCheie.getIdStire();
                    randCuvinteCheieCollectionNewRandCuvinteCheie.setIdStire(stiri);
                    randCuvinteCheieCollectionNewRandCuvinteCheie = em.merge(randCuvinteCheieCollectionNewRandCuvinteCheie);
                    if (oldIdStireOfRandCuvinteCheieCollectionNewRandCuvinteCheie != null && !oldIdStireOfRandCuvinteCheieCollectionNewRandCuvinteCheie.equals(stiri)) {
                        oldIdStireOfRandCuvinteCheieCollectionNewRandCuvinteCheie.getRandCuvinteCheieCollection().remove(randCuvinteCheieCollectionNewRandCuvinteCheie);
                        oldIdStireOfRandCuvinteCheieCollectionNewRandCuvinteCheie = em.merge(oldIdStireOfRandCuvinteCheieCollectionNewRandCuvinteCheie);
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
                Short id = stiri.getIdStire();
                if (findStiri(id) == null) {
                    throw new NonexistentEntityException("The stiri with id " + id + " no longer exists.");
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
            Stiri stiri;
            try {
                stiri = em.getReference(Stiri.class, id);
                stiri.getIdStire();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stiri with id " + id + " no longer exists.", enfe);
            }
            Categorii idCategorie = stiri.getIdCategorie();
            if (idCategorie != null) {
                idCategorie.getStiriCollection().remove(stiri);
                idCategorie = em.merge(idCategorie);
            }
            Autori idAutor = stiri.getIdAutor();
            if (idAutor != null) {
                idAutor.getStiriCollection().remove(stiri);
                idAutor = em.merge(idAutor);
            }
            Collection<PozeStiri> pozeStiriCollection = stiri.getPozeStiriCollection();
            for (PozeStiri pozeStiriCollectionPozeStiri : pozeStiriCollection) {
                pozeStiriCollectionPozeStiri.setIdStire(null);
                pozeStiriCollectionPozeStiri = em.merge(pozeStiriCollectionPozeStiri);
            }
            Collection<Comentarii> comentariiCollection = stiri.getComentariiCollection();
            for (Comentarii comentariiCollectionComentarii : comentariiCollection) {
                comentariiCollectionComentarii.setIdStire(null);
                comentariiCollectionComentarii = em.merge(comentariiCollectionComentarii);
            }
            Collection<RandCuvinteCheie> randCuvinteCheieCollection = stiri.getRandCuvinteCheieCollection();
            for (RandCuvinteCheie randCuvinteCheieCollectionRandCuvinteCheie : randCuvinteCheieCollection) {
                randCuvinteCheieCollectionRandCuvinteCheie.setIdStire(null);
                randCuvinteCheieCollectionRandCuvinteCheie = em.merge(randCuvinteCheieCollectionRandCuvinteCheie);
            }
            em.remove(stiri);
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

    public List<Stiri> findStiriEntities() {
        return findStiriEntities(true, -1, -1);
    }

    public List<Stiri> findStiriEntities(int maxResults, int firstResult) {
        return findStiriEntities(false, maxResults, firstResult);
    }

    private List<Stiri> findStiriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Stiri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Stiri findStiri(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stiri.class, id);
        } finally {
            em.close();
        }
    }

    public int getStiriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Stiri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
