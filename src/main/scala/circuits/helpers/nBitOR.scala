package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class nBitOR(n: Int) extends Module {
  val a   = IO(Input(Vec(n, Bool())))
  val out = IO(Output(Bool()))

  var result = a(0)

  for (i <- 1 until n) {
    result = OR(result, a(i))
  }
  out := result
}
