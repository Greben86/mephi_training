package mephi.exercise.grpc.gateway;

import com.google.protobuf.Empty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import mephi.exercise.grpc.AdviceServiceGrpc;
import mephi.exercise.grpc.AuthServiceGrpc;
import mephi.exercise.grpc.MicroServices;
import mephi.exercise.grpc.TimeServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class GatewayGrpcServiceApplication {

    @GrpcClient("advice_service")
    public AdviceServiceGrpc.AdviceServiceBlockingStub adviceServiceBlockingStub;
    @GrpcClient("auth_service")
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;
    @GrpcClient("time_service")
    public TimeServiceGrpc.TimeServiceBlockingStub timeServiceBlockingStub;

    public static void main(String[] args) {
        SpringApplication.run(GatewayGrpcServiceApplication.class, args);
    }

    @GetMapping("/message/get")
    public ResponseEntity<Response> getCompleteMessage(@RequestParam("user") String user) {
        var authResponse = authServiceBlockingStub
                .checkAuthByLogin(MicroServices.AuthRequest.newBuilder().setLogin(user).build());

        var adviceResponse = adviceServiceBlockingStub
                .getAdviceForUser(MicroServices.AdviceRequest.newBuilder().setLogin(user).build());

        var timeResponse = timeServiceBlockingStub
                .getCurrentTime(Empty.getDefaultInstance());

        var response = Response.builder()
                .setAuthServiceAnswer(authResponse.getStatus())
                .setAdviceServiceAnswer(adviceResponse.getMessage())
                .setTimeServiceAnswer(timeResponse.getCurrentTimestamp())
                .build();

        log.info("Gateway: {} -> {}", user, response);

        return ResponseEntity
                .status("OK".equals(authResponse.getStatus()) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info(this.getClass().getName() + " start");
    }

    @ToString
    @Getter
    @Builder(setterPrefix = "set")
    public static class Response {
        private final String authServiceAnswer;
        private final String adviceServiceAnswer;
        private final String timeServiceAnswer;
    }
}
