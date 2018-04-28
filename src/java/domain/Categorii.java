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
@Table(name = "CATEGORII")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categorii.findAll", query = "SELECT c FROM Categorii c"),
    @NamedQuery(name = "Categorii.findByIdCategorie", query = "SELECT c FROM Categorii c WHERE c.idCategorie = :idCategorie"),
    @NamedQuery(name = "Categorii.findByNumeCategorie", query = "SELECT c FROM Categorii c WHERE c.numeCategorie = :numeCategorie"),
    @NamedQuery(name = "Categorii.findByStareCategorii", query = "SELECT c FROM Categorii c WHERE c.stareCategorii = :stareCategorii")})
public class Categorii implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CATEGORIE")
    private Short idCategorie;
    @Column(name = "NUME_CATEGORIE")
    private String numeCategorie;
    @Column(name = "STARE_CATEGORII")
    private Short stareCategorii;
    @OneToMany(mappedBy = "idCategorie")
    private Collection<Stiri> stiriCollection;

    public Categorii() {
    }

    public Categorii(Short idCategorie) {
        this.idCategorie = idCategorie;
    }

    public Short getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Short idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNumeCategorie() {
        return numeCategorie;
    }

    public void setNumeCategorie(String numeCategorie) {
        this.numeCategorie = numeCategorie;
    }

    public Short getStareCategorii() {
        return stareCategorii;
    }

    public void setStareCategorii(Short stareCategorii) {
        this.stareCategorii = stareCategorii;
    }

    @XmlTransient
    public Collection<Stiri> getStiriCollection() {
        return stiriCollection;
    }

    public void setStiriCollection(Collection<Stiri> stiriCollection) {
        this.stiriCollection = stiriCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategorie != null ? idCategorie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorii)) {
            return false;
        }
        Categorii other = (Categorii) object;
        if ((this.idCategorie == null && other.idCategorie != null) || (this.idCategorie != null && !this.idCategorie.equals(other.idCategorie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Categorii[ idCategorie=" + idCategorie + " ]";
    }
    
}
