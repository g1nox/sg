package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Pedido;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author EduardoAlexis
 */
@Stateless
public class PedidoFacade extends AbstractFacade<Pedido> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoFacade() {
        super(Pedido.class);
    }
    
    
//     public List<Usuario> usuariosActivos(String usuario) {
//        // me tiene que devolver una lista con los usuarios activos
//        final Estado estado = ef.find(1);
//        final String admin = "admin";
//        return getEntityManager().createQuery("Select u FROM Usuario u Where u.username <> ?1 AND u.username <> ?2 AND u.idEstado = ?3")
//                .setParameter(1, usuario)
//                .setParameter(2, admin)
//                .setParameter(3, estado)
//                .getResultList();
//
//    }
//    
//    public Pedido findLastPedido (){
//    Pedido pedido = (Pedido)getEntityManager().createQuery("SELECT v FROM Pedido v Where v.idPedido = (SELECT MAX (vv.idPedido) FROM Pedido vv)").getSingleResult();
//    return pedido;
//    }
    
    
    public List<Pedido> findOrderBy() {
          
          List <Pedido> p  = null;
          p = (List<Pedido>)getEntityManager().createQuery ("SELECT  p  FROM  Pedido p ORDER BY p.idPedido DESC ").getResultList();
         return p;
      
      }
  
}
