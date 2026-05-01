package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class nBitNOT(n: Int) extends Module {
  val a   = IO(Input(Vec(n, Bool())))
  val out = IO(Output(Vec(n, Bool())))

  for (i <- 0 until n) {
    out(i) := NOT(a(i))
  }

}

object nBitNOT {
  def apply(width: Int, a: Seq[Bool]): Seq[Bool] = {
    val n = Module(new nBitNOT(width))
    n.a := a
    n.out
  }
}
