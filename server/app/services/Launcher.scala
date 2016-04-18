package services

import actors.Server
import akka.actor.{ActorSystem, Props}
import javax.inject._
import play.api.inject.ApplicationLifecycle
import scala.concurrent.Future

@Singleton
class Launcher @Inject() (system: ActorSystem, appLifecycle: ApplicationLifecycle) {

	println("Starting client server")
	val serv = system.actorOf(Props[Server], "server")

	// When the application starts, register a stop hook with the
	// ApplicationLifecycle object. The code inside the stop hook wil
	// be run when the application stops.
	appLifecycle.addStopHook { () =>
		Future.successful(())
	}
}
