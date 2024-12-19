package mephi.exercise.grpc.auth;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import mephi.exercise.grpc.AuthServiceGrpc.AuthServiceImplBase;
import mephi.exercise.grpc.MicroServices;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@GrpcService
@SpringBootApplication
public class AuthGrpcServiceApplication extends AuthServiceImplBase {
    private static final String SUPER_USER = "Виктор";

    public static void main(String[] args) {
        SpringApplication.run(AuthGrpcServiceApplication.class, args);
    }

    @Override
    public void checkAuthByLogin(MicroServices.AuthRequest request, StreamObserver<MicroServices.AuthResponse> responseObserver) {
        MicroServices.AuthResponse response = MicroServices.AuthResponse.newBuilder()
                .setStatus(SUPER_USER.equalsIgnoreCase(request.getLogin()) ? "OK" : "Access Denied")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Auth: {} -> {}", request.getLogin(), response.getStatus());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info(this.getClass().getName() + " start");
    }
}
