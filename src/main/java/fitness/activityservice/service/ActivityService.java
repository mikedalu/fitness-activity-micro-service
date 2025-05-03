package fitness.activityservice.service;

import fitness.activityservice.ActivityRepository;
import fitness.activityservice.dto.ActivityRequest;
import fitness.activityservice.dto.ActivityResponse;
import fitness.activityservice.model.Activity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //automatically execute dependcy inject of services into the constructure
@Slf4j //used for loggin
public class ActivityService {
    private final ActivityRepository activityRepository;
    private  final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}") //grabbing the properties from yml file
    private String exchange;
    @Value("${rabbitmq.routing.key}") //getting the properties form yml file
    private String routingKey;
     public ActivityResponse trackActivity(ActivityRequest activityRequest){

         boolean isValidUserId = validateUserId(activityRequest.getUserId());

         if(!isValidUserId) {
             throw new RuntimeException("Invalid User: "+ activityRequest.getUserId());
         }
         Activity activity = Activity.builder().userId(activityRequest.getUserId()).type(activityRequest.getType()).duration(activityRequest.getDuration()).startTime(activityRequest.getStartTime()).caloriesBurned(activityRequest.getCaloriesBurned()).additionalMetrics(activityRequest.getAdditionalMetrics()).build();

         Activity savedActivity = activityRepository.save(activity);

         //Publish to RabbitMQ for AI Processing
         try {
//             rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
             rabbitTemplate.convertAndSend(exchange,routingKey, savedActivity);

         } catch (Exception e){
            log.error("Failed to publish activity to RabbitMQ : ", e);
         }
         return mapToResponse(savedActivity);
     }
     private ActivityResponse mapToResponse(Activity activity){
         ActivityResponse activityResponse = new ActivityResponse();
         activityResponse.setId(activity.getId());
         activityResponse.setUserId(activity.getUserId());
         activityResponse.setType(activity.getType());
         activityResponse.setDuration(activity.getDuration());
         activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
         activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
         activityResponse.setStartTime(activity.getStartTime());
         activityResponse.setCreatedAt(activity.getCreatedAt());
         activityResponse.setUpdatedAt(activity.getUpdatedAt());

         return  activityResponse;
     }

    public List<ActivityResponse> getUserActivities(String userId) {
         List<Activity> activities = activityRepository.findByUserId(userId);
         return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }



    public ActivityResponse getActivityById(String activityId) {
         return activityRepository.findById(activityId).map(this::mapToResponse).orElseThrow(()-> new RuntimeException("Activity not found with ID " + activityId));

    }

    //This methods calls the user micro service to check if userId is valid
    public boolean validateUserId (String userId) {
         return userValidationService.validateUsesrId(userId);
    }
}
