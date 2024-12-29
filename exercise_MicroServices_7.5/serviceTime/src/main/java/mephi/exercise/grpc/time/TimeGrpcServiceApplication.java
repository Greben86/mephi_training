package mephi.exercise.grpc.time;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import mephi.exercise.grpc.MicroServices;
import net.devh.boot.grpc.server.service.GrpcService;
import mephi.exercise.grpc.TimeServiceGrpc.TimeServiceImplBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@GrpcService
@SpringBootApplication
public class TimeGrpcServiceApplication extends TimeServiceImplBase {

    public static void main(String[] args) {
        SpringApplication.run(TimeGrpcServiceApplication.class, args);
    }

    @Override
    public void getCurrentTime(Empty request, StreamObserver<MicroServices.TimeResponse> responseObserver) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date now = Calendar.getInstance().getTime();
        MicroServices.TimeResponse response = MicroServices.TimeResponse.newBuilder()
                .setCurrentTimestamp(dateFormat.format(now))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Time: {}", response.getCurrentTimestamp());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info(this.getClass().getName() + " start");
    }
}
