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


public class Exams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idExams;
    int Attempts;
    int Duration ;
    String Question;
    String CorrectAnswers;
    String StudentAnsewers;
    int iddcours ;
    int idEnrollement ;

    public int getIdExams() {
        return idExams;
    }

    public void setIdExams(int idExams) {
        this.idExams = idExams;
    }

    public int getAttempts() {
        return Attempts;
    }

    public void setAttempts(int attempts) {
        Attempts = attempts;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCorrectAnswers() {
        return CorrectAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        CorrectAnswers = correctAnswers;
    }

    public String getStudentAnsewers() {
        return StudentAnsewers;
    }

    public void setStudentAnsewers(String studentAnsewers) {
        StudentAnsewers = studentAnsewers;
    }

    public int getIddcours() {
        return iddcours;
    }

    public void setIddcours(int iddcours) {
        this.iddcours = iddcours;
    }

    public int getIdEnrollement() {
        return idEnrollement;
    }

    public void setIdEnrollement(int idEnrollement) {
        this.idEnrollement = idEnrollement;
    }

    public Exams(int idExams, int attempts, int duration, String question, String correctAnswers, int iddcours, String studentAnsewers, int idEnrollement) {
        this.idExams = idExams;
        Attempts = attempts;
        Duration = duration;
        Question = question;
        CorrectAnswers = correctAnswers;
        this.iddcours = iddcours;
        StudentAnsewers = studentAnsewers;
        this.idEnrollement = idEnrollement;
    }

    public Exams() {
    }
}
