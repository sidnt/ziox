package ziox

import zio.test._
import Assertion._

object a0a extends DefaultRunnableSpec {

  /**basic most test*/
  def spec = test("1+1=2")(assert(1+1)(equalTo(2)))
}

object a0b extends DefaultRunnableSpec {

  val additionSuite = suite("tests on addition")(
    test("a+0 = a")(assert(1+0)(equalTo(1))),
    test("a+a=2*a")(assert(1+1)(equalTo(2*1)))
  )

  val multiplicationSuite = suite("tests on multiplication")(
    test("a*0=0")(assert(1*0)(equalTo(0))),
    test("a*a=a^2")(assert(3*3)(equalTo(9)))
  )

  def spec = suite("addition & multiplication tests")(additionSuite, multiplicationSuite)

}

object a0c extends DefaultRunnableSpec {

  /**a spec can contain other specs or suites, as shown here */
  def spec = suite("nested more")(a0a.spec, a0b.additionSuite, a0b.multiplicationSuite)

}
