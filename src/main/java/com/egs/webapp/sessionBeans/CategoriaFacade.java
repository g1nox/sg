package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Categoria;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CategoriaFacade extends AbstractFacade<Categoria> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaFacade() {
        super(Categoria.class);
    }
    
    public Categoria findCategoria(String nombre) {
        try {
            Categoria result = (Categoria) getEntityManager().
                    createQuery("SELECT c FROM Categoria c WHERE c.nombre = ?1 ")
                    .setParameter(1, nombre)
                    .getSingleResult();

            return result;
        } catch (Exception e) {

            return null;
        }
    }
    
    
}
