package mephi.exercise.grpc.gateway;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import mephi.exercise.grpc.AdviceServiceGrpc;
import mephi.exercise.grpc.AuthServiceGrpc;
import mephi.exercise.grpc.MicroServices;
import mephi.exercise.grpc.TimeServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class GatewayGrpcServiceApplication implements ApplicationRunner {

    @GrpcClient("advice_service")
    public AdviceServiceGrpc.AdviceServiceBlockingStub adviceServiceBlockingStub;
    @GrpcClient("auth_service")
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;
    @GrpcClient("time_service")
    public TimeServiceGrpc.TimeServiceBlockingStub timeServiceBlockingStub;

    public static void main(String[] args) {
        SpringApplication.run(GatewayGrpcServiceApplication.class, args);
    }


//    @GetMapping("/message/get")
//    public ResponseEntity<String> getCompleteMessage(@RequestParam("user") String user) {
//        try {
//            log.info("start");
//            log.info("Channel: " + timeServiceBlockingStub.getChannel().authority());
//            log.info("Call options: " + timeServiceBlockingStub.getCallOptions().toString());
//            MicroServices.TimeResponse timeResponse = timeServiceBlockingStub
//                    .getCurrentTime(Empty.getDefaultInstance());
//
//            log.info("Gateway: {} -> {}", user, timeResponse.getCurrentTimestamp());
//
//            return ResponseEntity.ok(timeResponse.getCurrentTimestamp());
//        } catch (Exception e) {
//            log.error(e.getClass().getName(), e);
//            return ResponseEntity.ok(e.getMessage());
//        }
//    }

    @GetMapping("/message/get")
    public ResponseEntity<String> getCompleteMessage(@RequestParam("user") String user) {
        MicroServices.AuthResponse authResponse = authServiceBlockingStub
                .checkAuthByLogin(MicroServices.AuthRequest.newBuilder().setLogin(user).build());

        if (!"OK".equals(authResponse.getStatus())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse.getStatus());
        }

        MicroServices.AdviceResponse adviceResponse = adviceServiceBlockingStub
                .getAdviceForUser(MicroServices.AdviceRequest.newBuilder().setLogin(user).build());

        MicroServices.TimeResponse timeResponse = timeServiceBlockingStub
                .getCurrentTime(Empty.getDefaultInstance());

        log.info("Gateway: {} -> {} {}", user, timeResponse.getCurrentTimestamp(), adviceResponse.getMessage());

        return ResponseEntity.ok(timeResponse.getCurrentTimestamp() + " " + adviceResponse.getMessage());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("It work!");
    }
}
