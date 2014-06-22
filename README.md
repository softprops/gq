# gq

Graphite Query

> Stylish immutable async composible graphite queries 


## usage

A query is represented by an instance of a `gq.Query`.
A query defines an interface for fetching graphite data represented as a `qa.Series`
A `Series` provides an interface for accessing points represented as `gq.Point` ( a type alias for (Option[Double], Long) ).
A points time is reprented as a the time in milliseconds since the epoc.


```scala
// for future interface
import scala.concurrent.ExecutionContext.Implicits.global
import gq.Query

// define a query connection interface ( optionally with a login )
val q = Query("https://graphite.host.com").as(user, pass)

// lookup the graphite stats for a pattern 
val uppers = q.names("stats.timers.api.*.upper")

// fetching just the resulting names ( values are computed lazily )
uppers(_.map(_.name)).onComplete(println)

// find the point containing the maximum value in each matching series
uppers(_.map { line =>
  (line.name, line.points.collect {
    case (Some(value), time) => (value, time)
  } match {
    case empty if empty.isEmpty => 0D
    case xs => xs.maxBy(_._1)
  })
}).onComplete(println)

```

Doug Tangren (softprops) 2014
