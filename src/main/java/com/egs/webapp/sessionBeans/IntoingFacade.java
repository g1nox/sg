package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Intoing;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class IntoingFacade extends AbstractFacade<Intoing> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IntoingFacade() {
        super(Intoing.class);
    }
    
        public List<Intoing> findforDateing(Date fechaConsulta) {
          
          List <Intoing> i  = null;
          
          i = (List<Intoing>)getEntityManager().
              createQuery ("SELECT  i  FROM  Intoing i WHERE i.fecha = ?1 ").
              setParameter(1, fechaConsulta).
              getResultList();
         
          return i;
      
      }
        
         public List<Intoing> findOrderBy() {
          
          List <Intoing> p  = null;
          p = (List<Intoing>)getEntityManager().createQuery ("SELECT  i  FROM  Intoing i ORDER BY i.idIntoing DESC ").getResultList();
         return p;
      
      }
    
}