package ziox

import zio._ 
import zio.test._
import Assertion._

object a1a extends DefaultRunnableSpec {

  val effect = UIO(1+1)
  val effectsResult = 2

  def spec = testM("1+1=2")(
    assertM(effect)(equalTo(effectsResult))
  )

}

object a1b extends DefaultRunnableSpec {

  val e2 = UIO(1*0)
  val r2 = 0

  val a = 4
  val e3 = UIO(a+a)
  val r3 = a * 2

  def suite0 = suite("tests on addition")(
    testM("a+0=a")(
      assertM(e2)(equalTo(r2))
    ),
    testM("a+a=2a")(
      assertM(e3)(equalTo(r3))
    )
  )

  def spec: ZSpec[Environment,Failure] = suite0

}

object a1c extends DefaultRunnableSpec {

  val e1: URIO[Int, Int] = for {
    i <- ZIO.environment[Int]
  } yield (i + i)

  val e1p = e1.provide(1)

  def spec: ZSpec[Environment,Failure] =
    testM("a+a=2a (using environment)")(
      assertM(e1p)(equalTo(2))
    )

}

object a1d extends DefaultRunnableSpec {

  val e2: URIO[Int, Int] = for {
    i <- ZIO.environment[Int]
  } yield (i*i)

  def testM1(i: Int) =  testM("a+a=2a (using environment)")(
                          assertM(a1c.e1)(equalTo(i*2))
                        )

  def testM2(i: Int) =  testM("a*a=a^2 (using environment")(
                          assertM(e2)(equalTo(i*i))
                        )

  def suite0 = suite("tests on int (using environment)")(testM1(7),testM2(7))

  def suite1 = suite0.provide(7)

  def spec = suite1

}

object a1e extends DefaultRunnableSpec {

  val e1: URIO[Has[Int], Int] = for {
    i <- ZIO.access[Has[Int]](_.get)
  } yield (i+i)
  
  val e2: ZIO[Has[Int], Nothing, Int] = for {
    i <- ZIO.access[Has[Int]](_.get)
  } yield (i*i)

  def testM1(i: Int): ZSpec[Has[Int], Nothing] =
    testM("a+a=2a (using layer)")(
      assertM(e1)(equalTo(i*2))
    )

  def testM2(i: Int): ZSpec[Has[Int], Nothing] =  
    testM("a*a=a^2 (using layer")(
      assertM(e2)(equalTo(i*i))
    )

  val theInt = 7
  val tm1 = testM1(theInt)
  val tm2 = testM2(theInt)

  def suite0: Spec[Has[Int], TestFailure[Nothing], TestSuccess] =
    suite("tests on int (using layer)")(tm1,tm2)

  def suite1: Spec[Any, TestFailure[Nothing], TestSuccess] =
    suite0.provideLayer(ZLayer.succeed(theInt))

  def spec = suite1

}
