package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Categoria;
import com.egs.webapp.entities.Producto;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.CategoriaFacade;

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

@Named("categoriaController")
@SessionScoped
public class CategoriaController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.CategoriaFacade ejbFacade;
    private List<Categoria> items = null;
    private Categoria selected;
    
   private Categoria nombrecat;
   private List<Producto> prodconcategoria;
   
    
    @EJB
    private CategoriaFacade categoriaFacade;

    public CategoriaController() {
    }

    public Categoria getSelected() {
        return selected;
    }

    public void setSelected(Categoria selected) {
        this.selected = selected;
    }
   
    
      public CategoriaFacade getCategoriaFacade() {
        return categoriaFacade;
    }
      
      public void setCategoriaFacade(CategoriaFacade categoriaFacade) {
        this.categoriaFacade = categoriaFacade;
    }   
      
     @Inject
      
    private ProductoController currentproducto;

    public ProductoController getCurrentproducto() {
        return currentproducto;
    }

    public void setCurrentproducto(ProductoController currentproducto) {
        this.currentproducto = currentproducto;
    }
      

    public Categoria getNombrecat() {
        return nombrecat;
    }

    public void setNombrecat(Categoria nombrecat) {
        this.nombrecat = nombrecat;
    }

    public List<Producto> getProdconcategoria() {
        return prodconcategoria;
    }

    public void setProdconcategoria(List<Producto> prodconcategoria) {
        this.prodconcategoria = prodconcategoria;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CategoriaFacade getFacade() {
        return ejbFacade;
    }

    public Categoria prepareCreate() {
        selected = new Categoria();
        initializeEmbeddableKey();
        return selected;
    }

    public String create() {
        
            persist(PersistAction.CREATE,  "la categoria se ha creado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
          
             return goCategoriaCreate();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, "La categoria se ha eliminado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Categoria> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    
                    String nombre = getSelected().getNombre();
                    nombrecat = ejbFacade.findCategoria(nombre);
                    
                    if (nombrecat == null) {
                        
                        getFacade().edit(selected);
                        
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("La categoría ya existe");
                    }
                } 
                
                if (persistAction == PersistAction.DELETE) {
                    
                    int idcat = getSelected().getIdCategoria();
                    prodconcategoria = currentproducto.getEjbFacade().findProdCategoria(idcat);
                    
                    if (prodconcategoria.isEmpty()) {
                        getFacade().remove(selected);
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("No se puede eliminar la categoria porque tiene registros asociados");
                    }
                }
                
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

    public Categoria getCategoria(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Categoria> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Categoria> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Categoria.class)
    public static class CategoriaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategoriaController controller = (CategoriaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoriaController");
            return controller.getCategoria(getKey(value));
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
            if (object instanceof Categoria) {
                Categoria o = (Categoria) object;
                return getStringKey(o.getIdCategoria());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Categoria.class.getName()});
                return null;
            }
        }

    }
               public String goCategoriaCreate(){
               prepareCreate();
               return "categoria-create";
    }
               public String goCategoriaList(){
               prepareCreate();
               return "categoria-list";
    }
               
          public void llamarEditarCategoria(){
        
        if (selected != null)  {
        
        editarCategoria(selected, "editado Correctamente");
        
        } else{JsfUtil.addErrorMessage("Error al editar ");}
    }
        
         public void editarCategoria(Categoria currentCategoria, String successMessage) {
        if (currentCategoria != null) {
            
            try {
                getCategoriaFacade().edit(currentCategoria);
                JsfUtil.addSuccessMessage(successMessage);
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   } 
         
            
               
}
