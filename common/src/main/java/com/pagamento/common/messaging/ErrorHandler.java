package com.pagamento.common.messaging;

public interface ErrorHandler {

	void handle(Exception thrownException, ConsumerRecord<?, ?> record);

	void handle(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer);

}
