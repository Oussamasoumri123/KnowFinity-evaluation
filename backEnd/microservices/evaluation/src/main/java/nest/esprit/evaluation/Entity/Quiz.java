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

public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idQuiz;
    int Duration ;
    float scoreMin ;
    String Questions ;
    String CorrectAnswer;
    String stdAnswer;
    boolean isValidated;
    int IdStudent;
    int idchapitre;
    int idcour_chapitre;
    int idEnrollment;


    public int getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public float getScoreMin() {
        return scoreMin;
    }

    public void setScoreMin(float scoreMin) {
        this.scoreMin = scoreMin;
    }

    public String getQuestions() {
        return Questions;
    }

    public void setQuestions(String questions) {
        Questions = questions;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getStdAnswer() {
        return stdAnswer;
    }

    public void setStdAnswer(String stdAnswer) {
        this.stdAnswer = stdAnswer;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public int getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(int idStudent) {
        IdStudent = idStudent;
    }

    public int getIdchapitre() {
        return idchapitre;
    }

    public void setIdchapitre(int idchapitre) {
        this.idchapitre = idchapitre;
    }

    public int getIdcour_chapitre() {
        return idcour_chapitre;
    }

    public void setIdcour_chapitre(int idcour_chapitre) {
        this.idcour_chapitre = idcour_chapitre;
    }

    public Quiz(int idQuiz, int duration, float scoreMin, String questions, String correctAnswer, String stdAnswer, boolean isValidated, int idStudent, int idchapitre, int idcour_chapitre) {
        this.idQuiz = idQuiz;
        Duration = duration;
        this.scoreMin = scoreMin;
        Questions = questions;
        CorrectAnswer = correctAnswer;
        this.stdAnswer = stdAnswer;
        this.isValidated = isValidated;
        IdStudent = idStudent;
        this.idchapitre = idchapitre;
        this.idcour_chapitre = idcour_chapitre;
    }

    public Quiz() {
    }
}
