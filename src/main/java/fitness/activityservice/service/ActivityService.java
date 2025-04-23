package fitness.activityservice.service;

import fitness.activityservice.ActivityRepository;
import fitness.activityservice.dto.ActivityRequest;
import fitness.activityservice.dto.ActivityResponse;
import fitness.activityservice.model.Activity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
     public ActivityResponse trackActivity(ActivityRequest activityRequest){
         Activity activity = Activity.builder().userId(activityRequest.getUserId()).type(activityRequest.getType()).duration(activityRequest.getDuration()).startTime(activityRequest.getStartTime()).caloriesBurned(activityRequest.getCaloriesBurned()).additionalMetrics(activityRequest.getAdditionalMetrics()).build();

         Activity savedActivity = activityRepository.save(activity);

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
}
