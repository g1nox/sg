package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Ingrediente;
import com.egs.webapp.entities.Intoing;
import com.egs.webapp.sessionBeans.IntoingFacade;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
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

@Named("intoingController")
@SessionScoped
public class IntoingController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.IntoingFacade ejbFacade;
    private List<Intoing> items = null;
    private List<Intoing> itemsOrder = null;
    private Intoing selected;
    
    private Ingrediente selectedIngrediente;
    
    private List<Intoing> gastoing = null;
    private Date fechaConsulta;
    
    
    @Inject
    private UsuariosController contextUsuario;
    
    public UsuariosController getContextUsuario() {
        return contextUsuario;
    }

    public void setContextUsuario(UsuariosController contextUsuario) {
        this.contextUsuario = contextUsuario ;
    }
    
    @Inject    
    private IngredienteController currentingrediente;

    public IngredienteController getCurrentingrediente() {
        return currentingrediente;
    }

    public void setCurrentingrediente(IngredienteController currentingrediente) {
        this.currentingrediente = currentingrediente;
    }

   
    public IntoingController() {
    }

    public IntoingFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(IntoingFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public Intoing getSelected() {
        return selected;
    }

    public void setSelected(Intoing selected) {
        this.selected = selected;
    }

    public Ingrediente getSelectedIngrediente() {
        return selectedIngrediente;
    }

    public void setSelectedIngrediente(Ingrediente selectedIngrediente) {
        this.selectedIngrediente = selectedIngrediente;
    }

    public List<Intoing> getGastoing() {
        return gastoing;
    }

    public void setGastoing(List<Intoing> gastoing) {
        this.gastoing = gastoing;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public List<Intoing> getItemsOrder() {
          if (itemsOrder == null) {
            itemsOrder = getEjbFacade().findOrderBy();
        }
        return itemsOrder;
    }

    public void setItemsOrder(List<Intoing> itemsOrder) {
        this.itemsOrder = itemsOrder;
    }

    public Intoing prepareCreate() {
        selected = new Intoing();
        
        return selected;
    }

    public String create() {
            persist(PersistAction.CREATE,  "la entrada se ha creado correctamente");
        if (!JsfUtil.isValidationFailed()) {
            items = null;
            itemsOrder = null;// Invalidate list of items to trigger re-query.
            FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
          
             return goIntoingCreate();
        }
        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntradaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, "Entrada eliminada correctamente");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            itemsOrder = null;
        }
    }

    public List<Intoing> getItems() {
        if (items == null) {
            items = getEjbFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            
            try {
                if (persistAction == PersistAction.CREATE) {

                    Date d = new Date();
                    selected.setFecha(d);

                    SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
                    selected.setHora(hora.format(d));

                    //ingresar id_producto a bd
                    selected.setIdIngrediente(currentingrediente.getSelected());
                    // ingresar usuario a bd
                    selected.setIdUsuario(contextUsuario.getCurrentUser());

                    double Actual = currentingrediente.getSelected().getStockActual();

                    selected.setStockAnterior(Actual);
                    
                    if (selected.getMovimiento() == true ){
                     
                    selected.setStockActual(Actual + selected.getCantArt());

                    currentingrediente.getSelected().setStockActual(Actual + selected.getCantArt());
                    currentingrediente.actualizarStock();

                    getEjbFacade().edit(selected);
                
                    }
                    
                    if (selected.getMovimiento() == false ){
                     
                    selected.setStockActual(Actual - selected.getCantArt());

                    currentingrediente.getSelected().setStockActual(Actual - selected.getCantArt());
                    currentingrediente.actualizarStock();

                    getEjbFacade().edit(selected);
                   
                    }
                    
                    JsfUtil.addSuccessMessage(successMessage);
                   
                    //programar eliminar entradas
                } if (persistAction == PersistAction.DELETE){
                    
                    int ingrediente = selected.getIdIngrediente().getIdIngrediente();
                    
                    double cantidad = selected.getCantArt();
                    
                    selectedIngrediente = currentingrediente.getEjbFacade().find(ingrediente);
                    
                    double stockActual = selectedIngrediente.getStockActual();
                    
                    //elimina ingreso
                    if (selected.getMovimiento() == true ){
                     
                    selectedIngrediente.setStockActual(stockActual - cantidad);

                    currentingrediente.getEjbFacade().edit(selectedIngrediente);

                    getEjbFacade().remove(selected);

                    
                
                    }
                    
                    //elimina egreso
                    if (selected.getMovimiento() == false ){
                     
                    selectedIngrediente.setStockActual(stockActual + cantidad);

                    currentingrediente.getEjbFacade().edit(selectedIngrediente);

                    getEjbFacade().remove(selected);
                   
                   
                    }
                    JsfUtil.addSuccessMessage("stock ingrediente actualizado");
                    JsfUtil.addSuccessMessage(successMessage);
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

    public Intoing getEntrada(java.lang.Integer id) {
        return getEjbFacade().find(id);
    }

    @FacesConverter(forClass = Intoing.class)
    public static class IntoingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            IntoingController controller = (IntoingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "intoingController");
            return controller.getEntrada(getKey(value));
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
            if (object instanceof Intoing) {
                Intoing o = (Intoing) object;
                return getStringKey(o.getIdIntoing());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Intoing.class.getName()});
                return null;
            }
        }

    }
   
               public String goIntoingCreate(){
               prepareCreate();
               return "intoing-create";
    }
               public String goIntoingList(){
               prepareCreate();
               return "intoing-list";
    }
               
               
               
      public void gastofordate() {

        gastoing = ejbFacade.findforDateing(getFechaConsulta());

    }
         
               
}