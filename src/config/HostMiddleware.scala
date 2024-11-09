// WIP
/* 

package config
import org.http4s.*
import cats.effect.IO
import cats.data.Kleisli
import org.http4s.headers.{Host}

object HostMiddleware {

  def checkHost(expectedHost: String): HttpRoutes[IO] => HttpRoutes[IO] = { routes =>
    Kleisli { (req: Request[IO]) =>
      req.headers.get[Host] match {
        case Some(hostHeader) if hostHeader.host.toString == expectedHost =>
          routes(req)
        case _ =>
          IO.pure(NotFound("Invalid Host Header"))
      }
    }
  }
}
*/
