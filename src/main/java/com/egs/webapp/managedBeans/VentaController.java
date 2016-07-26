package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Venta;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
import com.egs.webapp.sessionBeans.VentaFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;

@Named("ventaController")
@ApplicationScoped
public class VentaController implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.VentaFacade ejbFacade;
    private List<Venta> items = null;
    private List<Venta> itemsFiltrados = null;
    private List<Object[]> totalpormes = null;
    private List<Object> totalmesero = null;
    private List<Venta> ventamesactual = null;
    private List<Venta> ventaxmes = null;
    
    private BarChartModel barModel;
    
    private List<Venta> totales;
    
    private List<Object> arqueolist = null;
    private List<Object> arqueototales = null;
    private List<Object> mesyaño = null;
    
   private Object seleccion;
    
   private Double  currentSelectAño;
   private Double  currentSelectMes;
   
   
   
  // private double m = Double.parseDouble(currentSelectMes);
  
   
    private Venta selected;
    
    private Venta FechaObj;

    private Date fechaConsulta;
    
    private long total;
    
    private Date fechaActual;
   
    

    @Inject
    private UsuariosController contextUsuario;

    public UsuariosController getContextUsuario() {
        return contextUsuario;
    }

    public void setContextUsuario(UsuariosController contextUsuario) {
        this.contextUsuario = contextUsuario;
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
    private MesaController currentmesa;

    public MesaController getCurrentmesa() {
        return currentmesa;
    }

    public void setCurrentmesa(MesaController currentmesa) {
        this.currentmesa = currentmesa;
    }

    @EJB
    private VentaFacade ventaFacade;

    public VentaController() {
    }

    public Venta getSelected() {
        return selected;
    }

    public void setSelected(Venta selected) {
        this.selected = selected;
    }

    public VentaFacade getVentaFacade() {
        return ventaFacade;
    }

    private VentaFacade getFacade() {
        return ejbFacade;
    }

    public void setVentaFacade(VentaFacade ventaFacade) {
        this.ventaFacade = ventaFacade;
    }

    public List<Venta> getItemsFiltrados() {
        return itemsFiltrados;
    }

    public void setItemsFiltrados(List<Venta> itemsFiltrados) {
        this.itemsFiltrados = itemsFiltrados;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public Object getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Object seleccion) {
        this.seleccion = seleccion;
    }

    public Double getCurrentSelectAño() {
        return currentSelectAño;
    }

    public void setCurrentSelectAño(Double currentSelectAño) {
        this.currentSelectAño = currentSelectAño;
    }

    public Double getCurrentSelectMes() {
        return currentSelectMes;
    }

    public void setCurrentSelectMes(Double currentSelectMes) {
        this.currentSelectMes = currentSelectMes;
    }


    public List<Object[]> getTotalpormes() {
        
        if (totalpormes == null) {
        totalpormes = getFacade().totalventapormes();
        }
        return totalpormes;
    }

    public void setTotalpormes(List<Object[]> totalpormes) {
        this.totalpormes = totalpormes;
    }

    public List<Object> getMesyaño() {
        
        mesyaño = ejbFacade.mesyaño();
        
        return mesyaño;
    }

    public void setMesyaño(List<Object> mesyaño) {
        this.mesyaño = mesyaño;
    }

    public Venta getFechaObj() {
        return FechaObj;
    }

    public void setFechaObj(Venta FechaObj) {
        this.FechaObj = FechaObj;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }


    public List<Venta> getVentamesactual() {
        
        return ventamesactual;
    }

    public void setVentamesactual(List<Venta> ventamesactual) {
        this.ventamesactual = ventamesactual;
    }

    public List<Venta> getVentaxmes() {  
        return ventaxmes;
    }

    public void setVentaxmes(List<Venta> ventaxmes) {
        this.ventaxmes = ventaxmes;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public List<Venta> getTotales() {
        return totales;
    }

    public void setTotales(List<Venta> totales) {
        this.totales = totales;
    }
    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public Venta prepareCreate() {
        selected = new Venta();
        //currentmesa.setSelected(null);
        initializeEmbeddableKey();
        return selected;
    }

    public void init() {

        totalpormes = getFacade().totalventapormes();
        
        
        
    }

    public String create() {
        if (currentpedido.getSelected().getEstado() == false) {
            persist(PersistAction.CREATE, "venta pagada correctamente");
            if (!JsfUtil.isValidationFailed()) {
                items = null;  // Invalidate list of items to trigger re-query.
                currentmesa.setItemsDisponibles(null); //limpar variable para consultar bd
                FacesContext facesContext = FacesContext.getCurrentInstance();
                Flash flash = facesContext.getExternalContext().getFlash();
                flash.setKeepMessages(true);
                flash.setRedirect(true);
                prepareCreate();
                init();
                return currentpedido.goPedidoList();
            }
        } else {
            return currentpedido.goPedidoList();
        }

        return null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VentaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VentaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Venta> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {

                    //define hora de venta
                    Date h = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    selected.setHora(sdf.format(h));
                    //define fecha venta
                    Date d = new Date();
                    selected.setFecha(d);

                    selected.setIdUsuario(contextUsuario.getCurrentUser());
                    selected.setTotal(currentpedido.getSelected().getTotal());
                    selected.setIdPedido(currentpedido.getSelected());

                    getFacade().edit(selected);

                    //modificar estado pedido
                    currentpedido.getSelected().setEstado(Boolean.TRUE);
                    currentpedido.llamarEditarPedido();

                    //traer mesa seleccionada del pedido
                    currentmesa.setSelected(currentpedido.getSelected().getIdMesa());
                    currentmesa.getSelected().setEstado(Boolean.FALSE);
                    currentmesa.llamarEditarMesa();

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

    public Venta getVenta(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Venta> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Venta> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }


    @FacesConverter(forClass = Venta.class)
    public static class VentaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VentaController controller = (VentaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ventaController");
            return controller.getVenta(getKey(value));
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
            if (object instanceof Venta) {
                Venta o = (Venta) object;
                return getStringKey(o.getIdVenta());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Venta.class.getName()});
                return null;
            }
        }

    }

    public String goVentaCreate() {
        prepareCreate();
        return "venta-create";
    }

    public String goVentaList() {
        prepareCreate();
        return "venta-list";
    }
    
     public String goVentaSearch() {
        return "venta-search";
    }
     
     public String goVentaSearchxmes() {
        return "venta-searchxmes";
    }
     

    public void search() {

        totalpormes = ejbFacade.totalventapormes();

    }
    
     public void searchfordate() {

        itemsFiltrados = ejbFacade.findforDate(getFechaConsulta());

    }
     
       public void arqueopordia() {

        arqueolist = ejbFacade.arqueo(getFechaConsulta());
        
        arqueototales = ejbFacade.resumen(getFechaConsulta());

    }
       
       
       public void consulta(){
       
      ventaxmes = ejbFacade.ventaxmes(getCurrentSelectMes(), getCurrentSelectAño());
           
          
       }
       
       private void convertir(){
       
          
       
       }
       
//    private void graficar(List<Object> totalpormes) {
//
//        barModel = new BarChartModel();
//
//        barModel.setTitle("Bar Chart");
//
//        barModel.setTitle("Bar Chart");
//        barModel.setLegendPosition("ne");
//
//        Axis xAxis = barModel.getAxis(AxisType.X);
//        xAxis.setLabel("Gender");
//
//        Axis yAxis = barModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Births");
//        yAxis.setMin(0);
//        yAxis.setMax(200);
//
//    }


    public long getTotal() {

        Date fechaActual = new Date();

        total = ejbFacade.totalventadiaria(fechaActual);

        return total;

    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Object> getTotalmesero() {
         if (totalmesero == null) {
        totalmesero = getFacade().meserotopxmes();
        }
        return totalmesero; 
    }

    public void setTotalmesero(List<Object> totalmesero) {
        this.totalmesero = totalmesero;
    }

    public List<Object> getArqueolist() {
        return arqueolist;
    }

    public void setArqueolist(List<Object> arqueolist) {
        this.arqueolist = arqueolist;
    }

    public List<Object> getArqueototales() {
        return arqueototales;
    }

    public void setArqueototales(List<Object> arqueototales) {
        this.arqueototales = arqueototales;
    }

}