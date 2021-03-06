package actors

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, OneForOneStrategy, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import java.net.InetSocketAddress
import scala.concurrent.duration._

object Server {

}

class Server extends Actor {
	implicit val system = context.system
	IO(Tcp) ! Bind(self, new InetSocketAddress("0.0.0.0", 4321))

	override def receive = {
		case b @ Bound(localAddress) =>
			println("Serveur lancé: " + localAddress)

		case CommandFailed(_: Bind) =>
			println("Binding failed")
			context.stop(self)

		case c @ Connected(remote, local) =>
			println("Connected; remote = " + remote + ", local = " + local)
			sender() ! Register(context.actorOf(Props[Socket]))
	}

	override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 0, withinTimeRange = 1.minute) {
		case _ => Stop
	}
}
