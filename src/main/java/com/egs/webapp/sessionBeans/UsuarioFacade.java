package com.egs.webapp.sessionBeans;


import com.egs.webapp.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
   
    // find user by username 
     public Usuario findUsuarioByUsername(String username) {

        try {
            Usuario usuario = (Usuario) getEntityManager().createNamedQuery("Usuario.findByUsername").
                    setParameter("username", username).
                    getSingleResult();
          
            return usuario;
       
        } catch (NoResultException nre) {

            return null;

        }
    }
    
     public List<Object> meseroTop() {

        Query q;
        
        q = getEntityManager().createQuery("select u, sum(v.total) as total"
                                          +  " from Venta v, Usuario u"
                                          +  " where u = v.idUsuario"
                                          +  " group by u.idUsuario"
                                          + " order by total desc");

        List <Object> resultList = q.getResultList();

        return resultList;
    }
    
    
    
}
