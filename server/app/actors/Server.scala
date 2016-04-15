package actors

import akka.actor.Actor
import akka.actor.Actor.Receive

class Server extends Actor {
	override def receive = {
		case _ => println("Unknown message")
	}
}
