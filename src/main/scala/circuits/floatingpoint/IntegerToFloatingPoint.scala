package sysarch.circuits.floatingpoint

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class IntegerToFloatingPoint extends Module {
  val intInput    = IO(Input(Vec(32, Bool())))
  val floatOutput = IO(Output(Vec(32, Bool())))

  val sign      = intInput(31)
  val magnitude = intInput.slice(0, 31)

  // special case all zero
  val isZero = IsAllZero(31, magnitude)
  // locate the first 1 in  magnitude
  val oneHot = ToOneHot(31, magnitude)

  val mantissa = oneHotToMan(31, oneHot, magnitude)
  val exp      = oneHotToExp(31, oneHot)

  for (i <- 0 until 23) {
    floatOutput(i) := AND(mantissa(i), NOT(isZero))
  }

  for (i <- 0 until 8) {
    floatOutput(23 + i) := AND(exp(i), NOT(isZero))
  }

  floatOutput(31) := AND(sign, NOT(isZero))

}
