package ziox

import zio._
import console._
import random._
import stream._

/**wazs that prints next 20 random ints to the console */
object s0 extends App {

  /* zio that outputs the next random int to the console */
  val z0 = for {
    i     <-    random.nextInt
    _     <-    console.putStrLn("next random int> " + i.toString)
  } yield ()

  val randomStream: ZStream[Random with Console, Nothing, Unit] = ZStream.repeatEffect(z0).take(20) //it's not taking 20 ints, but 20 units

  val z: ZIO[Random with Console, Nothing, Unit] = randomStream.runDrain

  def run(args: List[String]) = z.exitCode

}

object s1 extends App {

  /* zio that outputs the next random int */
  val z0 = random.nextInt
  def pprint(i:Int) = console.putStrLn("next positive random int> " + i)

  /* tap allows us to execute an effect for every element in the stream
   * while emitting the elements unchanged */
  val posRandomsStream: ZStream[Random with Console, Nothing, Int] =
    ZStream.repeatEffect(z0).filter(_ > 0).tap(pprint).take(20)

  val randomPosInts: ZIO[Random with Console, Nothing, Chunk[Int]] = posRandomsStream.runCollect

  def run(args: List[String]) = randomPosInts.flatMap(ci => putStrLn("got Chunk[Int] " + ci.toString)).exitCode

}
