package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class nBitAND(n: Int) extends Module {
  val a   = IO(Input(Vec(n, Bool())))
  val out = IO(Output(Bool()))

  // todo
  var result = a(0)

  for (i <- 1 until n) {
    result = AND(result, a(i))
  }
  out := result
}
