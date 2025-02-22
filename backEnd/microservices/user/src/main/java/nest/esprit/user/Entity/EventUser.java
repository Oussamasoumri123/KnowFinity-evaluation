package nest.esprit.user.Entity;

import jakarta.persistence.*;

import lombok.Data;
import nest.esprit.user.Entity.enumeration.EventType;

import java.time.LocalDateTime;

@Entity
@Data
public class EventUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private EventType eventType;
    private String description;
    private String device;
    private String ipAddress;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

}
