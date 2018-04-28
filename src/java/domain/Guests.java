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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oracle
 */
@Entity
@Table(name = "GUESTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Guests.findAll", query = "SELECT g FROM Guests g"),
    @NamedQuery(name = "Guests.findByIdGuest", query = "SELECT g FROM Guests g WHERE g.idGuest = :idGuest"),
    @NamedQuery(name = "Guests.findByStareGuest", query = "SELECT g FROM Guests g WHERE g.stareGuest = :stareGuest")})
public class Guests implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_GUEST")
    private String idGuest;
    @Column(name = "STARE_GUEST")
    private Short stareGuest;
    @OneToMany(mappedBy = "idGuest")
    private Collection<Comentarii> comentariiCollection;

    public Guests() {
    }

    public Guests(String idGuest) {
        this.idGuest = idGuest;
    }

    public String getIdGuest() {
        return idGuest;
    }

    public void setIdGuest(String idGuest) {
        this.idGuest = idGuest;
    }

    public Short getStareGuest() {
        return stareGuest;
    }

    public void setStareGuest(Short stareGuest) {
        this.stareGuest = stareGuest;
    }

    @XmlTransient
    public Collection<Comentarii> getComentariiCollection() {
        return comentariiCollection;
    }

    public void setComentariiCollection(Collection<Comentarii> comentariiCollection) {
        this.comentariiCollection = comentariiCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGuest != null ? idGuest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Guests)) {
            return false;
        }
        Guests other = (Guests) object;
        if ((this.idGuest == null && other.idGuest != null) || (this.idGuest != null && !this.idGuest.equals(other.idGuest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Guests[ idGuest=" + idGuest + " ]";
    }
    
}
