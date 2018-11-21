import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object StartChatBox {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    val chatSystem = ActorSystem("ChatSystem", config)
    val chatBox = chatSystem.actorOf(Props[ChatBox], name = "ChatBox")
  }
}