package protocol;

import java.nio.ByteBuffer;

public interface Message {
	static Message create(byte messageType) {
		return create(messageType, null);
	}

	static Message create(byte messageType, ByteBuffer payload) {
		switch (messageType) {
			default:
				throw new IllegalArgumentException("Unknown message type: " + messageType);
		}
	}
}
