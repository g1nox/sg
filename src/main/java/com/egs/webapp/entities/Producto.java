package com.egs.webapp.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author g1nox
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByIdProducto", query = "SELECT p FROM Producto p WHERE p.idProducto = :idProducto"),
    @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Producto.findByUnidadCompra", query = "SELECT p FROM Producto p WHERE p.unidadCompra = :unidadCompra"),
    @NamedQuery(name = "Producto.findByStockIdeal", query = "SELECT p FROM Producto p WHERE p.stockIdeal = :stockIdeal"),
    @NamedQuery(name = "Producto.findByStockMaximo", query = "SELECT p FROM Producto p WHERE p.stockMaximo = :stockMaximo"),
    @NamedQuery(name = "Producto.findByStockMinimo", query = "SELECT p FROM Producto p WHERE p.stockMinimo = :stockMinimo"),
    @NamedQuery(name = "Producto.findByStockActual", query = "SELECT p FROM Producto p WHERE p.stockActual = :stockActual"),
    @NamedQuery(name = "Producto.findByPrecioVenta", query = "SELECT p FROM Producto p WHERE p.precioVenta = :precioVenta"),
    @NamedQuery(name = "Producto.findByCompuesto", query = "SELECT p FROM Producto p WHERE p.compuesto = :compuesto"),
    @NamedQuery(name = "Producto.findByDisponible", query = "SELECT p FROM Producto p WHERE p.disponible = :disponible")})
public class Producto implements Serializable {
    @OneToMany(mappedBy = "idProducto")
    private List<DetallePedido> detallepedidoList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_producto")
    private Integer idProducto;
    @Size(max = 50)

    @OneToMany(mappedBy = "idProducto")
    private List<Receta> recetaList;

    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    
    @Column(name = "unidad_compra")
    private String unidadCompra;
    
    @Column(name = "stock_ideal")
    private Integer stockIdeal;
    
    @Column(name = "stock_maximo")
    private Integer stockMaximo;
    
    @Column(name = "stock_minimo")
    private Integer stockMinimo;
    
    @Column(name = "stock_actual")
    private Integer stockActual;
    
    @Column(name = "precio_venta")
    private Integer precioVenta;
    
    @Column(name = "compuesto")
    private Boolean compuesto;
    
    @Column(name = "disponible")
    private Boolean disponible;
    
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private Categoria idCategoria;
    
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_proveedor")
    @ManyToOne
    private Proveedor idProveedor;

    
    
    public Producto() {
        this.compuesto = false;
    }

    public Producto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadCompra() {
        return unidadCompra;
    }

    public void setUnidadCompra(String unidadCompra) {
        this.unidadCompra = unidadCompra;
    }

    public Integer getStockIdeal() {
        return stockIdeal;
    }

    public void setStockIdeal(Integer stockIdeal) {
        this.stockIdeal = stockIdeal;
    }

    public Integer getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(Integer stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Integer precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Boolean getCompuesto() {
        return compuesto;
    }

    public void setCompuesto(Boolean compuesto) {
        this.compuesto = compuesto;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.egs.webapp.entities.Producto[ idProducto=" + idProducto + " ]";
    }

    @XmlTransient
    public List<Receta> getRecetaList() {
        return recetaList;
    }

    public void setRecetaList(List<Receta> recetaList) {
        this.recetaList = recetaList;
    }

    @XmlTransient
    public List<DetallePedido> getDetallepedidoList() {
        return detallepedidoList;
    }

    public void setDetallepedidoList(List<DetallePedido> detallepedidoList) {
        this.detallepedidoList = detallepedidoList;
    }

}
