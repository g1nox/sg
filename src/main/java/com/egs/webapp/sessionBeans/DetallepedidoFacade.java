package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Detallepedido;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class DetallepedidoFacade extends AbstractFacade<Detallepedido> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetallepedidoFacade() {
        super(Detallepedido.class);
    }
       
}
