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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "STIRI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stiri.findAll", query = "SELECT s FROM Stiri s"),
    @NamedQuery(name = "Stiri.findByIdStire", query = "SELECT s FROM Stiri s WHERE s.idStire = :idStire"),
    @NamedQuery(name = "Stiri.findByTitlu", query = "SELECT s FROM Stiri s WHERE s.titlu = :titlu"),
    @NamedQuery(name = "Stiri.findByDataStire", query = "SELECT s FROM Stiri s WHERE s.dataStire = :dataStire"),
    @NamedQuery(name = "Stiri.findByNrAfisari", query = "SELECT s FROM Stiri s WHERE s.nrAfisari = :nrAfisari"),
    @NamedQuery(name = "Stiri.findByStareStiri", query = "SELECT s FROM Stiri s WHERE s.stareStiri = :stareStiri"),
    @NamedQuery(name = "Stiri.findByContinutStire", query = "SELECT s FROM Stiri s WHERE s.continutStire = :continutStire")})
public class Stiri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_STIRE")
    private Short idStire;
    @Basic(optional = false)
    @Column(name = "TITLU")
    private String titlu;
    @Column(name = "DATA_STIRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataStire;
    @Basic(optional = false)
    @Column(name = "NR_AFISARI")
    private int nrAfisari;
    @Column(name = "STARE_STIRI")
    private Short stareStiri;
    @Column(name = "CONTINUT_STIRE")
    private String continutStire;
    @OneToMany(mappedBy = "idStire")
    private Collection<PozeStiri> pozeStiriCollection;
    @JoinColumn(name = "ID_CATEGORIE", referencedColumnName = "ID_CATEGORIE")
    @ManyToOne
    private Categorii idCategorie;
    @JoinColumn(name = "ID_AUTOR", referencedColumnName = "ID_AUTOR")
    @ManyToOne
    private Autori idAutor;
    @OneToMany(mappedBy = "idStire")
    private Collection<Comentarii> comentariiCollection;
    @OneToMany(mappedBy = "idStire")
    private Collection<RandCuvinteCheie> randCuvinteCheieCollection;

    public Stiri() {
    }

    public Stiri(Short idStire) {
        this.idStire = idStire;
    }

    public Stiri(Short idStire, String titlu, int nrAfisari) {
        this.idStire = idStire;
        this.titlu = titlu;
        this.nrAfisari = nrAfisari;
    }

    public Short getIdStire() {
        return idStire;
    }

    public void setIdStire(Short idStire) {
        this.idStire = idStire;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public Date getDataStire() {
        return dataStire;
    }

    public void setDataStire(Date dataStire) {
        this.dataStire = dataStire;
    }

    public int getNrAfisari() {
        return nrAfisari;
    }

    public void setNrAfisari(int nrAfisari) {
        this.nrAfisari = nrAfisari;
    }

    public Short getStareStiri() {
        return stareStiri;
    }

    public void setStareStiri(Short stareStiri) {
        this.stareStiri = stareStiri;
    }

    public String getContinutStire() {
        return continutStire;
    }

    public void setContinutStire(String continutStire) {
        this.continutStire = continutStire;
    }

    @XmlTransient
    public Collection<PozeStiri> getPozeStiriCollection() {
        return pozeStiriCollection;
    }

    public void setPozeStiriCollection(Collection<PozeStiri> pozeStiriCollection) {
        this.pozeStiriCollection = pozeStiriCollection;
    }

    public Categorii getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Categorii idCategorie) {
        this.idCategorie = idCategorie;
    }

    public Autori getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Autori idAutor) {
        this.idAutor = idAutor;
    }

    @XmlTransient
    public Collection<Comentarii> getComentariiCollection() {
        return comentariiCollection;
    }

    public void setComentariiCollection(Collection<Comentarii> comentariiCollection) {
        this.comentariiCollection = comentariiCollection;
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
        hash += (idStire != null ? idStire.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stiri)) {
            return false;
        }
        Stiri other = (Stiri) object;
        if ((this.idStire == null && other.idStire != null) || (this.idStire != null && !this.idStire.equals(other.idStire))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Stiri[ idStire=" + idStire + " ]";
    }
    
}
