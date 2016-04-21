package actors

import akka.actor.Actor
import akka.io.Tcp.{PeerClosed, Received}
import akka.util.{ByteString, CompactByteString}
import java.nio.ByteOrder
import protocol.{Message, Protocol}
import scala.annotation.tailrec

object Socket {

}

class Socket extends Actor {
	private implicit val byteOrder = ByteOrder.BIG_ENDIAN

	def receive = buffer(CompactByteString())

	def buffer(buf: ByteString): Receive = {
		case Received(data) =>
			val (msgs, remainder) = splitMessages(buf ++ data)
			context.become(buffer(remainder))

		case PeerClosed =>
			println("Remote disconected")
			context.stop(self)
	}

	def splitMessages(data: ByteString): (List[Message], ByteString) = {
		@tailrec
		def split(messages: List[Message], current: ByteString): (List[Message], ByteString) = {
			if (current.length < Protocol.MESSAGE_HEADER_LENGTH) {
				(messages.reverse, current)
			} else {
				val it = current.iterator
				val mtype = it.getByte
				val size = it.getShort

				if (size > Protocol.MESSAGE_MAX_LENGHT || size < 0) {
					throw new RuntimeException("Invalid message size")
				}

				if (current.length < size + Protocol.MESSAGE_HEADER_LENGTH) {
					(messages.reverse, current)
				} else {
					val rem = current.drop(Protocol.MESSAGE_HEADER_LENGTH)
					if (size == 0) {
						split(Message.create(mtype) :: messages, rem)
					} else {
						val (head, tail) = rem.splitAt(size)
						split(Message.create(mtype, head.toByteBuffer) :: messages, tail)
					}
				}
			}
		}

		split(Nil, data)
	}
}
