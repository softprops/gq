# gq

Graphite Query

> Stylish immutable async composible graphite queries 


## usage

A query is represented by an instance of a `gq.Query`.
A query defines an interface for fetching graphite `qa.Stats` data representings results as a list of `qa.Series`
A `Series` provides an interface for accessing points represented as `gq.Point` ( a type alias for (Option[Double], Long) ).
A points time is reprented as a the time in milliseconds since the epoc.


```scala
// for future interface
import scala.concurrent.ExecutionContext.Implicits.global
// representing time
import scala.concurrent.duration._
import gq.{ Stat, Query }

// define a query connection interface ( optionally with a login )
val q = Query("https://graphite.host.com").as(user, pass)

// lookup the graphite stats for a pattern 
val uppers = q.names("stats.timers.api.*.upper")

// set a window of time for the query ( defaults to -24 hours until now )
val window = uppers.from(-4.hours).until(-3.hours)

// fetching just the resulting names ( values are computed lazily )
window(_.map(_.name)).onComplete(println)

// find the point containing the maximum value in each matching series
window(_.map { line =>
  (line.name, line.points.collect {
    case (Some(value), time) => (value, time)
  } match {
    case empty if empty.isEmpty => 0D
    case xs => xs.maxBy(_._1)
  })
}).onComplete(println)


// perform function transformation on the server
q.stat(Stat.Alias("stats.timers.api.*.upper", "api"))().onComplete(println)

// parse a query from a raw url query string ( useful for if you have saved queries )
q.str("target=alias(stats.timers.api.*.upper,'api')")().onComplete(println)
```



Doug Tangren (softprops) 2014
