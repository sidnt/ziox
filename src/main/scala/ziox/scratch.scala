package ziox

import zio._
import console._
import java.io.IOException

object scratch1 extends App {

  val z = putStrLn("how old?").flatMap(_ => getStrLn.flatMap(ageString => IO(ageString.toInt).orDie.flatMap(ageInt => (if(ageInt < 18) putStrLn("you're kid") else putStrLn("you're grown up")).map( _ => ExitCode(0)))).orDie)

  def run(args: List[String]): URIO[ZEnv,ExitCode] = z
  
}

object numberGuesser extends App {

  def run(args: List[String]): URIO[ZEnv,ExitCode] = putStrLn("guess a number between 1 and 5").flatMap( _ =>
    getStrLn.orDie.flatMap( numberString => 
      IO(numberString.toInt).orDie.flatMap( numberInt =>
        random.nextIntBetween(1,6).flatMap( randomNum =>
          putStrLn{if(numberInt == randomNum) s"yay, random num: $randomNum your guess $numberInt" else s"you fail. your guess $numberInt actual random number: $randomNum"}.map(_ => ExitCode(0))
        )
      )
    )
  )

}

object CollectAllAnswers extends App {

  val questions = List("Name?","Age?","Gender?","Mood?")

  def x = UIO(List.empty)

  def getAllAnswers(ls: List[String]): ZIO[Console, IOException, List[String]] = ls match {
    case Nil => UIO(List.empty)
    case head :: next => for {
      ans   <- putStrLn(head) *> getStrLn
      rest  <- getAllAnswers(next)
    } yield ans :: rest
  }

  val useAnswers = for {
    answers <- getAllAnswers(questions)
    _       <- putStrLn(answers.mkString(" - "))
  } yield ()

  def run(args: List[String]): URIO[ZEnv,ExitCode] = useAnswers.exitCode
}

object QuestionsForeach extends App {

  val questions = List("Name?","Age?","Gender?","Mood?")

  def getAllAnswers(ls: List[String]): ZIO[Console, IOException, List[String]] = ZIO.foreach(questions)(q=>putStrLn(q)*>getStrLn)

  val useAnswers = for {
    answers <- getAllAnswers(questions)
    _       <- putStrLn(answers.toString)
  } yield ()

  def run(args: List[String]): URIO[ZEnv,ExitCode] = useAnswers.exitCode

}
