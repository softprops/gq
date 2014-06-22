package gq

sealed trait Stat {
  def query: String
}

object Stat {
  case class Name(query: String) extends Stat

  // http://graphite.readthedocs.org/en/latest/functions.html

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.absolute */
  case class Abs(names: String*) extends Stat {
    val query = names.mkString(",")
  }

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.aggregateLine */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.alias */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.aliasByMetric */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.aliasByNode */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.aliasSub */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.alpha */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.areaBetween */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.asPercent */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.averageAbove */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.averageBelow */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.averageOutsidePercentile */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.averageSeries */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.averageSeriesWithWildcards */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.cactiStyle */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.consolidateBy */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.constantLine */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.countSeries */

  /** http://graphite.readthedocs.org/en/latest/functions.html#graphite.render.functions.cumulative */

  // so many...
}
