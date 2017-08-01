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
@Table(name = "intoing")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Intoing.findAll", query = "SELECT i FROM Intoing i"),
    @NamedQuery(name = "Intoing.findByIdIntoing", query = "SELECT i FROM Intoing i WHERE i.idIntoing = :idIntoing"),
    @NamedQuery(name = "Intoing.findByFecha", query = "SELECT i FROM Intoing i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "Intoing.findByHora", query = "SELECT i FROM Intoing i WHERE i.hora = :hora"),
    @NamedQuery(name = "Intoing.findByGasto", query = "SELECT i FROM Intoing i WHERE i.gasto = :gasto"),
    @NamedQuery(name = "Intoing.findByStockAnterior", query = "SELECT i FROM Intoing i WHERE i.stockAnterior = :stockAnterior"),
    @NamedQuery(name = "Intoing.findByStockActual", query = "SELECT i FROM Intoing i WHERE i.stockActual = :stockActual"),
    @NamedQuery(name = "Intoing.findByCantArt", query = "SELECT i FROM Intoing i WHERE i.cantArt = :cantArt"),
    @NamedQuery(name = "Intoing.findByNota", query = "SELECT i FROM Intoing i WHERE i.nota = :nota")})
public class Intoing implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_intoing")
    private Integer idIntoing;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 2147483647)
    @Column(name = "hora")
    private String hora;
    @Column(name = "gasto")
    private Integer gasto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stock_anterior")
    private Double stockAnterior;
    @Column(name = "stock_actual")
    private Double stockActual;
    @Column(name = "cant_art")
    private Double cantArt;
    @Size(max = 2147483647)
    @Column(name = "nota")
    private String nota;
     @Column(name = "movimiento")
    private Boolean movimiento;
    @JoinColumn(name = "id_ingrediente", referencedColumnName = "id_ingrediente")
    @ManyToOne
    private Ingrediente idIngrediente;
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_proveedor")
    @ManyToOne
    private Proveedor idProveedor;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public Intoing() {
    }

    public Intoing(Integer idIntoing) {
        this.idIntoing = idIntoing;
    }

    public Integer getIdIntoing() {
        return idIntoing;
    }

    public void setIdIntoing(Integer idIntoing) {
        this.idIntoing = idIntoing;
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

    public Integer getGasto() {
        return gasto;
    }

    public void setGasto(Integer gasto) {
        this.gasto = gasto;
    }

    public Double getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Double stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Double getStockActual() {
        return stockActual;
    }

    public void setStockActual(Double stockActual) {
        this.stockActual = stockActual;
    }

    public Double getCantArt() {
        return cantArt;
    }

    public void setCantArt(Double cantArt) {
        this.cantArt = cantArt;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }
    
    public Ingrediente getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Ingrediente idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
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
        hash += (idIntoing != null ? idIntoing.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Intoing)) {
            return false;
        }
        Intoing other = (Intoing) object;
        if ((this.idIntoing == null && other.idIntoing != null) || (this.idIntoing != null && !this.idIntoing.equals(other.idIntoing))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.egs.webapp.entities.Intoing[ idIntoing=" + idIntoing + " ]";
    }
    
}
