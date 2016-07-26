package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.DetallePedido;
import com.egs.webapp.entities.Ingrediente;
import com.egs.webapp.entities.Receta;
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
    private List<DetallePedido> items = null;
    private DetallePedido selected;
    // This items are only available in shopping cart 
    private DetallePedido currentDetallePedido;
    private List<DetallePedido> currentItems = null;

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

    public DetalleController() {

    }

    public DetallepedidoFacade getEjbFacade() {
        return ejbFacade;
    }

    public List<DetallePedido> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    public void setCurrentItems(List<DetallePedido> currentItems) {
        this.currentItems = currentItems;
    }

    public DetallePedido getSelected() {
        return selected;
    }

    protected void initializeEmbeddableKey() {
    }

    public DetallePedido prepareCreate() {

        this.currentItems = new ArrayList<DetallePedido>();
        this.currentDetallePedido = new DetallePedido();

        currentproducto.setSelectedProducto(null);

        return currentDetallePedido;
    }

    public void init() {

        if (currentDetallePedido == null) {
            currentDetallePedido = new DetallePedido();
        }
        if (currentItems == null) {
            currentItems = new ArrayList<DetallePedido>();
        }

    }

    public String reinit() {
        currentItems = new ArrayList<DetallePedido>();
        currentDetallePedido = new DetallePedido(); // setea el objeto detallepedido para usarlo de nuevo
        currentproducto.setSelectedProducto(null); // setea el campo de producto del command button

        return null;
    }

    public void rereinit() {
        currentItems = new ArrayList<DetallePedido>();

    }

    public void addShoppingCart() {

        if (currentproducto.getSelectedProducto().getCompuesto() == false) {

            int stockActual = currentproducto.getSelectedProducto().getStockActual();

            if (stockActual >= currentDetallePedido.getCantArt()) {

                currentproducto.getSelectedProducto().setStockActual(stockActual - currentDetallePedido.getCantArt());
                currentproducto.actualizarStock();

                //define hora de ingreso
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                currentDetallePedido.setHoraIng(sdf.format(d));

                currentDetallePedido.setPrecioUni(currentproducto.getSelectedProducto().getPrecioVenta());
                currentDetallePedido.setPrecioTotal(currentDetallePedido.getPrecioUni() * currentDetallePedido.getCantArt());
                currentDetallePedido.setIdProducto(currentproducto.getSelectedProducto());

                currentItems.add(currentDetallePedido);
                currentDetallePedido = new DetallePedido();
                
                //deja null el producto despues de agregarse al pedido
                currentproducto.setSelectedProducto(null);

            } else {
                //chantarle un mensaje aqui de que falta stock

                JsfUtil.addErrorMessage("Stock insuficiente");
            }
        } else {

            // le pasa el id del producto seleccionado
            int idProducto = currentproducto.getSelectedProducto().getIdProducto();

            List<Receta> recetas = currentreceta.getRecetasPorProducto(idProducto);
            ArrayList<Ingrediente> ingredientesSinStock = new ArrayList<Ingrediente>();
            ArrayList<Ingrediente> ingredientesConStock = new ArrayList<Ingrediente>();
            for (Receta receta : recetas) {
                double stockActual = receta.getIdIngrediente().getStockActual();

                if (stockActual < receta.getCantidad()) {

                    ingredientesSinStock.add(receta.getIdIngrediente());

                } else {
                    ingredientesConStock.add(receta.getIdIngrediente());
                }

            }

            if (ingredientesSinStock.size() > 0) {
                String mensaje = "";
                for(Ingrediente ingrediente : ingredientesSinStock ){
                 mensaje += "\n "+ ingrediente.getNombre();
                
                }
                JsfUtil.addSuccessMessage("Los siguientes productos no poseen stock"+mensaje);
                
            } else {
                for (Receta receta : recetas) {
                    double stockActual = receta.getIdIngrediente().getStockActual();
                    double cantidad = receta.getCantidad();

                    receta.getIdIngrediente().setStockActual(stockActual - cantidad);
                    currentingrediente.setSelected(receta.getIdIngrediente());
                    currentingrediente.actualizarStock();

                }
                    //define hora de ingreso
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    currentDetallePedido.setHoraIng(sdf.format(d));

                    currentDetallePedido.setPrecioUni(currentproducto.getSelectedProducto().getPrecioVenta());
                    currentDetallePedido.setPrecioTotal(currentDetallePedido.getPrecioUni() * currentDetallePedido.getCantArt());
                    currentDetallePedido.setIdProducto(currentproducto.getSelectedProducto());

                    currentItems.add(currentDetallePedido);
                    currentDetallePedido = new DetallePedido();
                
            }

            
        }

    }

    public void removeShoppingCart() {
        int stockActual = currentproducto.getSelectedProducto().getStockActual();

        currentproducto.getSelectedProducto().setStockActual(stockActual + currentDetallePedido.getCantArt());
        currentproducto.actualizarStock();

    }

    public String create() {
        persist(JsfUtil.PersistAction.CREATE, "detalle creado");
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

    public DetallePedido getDetallepedido(java.lang.Long id) {
        return getEjbFacade().find(id);
    }

    public List<DetallePedido> getItemsAvailableSelectMany() {
        return getEjbFacade().findAll();
    }

    public List<DetallePedido> getItemsAvailableSelectOne() {
        return getEjbFacade().findAll();
    }

    @FacesConverter(forClass = DetallePedido.class)
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
            if (object instanceof DetallePedido) {
                DetallePedido o = (DetallePedido) object;
                return getStringKey(o.getIdDetalle());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DetallePedido.class.getName()});
                return null;
            }
        }

    }

    public DetallePedido getCurrentDetallePedido() {
        return currentDetallePedido;
    }

    public List<DetallePedido> getCurrentItems() {
        return currentItems;
    }

    public String goDetalleCreate() {
        prepareCreate();
        return "pedido-create";
    }

    public String goDetalleList() {
        prepareCreate();
        return "pedido-list";
    }

    public String goDetalleEdit() {
        prepareCreate();
        return "pedido-update";
    }

    public void llamarView() {

        if (currentpedido != null) {

        } else {
            JsfUtil.addErrorMessage("Error");
        }
    }

    public void viewDetalle(DetallePedido currentDetallepedido) {
        if (currentpedido != null) {

            try {
                getDetallepedidoFacade().edit(currentDetallepedido);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

}
          
    
    
    
    

