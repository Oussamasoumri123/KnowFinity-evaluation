package nest.esprit.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import nest.esprit.course.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/course")
@Slf4j
public class testController {
    @GetMapping("/test")
    public ResponseEntity<?> getAccountDetails(
            @RequestHeader(value = "X-User-DTO", required = false) String userJson) {

        if (userJson == null) {
            return ResponseEntity.badRequest().body("Missing UserDTO header");
        }

        log.info("Received raw UserDTO JSON: {}", userJson); // Log raw JSON

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Add this line
            UserDTO userDTO = objectMapper.readValue(userJson, UserDTO.class);




            log.info("Deserialized UserDTO in Course-Service: {}", userDTO);

            return ResponseEntity.ok("User details: " + userDTO);
        } catch (JsonProcessingException e) {
            log.error("Error parsing UserDTO", e);
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing UserDTO");
        }
    }

}





