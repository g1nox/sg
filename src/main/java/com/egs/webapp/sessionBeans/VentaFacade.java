package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Venta;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class VentaFacade extends AbstractFacade<Venta> {

    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    

    public VentaFacade() {
        super(Venta.class);
    }

    public List<Venta> findforDate(Date fechaConsulta) {

        List<Venta> v = null;

        v = (List<Venta>) getEntityManager().
                createQuery("SELECT  v  FROM  Venta v WHERE v.fecha = ?1 ").
                setParameter(1, fechaConsulta).
                getResultList();

        return v;

    }
    
       public List<Venta> findOrderBy() {
          
          List <Venta> p  = null;
          p = (List<Venta>)getEntityManager().createQuery ("SELECT  v  FROM  Venta v ORDER BY v.idVenta DESC ").getResultList();
         return p;
      
      }

    public long totalventadiaria(Date fechaActual) {

        Object result;

        result = getEntityManager().
                createQuery("SELECT sum(v.total) FROM Venta v WHERE v.fecha = ?1 ").
                setParameter(1, fechaActual).
                getSingleResult();
        if (result != null) {
            long var = (long) result;
            return var;
        } else {
            long var = 0;
            return var;
        }
    }

         public List<Object[]> totalventapormes() {

        Query q;
        q = getEntityManager().createQuery("select extract(month v.fecha) as mes,"
                + " extract(year v.fecha) as año,"
                + " sum(v.total)"
                + " from Venta v "
                + " group by mes, año"
                + " order by año desc, mes desc ");

        List<Object[]> resultList = q.getResultList();

        return resultList;
    }
         
    
    public long findforParameter(double mes, double ano) {

        Object result;

        result = getEntityManager().
                createQuery("SELECT sum(v.total) as total"
                        + " FROM Venta v"
                        + " WHERE (EXTRACT(MONTH v.fecha)) = ?1"
                        + " AND (EXTRACT(YEAR v.fecha)) = ?2 ").
                setParameter(1, mes).
                setParameter(2, ano).
                getSingleResult();
        long var = (long) result;
        return var;
    }

    public List<Venta> ventaxmes(double mes, double ano) {

        List<Venta> v = null;

        v = (List<Venta>) getEntityManager().
                createQuery("SELECT dp.idProducto, sum(dp.cantArt) as cant, sum(dp.precioTotal)"
                        + " from DetallePedido dp, Venta v"
                        + " where dp.idPedido = v.idPedido"
                        + " and (EXTRACT(MONTH v.fecha)) = ?1 AND (EXTRACT(YEAR v.fecha)) = ?2 "
                        + " group by dp.idProducto"
                        + " order by cant desc ").
                setParameter(1, mes).
                setParameter(2, ano).
                getResultList();

        return v;

    }

    public List<Object[]> meserotopxmes(double mes, double año) {

         List <Object[]> resultList = null;
         
         resultList = (List<Object[]>) getEntityManager().createQuery(" SELECT u.username,"
              
                + " SUM(v.total) "
                + " FROM Venta v, Usuario u "
                + " WHERE v.idUsuario = u "
                + " AND EXTRACT(MONTH v.fecha) = ?1 "
                + " AND EXTRACT(YEAR v.fecha) = ?2 "
                + " GROUP BY u.idUsuario "
                + " ORDER BY SUM(v.total) DESC ").
                setParameter(1, mes).
                setParameter(2, año).
                setMaxResults(3).
                getResultList();

        

        return resultList;
    }
    

    public List<Object> meserotop() {

        Query q;
        q = getEntityManager().createQuery("SELECT u, sum(v.total) as total"
                + " FROM Venta v, Usuario u "
                + " where v.idUsuario = u"
                + " group by u.idUsuario order by total desc ");

        List<Object> resultList = q.getResultList();

        return resultList;
    }

    public List<Object> arqueo(Date fechaConsulta) {

        Query q;
        q = getEntityManager().createQuery("SELECT dp.idProducto, sum(dp.cantArt) as cant, sum(dp.precioTotal)"
                + " from DetallePedido dp, Venta v"
                + " where dp.idPedido = v.idPedido"
                + " and (v.fecha) = ?1 "
                + " group by dp.idProducto"
                + " order by cant desc ").
                setParameter(1, fechaConsulta);

        List<Object> resultList = q.getResultList();

        return resultList;
    }

    public List<Object> resumen(Date fechaConsulta) {

        Query q;
        q = getEntityManager().createQuery("SELECT  v.medioPago, sum(dp.precioTotal)"
                + " from DetallePedido dp, Venta v"
                + " where dp.idPedido = v.idPedido"
                + " and (v.fecha) = ?1"
                + " group by v.medioPago"
                + " order by v.medioPago asc").
                setParameter(1, fechaConsulta);

        List<Object> resultList = q.getResultList();

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

}
