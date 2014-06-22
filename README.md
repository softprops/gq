# gq

[Graphite](http://graphite.wikidot.com/) [Query](http://graphite.readthedocs.org/en/latest/render_api.html) ( \gee-cue\ )

> Stylish immutable async composible graphite queries 

## usage

A query is represented by an instance of a `gq.Query`.
A query defines an interface for fetching graphite `gq.Stats` data representing results as a list of `gq.Series`.
A `Series` provides an interface for accessing points represented as `gq.Point` ( a type alias for `(Option[Double], Long)` ).
A point's moment in time is represented as the time in milliseconds since the epoch.

```scala
// for future interface
import scala.concurrent.ExecutionContext.Implicits.global
// representing time
import scala.concurrent.duration._
import gq.{ Stat, Query }

// define a query connection interface ( optionally with a login )
val q = Query("https://graphite.host.com").as(user, pass)

// look up the graphite stats for a pattern
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
