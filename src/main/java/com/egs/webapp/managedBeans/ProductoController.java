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
import javax.annotation.PostConstruct;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@Named("productoController")
@SessionScoped
public class ProductoController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.ProductoFacade ejbFacade;
    private List<Producto> items = null;
    private List<Producto> itemsActivos = null;
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

    private boolean disponible;
    
    private boolean editable;
    
    private double cantidad;


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
    
    @PostConstruct
    public void iniciar() {
        createBarModel();
    }

    // utility attributes
    private boolean checkbox;

    public ProductoController() {
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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
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
            prepareCreate();
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

    public List<Producto> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    public List<Producto> getItemsActivos() {
        if (itemsActivos == null) {
            itemsActivos = getEjbFacade().findbyActivo();
        }
        return itemsActivos;
    }
    
 
    private void persist(PersistAction persistAction, String successMessage) {
        if (selectedProducto != null) {

            try {
                if (persistAction == PersistAction.CREATE) {

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
                         
                         }
                         
                       

                    } else {

                        selectedProducto.setRecetaList(currentreceta.getCurrentItems());
                        selectedProducto.setCompuesto(Boolean.TRUE);
                        selectedProducto.setIdCategoria(currentcategoria.getSelected());

                        for (Receta dv : currentreceta.getCurrentItems()) {
                            dv.setIdProducto(selectedProducto);
                        }

                        getEjbFacade().edit(selectedProducto);
                        JsfUtil.addSuccessMessage(successMessage);
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
                  
                    //getFacade().edit(selectedProducto);
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
    }

    public Producto getProducto(java.lang.Integer id) {
        return getEjbFacade().find(id);
    }

    public List<Producto> getItemsAvailableSelectMany() {
        return getEjbFacade().findAll();
    }

    public List<Producto> getItemsAvailableSelectOne() {
        return getEjbFacade().findAll();
    }

    public List<Producto> productosActivos() {

        if (items == null) {
            // no tiene que encontrarlos a todos   
            items = getEjbFacade().findbyActivo();
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
        prepareCreate();
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
        //items = getFacade().findAll();
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

    }
    
        private void createBarModel() {
        barModel = initBarModel();
         
        barModel.setTitle("Nombre del grafico");
        //barModel.setLegendPosition("ne");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Meses");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Ventas");
        yAxis.setMin(0);
        yAxis.setMax(100);
    }
       
       private BarChartModel initBarModel() {
        
        BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
       // boys.setLabel("Boys");
        
      getItemsActivos();
        
        for(Producto lista: itemsActivos){
            
            
            boys.set(lista.getNombre(), lista.getStockActual());
            
        }
        
        
//        boys.set(45, 120000);
//        boys.set(300, 100000);
       
        
        model.addSeries(boys);
        
      
         
        return model;
    }
       
       public void consulta(){
       
           productotop = ejbFacade.toppormes(getCurrentMes(), getCurrentAño());
           
           currentptm.consulta();
       }
       
       
      
   	
               
}
