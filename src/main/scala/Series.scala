package gq

/** Provides access to a lazily computed points in a series */
case class Series(
  name: String,
  from: Long,
  to: Long,
  private val step: Int,
  private val unparsed: String) {
  lazy val points: Stream[Series.Point] = {
    val stepMillis = step * 1000
    def next(time: Long, str: String, stream: Stream[Series.Point]): Stream[Series.Point] =
      str.takeWhile(_ != ',') match {
        case "" => stream
        case value =>
          // in a stat set, some values may not be present. let those be represented by -1
          val rep = if ("None" == value) None else Option(value.toDouble)
          (rep, time) #:: next(time + stepMillis, str.drop(value.size + 1), stream)
      }
    next(from, unparsed, Stream.empty)
  }
  override def toString = s"Series($name, $from, $to, $step, $points)"
}

object Series {
  type Point = (Option[Double], Long)
  val Headers = """(.+),(\d+),(\d+),(\d+)""".r
  def parse(str: String): Traversable[Series] =
    (List.empty[Series] /: str.split("\n")) {
      case (a, line) =>
        line.split('|') match {
          case Array(Headers(name, from, to, step), data) =>
            Series(name, from.toLong, to.toLong, step.toInt, data) :: a
          case _ =>
            a
      }
    }
}
