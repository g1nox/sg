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
    
    private Ingrediente nombreing;
    
    private List<Receta> recetaconingrediente;

    public Ingrediente getSelected() {
        return selected;
    }
    
    @Inject
    private RecetaController currentReceta;

    public RecetaController getCurrentReceta() {
        return currentReceta;
    }

    public void setCurrentReceta(RecetaController currentReceta) {
        this.currentReceta = currentReceta;
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

    private ProveedorController currentproveedor;

    public ProveedorController getCurrentproveedor() {
        return currentproveedor;
    }

    public void setCurrentproveedor(ProveedorController currentproveedor) {
        this.currentproveedor = currentproveedor;
    }

    @EJB
    private IngredienteFacade ingredienteFacade;

    public void setSelected(Ingrediente selected) {
        this.selected = selected;
    }

    public IngredienteFacade getEjbFacade() {
        return ejbFacade;
    }
    
    @Inject
    private ProductoController currentproducto;

    public ProductoController getCurrentproducto() {
        return currentproducto;
    }

    public void setCurrentproducto(ProductoController currentproducto) {
        this.currentproducto = currentproducto;
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

    public Ingrediente getNombreing() {
        return nombreing;
    }

    public void setNombreing(Ingrediente nombreing) {
        this.nombreing = nombreing;
    }

    public List<Receta> getRecetaconingrediente() {
        return recetaconingrediente;
    }

    public void setRecetaconingrediente(List<Receta> recetaconingrediente) {
        this.recetaconingrediente = recetaconingrediente;
    }
    
    public Ingrediente prepareCreate() {
        selected = new Ingrediente();
        currentReceta.init();

        return selected;
    }

    protected void setEmbeddableKeys() {
    }

    public void init() {

        if (selected == null) {
            selected = new Ingrediente();
        }
    }
    
    public void reinit (){
         
        if (selected == null){
         selected = new Ingrediente();
         }
         if (items == null){
         items = new ArrayList <Ingrediente>();
         }  
     }

    public String create() {
        persist(JsfUtil.PersistAction.CREATE, "ingrediente se creo");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);

            return goIngredienteCreate();
        }
        return null;
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
    }

     public void destroy() {
        persist(PersistAction.DELETE, "El ingrediente se ha eliminado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    
                    String nombre = getSelected().getNombre();
                    nombreing = ejbFacade.findIngrediente(nombre);
                    
                    if (nombreing == null) {
                        
                        selected.setIdCategoria(currentcategoria.getSelected());
                        selected.setIdProveedor(currentproveedor.getSelected());
                        
                        getFacade().edit(selected);
                        
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("El ingrediente ya existe");
                    }
                } else {
                    int iding = getSelected().getIdIngrediente();
                    recetaconingrediente = currentReceta.getRecetaFacade().findIngReceta(iding);
                    
                    if (recetaconingrediente.isEmpty()) {
                        getFacade().remove(selected);
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("No se puede eliminar el ingrediente porque tiene registros asociados");
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

    public Ingrediente getIngrediente(java.lang.Integer id) {
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
            if (object instanceof Ingrediente) {
                Ingrediente o = (Ingrediente) object;
                return getStringKey(o.getIdIngrediente());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Ingrediente.class.getName()});
                return null;
            }
        }

    }

    public String goIngredienteCreate() {
        prepareCreate();
        return "ingrediente-create";
    }

    public String goIngredienteList() {
        init();
        return "ingrediente-list";
    }

    public void actualizarStock() {
        try {
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Stock de ingrediente actualizado");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
     public void actualizarStockReceta(){
        
         
         
//        for (currentReceta.getSelected().getIdProducto() : recetalist){                    
//                           
//                           JsfUtil.addSuccessMessage("paso por aqui");
//        }                   
   }
    
          public void llamarEditarIngrediente(){
        
        if (selected != null)  {
        
        editarIngrediente(selected, "editado Correctamente");
        
        } else{JsfUtil.addErrorMessage("Error al editar ");}
    }
        
         public void editarIngrediente(Ingrediente currentIngrediente, String successMessage) {
        if (currentIngrediente != null) {
            
            try {
                getIngredienteFacade().edit(currentIngrediente);
                JsfUtil.addSuccessMessage(successMessage);
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   }        

}
