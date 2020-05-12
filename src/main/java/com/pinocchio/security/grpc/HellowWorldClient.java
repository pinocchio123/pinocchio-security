package com.pinocchio.security.grpc;

import com.pinocchio.grpc.helloworld.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HellowWorldClient {
    private static final Logger logger = LoggerFactory
            .getLogger(HellowWorldClient.class);
    private HelloWorldServiceGrpc.HelloWorldServiceBlockingStub helloWorldServiceBlockingStub;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8088).usePlaintext().build();
        helloWorldServiceBlockingStub = HelloWorldServiceGrpc.newBlockingStub(managedChannel);
    }

    public String sayHello(String firstName, String lastName) {
        Person person = Person.newBuilder().setFirstName(firstName).setLastName(lastName).build();
        logger.info("client sending {}", person);

        Greeting greeting = helloWorldServiceBlockingStub.sayHello(person);
        logger.info("client received {}", greeting);

        return greeting.getMessage();
    }

    public int addOperation(int a, int b) {
        A1 a1 = A1.newBuilder().setA(a).setB(b).build();
        A2 a2 = helloWorldServiceBlockingStub.addOperation(a1);
        return a2.getMessage();
    }

}
