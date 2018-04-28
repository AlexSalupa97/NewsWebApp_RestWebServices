/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oracle
 */
@Entity
@Table(name = "COMENTARII")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentarii.findAll", query = "SELECT c FROM Comentarii c"),
    @NamedQuery(name = "Comentarii.findByIdComentariu", query = "SELECT c FROM Comentarii c WHERE c.idComentariu = :idComentariu"),
    @NamedQuery(name = "Comentarii.findByDataComentariu", query = "SELECT c FROM Comentarii c WHERE c.dataComentariu = :dataComentariu"),
    @NamedQuery(name = "Comentarii.findByContinutComentariu", query = "SELECT c FROM Comentarii c WHERE c.continutComentariu = :continutComentariu"),
    @NamedQuery(name = "Comentarii.findByStareComentariu", query = "SELECT c FROM Comentarii c WHERE c.stareComentariu = :stareComentariu")})
public class Comentarii implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_COMENTARIU")
    private Short idComentariu;
    @Column(name = "DATA_COMENTARIU")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataComentariu;
    @Column(name = "CONTINUT_COMENTARIU")
    private String continutComentariu;
    @Column(name = "STARE_COMENTARIU")
    private Short stareComentariu;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    @ManyToOne
    private Useri idUser;
    @JoinColumn(name = "ID_STIRE", referencedColumnName = "ID_STIRE")
    @ManyToOne
    private Stiri idStire;
    @JoinColumn(name = "ID_GUEST", referencedColumnName = "ID_GUEST")
    @ManyToOne
    private Guests idGuest;

    public Comentarii() {
    }

    public Comentarii(Short idComentariu) {
        this.idComentariu = idComentariu;
    }

    public Short getIdComentariu() {
        return idComentariu;
    }

    public void setIdComentariu(Short idComentariu) {
        this.idComentariu = idComentariu;
    }

    public Date getDataComentariu() {
        return dataComentariu;
    }

    public void setDataComentariu(Date dataComentariu) {
        this.dataComentariu = dataComentariu;
    }

    public String getContinutComentariu() {
        return continutComentariu;
    }

    public void setContinutComentariu(String continutComentariu) {
        this.continutComentariu = continutComentariu;
    }

    public Short getStareComentariu() {
        return stareComentariu;
    }

    public void setStareComentariu(Short stareComentariu) {
        this.stareComentariu = stareComentariu;
    }

    public Useri getIdUser() {
        return idUser;
    }

    public void setIdUser(Useri idUser) {
        this.idUser = idUser;
    }

    public Stiri getIdStire() {
        return idStire;
    }

    public void setIdStire(Stiri idStire) {
        this.idStire = idStire;
    }

    public Guests getIdGuest() {
        return idGuest;
    }

    public void setIdGuest(Guests idGuest) {
        this.idGuest = idGuest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComentariu != null ? idComentariu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentarii)) {
            return false;
        }
        Comentarii other = (Comentarii) object;
        if ((this.idComentariu == null && other.idComentariu != null) || (this.idComentariu != null && !this.idComentariu.equals(other.idComentariu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Comentarii[ idComentariu=" + idComentariu + " ]";
    }
    
}
