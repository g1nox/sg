package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Detallepedido;
import com.egs.webapp.sessionBeans.DetallepedidoFacade;
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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author EduardoAlexis
 */
@Named(value = "detalleController")
@SessionScoped
public class DetalleController implements Serializable {

    /**
     * Creates a new instance of DetalleController
     */
    
    @EJB
    private com.egs.webapp.sessionBeans.DetallepedidoFacade ejbFacade;
    private List<Detallepedido> items = null;
    private Detallepedido selected;
    // This items are only available in shopping cart 
    private Detallepedido currentDetallePedido; 
    private List<Detallepedido> currentItems = null;
    
    
    
    
    @EJB
    private DetallepedidoFacade detallepedidoFacade;

    public DetallepedidoFacade getDetallepedidoFacade() {
        return detallepedidoFacade;
    }

    public void setDetallepedidoFacade(DetallepedidoFacade detallepedidoFacade) {
        this.detallepedidoFacade = detallepedidoFacade;
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
    private PedidoController currentpedido;

    public PedidoController getCurrentpedido() {
        return currentpedido;
    }

    public void setCurrentpedido(PedidoController currentpedido) {
        this.currentpedido = currentpedido;
    }

    
    public DetalleController() {
        
    }
        
    public DetallepedidoFacade getEjbFacade() {
        return ejbFacade;
    }
    
    public List<Detallepedido> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    public void setCurrentItems(List<Detallepedido> currentItems) {
        this.currentItems = currentItems;
    }
        

    public Detallepedido getSelected() {
        return selected;
    }
    
    protected void initializeEmbeddableKey() {
    }
    
    
    public Detallepedido prepareCreate() {
           
      this.currentItems = new ArrayList<Detallepedido>();
      this.currentDetallePedido = new Detallepedido();
      
      currentproducto.setSelectedProducto(null);
     
      return currentDetallePedido;
    }
    
    public void init (){
         if (currentDetallePedido==null){
         currentDetallePedido = new Detallepedido();
         }
         if (currentItems ==null){
         currentItems = new ArrayList <Detallepedido>();
         }
         
     }
    
    public String reinit (){
         currentItems = new ArrayList <Detallepedido>();
         currentDetallePedido = new Detallepedido(); // setea el objeto detallepedido para usarlo de nuevo
         currentproducto.setSelectedProducto(null); // setea el campo de producto del command button

         return null;
     }
    
    public void rereinit(){
        currentItems = new ArrayList <Detallepedido>();
    
    }
     
     
    public void addShoppingCart() {
     //   int stockActual = currentproducto.getSelectedProducto().getStockActual();
        Date d = new Date();
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    //    if(stockActual >= currentDetallePedido.getCantArt()){
      //  currentproducto.getSelectedProducto().setStockActual(stockActual - currentDetallePedido.getCantArt());
        currentDetallePedido.setHoraIng(sdf.format(d)); 
        currentDetallePedido.setPrecioUni(currentproducto.getSelectedProducto().getPrecio_venta());
        currentDetallePedido.setPrecioTotal(currentDetallePedido.getPrecioUni()*currentDetallePedido.getCantArt());
        currentDetallePedido.setIdProducto(currentproducto.getSelectedProducto());
        
        currentItems.add(currentDetallePedido);
        currentDetallePedido = new Detallepedido();  
//        }else{
//         //chantarle un mensaje aqui de que falta stock
//        }
    }
    
    
    
    
    public String create() {
        persist(JsfUtil.PersistAction.CREATE,  "detalle creado");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goDetalleCreate();
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
    
 private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (currentDetallePedido != null) {
           
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
//                    Date d = new Date();
//                    currentpedido.setFecha(d);
//                    currentpedido.setHora(d);
                    
                  
                   
                    getEjbFacade().edit(currentDetallePedido);
                } else {
                   getEjbFacade().remove(currentDetallePedido);
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
    
    
    public Detallepedido getDetallepedido(java.lang.Long id) {
        return getEjbFacade().find(id);
    }

    public List<Detallepedido> getItemsAvailableSelectMany() {
        return getEjbFacade().findAll();
    }

    public List<Detallepedido> getItemsAvailableSelectOne() {
        return getEjbFacade().findAll();
    }

    @FacesConverter(forClass = Detallepedido.class)
    public static class DetalleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "detalleController");
            return controller.getProducto(getKey(value));
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
            if (object instanceof Detallepedido) {
                Detallepedido o = (Detallepedido) object;
                return getStringKey(o.getIdDetalle());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Detallepedido.class.getName()});
                return null;
            }
        }

    }

    public Detallepedido getCurrentDetallePedido() {
        return currentDetallePedido;
    }

    public List<Detallepedido> getCurrentItems() {
        return currentItems;
    }
   
    public String goDetalleCreate(){
    prepareCreate();
    return "pedido-create";
    }
    
    public String goDetalleList(){
    prepareCreate();
    return "pedido-list";
    }
    
    public String goDetalleEdit(){
    prepareCreate();
    return "pedido-update";
   }
    
   

      public void llamarView() {

        if ( currentpedido != null)  {

                 } else{JsfUtil.addErrorMessage("Error");}
            }
    
       public void viewDetalle(Detallepedido currentDetallepedido) {
        if (currentpedido != null) {
            
            try {
                getDetallepedidoFacade().edit(currentDetallepedido);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   }
    
}
          
    
    
    
    

