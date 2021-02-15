package zion

import zio._
import console._

object ZMain extends App:
  def run(args: List[String]) = putStrLn("Hi").exitCode
