package sysarch.circuits.integer

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class OnesComplementToSignedMagnitude(width: Int) extends Module {
  val onesComplement  = IO(Input(Vec(width, Bool())))
  val signedMagnitude = IO(Output(Vec(width, Bool())))

  signedMagnitude := ConditionalInvertExceptSign(width, onesComplement, onesComplement(width - 1))
}
