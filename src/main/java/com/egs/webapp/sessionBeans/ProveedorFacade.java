/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egs.webapp.sessionBeans;

import com.egs.webapp.entities.Proveedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



@Stateless
public class ProveedorFacade extends AbstractFacade<Proveedor> {
    @PersistenceContext(unitName = "com.egs.webapp_HabanaSalsa_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProveedorFacade() {
        super(Proveedor.class);
    }
    
       public Proveedor findCategoria(String nombre) {
        try {
            Proveedor result = (Proveedor) getEntityManager().
                    createQuery("SELECT p FROM Proveedor p WHERE p.nombre = ?1 ")
                    .setParameter(1, nombre)
                    .getSingleResult();

            return result;
        } catch (Exception e) {

            return null;
        }
    }
    
}
