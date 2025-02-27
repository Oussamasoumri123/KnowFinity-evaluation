package nest.esprit.evaluation.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString

public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCertification;
    String Certificate;
    int idscore;
    int idEnrollement ;

    public int getIdCertification() {
        return idCertification;
    }

    public void setIdCertification(int idCertification) {
        this.idCertification = idCertification;
    }

    public String getCertificate() {
        return Certificate;
    }

    public void setCertificate(String certificate) {
        Certificate = certificate;
    }

    public int getIdscore() {
        return idscore;
    }

    public void setIdscore(int idscore) {
        this.idscore = idscore;
    }

    public int getIdEnrollement() {
        return idEnrollement;
    }

    public void setIdEnrollement(int idEnrollement) {
        this.idEnrollement = idEnrollement;
    }

    public Certification(int idCertification, int idscore, String certificate, int idEnrollement) {
        this.idCertification = idCertification;
        this.idscore = idscore;
        Certificate = certificate;
        this.idEnrollement = idEnrollement;
    }

    public Certification() {
    }
}
