package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Producto;
import com.egs.webapp.entities.Proveedor;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.ProveedorFacade;

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
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.context.Flash;
import javax.inject.Inject;

@Named("proveedorController")
@SessionScoped
public class ProveedorController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.ProveedorFacade ejbFacade;
    private List<Proveedor> items = null;
    private Proveedor selected;
    
    private Proveedor nombreprov;
    private List <Producto> prodconproveedor;
    
    @EJB
    private ProveedorFacade proveedorFacade;

    public ProveedorFacade getProveedorFacade() {
        return proveedorFacade;
    }

    public void setProveedorFacade(ProveedorFacade proveedorFacade) {
        this.proveedorFacade = proveedorFacade;
    }
      
    @Inject
      
    private ProductoController currentproducto;

    public ProductoController getCurrentproducto() {
        return currentproducto;
    }

    public void setCurrentproducto(ProductoController currentproducto) {
        this.currentproducto = currentproducto;
    }
    
    public ProveedorController() {
    }

    public Proveedor getSelected() {
        return selected;
    }

    public void setSelected(Proveedor selected) {
        this.selected = selected;
    }

    public Proveedor getNombreprov() {
        return nombreprov;
    }

    public void setNombreprov(Proveedor nombreprov) {
        this.nombreprov = nombreprov;
    }

    public List<Producto> getProdconproveedor() {
        return prodconproveedor;
    }

    public void setProdconproveedor(List<Producto> prodconproveedor) {
        this.prodconproveedor = prodconproveedor;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProveedorFacade getFacade() {
        return ejbFacade;
    }

    public Proveedor prepareCreate() {
        selected = new Proveedor();
        initializeEmbeddableKey();
        return selected;
    }

      public String create() {
            persist(PersistAction.CREATE,  "el proveedor se ha creado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goProveedorCreate();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProveedorUpdated"));
    }

    public void destroy() {
         persist(PersistAction.DELETE, "El proveedor se ha eliminado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Proveedor> getItems() {
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
                    
                    String nombre = getSelected().getNombre();
                    
                    nombreprov = ejbFacade.findProveedor(nombre);
                    
                    if(nombreprov == null){
                    
                    getFacade().edit(selected);
                     JsfUtil.addSuccessMessage(successMessage);
                    }else {
                        JsfUtil.addErrorMessage("El proveedor ya existe");
                    }
                }
                else {
                    int idprov = getSelected().getIdProveedor();
                    prodconproveedor = currentproducto.getEjbFacade().findProdProveedor(idprov);
                    
                    if (prodconproveedor.isEmpty()) {
                        getFacade().remove(selected);
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("No se puede eliminar el proveedor porque tiene registros asociados");
                    }
                }
            } 
             
                catch (EJBException ex) {
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

    public Proveedor getProveedor(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Proveedor> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Proveedor> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Proveedor.class)
    public static class ProveedorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedorController controller = (ProveedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedorController");
            return controller.getProveedor(getKey(value));
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
            if (object instanceof Proveedor) {
                Proveedor o = (Proveedor) object;
                return getStringKey(o.getIdProveedor());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Proveedor.class.getName()});
                return null;
            }
        }

    }
    
               public String goProveedorCreate(){
               prepareCreate();
               return "proveedor-create";
    }
               public String goProveedorList(){
               prepareCreate();
               return "proveedor-list";
    }
               

          public void llamarEditarProveedor(){
        
        if (selected != null)  {
        
        editarProveedor(selected, "editado Correctamente");
        
        } else{JsfUtil.addErrorMessage("Error al editar ");}
    }
        
         public void editarProveedor(Proveedor currentProveedor, String successMessage) {
        if (currentProveedor != null) {
            
            try {
                getProveedorFacade().edit(currentProveedor);
                JsfUtil.addSuccessMessage(successMessage);
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   }        

}
