package main.java.persistence;

import main.java.domain.CoffeeSort;
import main.java.exceptions.AppPersistenceException;

import javax.persistence.*;
import java.util.List;


public class CoffeeSortPersistence {

    @PersistenceUnit
    private EntityManagerFactory factory;
    @PersistenceContext
    private EntityManager em;


    public void getEntityManager() {
        factory = Persistence.createEntityManagerFactory("COFFEE_SORT_APP_PU");
        em = factory.createEntityManager();
    }


    public List<CoffeeSort> getAll()
            throws AppPersistenceException {

        getEntityManager();

        try {
            TypedQuery<CoffeeSort> query =
                    em.createQuery("SELECT n FROM CoffeeSort n", CoffeeSort.class);
            List<CoffeeSort> notes = query.getResultList();
            return notes;
        } catch (PersistenceException ex) {

            throw new AppPersistenceException();
        } finally {
            em.close();
            factory.close();
        }
    }

    public CoffeeSort create(CoffeeSort n)
            throws AppPersistenceException {

        getEntityManager();
        em.getTransaction().begin();

        try {
            em.persist(n);
            em.getTransaction().commit();
        } catch (PersistenceException ex) {

            Throwable t = null;
            for (t = ex.getCause(); t != null; t = t.getCause())
                System.out.println("Exception: " + t.getClass());
            throw new AppPersistenceException();
        } finally {
            em.close();
            factory.close();
        }

        return n;
    }

    public void update(CoffeeSort updatedN)
            throws AppPersistenceException {

        getEntityManager();
        em.getTransaction().begin();

        try {
            em.find(CoffeeSort.class, updatedN.getId());
            em.merge(updatedN);
            em.getTransaction().commit();
        } catch (PersistenceException ex) {

            throw new AppPersistenceException();
        } finally {
            em.close();
            factory.close();
        }
    }

    public void delete(long key)
            throws AppPersistenceException {

        getEntityManager();
        em.getTransaction().begin();

        try {
            CoffeeSort n = em.find(CoffeeSort.class, key);
            em.remove(n);
            em.getTransaction().commit();
        } catch (PersistenceException ex) {

            throw new AppPersistenceException();
        } finally {
            em.close();
            factory.close();
        }
    }
}