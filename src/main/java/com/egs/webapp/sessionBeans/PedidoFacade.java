package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Mesa;
import com.egs.webapp.entities.Pedido;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class PedidoFacade extends AbstractFacade<Pedido> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Inject
    MesaFacade mf;

    public PedidoFacade() {
        super(Pedido.class);
    }
    

//    public Pedido findLastPedido (){
//    Pedido pedido = (Pedido)getEntityManager().createQuery("SELECT v FROM Pedido v Where v.idPedido = (SELECT MAX (vv.idPedido) FROM Pedido vv)").getSingleResult();
//    return pedido;
//    }
    
    
    public List<Pedido> findOrderBy() {
          
          List <Pedido> p  = null;
          p = (List<Pedido>)getEntityManager().createQuery ("SELECT  p  FROM  Pedido p ORDER BY p.idPedido DESC ").getResultList();
         return p;
      
      }
    
//      public List<Pedido> findDate() {
//          
//          List <Pedido> p  = null;
//          
//          p = (List<Pedido>)getEntityManager().
//              createQuery ("SELECT  p  FROM  Pedido p WHERE p.fecha BETWEEN '2016-03-19' AND '2016-03-22' ").
//              getResultList();
//         
//          return p;
//      
//      }
      
      public List<Pedido> findDate(Date fechaConsulta) {
          
          List <Pedido> p  = null;
          
          p = (List<Pedido>)getEntityManager().
              createQuery ("SELECT  p  FROM  Pedido p WHERE p.fecha = ?1 ").
              setParameter(1, fechaConsulta).
              getResultList();
         
          return p;
      
      }
      
      public List<Pedido> today(Date fechaHoy) {
          
          List <Pedido> p  = null;
          
          p = (List<Pedido>)getEntityManager().
              createQuery ("SELECT  p  FROM  Pedido p WHERE p.fecha = ?1 ").
              setParameter(1, fechaHoy).
              getResultList();
         
          return p;
      
      }
      
      public List<Pedido> todayMesero(long idUsuario, Date fechaActual) {
          
          List <Pedido> p  = null;
          
          p = (List<Pedido>)getEntityManager().createQuery ("SELECT  p  FROM  Pedido p, Usuario u "
                                                          + " Where p.idUsuario = u "
                                                          + " and u.idUsuario = ?1 "
                                                          + " and p.fecha = ?2 ")
                  .setParameter(1, idUsuario)
                  .setParameter(2, fechaActual)
                  .getResultList();
         
          return p;
      
      }
      
           public List<Pedido> findidmesa(int idMesa) {

        try {

            Mesa mesa = mf.find(idMesa);
            
            List<Pedido> resultList = (List<Pedido>)getEntityManager().createQuery("SELECT  p  FROM  Pedido  p WHERE p.idMesa = ?1 ")
                    .setParameter(1, mesa)
                    .getResultList();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }
    
    
   
  
}
