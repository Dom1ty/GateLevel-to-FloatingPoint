package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

class nBitAdderSubtractor(width: Int) extends Module {
  val a          = IO(Input(Vec(width, Bool())))
  val b          = IO(Input(Vec(width, Bool())))
  val enable_sub = IO(Input(Bool()))
  val sum        = IO(Output(Vec(width, Bool())))
  val cout       = IO(Output(Bool()))

  // enable sub = true -> sub
  val not_b   = myNOT(width, b)
  val muxed_b = myMUX(width, enable_sub, b, not_b) // 用来运算的b

  // 减法就加一   加法 =  0  刚好作为进位
  var cin = enable_sub

  // n个fulladder 循环对每一位进行运算
  for (i <- 0 until width) {
    val fa = Module(new FullAdder)
    fa.a   := a(i)
    fa.b   := muxed_b(i)
    fa.cin := cin
    sum(i) := fa.sum
    // 更新下一轮的cin
    cin = fa.cout
  }
  // 给output赋值
  cout := XOR(enable_sub, cin) // cout 最后算出来的进位 加法 那么进位就是本身，减法就取反

}
