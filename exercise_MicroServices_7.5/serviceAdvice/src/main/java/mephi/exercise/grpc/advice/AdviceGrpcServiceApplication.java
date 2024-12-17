package mephi.exercise.grpc.advice;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import mephi.exercise.grpc.MicroServices;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import mephi.exercise.grpc.AdviceServiceGrpc.AdviceServiceImplBase;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@GrpcService
@SpringBootApplication
public class AdviceGrpcServiceApplication extends AdviceServiceImplBase {

    public static void main(String[] args) {
        SpringApplication.run(AdviceGrpcServiceApplication.class, args);
    }

    @Override
    public void getAdviceForUser(MicroServices.AdviceRequest request, StreamObserver<MicroServices.AdviceResponse> responseObserver) {
        MicroServices.AdviceResponse response = MicroServices.AdviceResponse.newBuilder()
                .setMessage(request.getLogin() + " молодец!")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Advice: {} -> {}", request.getLogin(), response.getMessage());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info(this.getClass().getName() + " start");
    }
}
