package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Bar;
import com.egs.webapp.sessionBeans.VentaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@Named("barModel")
@SessionScoped

public class BarModel implements Serializable {

    @EJB
    private com.egs.webapp.sessionBeans.VentaFacade ejbFacade;

    private List<Object[]> pormes;

    private List<Bar> listagrafico = new ArrayList<Bar>();

    private Bar obj;

    private BarChartModel barModel;

    @PostConstruct
    public void iniciar() {
        metodo();
        createBarModel();
    }

    public VentaFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(VentaFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public List<Object[]> getPormes() {

        pormes = getEjbFacade().totalventapormes();

        return pormes;
    }

    public void setPormes(List<Object[]> pormes) {
        this.pormes = pormes;
    }

    public Bar getObj() {
        return obj;
    }

    public void setObj(Bar obj) {
        this.obj = obj;
    }

    public List<Bar> getListagrafico() {
        return listagrafico;
    }

    public void setListagrafico(List<Bar> listagrafico) {
        this.listagrafico = listagrafico;
    }

    private void createBarModel() {

        barModel = initBarModel();

        barModel.setTitle("Ventas últimos meses");
        barModel.setBarWidth(40);

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Mes");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Total");
        yAxis.setMin(0);
        yAxis.setMax(223400);
    }

    private BarChartModel initBarModel() {

        BarChartModel model = new BarChartModel();

        ChartSeries meses = new ChartSeries();

        for (Bar lista : listagrafico) {

            meses.set(lista.getMes(), lista.getTotal());

        }

        model.addSeries(meses);

        return model;
    }

    public void metodo() {

        for (Object[] lista : getPormes()) {

            obj = new Bar();

            double año = (double) lista[1];
            int newaño = (int) año;

            obj.setAño(newaño);

            if (lista[0].equals(1.0)) {
                obj.setMes("Enero " + String.valueOf(newaño));
            }

            if (lista[0].equals(2.0)) {
                obj.setMes("Febrero " + String.valueOf(newaño));
            }

            if (lista[0].equals(3.0)) {
                obj.setMes("Marzo " + String.valueOf(newaño));
            }

            if (lista[0].equals(4.0)) {
                obj.setMes("Abril " + String.valueOf(newaño));
            }

            if (lista[0].equals(5.0)) {
                obj.setMes("Mayo " + String.valueOf(newaño));
            }

            if (lista[0].equals(6.0)) {
                obj.setMes("Junio " + String.valueOf(newaño));
            }

            if (lista[0].equals(7.0)) {
                obj.setMes("Julio " + String.valueOf(newaño));
            }

            if (lista[0].equals(8.0)) {
                obj.setMes("Agosto " + String.valueOf(newaño));
            }

            if (lista[0].equals(9.0)) {
                obj.setMes("Septiembre " + String.valueOf(newaño));
            }

            if (lista[0].equals(10.0)) {
                obj.setMes("Octubre " + String.valueOf(newaño));
            }

            if (lista[0].equals(11.0)) {
                obj.setMes("Noviembre " + String.valueOf(newaño));
            }

            if (lista[0].equals(12.0)) {
                obj.setMes("Diciembre " + String.valueOf(newaño));
            }

            obj.setTotal((Long) lista[2]);

            listagrafico.add(obj);

        }

    }

}
