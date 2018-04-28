/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oracle
 */
@Entity
@Table(name = "POZE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poze.findAll", query = "SELECT p FROM Poze p"),
    @NamedQuery(name = "Poze.findByIdPoza", query = "SELECT p FROM Poze p WHERE p.idPoza = :idPoza"),
    @NamedQuery(name = "Poze.findByPozitiePoza", query = "SELECT p FROM Poze p WHERE p.pozitiePoza = :pozitiePoza"),
    @NamedQuery(name = "Poze.findByStarePoza", query = "SELECT p FROM Poze p WHERE p.starePoza = :starePoza")})
public class Poze implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_POZA")
    private Short idPoza;
    @Basic(optional = false)
    @Lob
    @Column(name = "LINK_POZA")
    private byte[] linkPoza;
    @Column(name = "POZITIE_POZA")
    private String pozitiePoza;
    @Column(name = "STARE_POZA")
    private Short starePoza;
    @OneToMany(mappedBy = "idPoza")
    private Collection<PozeStiri> pozeStiriCollection;

    
    public ImageIcon getImage()
    {
        return new ImageIcon(new ImageIcon(linkPoza).getImage());
    }
    
    
    public Poze() {
    }

    public Poze(Short idPoza) {
        this.idPoza = idPoza;
    }

    public Poze(Short idPoza, byte[] linkPoza) {
        this.idPoza = idPoza;
        this.linkPoza = linkPoza;
    }

    public Short getIdPoza() {
        return idPoza;
    }

    public void setIdPoza(Short idPoza) {
        this.idPoza = idPoza;
    }

    public byte[] getLinkPoza() {
        return linkPoza;
    }

    public void setLinkPoza(byte[] linkPoza) {
        this.linkPoza = linkPoza;
    }

    public String getPozitiePoza() {
        return pozitiePoza;
    }

    public void setPozitiePoza(String pozitiePoza) {
        this.pozitiePoza = pozitiePoza;
    }

    public Short getStarePoza() {
        return starePoza;
    }

    public void setStarePoza(Short starePoza) {
        this.starePoza = starePoza;
    }

    @XmlTransient
    public Collection<PozeStiri> getPozeStiriCollection() {
        return pozeStiriCollection;
    }

    public void setPozeStiriCollection(Collection<PozeStiri> pozeStiriCollection) {
        this.pozeStiriCollection = pozeStiriCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPoza != null ? idPoza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Poze)) {
            return false;
        }
        Poze other = (Poze) object;
        if ((this.idPoza == null && other.idPoza != null) || (this.idPoza != null && !this.idPoza.equals(other.idPoza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Poze[ idPoza=" + idPoza + " ]";
    }
    
}
