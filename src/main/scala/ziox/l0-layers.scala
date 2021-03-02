package ziox

import zio._ 
import console._ 

object l0 extends App {

  final case class Err(reason: String)

  
  /** we have an effect, that environmentally depends on a pair of int
   * does something with it during execution, to produce a single int, 
   * might fail with an Err(...) in the process of its execution */
  
  val z0: ZIO[(Int,Int), Err, Int] =
    for {
      pair  <-  ZIO.access[(Int,Int)](identity)
      res   <-  if(pair._2 == 0) IO.fail(Err("division by zero!")) else UIO(pair._1/pair._2)
    } yield res


  /** interestingly enough, the z0 has been lifted just like that into ZLayer type
   * what does that even mean?
   * first'all it assumes a type which mirrors that of the original z0
   * and also takes up the behaviour of z0, wrt to the internal details of arriving at ROut from RIn */
  val l0: ZLayer[(Int,Int), Err, Has[Int]] = z0.toLayer

  // val l0a = l0.provide(9) // we can't provide things to layers
  // val l0a = Has((9,5)) >>> l0 // we can't even feed Has instances to layers
  val l0a = ZLayer.succeed((9,4))

  /** however, here we have an example of a layer, conforming to the exact same type
   * but behaving differently.
   * this layer depends on a pair of ints, but instead of dividing them, it sums them up */
  val l1: ZLayer[(Int,Int), Err, Has[Int]] = ZLayer.fromFunctionM( (pair) => UIO(pair._1 + pair._2))


  val app = for {
    z0e1    <-    z0.provide((3,0)).catchAll(e => putStrLn(e.toString))
    z0e2    <-    z0.provide((3,3)).catchAll(e => putStrLn(e.toString))
    _       <-    putStrLn(z0e1.toString + " " + z0e2)
  } yield ()

  def run(args: List[String]) = app.exitCode

}
