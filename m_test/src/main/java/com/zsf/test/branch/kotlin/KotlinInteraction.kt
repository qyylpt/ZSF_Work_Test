package com.zsf.test.branch.kotlin

import com.zsf.application.BaseCommonApplication
import com.zsf.global.GlobalData
import com.zsf.test.R

/**
 * @author zsf
 * @date 2019/12/16
 * @Usage
 */

fun getDefaultString(): String{
    return GlobalData.getContext().resources.getString(R.string.m_test_kotlin_textView_result)
}

/**
 * 函数传递参数
 */
fun sum(a: Int, b: Int): Int{
    return a + b
}

/**
 * 函数传递可变多个参数
 */
fun sums(vararg v : Int): Int{
    var sun : Int = 0
    for (vt in v){
        sun += vt
    }
    return sun
}

/**
 * 类型转化
 */
fun instanceof(obj : Any): Int{
    if (obj is String){
        return obj.length
    }
    return 0
}

fun range(args: IntArray){
    // 遍历输出
    println("遍历输出：")
    for (arg in args){
        println(arg)
    }

    // 区间遍历
    println("区间遍历输出")
    for (arg in 1..4){
        println(arg)
    }

    // 正向步长
    println("正向步长")
    for (arg in 1..6 step 2){
        println(arg)
    }

    // 反向步长
    println("反向步长")
    for (arg in 6 downTo 3 step 2){
        println(arg)
    }

    // 使用until排除最后一个元素
    println("使用until排除最后一个元素")
    for (arg in 1 until 6){
        println(arg)
    }
}
fun label(){
    loop@ for (i in 1..100){
        println("外层：$i")
        for (i in 1..100){
            if (i == 20){
                println("内层：$i")
                break@loop
            }
        }
    }
}

/**
 * 主 & 次构造函数
 */
class Person{
    var lastName: String = ""
        get() = "名字：$lastName"
        set(value){
            field = "新名字：$value"
        }
    var age: Int? = null
        get() = age
        set(value){
            if (value != null) {
                field = if (value > 20){
                    value
                } else -1
            }
        }
    lateinit var sex: String
}

class NewPerson(name: String){
    var name: String? = name
        set(value){
            field =  "setter方法设置属性值name：$value"
        }
    var age: Int? = null

    init {
        println("初始化参数name：$name")
    }
    constructor(name: String, age: Int) : this(name) {
        this.age = age
        this.name = name
        println("次构造函数: name = $name; age: $age")
    }

    override fun toString(): String {
        return "name : $name; age : $age"
    }
}

/**
 * 1.可继承类
 * 2.抽象类
 * 3.实现类
 */
open class Zero{
    fun zero(from: String){
        println("Zero: 方法 from $from")
    }
}
abstract class One : Zero(){
    var one: String = "abstractOne"
    abstract fun abstractOne()
}
class Two : One() {
    override fun abstractOne() {
        zero("来自子类实现方法")
        println("抽象子类abstractOne执行")
    }
}

/**
 * 嵌套类
 */
class Outer(type: String){
    var outerType: String = ""
    init {
        outerType = type
    }
    fun printOuterFun(){
        println("外部类输出：$outerType")
    }
    class Inside(type: String){
        var insideType: String = ""
        init {
            insideType = type
        }
        fun printInsideFun(){
            println("嵌套类输出：$insideType")
        }
    }
}

/**
 * 内部类
 */
class OuterClass(type:String){
    var outerDes: String = "外部类"
    init {
        outerDes = type
    }
    fun outerFun(){
        println("$outerDes：方法outerFun调用 $outerDes")
    }
    inner class Inner(type: String){
        var innerDes: String = "内部类"
        init {
            innerDes = "$outerDes -> $type"
        }
        fun innerFun(){
            outerFun()
            println("$innerDes：方法innerFun调用")
        }
    }
}


/**
 * 匿名内部类
 */
interface TestInterface{
    fun testInterface()
}
class Test{
    fun test(testInterface: TestInterface){
        testInterface.testInterface()
    }
}

/**
 * setter中设置属性值需要注意的地方
 */
