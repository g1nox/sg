package com.egs.webapp.managedBeans;


import com.egs.webapp.entities.Bar2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@Named("productoTopModel")
@SessionScoped

public class ProductoTopModel implements Serializable {
    
    

    private List<Bar2> listagrafico = new ArrayList<Bar2>();

    private BarChartModel barModel;

    private Bar2 obj;
    
    
    private Double currentMes;
    private Double currentAño;
    
    @Inject
    private ProductoController currentproducto;

    public ProductoController getCurrentproducto() {
        return currentproducto;
    }

    public void setCurrentproducto(ProductoController currentproducto) {
        this.currentproducto = currentproducto;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public Bar2 getObj() {
        return obj;
    }

    public void setObj(Bar2 obj) {
        this.obj = obj;
    }

    public void setListagrafico(List<Bar2> listagrafico) {
        this.listagrafico = listagrafico;
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
    
     private void createBarModel() {

        barModel = initBarModel();

        barModel.setTitle("Ventas últimos meses");
        barModel.setBarWidth(40);

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Producto");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Unidades vendidas");
        yAxis.setMin(0);
        yAxis.setMax(120);
    }

    private BarChartModel initBarModel() {

        BarChartModel model = new BarChartModel();

        ChartSeries productos = new ChartSeries();

        for (Bar2 lista : listagrafico) {

            productos.set(lista.getNombre(), lista.getTotal());

        }
        
        model.addSeries(productos);
        
        listagrafico = new ArrayList <Bar2>();

        return model;
    }
    
      public void consulta(){
       
   
   for (Object[] lista : currentproducto.getProductotop()) {
         
         obj = new Bar2();
         
         obj.setNombre((String) lista[0]);
         obj.setTotal((Long) lista[1]);
         
         listagrafico.add(obj);
       
        }
   
        createBarModel();
   
       }
   
    
}
