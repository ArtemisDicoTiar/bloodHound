
import utils.logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

package object services {
  implicit class Handler[T] (res: Future[Try[T]]) {
    def handleDBResponse(): Future[Any] = {
      res.map {
        case Success(value) => value
        case Failure(exception) =>
          logger.error(exception.toString)
          exception.toString
      }
    }
  }
}
