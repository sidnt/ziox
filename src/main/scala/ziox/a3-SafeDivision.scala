package zion

import zio._
import console._

object SafeDivision extends App {

  def safeDivision(n: Int, d:Int): IO[Unit, Int] = {
    Task(n/d).catchAll(t => IO.fail(()))
  }

  val getValidInt: URIO[Console, Int] = (for {
    inputInt <- getStrLn
    validInt <- Task(inputInt.toInt)
  } yield validInt).catchAll(t => (putStr("invalid int. try again> ") *> getValidInt))

  val safeDivisionApp = (for {
    numer <- putStr("\nwhat number to divide? > ") *> getValidInt
    denom <- putStr("\nwhat number to divide it with? > ") *> getValidInt
    result <- safeDivision(numer, denom)
    _ <- putStr(s"\ngot result > $result\n")
  } yield ()).catchAll( _ => putStrLn("\ndivision failed\n"))

  val program = safeDivisionApp.exitCode

  def run(args: List[String]) = program

}