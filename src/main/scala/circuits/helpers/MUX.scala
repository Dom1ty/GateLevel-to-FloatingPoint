package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class Mux(width: Int) extends Module {
  val a   = IO(Input(Vec(width, Bool())))
  val b   = IO(Input(Vec(width, Bool())))
  val sel = IO(Input(Bool()))
  val out = IO(Output(Vec(width, Bool())))

  // todo
  for (i <- 0 until width) {
    val choice1 = AND(a(i), NOT(sel))
    val choice2 = AND(b(i), sel) // 利用and 和1 等于本身特性 实现 sel = 0 -> a(i)    sel = 1 -> b(i)
    out(i) := OR(choice1, choice2)
  }
}

object Mux {
  def apply(width: Int, sel: Bool, a: Seq[Bool], b: Seq[Bool]): Seq[Bool] = {
    val m = Module(new Mux(width))
    m.a   := a
    m.b   := b
    m.sel := sel
    m.out
  }
}
