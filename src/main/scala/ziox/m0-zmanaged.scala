package ziox

import zio._
import console._

object ZManagedExample1 extends App {

  val zm1 = ZManaged.make(putStrLn("acquiring 1")*>UIO(1))(i => putStrLn(s"releasing $i"))
  val zm2 = ZManaged.make(putStrLn("acquiring 2")*>UIO(2))(i => putStrLn(s"releasing $i"))
  val zm3 = ZManaged.make(putStrLn("acquiring 3")*>UIO(3))(i => putStrLn(s"releasing $i"))
  

  val compositeZm = for {
    v1  <-  zm1
    v2  <-  zm2
    v3  <-  zm3
  } yield s"acquired $v1, and $v2, and $v3"

  val cmz = compositeZm.use(s => putStrLn(s"using composite zmanaged.\ngot string> $s"))

  def run(args: List[String]): URIO[ZEnv,ExitCode] = cmz.exitCode

}
