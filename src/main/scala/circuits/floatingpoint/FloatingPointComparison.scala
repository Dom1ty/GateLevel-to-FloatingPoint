package sysarch.circuits.floatingpoint

import sysarch.chisel._
import sysarch.gates._
import sysarch.circuits.helpers._

// Output: 00 for equal, 01 for floatInput1 > floatInput2, 10 for floatInput1 < floatInput2, 11 for unordered (NaN cases)

class FloatingPointComparison extends Module {
  val a                = IO(Input(Vec(32, Bool())))
  val b                = IO(Input(Vec(32, Bool())))
  val comparisonResult = IO(Output(Vec(2, Bool())))

  val aSign = GetSign(a)
  val bSign = GetSign(b)
  val aExp  = GetExponent(a)
  val bExp  = GetExponent(b)
  val aMan  = GetMantissa(a)
  val bMan  = GetMantissa(b)

  // 11 case
  val unordered = OR(IsNaN(a), IsNaN(b))

  // diff sign
  val is_diff_sign = XOR(aSign, bSign)
  val same_sign    = NOT(is_diff_sign)

  // exp  and  mantissa
  val (expGt, expEq) = nBitComparator(8, aExp, bExp)
  val (manGt, manEq) = nBitComparator(23, aMan, bMan)

  val expLt = AND(NOT(expEq), NOT(expGt))
  val manLt = AND(NOT(manEq), NOT(manGt))

  // for pos
  // exp大 -> 数更大  正数：只看 exponent 就够了不需要考虑man
  // exp == ，man大 -> 数更大
  val posAgreater = OR(expGt, AND(expEq, manGt))
  val posALess    = OR(expLt, AND(expEq, manLt))

  // for neg
  val negAgreater = OR(expLt, AND(expEq, manLt))
  val negALess    = OR(expGt, AND(expEq, manGt))

  // 符号不同情况 a = 0     b= 1
  val aPosBNeg = AND(NOT(aSign), bSign)
  // a=  1     b = 0
  val aNegBPos = AND(aSign, NOT(bSign))

  // 相同符号
  val bothPos = AND(same_sign, NOT(aSign))
  val bothNeg = AND(same_sign, aSign)

  // 推出最后大小
  val aGreater = OR(aPosBNeg, OR(AND(bothPos, posAgreater), AND(bothNeg, negAgreater)))
  val aLess    = OR(aNegBPos, OR(AND(bothPos, posALess), AND(bothNeg, negALess)))

  // 相等情况    1.符号   man   exp
  val normalEq = AND(same_sign, AND(expEq, manEq))
  // 2 . 2. +0 -0
  val bothZero = AND(IsZero(a), IsZero(b))

  val equal = OR(normalEq, bothZero)

  comparisonResult(0) := OR(unordered, aGreater)
  comparisonResult(1) := OR(unordered, aLess)
}
