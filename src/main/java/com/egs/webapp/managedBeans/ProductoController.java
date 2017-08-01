package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.DetallePedido;
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
import org.primefaces.model.chart.BarChartModel;

@Named("productoController")
@SessionScoped
public class ProductoController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.ProductoFacade ejbFacade;
    private List<Producto> items = null;
    private List<Producto> itemsActivos = null;
    
    private List<Producto> productos = null;
    
    private Producto selectedProducto;
    
    private Producto prodrepedido = null;
    
    private BarChartModel barModel;
    
    private List<Object> mesyaño = null;
    
    private Double currentMes;
    private Double currentAño;
    
    private List<DetallePedido> productocondetalles = null;
    
    private List<Object> itemsTop = null;
    private List<Object> itemspormes = null;
    
    private List<Object[]> productotop = null;
    
    // consulta stock producto en bd
    private int stockActual;


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

    @Inject
    private DetalleController currentDetallePedido;

    public DetalleController getCurrentDetallePedido() {
        return currentDetallePedido;
    }

    public void setCurrentDetallePedido(DetalleController currentDetallePedido) {
        this.currentDetallePedido = currentDetallePedido;
    }
    
    @Inject
    private ProductoTopModel currentptm;

    public ProductoTopModel getCurrentptm() {
        return currentptm;
    }

    public void setCurrentptm(ProductoTopModel currentptm) {
        this.currentptm = currentptm;
    }
    
