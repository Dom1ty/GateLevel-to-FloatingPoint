package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class HalfAdder extends Module {
  val a    = IO(Input(Bool()))
  val b    = IO(Input(Bool()))
  val sum  = IO(Output(Bool()))
  val cout = IO(Output(Bool()))

  sum  := XOR(a, b)
  cout := AND(a, b)
}

object HalfAdder {
  def apply(a: Bool, b: Bool): (Bool, Bool) = {
    val ha = Module(new HalfAdder)
    ha.a := a
    ha.b := b

    (ha.sum, ha.cout)
  }
}
