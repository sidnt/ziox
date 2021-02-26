package ziox

import zio._
import console._
import stream._

/**wazs that prints next 20 random ints to the console */
object s0 extends App {

  /* zio that outputs the next random int to the console */
  val z0 = for {
    i     <-    random.nextInt
    _     <-    console.putStrLn("next random int> " + i.toString)
  } yield ()

  val randomStream = ZStream.repeatEffect(z0).take(20)

  val z = randomStream.runDrain

  def run(args: List[String]) = z.exitCode

}
