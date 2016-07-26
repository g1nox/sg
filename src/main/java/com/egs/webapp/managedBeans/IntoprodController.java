package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Intoprod;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.IntoprodFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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

@Named("intoprodController")
@SessionScoped
public class IntoprodController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.IntoprodFacade ejbFacade;
    private List<Intoprod> items = null;
    private List<Intoprod> gastolist = null;
    
    private Intoprod selected;
    
    private Date fechaConsulta;
    
    @EJB
    private IntoprodFacade intoprodFacade;

    public IntoprodFacade getIntoprodFacade() {
        return intoprodFacade;
    }

    public void setIntoprodFacade(IntoprodFacade intoprodFacade) {
        this.intoprodFacade = intoprodFacade;
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
    private ProductoController currentproducto;

    public ProductoController getCurrentproducto() {
        return currentproducto;
    }

    public void setCurrentproducto(ProductoController currentproducto) {
        this.currentproducto = currentproducto;
    }

    public IntoprodController() {
    }

    public Intoprod getSelected() {
        return selected;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public void setSelected(Intoprod selected) {
        this.selected = selected;
    }

    public List<Intoprod> getGastolist() {
        return gastolist;
    }

    public void setGastolist(List<Intoprod> gastolist) {
        this.gastolist = gastolist;
    }
    

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private IntoprodFacade getFacade() {
        return ejbFacade;
    }

    public Intoprod prepareCreate() {
        selected = new Intoprod();
        initializeEmbeddableKey();
        return selected;
    }

    public String create() {
            persist(PersistAction.CREATE,  "la entrada prod se ha creado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
          
             return currentproducto.goProductoStockList();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("IntoprodUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("IntoprodDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Intoprod> getItems() {
        if (items == null) {
            items = getIntoprodFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    
                    
                    Date d = new Date();
                    selected.setFecha(d);
                    
                    SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
                    selected.setHora(hora.format(d));
                    
                     //ingresar id_producto a bd
                     selected.setIdProducto(currentproducto.getSelectedProducto());
                    
                     // ingresar usuario a bd
                     selected.setIdUsuario(contextUsuario.getCurrentUser());
                      
                     int Actual = currentproducto.getSelectedProducto().getStockActual();
                        
                        selected.setStockAnterior(Actual);
                        selected.setStockActual(Actual + selected.getCantArt());
                        
                        currentproducto.getSelectedProducto().setStockActual(Actual + selected.getCantArt());
                        currentproducto.actualizarStock();  
                    
                    
                    
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
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

    public Intoprod getIntoprod(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Intoprod> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Intoprod> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Intoprod.class)
    public static class IntoprodControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            IntoprodController controller = (IntoprodController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "intoprodController");
            return controller.getIntoprod(getKey(value));
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
            if (object instanceof Intoprod) {
                Intoprod o = (Intoprod) object;
                return getStringKey(o.getIdIntoprod());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Intoprod.class.getName()});
                return null;
            }
        }

    }
               public String goIntoprodCreate(){
               prepareCreate();
               return "intoprod-create";
    }
               public String goIntoprodList(){
               prepareCreate();
               return "intoprod-list";
    }
               
       public void gastofordate() {

        gastolist = ejbFacade.findforDate(getFechaConsulta());

    }
               
               
}
