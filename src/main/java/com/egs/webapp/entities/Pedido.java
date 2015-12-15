/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egs.webapp.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT v FROM Pedido v"),
    @NamedQuery(name = "Pedido.findByIdPedido", query = "SELECT v FROM Pedido v WHERE v.idPedido = :idPedido"),
    @NamedQuery(name = "Pedido.findByMesa", query = "SELECT v FROM Pedido v WHERE v.mesa = :mesa"),
    @NamedQuery(name = "Pedido.findBydatetime", query = "SELECT v FROM Pedido v WHERE v.datetime = :datetime"),
    @NamedQuery(name = "Pedido.findByTotal", query = "SELECT v FROM Pedido v WHERE v.total = :total")})
    
public class Pedido implements Serializable {
    @OneToMany(mappedBy = "idPedido")
    private List<Mesa> mesaList;
    @OneToMany(mappedBy = "idPedido")
    private List<Venta> ventaList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    
    @Column(name = "id_pedido")
    private Integer idPedido;
    
    @Column(name = "total")
    private Integer total;
    
    @Column(name = "mesa")
    private Integer mesa;
    
    @Column(name = "datetime")
    @Temporal(TemporalType.DATE)
    private Date datetime;
    
    @Column(name = "estado")
    private Boolean estado;
    
    @OneToMany(mappedBy = "idPedido")
    private List<Detallepedido> detallepedidoList;
    
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public Pedido() {
        
    }

    public Pedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getidPedido() {
        return idPedido;
    }

    public void setidPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
   
    @XmlTransient
    public List<Detallepedido> getDetallepedidoList() {
        return detallepedidoList;
    }

    public void setDetallepedidoList(List<Detallepedido> detallepedidoList) {
        this.detallepedidoList = detallepedidoList;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.egs.webapp.entities.Pedido[ idPedido=" + idPedido + " ]";
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @XmlTransient
    public List<Mesa> getMesaList() {
        return mesaList;
    }

    public void setMesaList(List<Mesa> mesaList) {
        this.mesaList = mesaList;
    }
    
    
}
