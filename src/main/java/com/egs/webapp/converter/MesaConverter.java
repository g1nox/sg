/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egs.webapp.converter;

import com.egs.webapp.entities.Mesa;
import com.egs.webapp.sessionBeans.MesaFacade;
import com.egs.webapp.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author EduardoAlexis
 */
@FacesConverter(value = "mesaConverter")
public class MesaConverter implements Converter{
@Inject
    private MesaFacade ejbFacade;
    @Override
    
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }
    
    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.parseInt(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof Mesa) {
            Mesa o = (Mesa) object;
            return getStringKey(o.getIdMesa());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Mesa.class.getName()});
            return null;
        }
    }

    
    
}
