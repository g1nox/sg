package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Ingrediente;
import com.egs.webapp.entities.Receta;
import com.egs.webapp.sessionBeans.IngredienteFacade;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;


@Named(value = "ingredienteController")
@SessionScoped
public class IngredienteController implements Serializable {

    /**
     * Creates a new instance of IngredienteController
     */
    
    @EJB
    private com.egs.webapp.sessionBeans.IngredienteFacade ejbFacade;
    private List<Ingrediente> items = null;
    private Ingrediente selected;
    
    @Inject
    private RecetaController currentReceta;

    public RecetaController getCurrentReceta() {
        return currentReceta;
    }

    public void setCurrentReceta(RecetaController currentReceta) {
        this.currentReceta = currentReceta;
    }
    
    
    @EJB
    
    private IngredienteFacade ingredienteFacade;

    public void setSelected(Ingrediente selected) {
        this.selected = selected;
    }
    
   
    public IngredienteController() {
    }

   public IngredienteFacade getIngredienteFacade() {
        return ingredienteFacade;
    }
    
   private IngredienteFacade getFacade() {
        return ejbFacade;
    } 
   
    
    public List<Ingrediente> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Ingrediente getSelected() {
        return selected;
    }
    
     public Ingrediente prepareCreate() {
        selected = new Ingrediente();
        currentReceta.init();
      
        return selected;
    }
     
     protected void setEmbeddableKeys() {
    }
     
     public void init (){
         
        if (selected == null){
         selected = new Ingrediente();
         }
         
     }
     
    public String create() {
        persist(JsfUtil.PersistAction.CREATE,  "ingrediente se creo");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
             FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goIngredienteCreate();
        }
        return null;
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
     private void persist(PersistAction persistAction, String successMessage) {
          if (selected != null) {
           
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
           
                    getFacade().edit(selected);
                } else {
                   getFacade().remove(selected);
               }
                
                prepareCreate();
                currentReceta.reinit();
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
    
    
    public Ingrediente getIngrediente(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Ingrediente> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ingrediente> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Ingrediente.class)
    public static class IngredienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ingredienteController");
            return controller.getProducto(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Ingrediente) {
                Ingrediente o = (Ingrediente) object;
                return getStringKey(o.getIdIngrediente());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Ingrediente.class.getName()});
                return null;
            }
        }

    }
    
    public String goIngredienteCreate(){
    prepareCreate();
    return "ingrediente-create";
    }
    
    public String goIngredienteNew(){
    prepareCreate();
    return "ingrediente-new";
    }
    
    public String goIngredienteList(){
    return "ingrediente-list";
    }


}
