import akka.actor.{Actor, ActorLogging, ActorSelection}

object Client {
  case class SendMessage(to: String, message: String)
  case class ReceiveMessage(from: String, message: String)
}

class Client(name: String, ip: String) extends Actor with ActorLogging {

  import ChatBox._
  import Client._

  var chatBox: Option[ActorSelection] = None

  override def preStart(): Unit = {
    chatBox = Some(context.actorSelection("akka.tcp://ChatSystem@$ip:2222/" +
      "user/ChatBox")) // node that chat box actor lookup is done using ChatBoxActor Running machine IP.
    //localhost if both ChatBoxActor and Client Actor are running on same machine.

    chatBox.map(actor => actor ! Register(name))

    chatBox.getOrElse({
      println("ChatBox unreachable, shutting down :(")
      context.stop(self)
    })
  }

  override def receive = {
    case SendMessage(to, message) => chatBox.map(actor => actor ! Message(name, to, message))
    case ReceiveMessage(from, message) =>
      println(s"$from says: $message")
    case _ => log.info("unknown message")
  }
}