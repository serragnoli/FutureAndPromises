import java.io.{File, FileOutputStream, PrintWriter}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * Created by tvsdev on 07/01/2017.
  */
object Runner {
  def main(args: Array[String]): Unit = {
    implicit val ec = ExecutionContext.global
    val Session: Session = new Session

    Session.getFriendsFor(171) onComplete {
      case Success(x) =>
        for (i <- x) println(s"Friend: $i")
      case Failure(t) => println(s"Failed getting friends list due to $t")
    }

    Session.getPostsFor(171) onComplete {
      case Success(x) =>
        for (i <- x) println(s"One post was $i")
      case Failure(t) =>
        println(s"Failed getting post list due $t")
    }

    val waitingTime: Int = 3
    println(s"Waiting for $waitingTime seconds before end of execution")
    Thread.sleep(waitingTime.second.toMillis)
  }
}

class Session {
  private val NetworkLatency: Long = 3.seconds.toMillis
  private val Facebook: Facebook = new Facebook

  private def considerNetworkLatency(): Unit = {
    Thread.sleep(NetworkLatency)
  }

  def getFriendsFor(userId: Long): Future[List[Friend]] = Future {
    considerNetworkLatency()

    Facebook.getFriends(userId)
  }

  def getPostsFor(userId: Long): Future[List[String]] = Future {
    considerNetworkLatency()

    Facebook.getRecentPosts(userId)
  }
}

class Facebook {
  def getFriends(userId: Long): List[Friend] = {
    val karen = Friend(firstName = "Karen", surname = "Lopes")
    val isabella = Friend("Isabella")

    List[Friend](new Friend, karen, isabella)
  }

  def getRecentPosts(userId: Long): List[String] = {
    List[String]("Post 1", "Post 2", "Post 3", "Post 4")
  }
}

case class Friend(firstName: String = "Fabio", surname: String = "Serragnoli")

