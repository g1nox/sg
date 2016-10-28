package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.DetallePedido;
import com.egs.webapp.entities.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class DetallepedidoFacade extends AbstractFacade<DetallePedido> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Inject
    ProductoFacade pf;

    public DetallepedidoFacade() {
        super(DetallePedido.class);
    }
    
        public List<DetallePedido> findProdDetalle(int idProducto) {

        try {

            Producto producto = pf.find(idProducto);
            
            List<DetallePedido> resultList = (List<DetallePedido>)getEntityManager().createQuery("SELECT  dp  FROM  DetallePedido dp WHERE dp.idProducto = ?1 ")
                    .setParameter(1, producto)
                    .getResultList();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }
       
}
