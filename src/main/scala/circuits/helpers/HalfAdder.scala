package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class HalfAdder extends Module {
  val a    = IO(Input(Bool()))
  val b    = IO(Input(Bool()))
  val sum  = IO(Output(Bool()))
  val cout = IO(Output(Bool()))

  // todo
  sum  := XOR(a, b)
  cout := AND(a, b)
  // val andgate = Module(new ANDGate)
  // val xorgate = Module(new XORGate)
  // andgate.a := a
  // andgate.b := b
  // xorgate.a := a
  // xorgate.b := b
  // cout := andgate.out
  // sum  := xorgate.out
}
