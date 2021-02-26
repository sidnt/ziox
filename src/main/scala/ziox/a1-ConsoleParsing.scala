package ziox

import zio._
import console._

import java.io.IOException

/* wazp that gets a line from console
 * and prints it back out */
object ConsoleEcho extends App {

  val consoleEcho = for {
    _ <- putStrLn("\n---")
    _ <- putStrLn("Enter a line>")
    l <- getStrLn
    _ <- putStrLn(s"You entered>\n$l")
    _ <- putStrLn("---\n")
  }  yield ()

  def run(args: List[String]) = consoleEcho.exitCode

}
/* wazp that asks for a number
 * and tells back whether the user cheated 
 * . 
 * the difference between TestIntegrality and TestIntegrality2 is in the decision of 
 * where should the if be put. whether the if check should return an effect or the message string */
object TestIntegrality extends App {

  def isItAnInteger(n: String): UIO[Boolean] = Task(n.toInt).fold(_ => false, _ => true)

  val testIntegrality: ZIO[Console, IOException, Unit] = for {
    _ <- putStrLn("\n---")
    _ <- putStrLn("Enter an integer>")
    l <- getStrLn
    b <- isItAnInteger(l)
    _ <- if (b) putStrLn(s"You indeed entered an integer.") else putStrLn("You didn't enter an integer.")
    _ <- putStrLn("---\n")
  } yield () 

  def run(args: List[String]) = testIntegrality.exitCode

}

object TestIntegrality2 extends App {

  val testIntegrality: RIO[Console, Unit] = for {
    _ <- putStrLn("\n---")
    _ <- putStrLn("Enter an integer>")
    l <- getStrLn.orDie
    b <- TestIntegrality.isItAnInteger(l)
    _ <- putStrLn { if (b) s"You indeed entered an integer." else "You didn't enter an integer." }
    _ <- putStrLn("---\n")
  } yield () 

  def run(args: List[String]) = testIntegrality.exitCode

}

object NagForInt_Buggy extends App {
  import java.io.IOException

  val nagForInt: ZIO[Console, IOException, Unit] = for {
    _ <- putStrLn("\n---")
    _ <- putStrLn("Enter an integer>")
    l <- getStrLn
    b <- TestIntegrality.isItAnInteger(l)
    _ <- if (b) putStrLn(s"You indeed entered an integer.") else nagForInt
    _ <- putStrLn("---\n")
  } yield ()

  def run(args: List[String]) = nagForInt.exitCode

}
object NagForInt extends App {

  val nagForInt: ZIO[Console, Unit, Unit] = for {
    _ <- putStrLn("\n---")
    _ <- putStrLn("Enter an integer>")
    l <- getStrLn.orDie
    b <- TestIntegrality.isItAnInteger(l)
    _ <- if (b) putStrLn(s"You indeed entered an integer.") else IO.fail(())
    _ <- putStrLn("---\n")
  } yield ()

  def run(args: List[String]) = nagForInt.retry(Schedule.forever).exitCode
}

object NagForInt_improved extends App {

  val readInt = for {
    intS  <-  putStr("enter an int> ") *> getStrLn
    int   <-  Task(intS.toInt)
  } yield int

  val nagForInt: URIO[Console, Int] = readInt orElse putStrLn("A valid Int wasn't entered. Please retry.") *> nagForInt

  def run(args: List[String]): URIO[ZEnv,ExitCode] = nagForInt.exitCode

}
