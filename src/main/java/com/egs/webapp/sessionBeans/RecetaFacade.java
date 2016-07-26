package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Ingrediente;
import com.egs.webapp.entities.Producto;
import com.egs.webapp.entities.Receta;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RecetaFacade extends AbstractFacade<Receta> {

    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Inject
    ProductoFacade pf;
    
    @Inject
    IngredienteFacade ingf;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecetaFacade() {
        super(Receta.class);
    }

    public List<Receta> recetaidProducto(int idProducto) {

        Producto prod = pf.find(idProducto);
        
        List<Receta> out = (List<Receta>)getEntityManager().createQuery("Select r FROM Receta r Where r.idProducto = ?1 ")
                .setParameter(1, prod)
                .getResultList();
        return out;
    }
    
             public List<Receta> findIngReceta(int idIngrediente) {

        try {

            Ingrediente ingrediente = ingf.find(idIngrediente);
            
            List<Receta> resultList = (List<Receta>)getEntityManager().createQuery("SELECT  r  FROM  Receta  r WHERE r.idIngrediente = ?1 ")
                    .setParameter(1, ingrediente)
                    .getResultList();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }
    
     

}


