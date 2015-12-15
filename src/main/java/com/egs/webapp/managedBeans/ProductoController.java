package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Producto;
import com.egs.webapp.entities.Receta;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.ProductoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import javax.faces.context.FacesContext;


@Named("productoController")
@SessionScoped
public class ProductoController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.ProductoFacade ejbFacade;
    private List<Producto> items = null;
    private List<Producto> itemsActivos = null;
    private Producto selectedProducto;
    
    private boolean disponible;
    
    private Producto producto;
     
    private List<Producto> productos;
    
    
    @EJB
    
    private ProductoFacade productoFacade;
   
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
    
    @Inject 
    
    private IngredienteController currentingrediente;

    public IngredienteController getCurrentingrediente() {
        return currentingrediente;
    }

    public void setCurrentingrediente(IngredienteController currentingrediente) {
        this.currentingrediente = currentingrediente;
    }
    
    
    @Inject
    private RecetaController currentreceta;

    public RecetaController getCurrentreceta() {
        return currentreceta;
    }

    public void setCurrentreceta(RecetaController currentreceta) {
        this.currentreceta = currentreceta;
    }

    
    // utility attributes
    private boolean checkbox; 
    
    
    public ProductoController() {
    }
    
    public ProductoFacade getProductoFacade() {
        return productoFacade;
    }

    public void setProductoFacade(ProductoFacade usuarioFacade) {
        this.productoFacade = usuarioFacade;
    }    
        
    
    
    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
    
    public Producto getSelectedProducto() {
        return selectedProducto;
    }

    public void setSelectedProducto(Producto selectedProducto) {
        this.selectedProducto = selectedProducto;   
    }
    
       public Producto getProducto() {
        return producto;
    }
 
    public List<Producto> getProductos() {
        return productos;
    }
    
    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProductoFacade getFacade() {
        return ejbFacade;
    }
    

    public Producto prepareCreate() {
        selectedProducto = new Producto();
//        currentDetalle.init();
        
        currentingrediente.setSelected(null);
        currentcategoria.setSelected(null);
        currentproveedor.setSelected(null);
        currentreceta.setSelected(null);
        currentreceta.init();
        return selectedProducto;
    }

    public String create() {
        persist(PersistAction.CREATE, "Producto creado correctamente");
        
    
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goProductoCreate();
        }
        return null;
    }
    
        public String createCompuesto() {
        persist(PersistAction.CREATE, "Producto compuesto creado correctamente");
        
    
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goProductoCreateCompuesto();
        }
        return null;
    }
    

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedProducto = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Producto> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public List<Producto> getItemsActivos() {
        if (itemsActivos == null) {
            itemsActivos = getFacade().findbyActivo();
        }
        return itemsActivos;
    }
    private void persist(PersistAction persistAction, String successMessage) {
        if (selectedProducto != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    
                   if(selectedProducto.getCompuesto()){
                   selectedProducto.setRecetaList(currentreceta.getCurrentItems());
                   selectedProducto.setCompuesto(Boolean.TRUE);
                   selectedProducto.setIdCategoria(currentcategoria.getSelected());
                   
//                    //esta wea es maldad
//                   selectedProducto.setIdProducto(getFacade().findLastProducto().getIdProducto()+1);
                   
                   for (Receta dv: currentreceta.getCurrentItems()){
                     dv.setIdProducto(selectedProducto);
                   }
                   
               
                   
                   
                    }else{
                    this.selectedProducto.setIdCategoria(currentcategoria.getSelected());
                    this.selectedProducto.setIdProveedor(currentproveedor.getSelected());
                    
                    selectedProducto.setStockActual(0);
                    selectedProducto.setStockIdeal(0);
                    selectedProducto.setStockMaximo(0);
                    selectedProducto.setStockMinimo(0);
                    
                    }
                    getFacade().edit(selectedProducto);
                    
                    
                } else {
                    getFacade().remove(selectedProducto);
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

    public Producto getProducto(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Producto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Producto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Producto> productosActivos() {
       
          if (items == null) {

            // no tiene que encontrarlos a todos   
            items = getProductoFacade().findbyActivo();
        }
     return items;
    }
   
    

    @FacesConverter(forClass = Producto.class)
    public static class ProductoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
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
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdProducto());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }

    }
    
            public String goProductoCreate(){
            prepareCreate();
            return "producto-create";
            }
            
            public String goProductoCreateCompuesto(){
            prepareCreate();
            selectedProducto.setCompuesto(true);
            return "ingrediente-new";
            }

            public String goProductoList(){
            prepareCreate();
            return "producto-list";
            }
            
            public String goProductoCreateStock(){
            return "stock-create";
            }
             
            public String goProductoUpdateStock(){
            prepareCreate();
            return "stock-update";
            }
             
            public String goProductoStockList(){
            return "stock-list";
            }
            
            
            
    
            public void llamarEditarProducto(){

            if (selectedProducto != null)  {

            editarProducto(selectedProducto);

            } else{JsfUtil.addErrorMessage("Error al editar ");}
        }
        
         public void editarProducto(Producto currentProducto) {
        if (currentProducto != null) {
            
            try {
                getProductoFacade().edit(currentProducto);
              //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
            JsfUtil.addSuccessMessage("Producto editado correctamente ");
            
            
            
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   }
         

    
    public void init() {
        if (selectedProducto==null){
         selectedProducto = new Producto();
         }
         if (items ==null){
         items = new ArrayList <Producto>();
         }
         
    } 
    public String reinit() {
        producto = new Producto();
         
        return null;
    }
        
 
 

         
               
}
