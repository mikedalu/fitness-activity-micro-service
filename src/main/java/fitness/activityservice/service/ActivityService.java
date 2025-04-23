package fitness.activityservice.service;

import fitness.activityservice.ActivityRepository;
import fitness.activityservice.dto.ActivityRequest;
import fitness.activityservice.dto.ActivityResponse;
import fitness.activityservice.model.Activity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
     public ActivityResponse trackActivity(ActivityRequest activityRequest){
         Activity activity = Activity.builder().userId(activityRequest.getUserId()).type(activityRequest.getType()).duration(activityRequest.getDuration()).startTime(activityRequest.getStartTime()).additionalMetrics(activityRequest.getAdditionalMedtrics()).build();

         Activity savedActivity = activityRepository.save(activity);

         return mapToResponse(savedActivity);
     }
     private ActivityResponse mapToResponse(Activity activity){
         ActivityResponse activityResponse = new ActivityResponse();
         activityResponse.setId(activity.getId());
         activityResponse.setType(activity.getType());
         activityResponse.setDuration(activity.getDuration());
         activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
         activityResponse.setCaloriesBurned(activity.getCaloriesBurned());

         return  activityResponse;
     }
}
