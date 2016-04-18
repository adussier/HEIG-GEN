package actors

import akka.actor.Actor
import akka.io.Tcp.{PeerClosed, Received, Write}

object Socket {

}

class Socket extends Actor {
	override def receive = {
		case Received(data) =>
			println("Received: " + data)
			sender() ! Write(data)

		case PeerClosed =>
			println("Remote disconected")
			context.stop(self)
	}
}
