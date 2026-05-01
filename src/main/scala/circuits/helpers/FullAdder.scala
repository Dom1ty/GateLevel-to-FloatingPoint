package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class FullAdder extends Module {
  val a    = IO(Input(Bool()))
  val b    = IO(Input(Bool()))
  val cin  = IO(Input(Bool()))
  val sum  = IO(Output(Bool()))
  val cout = IO(Output(Bool()))

  // todo
  sum  := XOR(XOR(a, b), cin)
  cout := OR(AND(XOR(a, b), cin), AND(a, b))

}

object myFullAdder {
  def apply(a: Bool, b: Bool, cin: Bool): (Bool, Bool) = {
    val fa = Module(new FullAdder)
    fa.a   := a
    fa.b   := b
    fa.cin := cin

    (fa.sum, fa.cout)
  }
}
