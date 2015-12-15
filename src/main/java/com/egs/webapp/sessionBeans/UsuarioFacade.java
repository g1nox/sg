package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Estado;
import com.egs.webapp.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author EduardoAlexis
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @Inject
    EstadoFacade ef;

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
            Usuario usuario = (Usuario) getEntityManager().createNamedQuery("Usuario.findByUsername").setParameter("username", username).getSingleResult();
            return usuario;
        } catch (NoResultException nre) {

            return null;

        }

    }
    
     public List<Usuario> usuariosActivos(String usuario) {
        // me tiene que devolver una lista con los usuarios activos
        final Estado estado = ef.find(1);
        final String admin = "admin";
        return getEntityManager().createQuery("Select u FROM Usuario u Where u.username <> ?1 AND u.username <> ?2 AND u.idEstado = ?3")
                .setParameter(1, usuario)
                .setParameter(2, admin)
                .setParameter(3, estado)
                .getResultList();

    }
    
    
    
}
