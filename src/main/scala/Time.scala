package gq

import scala.concurrent.duration.{ Duration => SDuration }
import java.util.{ Date => JDate }

sealed trait Time {
  def value: String
}

object Time {
  /*case class Duration(dur: SDuration) extends Time {
    def value = ...
  }*/
  case class Date(date: JDate) extends Time {
    def value = Millis(date.getTime).value
  }
  case class Millis(millis: Long) extends Time {
    def value = millis.toString
  }
}
