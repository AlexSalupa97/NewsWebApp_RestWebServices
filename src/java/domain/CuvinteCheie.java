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
@Table(name = "CUVINTE_CHEIE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuvinteCheie.findAll", query = "SELECT c FROM CuvinteCheie c"),
    @NamedQuery(name = "CuvinteCheie.findByNumeCuvantCheie", query = "SELECT c FROM CuvinteCheie c WHERE c.numeCuvantCheie = :numeCuvantCheie")})
public class CuvinteCheie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUME_CUVANT_CHEIE")
    private String numeCuvantCheie;
    @OneToMany(mappedBy = "numeCuvantCheie")
    private Collection<RandCuvinteCheie> randCuvinteCheieCollection;

    public CuvinteCheie() {
    }

    public CuvinteCheie(String numeCuvantCheie) {
        this.numeCuvantCheie = numeCuvantCheie;
    }

    public String getNumeCuvantCheie() {
        return numeCuvantCheie;
    }

    public void setNumeCuvantCheie(String numeCuvantCheie) {
        this.numeCuvantCheie = numeCuvantCheie;
    }

    @XmlTransient
    public Collection<RandCuvinteCheie> getRandCuvinteCheieCollection() {
        return randCuvinteCheieCollection;
    }

    public void setRandCuvinteCheieCollection(Collection<RandCuvinteCheie> randCuvinteCheieCollection) {
        this.randCuvinteCheieCollection = randCuvinteCheieCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeCuvantCheie != null ? numeCuvantCheie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuvinteCheie)) {
            return false;
        }
        CuvinteCheie other = (CuvinteCheie) object;
        if ((this.numeCuvantCheie == null && other.numeCuvantCheie != null) || (this.numeCuvantCheie != null && !this.numeCuvantCheie.equals(other.numeCuvantCheie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.CuvinteCheie[ numeCuvantCheie=" + numeCuvantCheie + " ]";
    }
    
}
