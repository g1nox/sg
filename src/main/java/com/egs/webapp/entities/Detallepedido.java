package com.egs.webapp.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author g1nox
 */
@Entity
@Table(name = "detallepedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallepedido.findAll", query = "SELECT d FROM Detallepedido d"),
    @NamedQuery(name = "Detallepedido.findByIdDetalle", query = "SELECT d FROM Detallepedido d WHERE d.idDetalle = :idDetalle"),
    @NamedQuery(name = "Detallepedido.findByCantArt", query = "SELECT d FROM Detallepedido d WHERE d.cantArt = :cantArt"),
    @NamedQuery(name = "Detallepedido.findByPrecioUni", query = "SELECT d FROM Detallepedido d WHERE d.precioUni = :precioUni"),
    @NamedQuery(name = "Detallepedido.findByPrecioTotal", query = "SELECT d FROM Detallepedido d WHERE d.precioTotal = :precioTotal"),
    @NamedQuery(name = "Detallepedido.findByHoraIng", query = "SELECT d FROM Detallepedido d WHERE d.horaIng = :horaIng")})

public class Detallepedido implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    
    @Column(name = "id_detalle")
    private Integer idDetalle;
    
    @Column(name = "cant_art")
    private Integer cantArt;
    
    @Column(name = "precio_uni")
    private Integer precioUni;
    
    @Column(name = "precio_total")
    private Integer precioTotal;
    @Size(max = 2147483647)
    
    @Column(name = "hora_ing")
    private String horaIng;
    
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne
    private Pedido idPedido;
    
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne
    private Producto idProducto;

    public Detallepedido() {
    }

    public Detallepedido(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getCantArt() {
        return cantArt;
    }

    public void setCantArt(Integer cantArt) {
        this.cantArt = cantArt;
    }

    public Integer getPrecioUni() {
        return precioUni;
    }

    public void setPrecioUni(Integer precioUni) {
        this.precioUni = precioUni;
    }

    public Integer getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Integer precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getHoraIng() {
        return horaIng;
    }

    public void setHoraIng(String horaIng) {
        this.horaIng = horaIng;
    }

    public Pedido getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedido idPedido) {
        this.idPedido = idPedido;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalle != null ? idDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallepedido)) {
            return false;
        }
        Detallepedido other = (Detallepedido) object;
        if ((this.idDetalle == null && other.idDetalle != null) || (this.idDetalle != null && !this.idDetalle.equals(other.idDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.egs.webapp.entities.Detallepedido[ idDetalle=" + idDetalle + " ]";
    }
    
}
