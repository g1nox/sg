package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Categoria;
import com.egs.webapp.entities.Producto;
import com.egs.webapp.entities.Proveedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductoFacade extends AbstractFacade<Producto> {

    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Inject
    CategoriaFacade cf;
    
    @Inject
    ProveedorFacade pf;

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

        List<Producto> pro = null;
        pro = (List<Producto>) getEntityManager().createQuery("SELECT  p  FROM  Producto  p WHERE p.disponible = TRUE").getResultList();

        return pro;

    }
    
     public List<Producto> findNocompuesto() {

        List<Producto> pro = null;
        pro = (List<Producto>) getEntityManager().createQuery("SELECT  p  FROM  Producto  p WHERE p.compuesto = FALSE").getResultList();

        return pro;

    }

    public List<Object> productomasvendido() {

        Query q;
        q = getEntityManager().createQuery("SELECT p, sum(dp.cantArt) as total "
                + " FROM Producto p, DetallePedido dp, Pedido pe, Venta v "
                + " where p = dp.idProducto "
                + " and dp.idPedido = pe "
                + " and pe = v.idPedido  "
                + " group by p.idProducto "
                + " order by total desc ");

        List <Object> resultList = q.getResultList();

        return resultList;
    }

    
      public List<Object> productomasvendidopormes() {

        Query q;
        q = getEntityManager().createQuery(" select p.nombre, extract(month v.fecha), extract(year v.fecha), sum(dp.cantArt)"
                                         + " from DetallePedido dp, Pedido pe, Venta v, Producto p "
                                         + " where pe = dp.idPedido "
                                         + " and pe = v.idPedido"
                                         + " and dp.idProducto = p"
                                         + " group by p.idProducto, extract(month v.fecha), extract(year v.fecha)"
                                         + " order by extract(year v.fecha) desc, extract(month v.fecha), sum(dp.cantArt) desc");

        List <Object> resultList = q.getResultList();

        return resultList;
    }
      
      public List<Object[]> toppormes(double mes, double año) {

        List <Object[]> resultList = null;
          
         
        resultList = (List<Object[]>) getEntityManager().
                createQuery("select p.nombre, sum(dp.cantArt)"
                                         + " from DetallePedido dp, Pedido pe, Venta v, Producto p "
                                         + " where pe = dp.idPedido "
                                         + " and pe = v.idPedido"
                                         + " and dp.idProducto = p "
                                         + " and extract(month v.fecha) = ?1 "
                                         + " and extract(year v.fecha) =  ?2 "
                                         + " group by p.idProducto "
                                         + " order by sum(dp.cantArt) desc ").
                setParameter(1, mes).
                setParameter(2, año).
                setMaxResults(3).
                getResultList();

        

        return resultList;
    }
      
      
      
         public List<Object> mesyaño() {

        Query q;
        q = getEntityManager().createQuery("SELECT extract(month v.fecha), extract(year v.fecha)"
                + " FROM Venta v"
                + " group by extract(month v.fecha), extract(year v.fecha)");

        List<Object> resultList = q.getResultList();

        return resultList;
    }
      
      
          public List<Producto> findProdCategoria(int idCategoria) {

        try {

            Categoria prod = cf.find(idCategoria);
            
            List<Producto> resultList = (List<Producto>)getEntityManager().createQuery("SELECT  p  FROM  Producto  p WHERE p.idCategoria = ?1 ")
                    .setParameter(1, prod)
                    .getResultList();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }
          
         public List<Producto> findProdProveedor(int idProveedor) {

        try {

            Proveedor prod = pf.find(idProveedor);

            List<Producto> resultList = (List<Producto>) getEntityManager().createQuery("SELECT  p  FROM  Producto  p WHERE p.idProveedor = ?1 ")
                    .setParameter(1, prod)
                    .getResultList();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public int stockAcualProducto(int idProducto) {

        Object result;

        result = getEntityManager().
                createQuery("SELECT p.stockActual FROM Producto p WHERE p.idProducto = ?1 ").
                setParameter(1, idProducto).
                getSingleResult();
        int var = (int) result;

        return var;
    }
    
        public Producto findProducto(String nombre) {
        try {
            Producto result = (Producto) getEntityManager().
                    createQuery("SELECT p FROM Producto p WHERE p.nombre = ?1 ")
                    .setParameter(1, nombre)
                    .getSingleResult();

            return result;
        } catch (Exception e) {

            return null;
        }
    }

  
    
}
