package gq

import dispatch._
import scala.concurrent.{ ExecutionContext, Future }

case class Credentials(user: String, pass: String) {
  override def toString = s"Credentials($user, ${"x"*pass.size})"
}

case class Query(
  private val host: String,
  private val credentials: Option[Credentials] = None,
  private val _from: Option[Time] = None, // will default to 24 hours ago
  private val _until: Option[Time] = None, // will default to now
  private val stats: Seq[Stat] = Seq.empty,
  private val http: Http = Http)
 (implicit ec: ExecutionContext) {

  def as(user: String, password: String) =
    copy(credentials = Some(Credentials(user, password)))

  def from(f: Time) =  copy(_from = Some(f))

  def until(u: Time) = copy(_until = Some(u))

  def stat(sx: Stat*) = copy(stats = stats ++ sx)

  def names(nx: String*) = copy(stats = stats ++ nx.map(Stat(_)))

  def apply(): Future[Traversable[Series]] = apply(identity)

  def apply[T](transform: Traversable[Series] => T): Future[T] =
    http((credentials match {
      case Some(Credentials(user, pass)) => url(host).as_!(user, pass)
      case _ => url(host)
    }) / "render" <<?
         (("format" -> "raw") :: Nil)
         ++ _from.map(("from" -> _.value))
         ++ _until.map(("until" -> _.value))
         ++ stats.map(("target" -> _.query)) OK {
           resp => transform(Series.parse(resp.getResponseBody))
         })

  def close() = http.shutdown()
  
}

