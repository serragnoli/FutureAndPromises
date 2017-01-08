import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

object Runner {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  def main(args: Array[String]): Unit = {
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
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  private val NetworkLatency: Long = 2.seconds.toMillis
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
    val karen = Friend(firstName = "Mary", surname = "Terry")
    val isabella = Friend("Jenny")

    List[Friend](new Friend, karen, isabella)
  }

  def getRecentPosts(userId: Long): List[String] = {
    List[String]("Post 1", "Post 2", "Post 3", "Post 4")
  }
}

case class Friend(firstName: String = "John", surname: String = "Smith")

