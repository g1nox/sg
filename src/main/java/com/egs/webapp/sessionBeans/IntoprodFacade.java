package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Intoprod;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class IntoprodFacade extends AbstractFacade<Intoprod> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IntoprodFacade() {
        super(Intoprod.class);
    }
    
      public List<Intoprod> findforDate(Date fechaConsulta) {
          
          List <Intoprod> i  = null;
          
          i = (List<Intoprod>)getEntityManager().
              createQuery ("SELECT  i  FROM  Intoprod i WHERE i.fecha = ?1 ").
              setParameter(1, fechaConsulta).
              getResultList();
         
          return i;
      
      }
    
}