//    @PostConstruct
//    public void iniciar() {
//        createBarModel();
//    }


    public Producto getSelectedProducto() {
        return selectedProducto;
    }

    public void setSelectedProducto(Producto selectedProducto) {
        this.selectedProducto = selectedProducto;
    }

    public List<Producto> getItems() {
        return items;
    }

    public void setItems(List<Producto> items) {
        this.items = items;
    }

    public List<Producto> getProductos() {
               if (productos == null) {
            // no tiene que encontrarlos a todos   
            productos = getEjbFacade().findNocompuesto();
        }
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    

    public List<Producto> getItemsActivos() {
          if (itemsActivos == null) {
            // no tiene que encontrarlos a todos   
            itemsActivos = getEjbFacade().findbyActivo();
        }
        return itemsActivos;
   
    }

    public void setItemsActivos(List<Producto> itemsActivos) {
        this.itemsActivos = itemsActivos;
    }

    public ProductoFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ProductoFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Object> getItemsTop() {
        
        if (itemsTop == null) {
            // no tiene que encontrarlos a todos   
           itemsTop = getEjbFacade().productomasvendido();
        }
        return itemsTop;
    }

    public List<Object> getItemspormes() {
        return itemspormes;
    }

    public void setItemspormes(List<Object> itemspormes) {
        this.itemspormes = itemspormes;
    }

    public int getStockActual(int idProducto){
  
        stockActual = getEjbFacade().stockAcualProducto(idProducto);
        
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public List<DetallePedido> getProductocondetalles() {
        return productocondetalles;
    }

    public void setProductocondetalles(List<DetallePedido> productocondetalles) {
        this.productocondetalles = productocondetalles;
    }
    
    public Producto getProdrepedido() {
        return prodrepedido;
    }

    public void setProdrepedido(Producto prodrepedido) {
        this.prodrepedido = prodrepedido;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public Double getCurrentMes() {
        return currentMes;
    }

    public void setCurrentMes(Double currentMes) {
        this.currentMes = currentMes;
    }

    public Double getCurrentAño() {
        return currentAño;
    }

    public void setCurrentAño(Double currentAño) {
        this.currentAño = currentAño;
    }

    public List<Object[]> getProductotop() {
        return productotop;
    }

    public void setProductotop(List<Object[]> productotop) {
        this.productotop = productotop;
    }

    public List<Object> getMesyaño() {
        
        mesyaño = ejbFacade.mesyaño();
        
        return mesyaño;
    }

    public void setMesyaño(List<Object> mesyaño) {
        this.mesyaño = mesyaño;
    }
    
    public Producto prepareCreate() {
        selectedProducto = new Producto();
        
        currentcategoria.setSelected(null);
        currentproveedor.setSelected(null);
       
        currentreceta.init();

        return selectedProducto;
    }
    
     public Producto prepareCreateCompuesto() {
        selectedProducto = new Producto();
        
        currentcategoria.setSelected(null);
        currentingrediente.setSelected(null);
       
        currentreceta.reset();

        return selectedProducto;
    }
    
      public List<Receta> prepareUpdate() {
         
        currentreceta.reset();
        currentingrediente.setSelected(null);
        
        
        return selectedProducto.getRecetaList();
        
    }

    public String create() {
        persist(PersistAction.CREATE, "Producto creado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query. 
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);

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
            prepareCreateCompuesto();
            return goProductoCreateCompuesto();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, "producto actualizado");
         if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            prepareUpdate();
         }
    }

    public void destroy() {
        persist(PersistAction.DELETE, "el producto se ha eliminado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            selectedProducto = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }


    
 
    private void persist(PersistAction persistAction, String successMessage) {
   
            try {
                if (persistAction == PersistAction.CREATE ) {

                    if (selectedProducto.getCompuesto() == false) {
                        
                         String nombre = getSelectedProducto().getNombre();
                         prodrepedido = getEjbFacade().findProducto(nombre);
                         
                         if (prodrepedido == null) {
                        selectedProducto.setIdCategoria(currentcategoria.getSelected());
                        selectedProducto.setIdProveedor(currentproveedor.getSelected());

                        selectedProducto.setStockActual(0);
                        selectedProducto.setStockIdeal(0);
                        selectedProducto.setStockMaximo(0);
                        selectedProducto.setStockMinimo(0);
                        selectedProducto.setPrecioVenta(0);

                        getEjbFacade().edit(selectedProducto);
                        JsfUtil.addSuccessMessage(successMessage);
                         
                         } else{
                         
                             JsfUtil.addErrorMessage("El producto ya existe");
                             goProductoCreate();
                         
                         }
                         
                       

                    } else {
                        
                        if (!currentreceta.getCurrentItems().isEmpty()) {
                        
                         String nombre = getSelectedProducto().getNombre();
                         prodrepedido = getEjbFacade().findProducto(nombre);
                         
                         if (prodrepedido == null) {

                        selectedProducto.setRecetaList(currentreceta.getCurrentItems());
                        selectedProducto.setCompuesto(Boolean.TRUE);
                        selectedProducto.setIdCategoria(currentcategoria.getSelected());

                        for (Receta dv : currentreceta.getCurrentItems()) {
                            dv.setIdProducto(selectedProducto);
                        }

                        getEjbFacade().edit(selectedProducto);
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                           
                              JsfUtil.addErrorMessage("El producto compuesto ya existe");
                             goProductoCreateCompuesto();
                             
                         }
                         
                    } else {
                        
                        JsfUtil.addErrorMessage("Se necesita al menos 1 ingrediente para crear un producto compuesto");
                        
                        }
                         
                    }

                }
                
                
                if (persistAction == PersistAction.DELETE){
                    
                    int idProducto = getSelectedProducto().getIdProducto();
                     
                    productocondetalles = currentDetallePedido.getDetallepedidoFacade().findProdDetalle(idProducto);
                    
                    if (productocondetalles.isEmpty()){
                    getEjbFacade().remove(selectedProducto);
                    JsfUtil.addSuccessMessage(successMessage);
                    } else {
                    
                        JsfUtil.addErrorMessage("No se puede eliminar el producto porque tiene registros asociados");
                    
                    }
                    
                    
                }
                
                if (persistAction == PersistAction.UPDATE) {

                    List<Receta> detalleaux = new ArrayList<Receta>();
                    detalleaux = selectedProducto.getRecetaList();
                   

//                   for (Receta receta : detalleaux) {
//                       if(currentreceta.getCurrentItems().contains(receta)){
//                           JsfUtil.addErrorMessage("receta ya contiene este ingrediente");
//                      } else {
//                       currentreceta.getCurrentItems().add(receta);
//                       }
//                    }
                    
                   detalleaux.add(currentreceta.getCurrentReceta());
                   selectedProducto.setRecetaList(detalleaux);
                  
                  
                    currentreceta.getEjbFacade().edit(currentreceta.getCurrentReceta());
                  

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

    public Producto getProducto(java.lang.Integer id) {
        return getEjbFacade().find(id);
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
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdProducto());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }

    }
    
    public void UpdateIngredientesStock(){
    
          List<Receta> recetas = selectedProducto.getRecetaList();
    
           for (Receta receta : recetas) {
                double stockActual = receta.getIdIngrediente().getStockActual();
                double cantidad = receta.getCantidad();

                receta.getIdIngrediente().setStockActual(stockActual + cantidad);
                currentingrediente.setSelected(receta.getIdIngrediente());
                currentingrediente.actualizarStock();

            }
    
    }
    
    

    public String goProductoCreate() {
        prepareCreate();
        return "producto-create";
    }

    public String goProductoCreateCompuesto() {
        prepareCreateCompuesto();
        selectedProducto.setCompuesto(true);
        return "receta-create";
    }

    public String goProductoList() {
        prepareCreate();
        return "producto-list";
    }

    public String goProductoCreateStock() {
        return "stock-create";
    }

    public String goProductoUpdateStock() {
        prepareCreate();
        return "stock-update";
    }

    public String goProductoStockList() {
        reinit();
        return "stock-list";
    }

    public void llamarEditarProducto() {

        if (selectedProducto != null) {

            editarProducto(selectedProducto);

        } else {
            JsfUtil.addErrorMessage("Error al editar ");
        }
    }

    public void editarProducto(Producto currentProducto) {
        try {
            if (currentProducto != null) {
                getEjbFacade().edit(currentProducto);
                
                JsfUtil.addSuccessMessage("Producto editado correctamente ");
            } else {
                getEjbFacade().edit(selectedProducto);
               
                JsfUtil.addSuccessMessage("Producto editado correctamente ");
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    


    public void actualizarStock() {

        try {
            getEjbFacade().edit(selectedProducto);
            JsfUtil.addSuccessMessage("Stock de producto actualizado");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void init() {
        if (selectedProducto == null) {
            selectedProducto = new Producto();
        }
        if (items == null) {
            items = new ArrayList<Producto>();
        }

    }

    public void reinit() {
        itemsActivos = getEjbFacade().findbyActivo();
        productos = getEjbFacade().findNocompuesto();

    }
      
       public void consulta(){
       
           productotop = ejbFacade.toppormes(getCurrentMes(), getCurrentAño());
           
           currentptm.creamodelo();
       }
       
       
      
   	
               
}
