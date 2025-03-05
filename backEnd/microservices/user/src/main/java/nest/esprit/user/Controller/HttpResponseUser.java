    package nest.esprit.user.Controller;

    import com.fasterxml.jackson.annotation.JsonInclude;
    import org.springframework.http.HttpStatus;

    import java.util.Map;

    import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

    @JsonInclude(NON_DEFAULT)
    public class HttpResponseUser {
        private String timeStamp;
        private int statusCode;
        private HttpStatus status;
        private String reason;
        private String message;
        private String developerMessage;
        private Map<?, ?> data;

        // Private constructor for builder pattern
        private HttpResponseUser(Builder builder) {
            this.timeStamp = builder.timeStamp;
            this.statusCode = builder.statusCode;
            this.status = builder.status;
            this.reason = builder.reason;
            this.message = builder.message;
            this.developerMessage = builder.developerMessage;
            this.data = builder.data;
        }

        // Getters
        public String getTimeStamp() {
            return timeStamp;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getReason() {
            return reason;
        }

        public String getMessage() {
            return message;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }

        public Map<?, ?> getData() {
            return data;
        }

        // Static inner Builder class
        public static class Builder {
            private String timeStamp;
            private int statusCode;
            private HttpStatus status;
            private String reason;
            private String message;
            private String developerMessage;
            private Map<?, ?> data;

            public Builder timeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
                return this;
            }

            public Builder statusCode(int statusCode) {
                this.statusCode = statusCode;
                return this;
            }

            public Builder status(HttpStatus status) {
                this.status = status;
                return this;
            }

            public Builder reason(String reason) {
                this.reason = reason;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public Builder developerMessage(String developerMessage) {
                this.developerMessage = developerMessage;
                return this;
            }

            public Builder data(Map<?, ?> data) {
                this.data = data;
                return this;
            }

            public HttpResponseUser build() {
                return new HttpResponseUser(this);
            }
        }

        // Static factory method for builder
        public static Builder builder() {
            return new Builder();
        }
    }
