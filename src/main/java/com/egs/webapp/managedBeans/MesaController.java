package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Mesa;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.MesaFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("mesaController")
@SessionScoped
public class MesaController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.MesaFacade ejbFacade;
    private List<Mesa> items = null;
    private Mesa selected;
    
    @EJB
    private MesaFacade mesaFacade;
    
    @Inject
    private PedidoController currentpedido;

    public PedidoController getCurrentpedido() {
        return currentpedido;
    }

    public void setCurrentpedido(PedidoController currentpedido) {
        this.currentpedido = currentpedido;
    }
    
    public MesaController() {
    }

    public Mesa getSelected() {
        return selected;
    }

    public void setSelected(Mesa selected) {
        this.selected = selected;
    }
    
    
      public MesaFacade getMesaFacade() {
        return mesaFacade;
    }
      
      public void setMesaFacade(MesaFacade mesaFacade) {
        this.mesaFacade = mesaFacade;
    }   

    

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MesaFacade getFacade() {
        return ejbFacade;
    }

    public Mesa prepareCreate() {
        selected = new Mesa();
        initializeEmbeddableKey();
        return selected;
    }

    public String create() {
            persist(PersistAction.CREATE,  "");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goMesaCreate();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MesaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MesaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Mesa> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    
                    
                    int num = 0;

                
                    items.add(selected);
                
               
                selected = new Mesa();  
                    
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Mesa getMesa(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Mesa> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Mesa> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Mesa.class)
    public static class MesaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MesaController controller = (MesaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mesaController");
            return controller.getMesa(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
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
               public String goMesaCreate(){
               prepareCreate();
               return "mesa-create";
    }
               public String goMesaList(){
               prepareCreate();
               return "mesa-list";
    }
               
          public void llamarEditarMesa(){
        
        if (selected != null)  {
        
        editarMesa(selected, "editado Correctamente");
        
        } else{JsfUtil.addErrorMessage("Error al editar ");}
    }
        
         public void editarMesa(Mesa currentMesa, String successMessage) {
        if (currentMesa != null) {
            
            try {
                getMesaFacade().edit(currentMesa);
                JsfUtil.addSuccessMessage(successMessage);
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   }        
         
         public void llamarEdit(){

          getFacade().edit(selected);
          
        }
         
         
        
               
               
               
               
}
