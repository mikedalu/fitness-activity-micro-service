package fitness.activityservice.controller;

import fitness.activityservice.dto.ActivityRequest;
import fitness.activityservice.dto.ActivityResponse;
import fitness.activityservice.service.ActivityService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {
    private ActivityService activityService;
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request){
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    @GetMapping()
    public ResponseEntity<List<ActivityResponse>> getUserActivities (@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getSingleActivity (@PathVariable(name = "activityId") String activityId) {
            System.out.println(activityId + " Activity Id from request");
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
