/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oracle
 */
@Entity
@Table(name = "USERI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Useri.findAll", query = "SELECT u FROM Useri u"),
    @NamedQuery(name = "Useri.findByIdUser", query = "SELECT u FROM Useri u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "Useri.findByNumeUser", query = "SELECT u FROM Useri u WHERE u.numeUser = :numeUser"),
    @NamedQuery(name = "Useri.findByParolaUser", query = "SELECT u FROM Useri u WHERE u.parolaUser = :parolaUser"),
    @NamedQuery(name = "Useri.findByDataCreare", query = "SELECT u FROM Useri u WHERE u.dataCreare = :dataCreare"),
    @NamedQuery(name = "Useri.findByEmailUser", query = "SELECT u FROM Useri u WHERE u.emailUser = :emailUser"),
    @NamedQuery(name = "Useri.findByStareUseri", query = "SELECT u FROM Useri u WHERE u.stareUseri = :stareUseri"),
    @NamedQuery(name = "Useri.findByUltimaActivitate", query = "SELECT u FROM Useri u WHERE u.ultimaActivitate = :ultimaActivitate"),
    @NamedQuery(name = "Useri.findByTipUser", query = "SELECT u FROM Useri u WHERE u.tipUser = :tipUser")})
public class Useri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USER")
    private Short idUser;
    @Column(name = "NUME_USER")
    private String numeUser;
    @Column(name = "PAROLA_USER")
    private String parolaUser;
    @Column(name = "DATA_CREARE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCreare;
    @Column(name = "EMAIL_USER")
    private String emailUser;
    @Column(name = "STARE_USERI")
    private Short stareUseri;
    @Column(name = "ULTIMA_ACTIVITATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaActivitate;
    @Column(name = "TIP_USER")
    private Short tipUser;
    @OneToMany(mappedBy = "idUser")
    private Collection<Comentarii> comentariiCollection;

    public Useri() {
    }

    public Useri(Short idUser) {
        this.idUser = idUser;
    }

    public Short getIdUser() {
        return idUser;
    }

    public void setIdUser(Short idUser) {
        this.idUser = idUser;
    }

    public String getNumeUser() {
        return numeUser;
    }

    public void setNumeUser(String numeUser) {
        this.numeUser = numeUser;
    }

    public String getParolaUser() {
        return parolaUser;
    }

    public void setParolaUser(String parolaUser) {
        this.parolaUser = parolaUser;
    }

    public Date getDataCreare() {
        return dataCreare;
    }

    public void setDataCreare(Date dataCreare) {
        this.dataCreare = dataCreare;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Short getStareUseri() {
        return stareUseri;
    }

    public void setStareUseri(Short stareUseri) {
        this.stareUseri = stareUseri;
    }

    public Date getUltimaActivitate() {
        return ultimaActivitate;
    }

    public void setUltimaActivitate(Date ultimaActivitate) {
        this.ultimaActivitate = ultimaActivitate;
    }

    public Short getTipUser() {
        return tipUser;
    }

    public void setTipUser(Short tipUser) {
        this.tipUser = tipUser;
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
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Useri)) {
            return false;
        }
        Useri other = (Useri) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Useri[ idUser=" + idUser + " ]";
    }
    
}
