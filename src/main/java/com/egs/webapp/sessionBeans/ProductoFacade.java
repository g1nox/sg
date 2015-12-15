package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class ProductoFacade extends AbstractFacade<Producto> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }
    
//    public Producto findLastProducto (){
//        
//        Producto producto = (Producto)getEntityManager().createQuery("SELECT v FROM Producto v Where v.idProducto = (SELECT MAX (vv.idProducto) FROM Producto vv)").getSingleResult();
//        
//        return producto;
//    }
    
    
    public List<Producto> findbyActivo() {
          
        List <Producto> pro  = null;
        pro = (List<Producto>)getEntityManager().createQuery ("SELECT  p  FROM  Producto  p WHERE p.disponible = TRUE").getResultList();
    
        return pro;
      
      }
 
      
    
}
