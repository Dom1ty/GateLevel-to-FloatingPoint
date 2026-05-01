package sysarch.circuits.integer

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class SignedMagnitudeToOnesComplement(width: Int) extends Module {
  val signedMagnitude = IO(Input(Vec(width, Bool())))

  val onesComplement = IO(Output(Vec(width, Bool())))

  // if sign = 0 不取反     if sign = 1  符号位置不变取反      通过  原码  xor 0 = 原码       原码xor 1 =  取反实现
  val sign = signedMagnitude(width - 1)
  onesComplement := ConditionalInvertExceptSign(width, signedMagnitude, sign)

}
