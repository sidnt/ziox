package ziox

import zio._
import console._
import duration._

object HiEverySecond_Buggy extends App {

  val program = putStrLn("Hi").repeat(Schedule.spaced(1.second))

  def run(args: List[String]) = program.exitCode

}

/* Ctrl+C doesn't work.
 * #howto make it work? */
object HiEverySecond extends App {

  val oncePerSecond = Schedule.spaced(1.second)
  val p0 = putStrLn("Hi").repeat(oncePerSecond)

  val p1 = for {
    _     <-    putStrLn("printing hi every second. press 'return/enter' key to interrupt.")
    hi    <-    p0.fork
    _     <-    getStrLn *> hi.interrupt
  } yield ()

  def run(args: List[String]) = p1.exitCode

}
