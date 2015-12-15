package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Detallepedido;
import com.egs.webapp.entities.Pedido;
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
    private Pedido selected;
    
    private int cant =0;
    
    @Inject
    private DetalleController currentDetallePedido;
    
    public DetalleController getCurrentDetallePedido() {
        return currentDetallePedido;
    }

    public void setCurrentDetallePedido(DetalleController currentDetallePedido) {
        this.currentDetallePedido = currentDetallePedido ;
    }
    
    @Inject
    private UsuariosController contextUsuario;
    
    public UsuariosController getContextUsuario() {
        return contextUsuario;
    }

    public void setContextUsuario(UsuariosController contextUsuario) {
        this.contextUsuario = contextUsuario ;
    }
    
    @Inject
    private MesaController currentmesa;

    public MesaController getCurrentmesa() {
        return currentmesa;
    }

    public void setCurrentmesa(MesaController currentmesa) {
        this.currentmesa = currentmesa;
    }
    
    public PedidoController() {
    }

    public PedidoFacade getEjbFacade() {
        return ejbFacade;
    }
    
    public List<Pedido> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    public Pedido getSelected() {
        return selected;
    }
    
    public void setSelected(Pedido selected) {
        this.selected = selected;
    }

    public void setEjbFacade(PedidoFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
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

    
    public Pedido prepareCreate() {
        selected = new Pedido();
        
        currentDetallePedido.init();
      
        return selected;
    }
     
    public List<Detallepedido> prepareUpdate() {
           
        currentDetallePedido.rereinit();
       
      
        return selected.getDetallepedidoList();
    }
     
     
     
     
    public String create() {
        persist(JsfUtil.PersistAction.CREATE,  "el pedido se creo");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
             FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             prepareCreate();
             return goPedidoCreate();
        }
        return null;
    }
    

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, "alan tiwa");
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
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    
    
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
           
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    if(persistAction == JsfUtil.PersistAction.CREATE){
                        
                    Date d = new Date();
                    selected.setDatetime(d);
//                    
//                    SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
//                    selected.setHora(hora.format(d));
//                    
//                    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
//                    selected.setFecha(fecha.format(d));
                    

                    selected.setEstado(Boolean.FALSE);
                    
                    selected.setIdUsuario(contextUsuario.getCurrentUser());
                 //   selected.setidPedido(getEjbFacade().findLastPedido().getidPedido()+1);
                    int total = 0;
                 
                    for (Detallepedido dv: currentDetallePedido.getCurrentItems()){
                    
                     dv.setIdPedido(selected);
                     total += dv.getPrecioTotal();
                    }
                  
                    
                    selected.setTotal(total);
                    selected.setDetallepedidoList(currentDetallePedido.getCurrentItems());
                    
                    
                    getEjbFacade().edit(selected);
                    
                     prepareCreate();
                     currentDetallePedido.reinit();
                     JsfUtil.addSuccessMessage(successMessage);
                    
                    }
                    
                    if(persistAction == JsfUtil.PersistAction.UPDATE){
                        int total = selected.getTotal();
                         for (Detallepedido dv: currentDetallePedido.getCurrentItems()){

                            dv.setIdPedido(selected);
                            total += dv.getPrecioTotal();
                            
                         }
                         selected.setTotal(total);
                         
                         List<Detallepedido> detalleaux = new ArrayList<Detallepedido>();
                         detalleaux = selected.getDetallepedidoList();
                         for(Detallepedido dv: detalleaux){
                             currentDetallePedido.getCurrentItems().add(dv);
                         }
                         selected.setDetallepedidoList(currentDetallePedido.getCurrentItems());
                     
                         getEjbFacade().edit(selected);
                         
                         //cambia estado de la mesa
                         currentmesa.getSelected().setEstado(Boolean.TRUE);
                         currentmesa.llamarEdit();
                         
                         JsfUtil.addSuccessMessage("Producto agregado correctamente al pedido");
                         
                     }
        
                    
                } else {
                   getEjbFacade().remove(selected);
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
            if (object instanceof Pedido) {
                Pedido o = (Pedido) object;
                return getStringKey(o.getidPedido());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Pedido.class.getName()});
                return null;
            }
        }

    }
    
    public String goPedidoCreate(){
    prepareCreate();
    return "pedido-create";
    }
    
    public String goPedidoList(){
    return "pedido-list";
    }
    
       public void llamarEdit(){

          getEjbFacade().edit(selected);
          
        }
    
    
}