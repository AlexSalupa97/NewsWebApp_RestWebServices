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
@Table(name = "AUTORI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autori.findAll", query = "SELECT a FROM Autori a"),
    @NamedQuery(name = "Autori.findByIdAutor", query = "SELECT a FROM Autori a WHERE a.idAutor = :idAutor"),
    @NamedQuery(name = "Autori.findByNumeAutor", query = "SELECT a FROM Autori a WHERE a.numeAutor = :numeAutor"),
    @NamedQuery(name = "Autori.findByEmailAutor", query = "SELECT a FROM Autori a WHERE a.emailAutor = :emailAutor"),
    @NamedQuery(name = "Autori.findByAdresaAutor", query = "SELECT a FROM Autori a WHERE a.adresaAutor = :adresaAutor"),
    @NamedQuery(name = "Autori.findByStareAutor", query = "SELECT a FROM Autori a WHERE a.stareAutor = :stareAutor"),
    @NamedQuery(name = "Autori.findByPrenumeAutor", query = "SELECT a FROM Autori a WHERE a.prenumeAutor = :prenumeAutor")})
public class Autori implements Serializable {
    @Column(name = "PAROLA_AUTOR")
    private String parolaAutor;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_AUTOR")
    private Short idAutor;
    @Column(name = "NUME_AUTOR")
    private String numeAutor;
    @Column(name = "EMAIL_AUTOR")
    private String emailAutor;
    @Column(name = "ADRESA_AUTOR")
    private String adresaAutor;
    @Column(name = "STARE_AUTOR")
    private Short stareAutor;
    @Column(name = "PRENUME_AUTOR")
    private String prenumeAutor;
    @OneToMany(mappedBy = "idAutor")
    private Collection<Stiri> stiriCollection;

    public Autori() {
    }

    public Autori(Short idAutor) {
        this.idAutor = idAutor;
    }

    public Short getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Short idAutor) {
        this.idAutor = idAutor;
    }

    public String getNumeAutor() {
        return numeAutor;
    }

    public void setNumeAutor(String numeAutor) {
        this.numeAutor = numeAutor;
    }

    public String getEmailAutor() {
        return emailAutor;
    }

    public void setEmailAutor(String emailAutor) {
        this.emailAutor = emailAutor;
    }

    public String getAdresaAutor() {
        return adresaAutor;
    }

    public void setAdresaAutor(String adresaAutor) {
        this.adresaAutor = adresaAutor;
    }

    public Short getStareAutor() {
        return stareAutor;
    }

    public void setStareAutor(Short stareAutor) {
        this.stareAutor = stareAutor;
    }

    public String getPrenumeAutor() {
        return prenumeAutor;
    }

    public void setPrenumeAutor(String prenumeAutor) {
        this.prenumeAutor = prenumeAutor;
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
        hash += (idAutor != null ? idAutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autori)) {
            return false;
        }
        Autori other = (Autori) object;
        if ((this.idAutor == null && other.idAutor != null) || (this.idAutor != null && !this.idAutor.equals(other.idAutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Autori[ idAutor=" + idAutor + " ]";
    }

    public String getParolaAutor() {
        return parolaAutor;
    }

    public void setParolaAutor(String parolaAutor) {
        this.parolaAutor = parolaAutor;
    }
    
}
