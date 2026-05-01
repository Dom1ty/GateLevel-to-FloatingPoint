package sysarch.circuits.integer

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class TwosComplementToSignedMagnitude(width: Int) extends Module {
  val twosComplement  = IO(Input(Vec(width, Bool())))
  val signedMagnitude = IO(Output(Vec(width, Bool())))

  val sign = twosComplement(width - 1)

  val inverted = ConditionalInvertExceptSign(width, twosComplement, sign)
  signedMagnitude := ConditionalIncrement(width, inverted, sign)
}
