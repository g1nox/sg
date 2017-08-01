package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Mesa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MesaFacade extends AbstractFacade<Mesa> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MesaFacade() {
        super(Mesa.class);
    }
    
    public List<Mesa> findbyDisponible() {
          
        List <Mesa> m  = null;
        m = (List<Mesa>)getEntityManager().createQuery ("SELECT  p  FROM  Mesa  p WHERE p.estado = FALSE").getResultList();
    
        return m;
      
      }
    
       public Mesa findMesa(String nombre) {
        try {
            Mesa result = (Mesa) getEntityManager().
                    createQuery("SELECT m FROM Mesa m WHERE m.nombre = ?1 ")
                    .setParameter(1, nombre)
                    .getSingleResult();

            return result;
        } catch (Exception e) {

            return null;
        }
    }
    
}