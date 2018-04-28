/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oracle
 */
@Entity
@Table(name = "RAND_CUVINTE_CHEIE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RandCuvinteCheie.findAll", query = "SELECT r FROM RandCuvinteCheie r"),
    @NamedQuery(name = "RandCuvinteCheie.findByIdRandCuvantCheie", query = "SELECT r FROM RandCuvinteCheie r WHERE r.idRandCuvantCheie = :idRandCuvantCheie")})
public class RandCuvinteCheie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_RAND_CUVANT_CHEIE")
    private Short idRandCuvantCheie;
    @JoinColumn(name = "ID_STIRE", referencedColumnName = "ID_STIRE")
    @ManyToOne
    private Stiri idStire;
    @JoinColumn(name = "NUME_CUVANT_CHEIE", referencedColumnName = "NUME_CUVANT_CHEIE")
    @ManyToOne
    private CuvinteCheie numeCuvantCheie;

    public RandCuvinteCheie() {
    }

    public RandCuvinteCheie(Short idRandCuvantCheie) {
        this.idRandCuvantCheie = idRandCuvantCheie;
    }

    public Short getIdRandCuvantCheie() {
        return idRandCuvantCheie;
    }

    public void setIdRandCuvantCheie(Short idRandCuvantCheie) {
        this.idRandCuvantCheie = idRandCuvantCheie;
    }

    public Stiri getIdStire() {
        return idStire;
    }

    public void setIdStire(Stiri idStire) {
        this.idStire = idStire;
    }

    public CuvinteCheie getNumeCuvantCheie() {
        return numeCuvantCheie;
    }

    public void setNumeCuvantCheie(CuvinteCheie numeCuvantCheie) {
        this.numeCuvantCheie = numeCuvantCheie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRandCuvantCheie != null ? idRandCuvantCheie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RandCuvinteCheie)) {
            return false;
        }
        RandCuvinteCheie other = (RandCuvinteCheie) object;
        if ((this.idRandCuvantCheie == null && other.idRandCuvantCheie != null) || (this.idRandCuvantCheie != null && !this.idRandCuvantCheie.equals(other.idRandCuvantCheie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.RandCuvinteCheie[ idRandCuvantCheie=" + idRandCuvantCheie + " ]";
    }
    
}
