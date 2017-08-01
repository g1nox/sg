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
    
    long yMax = 0;
    
    String mes;
    String año;
    
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


    public long getyMax() {
        return yMax;
    }

    public void setyMax(long yMax) {
        this.yMax = yMax;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }
    
    private void changemesaño(){
        
        double m = currentproducto.getCurrentMes();
        double a = currentproducto.getCurrentAño();
        
        int i = (int)m;
        int ii = (int)a;
        
        if (i == 1){
            mes = "Enero";
        }
        if (i == 2){
            mes = "Febrero";
        }
        if (i == 3){
            mes = "Marzo";
        }
        if (i == 4){
            mes = "Abril";
        }
        if (i == 5){
            mes = "Mayo";
        }
        if (i == 6){
            mes = "Junio";
        }
        if (i == 7){
            mes = "Julio";
        }
        if (i == 8){
            mes = "Agosto";
        }
        if (i == 9){
            mes = "Septiembre";
        }
        if (i == 10){
            mes = "Octubre";
        }
        if (i == 11){
            mes = "Noviembre";
        }
        if (i == 12){
            mes = "Diciembre";
        }
        
        año = String.valueOf(ii);
    
    }
    
     private void createBarModel() {

        barModel = initBarModel();
        
        changemesaño();

        barModel.setTitle(mes+" "+año);
        barModel.setBarWidth(40);

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Producto");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        
        yAxis.setLabel("Unidades vendidas");
        yAxis.setMin(0);
        yAxis.setMax(yMax);
    }

    private BarChartModel initBarModel() {

        BarChartModel model = new BarChartModel();

        ChartSeries productos = new ChartSeries();
        
        yMax = 0;

        for (Bar2 lista : listagrafico) {

             if(lista.getTotal() > yMax ){
                
                yMax = lista.getTotal();
            
            }
            
            productos.set(lista.getNombre(), lista.getTotal());

        }
        
        model.addSeries(productos);
        
        listagrafico = new ArrayList <Bar2>();

        return model;
    }
    
      public void creamodelo(){
       
   
   for (Object[] lista : currentproducto.getProductotop()) {
         
         obj = new Bar2();
         
         obj.setNombre((String) lista[0]);
         obj.setTotal((Long) lista[1]);
         
         listagrafico.add(obj);
       
        }
   
        createBarModel();
   
       }
   
    
}
