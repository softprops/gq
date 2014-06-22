package gq

import dispatch.{ Http, Req, url }
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration.Duration

case class Credentials(user: String, pass: String) {
  override def toString = s"Credentials($user, ${"x"*pass.size})"
}

case class Query(
  private val host: String,
  private val credentials: Option[Credentials] = None,
  private val _from: Option[Time]              = None, // will default to 24 hours ago
  private val _until: Option[Time]             = None, // will default to now
  private val stats: Seq[Stat]                 = Seq.empty,
  private val http: Http                       = Http)
 (implicit ec: ExecutionContext) {

  def as(user: String, password: String) =
    copy(credentials = Some(Credentials(user, password)))

  def from(f: Duration): Query = from(Time.Duration(f))

  def from(f: Time): Query = copy(_from = Some(f))

  def until(u: Duration): Query = until(Time.Duration(u))

  def until(u: Time): Query = copy(_until = Some(u))

  def stat(sx: Stat*) = copy(stats = stats ++ sx)

  def names(nx: String*) = copy(stats = stats ++ nx.map(Stat.Name(_)))

  case class Str(str: String) {
    private[this] val canonicalized = {
      val queryStr = if (str.startsWith("?")) str.drop(1) else str
      s"$host/render?format=raw&$queryStr"
    }

    def apply(): Future[Traversable[Series]] = apply(identity)

    def apply[T](transform: Traversable[Series] => T): Future[T] =
      request(url(canonicalized), transform)
  }

  /** process a raw query string */
  def str(queryString: String) =  Str(queryString)

  def apply(): Future[Traversable[Series]] = apply(identity)

  def apply[T](transform: Traversable[Series] => T): Future[T] =
    request(url(host) / "render" <<?
            (("format" -> "raw") :: Nil)
            ++ _from.map(("from"   -> _.value))
            ++ _until.map(("until" -> _.value))
            ++ stats.map(("target" -> _.query)), transform)

  def close() = http.shutdown()
  
  override def toString = {
    val qStr = (_from.map(("from" -> _.value))
     ++ _until.map(("until" -> _.value))
     ++ stats.map(("target" -> _.query))).map {
       case (k, v) => s"$k=$v"
     } mkString("&")
    s"$host/render?format=raw&$qStr"
  }

 private def request[T](req: Req, transform: Traversable[Series] => T): Future[T] =
    http((credentials match {
      case Some(Credentials(user, pass)) => req.as_!(user, pass)
      case _ => req
    }) OK {
      resp => transform(Series.parse(resp.getResponseBody))
    })
}

