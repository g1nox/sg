package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Producto;
import com.egs.webapp.entities.Receta;
import com.egs.webapp.sessionBeans.RecetaFacade;
import com.egs.webapp.util.JsfUtil;
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


@Named(value = "recetaController")
@SessionScoped
public class RecetaController implements Serializable {

    /**
     * Creates a new instance of RecetaController
     */
    
    @EJB
    private com.egs.webapp.sessionBeans.RecetaFacade ejbFacade;
    private List<Receta> items = null;
    private Receta selected;
    // This items are only available in shopping cart 
    private Receta currentReceta; 
    
    private List<Receta> currentItems = null;
    
    
    @EJB
    private RecetaFacade recetaFacade;

    public RecetaFacade getRecetaFacade() {
        return recetaFacade;
    }

    public void setRecetaFacade(RecetaFacade recetaFacade) {
        this.recetaFacade = recetaFacade;
    }
    
    @Inject    
    private ProductoController currentproducto;

    public ProductoController getCurrentproducto() {
        return currentproducto;
    }

    public void setCurrentproducto(ProductoController currentproducto) {
        this.currentproducto = currentproducto;
    }
    
    @Inject
    private CategoriaController currentcategoria;

    public CategoriaController getCurrentcategoria() {
        return currentcategoria;
    }

    public void setCurrentcategoria(CategoriaController currentcategoria) {
        this.currentcategoria = currentcategoria;
    }
    
    
    
    
    @Inject    
    private IngredienteController currentingrediente;

    public IngredienteController getCurrentingrediente() {
        return currentingrediente;
    }

    public void setCurrentingrediente(IngredienteController currentingrediente) {
        this.currentingrediente = currentingrediente;
    }
  
    public RecetaController() {
        
    }
        
    public RecetaFacade getEjbFacade() {
        return ejbFacade;
    }
    
    public List<Receta> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    public void setCurrentItems(List<Receta> currentItems) {
        this.currentItems = currentItems;
    }
    
        
    public Receta getSelected() {
        return selected;
    }
    
    public void setSelected(Receta selected) {
        this.selected = selected;
    }
    
    protected void initializeEmbeddableKey() {
    }
    
    
    public Receta prepareCreate() {
           
      this.currentItems=new ArrayList<Receta>();
      this.currentReceta = new Receta();
     // currentproducto.setSelectedProducto(null);
     // currentcategoria.setSelected(null);
      currentingrediente.setSelected(null);
     // currentproducto.init();
     currentingrediente.init();
      return currentReceta;
    }
    
    public void init (){
         
        if (currentReceta == null){
         currentReceta = new Receta();
         }
         if (currentItems == null){
         currentItems = new ArrayList <Receta>();
         }  
     }
    
    public String reinit (){
         currentItems = new ArrayList <Receta>();
         currentReceta = new Receta(); // setea el objeto receta para usarlo de nuevo
         currentingrediente.setSelected(null);

         return null;
     }
     
     
    public void addIngrediente() {
        
        currentReceta.setIdIngrediente(currentingrediente.getSelected());
        currentReceta.setUmedida(currentingrediente.getSelected().getUmedida());
        
        currentItems.add(currentReceta);
        currentReceta = new Receta();              
          
    }
    
    public String create() {
        persist(JsfUtil.PersistAction.CREATE,  "producto compuesto creado");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goRecetaCreate();
        }
        return null;
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("IngredienteUpdated"));
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("IngredienteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
           
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                   currentproducto.getSelectedProducto().setRecetaList(currentItems);
                   currentproducto.getSelectedProducto().setCompuesto(Boolean.TRUE);
                   currentproducto.getSelectedProducto().setIdCategoria(currentcategoria.getSelected());
                   
                   getEjbFacade().edit(selected);
                } else {
                    getEjbFacade().remove(selected);
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
    
    
    public Receta getReceta(java.lang.Long id) {
        return getEjbFacade().find(id);
    }

    public List<Receta> getItemsAvailableSelectMany() {
        return getEjbFacade().findAll();
    }

    public List<Receta> getItemsAvailableSelectOne() {
        return getEjbFacade().findAll();
    }

    @FacesConverter(forClass = Receta.class)
    public static class RecetaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            IngredienteController controller = (IngredienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "recetaController");
            return controller.getIngrediente(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
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
            if (object instanceof Receta) {
                Receta o = (Receta) object;
                return getStringKey(o.getIdReceta());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Receta.class.getName()});
                return null;
            }
        }

    }

    public Receta getCurrentReceta() {
        return currentReceta;
    }

    public List<Receta> getCurrentItems() {
        return currentItems;
    }
    
    
    public String goRecetaCreate(){
    prepareCreate();
    return "ingrediente-new";
    }
    
    public String goRecetaList(){
    prepareCreate();
    return "pedido-list";
    }
    
    
    
    
}

