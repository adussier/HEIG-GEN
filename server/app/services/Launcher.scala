package services

import akka.actor.ActorSystem
import akka.io.{IO, Tcp}
import javax.inject._
import play.api.inject.ApplicationLifecycle
import scala.concurrent.Future

@Singleton
class Launcher @Inject()(system: ActorSystem, appLifecycle: ApplicationLifecycle) {

	println("Starting client server")

	val manager = IO(Tcp)

	// When the application starts, register a stop hook with the
	// ApplicationLifecycle object. The code inside the stop hook wil
	// be run when the application stops.
	appLifecycle.addStopHook { () =>
		Future.successful(())
	}
}
