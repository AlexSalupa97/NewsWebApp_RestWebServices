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
@Table(name = "POZE_STIRI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PozeStiri.findAll", query = "SELECT p FROM PozeStiri p"),
    @NamedQuery(name = "PozeStiri.findByIdPozaStire", query = "SELECT p FROM PozeStiri p WHERE p.idPozaStire = :idPozaStire")})
public class PozeStiri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_POZA_STIRE")
    private Short idPozaStire;
    @JoinColumn(name = "ID_STIRE", referencedColumnName = "ID_STIRE")
    @ManyToOne
    private Stiri idStire;
    @JoinColumn(name = "ID_POZA", referencedColumnName = "ID_POZA")
    @ManyToOne
    private Poze idPoza;

    public PozeStiri() {
    }

    public PozeStiri(Short idPozaStire) {
        this.idPozaStire = idPozaStire;
    }

    public Short getIdPozaStire() {
        return idPozaStire;
    }

    public void setIdPozaStire(Short idPozaStire) {
        this.idPozaStire = idPozaStire;
    }

    public Stiri getIdStire() {
        return idStire;
    }

    public void setIdStire(Stiri idStire) {
        this.idStire = idStire;
    }

    public Poze getIdPoza() {
        return idPoza;
    }

    public void setIdPoza(Poze idPoza) {
        this.idPoza = idPoza;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPozaStire != null ? idPozaStire.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PozeStiri)) {
            return false;
        }
        PozeStiri other = (PozeStiri) object;
        if ((this.idPozaStire == null && other.idPozaStire != null) || (this.idPozaStire != null && !this.idPozaStire.equals(other.idPozaStire))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.PozeStiri[ idPozaStire=" + idPozaStire + " ]";
    }
    
}
