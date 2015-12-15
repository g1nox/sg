/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Rol;
import com.egs.webapp.sessionBeans.RolFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author EduardoAlexis
 */
@Named(value = "rolController")
@SessionScoped
public class RolController implements Serializable {
   
    @EJB
    private RolFacade rolFacade;
    private Rol selected ; 
    
    private List<Rol> items = null;
    
    public RolController() {
    }

    public RolFacade getRolFacade() {
        return rolFacade;
    }

    public List<Rol> getItems() {
        if (items==null){
        items=getRolFacade().findAll();
        }
        return items;
    }

    public Rol getSelected() {
        return selected;
    }

    public void setSelected(Rol selected) {
        this.selected = selected;
    }
    
    
}