class SetterForField{
    var name: String = ""
        set(name){
            println("1----- $name")
            // 这里不能使用 name = "" 这种方式,因为他编译之后等同于setter方法，这样就会陷入死循环,直至内存溢出
            // this.name = "你好"
            field = name
        }
}

/**
 * 1.继承中的构造函数调用
 * 2.重写
 */
open class Teacher(name: String){
    constructor(name: String, age: Int):this(name){
        println("************** 基类次构造函数 ****************")
        println("基类姓名：$name")
        println("基类姓名：$age")
    }
    open fun fly(){
        println("基类 fly 方法")
    }
    fun walk(){
        println("基类 walk 方法")
    }
}
interface TeacherInterface{
    fun read(book: String){
        println("老师接口阅读：$book")
    }
    fun write(assignment: String){
        println("老师接口写作业：$assignment")
    }
    fun money()
}
class Student: Teacher, TeacherInterface{
    constructor(name: String, age: Int, sex: String, number: String):super(name, age){
        println("************* 继承类次级构造函数 **************")
        println("姓名：$name")
        println("年龄：$age")
        println("编号：${number.toInt()}")
        println("性别：$sex")
    }

    override fun fly() {
        super.fly()
        println("继承类 重新 父类,fly方法")
    }

    override fun read(book: String) {
        super.read(book)
        println("学生阅读")
    }

    override fun write(assignment: String) {
        super.write(assignment)
        println("学生写作")
    }

    override fun money() {
        println("学生必须要实现的方法交学费")
    }
}

/**
 * 1.扩展类函数
 * 2.扩展函数的函数
 * 3.扩展函数静态调用：由调用函数的 对象表达式 决定,我不是动态的类型决定
 * 4.伴生对象扩展
 * 5.一个类内部为另一个类声明扩展
 * 6.伴生对象扩展范围
 */
class User(var name: String)

fun User.buy(goods: String){
    // 1 扩展类函数
    println("扩展User的购买buy方法：${name}购买物品$goods")
}

fun MutableList<Int>.swap(index1: Int, index2: Int){
    // 2 扩展函数的函数
    var tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

// 3
open class C
class D: C()
fun C.foo():String{
    return "C"
}
fun D.foo():String{
    return "D"
}
fun printFoo(c: C){
    // 这里表达式是什么，就调用哪个的方法。而不是传递的对象（这里传入C的子类D，但是执行的C中得扩展方法）
    println("这里表达式是什么，就调用哪个的方法。而不是传递的对象（这里传入C的子类D，但是执行的C中得扩展方法）; 执行结果：${c.foo()}")
}

// 4 伴生对象扩展
class MyClass(){
    companion object{}
}
fun MyClass.Companion.foo(){
    println("伴生对象的扩展函数：伴生属性 = $no")
}
val MyClass.Companion.no: Int
    get() = 10
// 5 在一个类内部为另外一个类扩展
class X{
    fun bar(){
        println("X bar")
    }
}
class Y{
    private fun baz(){
        println("Y baz")
    }
    private fun X.foo(){
        println("Y中扩展X类方法")
        bar()
        baz()
    }
    fun caller(x: X){
        x.foo()
    }
}
// 6 伴随对象扩展使用范围
class DoWithClass{
    companion object{
        var oneField: Int = 1
        var twoField = "伴随对象第二个属性"
        fun companionFun1(){
            println("伴随对象：第一个伴随方法")
        }
    }
    fun DoWithClass.Companion.one(){
        println("伴随对象的扩展函数（类内部），如果类外部存在一样的扩展方法，优先使用内部扩展方法")
    }
    fun testOne(){
        DoWithClass.one()
    }
}
fun DoWithClass.Companion.one(){
    println("类外伴随对象扩展方法")
    DoWithClass.companionFun1()
}

/**
 * 数据类
 */
data class DataUser(var name: String, var age: Int)
fun doDataUser(name: String, age: Int){
    var jack = DataUser("jack", 18)
    // 使用 copy 方复制数据类，并修改age属性
    var oldJack = jack.copy(name = name, age = age)
    println(jack)
    println(oldJack)
    // 对数据类解构声明中使用
    var jane = DataUser("jane", 28)
    var (name, age) = jane
    println("$name 今年 $age 岁！")
}
// 密封类：受限制的《类继承结构》
