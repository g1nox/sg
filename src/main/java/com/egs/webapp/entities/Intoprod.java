package com.egs.webapp.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author g1nox
 */
@Entity
@Table(name = "intoprod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Intoprod.findAll", query = "SELECT i FROM Intoprod i"),
    @NamedQuery(name = "Intoprod.findByIdIntoprod", query = "SELECT i FROM Intoprod i WHERE i.idIntoprod = :idIntoprod"),
    @NamedQuery(name = "Intoprod.findByFecha", query = "SELECT i FROM Intoprod i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "Intoprod.findByHora", query = "SELECT i FROM Intoprod i WHERE i.hora = :hora"),
    @NamedQuery(name = "Intoprod.findByStockAnterior", query = "SELECT i FROM Intoprod i WHERE i.stockAnterior = :stockAnterior"),
    @NamedQuery(name = "Intoprod.findByStockFinal", query = "SELECT i FROM Intoprod i WHERE i.stockFinal = :stockFinal"),
    @NamedQuery(name = "Intoprod.findByCantArt", query = "SELECT i FROM Intoprod i WHERE i.cantArt = :cantArt"),
    @NamedQuery(name = "Intoprod.findByMovimiento", query = "SELECT i FROM Intoprod i WHERE i.movimiento = :movimiento")})
public class Intoprod implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    
    @Column(name = "id_intoprod")
    private Integer idIntoprod;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 2147483647)
    @Column(name = "hora")
    private String hora;
    @Column(name = "stock_anterior")
    private Integer stockAnterior;
    @Column(name = "stock_final")
    private Integer stockFinal;
    @Column(name = "cant_art")
    private Integer cantArt;
    @Column(name = "gasto")
    private Integer gasto;
    @Column(name = "movimiento")
    private Boolean movimiento;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne
    private Producto idProducto;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public Intoprod() {
    }

    public Intoprod(Integer idIntoprod) {
        this.idIntoprod = idIntoprod;
    }

    public Integer getIdIntoprod() {
        return idIntoprod;
    }

    public void setIdIntoprod(Integer idIntoprod) {
        this.idIntoprod = idIntoprod;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getStockFinal() {
        return stockFinal;
    }

    public void setStockFinal(Integer stockFinal) {
        this.stockFinal = stockFinal;
    }

    public Integer getCantArt() {
        return cantArt;
    }

    public void setCantArt(Integer cantArt) {
        this.cantArt = cantArt;
    }

    public Integer getGasto() {
        return gasto;
    }

    public void setGasto(Integer gasto) {
        this.gasto = gasto;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIntoprod != null ? idIntoprod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Intoprod)) {
            return false;
        }
        Intoprod other = (Intoprod) object;
        if ((this.idIntoprod == null && other.idIntoprod != null) || (this.idIntoprod != null && !this.idIntoprod.equals(other.idIntoprod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.egs.webapp.entities.Intoprod[ idIntoprod=" + idIntoprod + " ]";
    }
    
}
