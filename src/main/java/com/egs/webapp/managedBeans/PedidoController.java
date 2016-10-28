package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.DetallePedido;
import com.egs.webapp.entities.Pedido;
import com.egs.webapp.entities.Receta;
import com.egs.webapp.sessionBeans.PedidoFacade;
import com.egs.webapp.util.JsfUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@Named(value = "pedidoController")
@SessionScoped
public class PedidoController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.PedidoFacade ejbFacade;
    private List<Pedido> items = null;
    private List<Pedido> itemsOrderBy = null;
    private List<Pedido> itemsFiltrados = null;
    private List<Pedido> itemsToday = null;
    private List<Pedido> itemsTodayUsuario = null;

    private Pedido selected;
    
    private Pedido productosPedido;

    private Date fechaConsulta;

    @Inject
    private DetalleController currentDetallePedido;

    public DetalleController getCurrentDetallePedido() {
        return currentDetallePedido;
    }

    public void setCurrentDetallePedido(DetalleController currentDetallePedido) {
        this.currentDetallePedido = currentDetallePedido;
    }

    @Inject
    private UsuariosController currentUsuario;

    public UsuariosController getCurrentUsuario() {
        return currentUsuario;
    }

    public void setCurrentUsuario(UsuariosController currentUsuario) {
        this.currentUsuario = currentUsuario;
    }

    @Inject
    private MesaController currentmesa;

    public MesaController getCurrentmesa() {
        return currentmesa;
    }

    public void setCurrentmesa(MesaController currentmesa) {
        this.currentmesa = currentmesa;
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
    private RecetaController currentreceta;

    public RecetaController getCurrentreceta() {
        return currentreceta;
    }

    public void setCurrentreceta(RecetaController currentreceta) {
        this.currentreceta = currentreceta;
    }
    
    @Inject
    private IngredienteController currentingrediente;

    public IngredienteController getCurrentingrediente() {
        return currentingrediente;
    }

    public void setCurrentingrediente(IngredienteController currentingrediente) {
        this.currentingrediente = currentingrediente;
    }
  

    public PedidoController() {
    }

    public PedidoFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PedidoFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Pedido> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    public void setItems(List<Pedido> items) {
        this.items = items;
    }

    public List<Pedido> getItemsOrderBy() {
        if (itemsOrderBy == null) {
            itemsOrderBy = getEjbFacade().findOrderBy();
        }
        return itemsOrderBy;
    }

    public void setItemsOrderBy(List<Pedido> itemsOrderBy) {
        this.itemsOrderBy = itemsOrderBy;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public Pedido getSelected() {
        return selected;
    }

    public void setSelected(Pedido selected) {
        this.selected = selected;
    }

    public List<Pedido> getItemsFiltrados() {
        return itemsFiltrados;
    }

    public void setItemsFiltrados(List<Pedido> itemsFiltrados) {
        this.itemsFiltrados = itemsFiltrados;
    }

    public List<Pedido> getItemsToday() {
        
        Date fechaActual = new Date();
        itemsToday = ejbFacade.today(fechaActual);
        
        return itemsToday;
    }

    public void setItemsToday(List<Pedido> itemsToday) {
        this.itemsToday = itemsToday;
    }

    public List<Pedido> getItemsTodayUsuario() {
        
        long idUsuario = currentUsuario.getCurrentUser().getIdUsuario();
        Date fechaActual = new Date();
        
        itemsTodayUsuario = ejbFacade.todayMesero(idUsuario, fechaActual);
        
        return itemsTodayUsuario;
    }

    public void setItemsTodayUsuario(List<Pedido> itemsTodayUsuario) {
        this.itemsTodayUsuario = itemsTodayUsuario;
    }
    
    public Pedido getProductosPedido() {
        return productosPedido;
    }

    public void setProductosPedido(Pedido productosPedido) {
        this.productosPedido = productosPedido;
    }

    //metodos
    public void init() {

        if (itemsOrderBy == null) {
            itemsOrderBy = new ArrayList<Pedido>();
        }
    }

    public Pedido prepareCreate() {
        selected = new Pedido();
        
        currentproducto.setSelectedProducto(null);
        currentproducto.reinit();
      
        currentDetallePedido.init();
       
        currentmesa.setSelected(null);
        currentmesa.init();
        
        return selected;
    }

    public Pedido prepareSearch() {
        selected = new Pedido();
        return selected;

    }

    public List<DetallePedido> prepareUpdate() {

        currentDetallePedido.rereinit();

        return selected.getDetallepedidoList();
    }
    
  
    public String create() {

        if (!currentDetallePedido.getCurrentItems().isEmpty()) {

            if (currentDetallePedido.comprobarStock() == true) {

                persist(JsfUtil.PersistAction.CREATE, "el pedido se creo");
                if (!JsfUtil.isValidationFailed()) {
                    itemsOrderBy = null;    // Invalidate list of items to trigger re-query.
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    Flash flash = facesContext.getExternalContext().getFlash();
                    flash.setKeepMessages(true);
                    flash.setRedirect(true);
                    prepareCreate();
                    
                    if(currentUsuario.getCurrentUser().getIdRol().getIdRol() == 1){

                        return goPedidoListAdmin();
                    }
                     if(currentUsuario.getCurrentUser().getIdRol().getIdRol() == 3){

                        return goPedidoTodayUser();
                    }
                    
                }

            }
        }
        
        JsfUtil.addErrorMessage("Ingrese productos al pedido antes de finalizar");

        return null;

    }


    public void update() {

        if (!currentDetallePedido.getCurrentItems().isEmpty()) {

            persist(JsfUtil.PersistAction.UPDATE, "alan tiwa");
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
                FacesContext facesContext = FacesContext.getCurrentInstance();
                Flash flash = facesContext.getExternalContext().getFlash();
                flash.setKeepMessages(true);
                flash.setRedirect(true);
                prepareUpdate();

            }

        } else {

            JsfUtil.addErrorMessage("Ingrese productos al pedido antes de finalizar");

        }

    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, "eliminado xuxeta");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null; // Invalidate list of items to trigger re-query.
            itemsOrderBy = null;    
    }}

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {

            try {

                if (persistAction == JsfUtil.PersistAction.CREATE) {

                    //fecha ingreso pedido
                    Date f = new Date();
                    selected.setFecha(f);
                    //define hora pedido
                    Date h = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    selected.setHora(sdf.format(h));

                    //define estado del pedido
                    selected.setEstado(Boolean.FALSE);

                    // define la mesa del pedido
                    selected.setIdMesa(currentmesa.getSelected());

                    // ingresar usuario a bd
                    selected.setIdUsuario(currentUsuario.getCurrentUser());

                    //modificar estado mesa
                    currentmesa.getSelected().setEstado(Boolean.TRUE);
                    currentmesa.llamarEditarMesa();
                    
                    int total = 0;
                    for (DetallePedido dv : currentDetallePedido.getCurrentItems()) {
                        
                        dv.setIdPedido(selected);
                        
                        total += dv.getPrecioTotal();
                     
                        
                    }
                    selected.setTotal(total);
                    selected.setDetallepedidoList(currentDetallePedido.getCurrentItems());

                    getEjbFacade().edit(selected);
                    
                    //actualizar stock productos del carro
                    updateStock();
                    
                    currentDetallePedido.reinit();
                    
                    JsfUtil.addSuccessMessage(successMessage);

                }

                if (persistAction == JsfUtil.PersistAction.UPDATE) {
                    
                  List<DetallePedido> itemsOrders = new ArrayList<DetallePedido>();
                  List<DetallePedido> nueva = new ArrayList<DetallePedido>();
                  
                 int total = selected.getTotal();

                  
                  itemsOrders = selected.getDetallepedidoList();
                    
                  for (DetallePedido ls : currentDetallePedido.getCurrentItems()){
                  
                      int idProducto = ls.getIdProducto().getIdProducto();
                      int cantidad = ls.getCantArt();
                      
                      //ver si contiene un producto igual
                      if (itemsOrders.contains(ls)){
                          
                          for ( DetallePedido obj: itemsOrders){
                          
                          if (idProducto == obj.getIdProducto().getIdProducto()){
                            
                              //sumar cantidad
                              int sumaCant = cantidad + obj.getCantArt();
                              int precioTotal = sumaCant * obj.getPrecioUni();
                              
                            obj.setCantArt(sumaCant);
                            obj.setPrecioTotal(precioTotal);
                            
                            total+= ls.getCantArt() * ls.getPrecioUni();
                            
                            
                            currentDetallePedido.getEjbFacade().edit(obj);
                          
                          } 
                      
                          
                          }
                          
                          
                      } else {
                          
                        total+= ls.getCantArt() * ls.getPrecioUni();
                      
                         // agrega obj a la lista
                        ls.setIdPedido(selected);
                        nueva.add(ls);
                      
                      }
                   
                  }
                  
                          for( DetallePedido old : itemsOrders){
                      
                          if(!nueva.contains(old)){
                          
                              nueva.add(old);
                          
                          }
                      
                      }
                  
                    selected.setTotal(total);
                    selected.setDetallepedidoList(nueva); 

                    getEjbFacade().edit(selected);
                    JsfUtil.addSuccessMessage("Mesa actualizada correctamente");
                    
                    
           
                }
                

                if (persistAction == JsfUtil.PersistAction.DELETE) {

                    List<DetallePedido> productos = selected.getDetallepedidoList();

                    for (DetallePedido producto : productos) {

                        if (producto.getIdProducto().getCompuesto() == true) {

                            int idProducto = producto.getIdProducto().getIdProducto();

                            List<Receta> recetas = currentreceta.getRecetasPorProducto(idProducto);

                            for (Receta receta : recetas) {

                                double stockActual = receta.getIdIngrediente().getStockActual();
                                double cantidad = (receta.getCantidad() * producto.getCantArt());

                                receta.getIdIngrediente().setStockActual(stockActual + cantidad);
                                currentingrediente.setSelected(receta.getIdIngrediente());
                                currentingrediente.actualizarStock();

                            }

                        } else {

                            int stockActual = producto.getIdProducto().getStockActual();

                            producto.getIdProducto().setStockActual(stockActual + producto.getCantArt());

                            currentproducto.setSelectedProducto(producto.getIdProducto());

                            currentproducto.actualizarStock();

                    }

                    //modificar estado mesa
                    currentmesa.setSelected(selected.getIdMesa());
                    currentmesa.getSelected().setEstado(Boolean.FALSE);
                    currentmesa.llamarEditarMesa();

                    getEjbFacade().remove(selected);
                    currentmesa.setSelected(null);
                    JsfUtil.addSuccessMessage(successMessage);
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
    
    public void updateStock() {

        for (DetallePedido producto : currentDetallePedido.getCurrentItems()) {

            if (producto.getIdProducto().getCompuesto() == false) {
            
            int stockActual = producto.getIdProducto().getStockActual();
            int cantidad = producto.getCantArt();

            producto.getIdProducto().setStockActual(stockActual - cantidad);
            currentproducto.setSelectedProducto(producto.getIdProducto());
            currentproducto.actualizarStock();
            
            } else {
                
            // le pasa el id del producto seleccionado
            
             int idProducto = producto.getIdProducto().getIdProducto();

            List<Receta> recetas = currentreceta.getRecetasPorProducto(idProducto);
            
            for (Receta receta : recetas) {
                    
                    double stockActual = receta.getIdIngrediente().getStockActual();
                    double cantidad = (receta.getCantidad() * producto.getCantArt());

                    receta.getIdIngrediente().setStockActual(stockActual - cantidad);
                    currentingrediente.setSelected(receta.getIdIngrediente());
                    currentingrediente.actualizarStock();

                }
            
            }
            
        }
    }
 

    public Pedido getPedido(java.lang.Long id) {
        return getEjbFacade().find(id);
    }

    public List<Pedido> getItemsAvailableSelectMany() {
        return getEjbFacade().findAll();
    }

    public List<Pedido> getItemsAvailableSelectOne() {
        return getEjbFacade().findAll();
    }

    void setFecha(Date d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setHora(Date d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FacesConverter(forClass = Pedido.class)
    public static class PedidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pedidoController");
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
            if (object instanceof Pedido) {
                Pedido o = (Pedido) object;
                return getStringKey(o.getIdPedido());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Pedido.class.getName()});
                return null;
            }
        }

    }

    public String goPedidoCreateAdmin() {
        prepareCreate();
        return "pedido-create-admin";
    }
    
     public String goPedidoCreateMesero() {
        prepareCreate();
        return "pedido-create-mesero";
    }

    public String goPedidoListAdmin() {
        
        return "pedido-list-admin";
    }
    
      public String goPedidoListMesero() {
        
        return "pedido-list-mesero";
    }

    public String goPedidoSearch() {

        return "pedido-search";

    }
    
     public String goPedidoTodayAdmin() {

        return "pedido-today-admin";

    }
     
       public String goPedidoTodayUser() {

        return "pedido-today-mesero";

    }

    public void llamarEditarPedido() {

        getEjbFacade().edit(selected);

    }

    public void search() {

        itemsFiltrados = ejbFacade.findDate(fechaConsulta);

    }

}
