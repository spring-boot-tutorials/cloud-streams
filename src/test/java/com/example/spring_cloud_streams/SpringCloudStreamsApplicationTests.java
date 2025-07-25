package com.example.spring_cloud_streams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@EnableTestBinder
@SpringBootTest
class SpringCloudStreamsApplicationTests {

	@Autowired
	private InputDestination input;

	@Autowired
	private OutputDestination output;

	@Test
	void whenSendingLogMessage_thenMessageIsEnrichedWithPrefix() {
		Message<String> messageIn = MessageBuilder.withPayload("hello world").build();
		input.send(messageIn, "my.input.queue.log.messages");

		Message<byte[]> messageOut = output.receive(1000L, "my.output.queue.log.messages");
		assertThat(messageOut.getPayload())
				.asString()
				.isEqualTo("[Marcus] - hello world");
	}

	@Test
	void whenProcessingLongLogMessage_thenItsEnrichedWithPrefix() {
		Message<String> messageIn = MessageBuilder.withPayload("hello world").build();
		input.send(messageIn, "processLogs-in-0");

		Message<byte[]> messageOut = output.receive(1000L, "my.output.queue.log.messages");
		assertThat(messageOut.getPayload())
				.asString()
				.isEqualTo("[Marcus] - hello world");
	}

	@Test
	void whenProcessingShortLogMessage_thenItsNotEnrichedWithPrefix() {
		Message<String> messageIn = MessageBuilder.withPayload("hello").build();
		input.send(messageIn, "processLogs-in-0");

		Message<byte[]> messageOut = output.receive(1000L, "my.output.queue.log.messages");
		assertThat(messageOut .getPayload())
				.asString()
				.isEqualTo("hello");
	}
}