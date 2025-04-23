package fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient webClient;
    public boolean  validateUsesrId (String userId){

        try {

        return webClient.get().uri("/api/users/{userId}/validate",userId).retrieve().bodyToMono(Boolean.class).block();
        }
        catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new RuntimeException("User not found " + userId);
            } else {
                if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                    throw  new RuntimeException("Invalid User ID " +userId);
                }
            }
            return false;
        }
    }

}









