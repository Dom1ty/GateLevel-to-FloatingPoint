package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class nBitComparator(width: Int) extends Module {
  val a  = IO(Input(Vec(width, Bool())))
  val b  = IO(Input(Vec(width, Bool())))
  val gt = IO(Output(Bool()))
  val eq = IO(Output(Bool()))

  // loop from  n digit to 0 digit eq =
  val gtW = Wire(Vec(width + 1, Bool()))
  val eqW = Wire(Vec(width + 1, Bool()))

  gtW(width) := false.B
  eqW(width) := true.B

  for (i <- (0 until width).reverse) {
    val bitEq = NOT(XOR(a(i), b(i)))
    val bitGt = AND(a(i), NOT(b(i)))

    gtW(i) := OR(gtW(i + 1), AND(eqW(i + 1), bitGt))
    eqW(i) := AND(eqW(i + 1), bitEq)

  }
  gt := gtW(0)
  eq := eqW(0)
}

object nBitComparator {
  def apply(width: Int, a: Seq[Bool], b: Seq[Bool]): (Bool, Bool) = {
    val comp = Module(new nBitComparator(width))
    comp.a := a
    comp.b := b

    (comp.gt, comp.eq)
  }
}
