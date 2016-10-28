package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Mesa;
import com.egs.webapp.entities.Pedido;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.MesaFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("mesaController")
@ApplicationScoped
public class MesaController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.MesaFacade ejbFacade;
    private List<Mesa> items = null;
    private List<Mesa> itemsDisponibles = null;
    private Mesa selected;
    
    private List<Pedido> mesaconpedido;
    
    @Inject
    private PedidoController currentpedido;

    public PedidoController getCurrentpedido() {
        return currentpedido;
    }

    public void setCurrentpedido(PedidoController currentpedido) {
        this.currentpedido = currentpedido;
    }
    
    @EJB
    private MesaFacade mesaFacade;

    public MesaController() {
    }

    public Mesa getSelected() {
        return selected;
    }

    public void setSelected(Mesa selected) {
        this.selected = selected;
    }

    public MesaFacade getMesaFacade() {
        return mesaFacade;
    }

    public void setMesaFacade(MesaFacade mesaFacade) {
        this.mesaFacade = mesaFacade;
    }
    
    private MesaFacade getFacade() {
        return ejbFacade;
    }

    public List<Pedido> getMesaconpedido() {
        return mesaconpedido;
    }

    public void setMesaconpedido(List<Pedido> mesaconpedido) {
        this.mesaconpedido = mesaconpedido;
    }

    public Mesa prepareCreate() {
        selected = new Mesa();
        
        return selected;
    }

    public void init() {
        //obliga hacer la consulta a la bd
        itemsDisponibles = getFacade().findbyDisponible();
    }

    public String create() {
        persist(PersistAction.CREATE, "la mesa se ha creado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            itemsDisponibles = null;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            prepareCreate();
            return goMesaCreate();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MesaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, "La mesa se ha eliminado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            itemsDisponibles = null;

        }
    }

    public List<Mesa> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Mesa> getItemsDisponibles() {

        if (itemsDisponibles == null) {

            // no tiene que encontrarlos a todas las mesas  
            itemsDisponibles = getFacade().findbyDisponible();

        }

        return itemsDisponibles;
    }

    public void setItemsDisponibles(List<Mesa> itemsDisponibles) {
        this.itemsDisponibles = itemsDisponibles;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    
                    int idMesa = getSelected().getIdMesa();
                    
                    mesaconpedido = currentpedido.getEjbFacade().findidmesa(idMesa);
                    
                    if(mesaconpedido.isEmpty()){
                        getFacade().remove(selected);
                         JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("No se puede eliminar ya que tiene registros asociados");
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

    public Mesa getMesa(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Mesa> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Mesa> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Mesa.class)
    public static class MesaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MesaController controller = (MesaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mesaController");
            return controller.getMesa(getKey(value));
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
            if (object instanceof Mesa) {
                Mesa o = (Mesa) object;
                return getStringKey(o.getIdMesa());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Mesa.class.getName()});
                return null;
            }
        }

    }

    public String goMesaCreate() {
        prepareCreate();
        return "mesa-create";
    }

    public String goMesaList() {
        prepareCreate();
        return "mesa-list";
    }

//            public void llamarEditarMesa(){
//
//          getFacade().edit(selected);
//          
////        }
    public void llamarEditarMesa() {

        if (selected != null) {

            editarMesa(selected);

        } else {
            JsfUtil.addErrorMessage("Error al editar mesa ");
        }
    }

    public void editarMesa(Mesa currentMesa) {
        if (currentMesa != null) {

            try {
                getMesaFacade().edit(currentMesa);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

}
