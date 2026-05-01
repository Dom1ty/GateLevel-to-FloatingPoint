package sysarch.circuits.integer

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class SignedMagnitudeToTwosComplement(width: Int) extends Module {
  val signedMagnitude = IO(Input(Vec(width, Bool())))
  val twosComplement  = IO(Output(Vec(width, Bool())))

  val sign = signedMagnitude(width - 1)

  val inverted = ConditionalInvertExceptSign(width, signedMagnitude, sign)
  twosComplement := ConditionalIncrement(width, inverted, sign)

}
