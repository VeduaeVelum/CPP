package main_classes

/**
 * @Class ScalaFunctions Class sort replays fields with the quick sort
 */

class ScalaFunctions {
  def qsort(xs: Array[Int], l: Int, r: Int) {
    val pivot = xs((l + r) / 2)
    var i = l
    var j = r
    while (i <= j) {
      while (xs(i) < pivot) i += 1
      while (xs(j) > pivot) j -= 1
      if (i <= j) {
        val temp = xs(i)
        xs(i) = xs(j)
        xs(j) = temp
        i += 1
        j -= 1
      }
    }
    if (l < j) qsort(xs, l, j)
    if (i < r) qsort(xs, i, r)
  }
  
  def qsort(xs: Array[Long], l: Int, r: Int) {
    val pivot = xs((l + r) / 2)
    var i = l
    var j = r
    while (i <= j) {
      while (xs(i) < pivot) i += 1
      while (xs(j) > pivot) j -= 1
      if (i <= j) {
        val temp = xs(i)
        xs(i) = xs(j)
        xs(j) = temp
        i += 1
        j -= 1
      }
    }
    if (l < j) qsort(xs, l, j)
    if (i < r) qsort(xs, i, r)
  } 
  
  def count(array: Array[Double]): Double = {
    array.foldLeft(0.0)((m: Double, n: Double) => m + n)
  }
  
  def chooser(value : Any): String = {
    value match {
      case "Head" => "--USER NOTATION--\r\nA - alive cell, " +
        "D - dead cell, matrix shows polygon state\r\n\r\n"
      case value: Int => "Game FPS: " + value.toString() + "\r\n"
      case value: Long => "\r\nGeneration: " + value.toString() + "\r\n"
      case true => "A "
      case false => "D "
    }
  }
 }