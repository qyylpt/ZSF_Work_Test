package com.zsf.test.branch.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.zsf.test.R
import kotlinx.android.synthetic.main.activity_kotlin.*

@Route(path = "/branch/kotlin/KotlinActivity")
class KotlinActivity : AppCompatActivity() , View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        m_test_textView_kotlin_result.text = getDefaultString()
        init()
    }

    private fun init() {
        m_test_kotlin_button_sum_two.setOnClickListener(this)
        m_test_kotlin_button_sum_more.setOnClickListener(this)
        m_test_kotlin_button_label.setOnClickListener(this)
        m_test_kotlin_button_constructor.setOnClickListener(this)
        m_test_kotlin_button_abstract.setOnClickListener(this)
        m_test_kotlin_button_nest_class.setOnClickListener(this)
        m_test_kotlin_button_inner.setOnClickListener(this)
        m_test_kotlin_button_NoNameInner_class.setOnClickListener(this)
        m_test_kotlin_button_setter.setOnClickListener(this)
        m_test_kotlin_button_extend.setOnClickListener(this)
        m_test_kotlin_button_decorate.setOnClickListener(this)
        m_test_kotlin_button_data.setOnClickListener(this)
        var args : Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        var argsTwo : IntArray = IntArray(20, {i -> (i + 1)})
        range(argsTwo)
    }

    override fun onClick(view: View?) {
        var id : Int = view!!.id
        when(id){
            R.id.m_test_kotlin_button_sum_two ->
                m_test_textView_kotlin_result.text = sum(100, 200).toString()
            R.id.m_test_kotlin_button_sum_more ->
                m_test_textView_kotlin_result.text = sums(100, 200, 300).toString()
            R.id.m_test_kotlin_button_label ->{
                label()
            }
            R.id.m_test_kotlin_button_constructor -> {
                var newPersonOne: NewPerson = NewPerson("我是主构造函数")
                var newPersonTwo: NewPerson = NewPerson("我是次构造函数", 18)
                newPersonOne.name = "主构造函数对象设置名称"
                newPersonOne.age = 28
                println(newPersonOne.toString())

                newPersonTwo.name = "次构造函数对象设置对象"
                newPersonTwo.age = 38
                println(newPersonTwo.toString())
            }
            R.id.m_test_kotlin_button_abstract -> {
                var zero: Zero = Zero()
                var two: Two = Two()
                zero.zero("来自Zero对象")
                two.abstractOne()
                two.zero("来自子类直接调用父类方法")
            }
            R.id.m_test_kotlin_button_nest_class -> {
                var outer: Outer = Outer("外部")
                var inside: Outer.Inside = Outer.Inside("内部")
                outer.printOuterFun()
                inside.printInsideFun()
            }
            R.id.m_test_kotlin_button_inner -> {
                var outerClass: OuterClass = OuterClass("外部调用")
                outerClass.outerFun()
                var inner: OuterClass.Inner = OuterClass("内部类持有").Inner("内部调用")
                inner.innerFun()
            }
            R.id.m_test_kotlin_button_NoNameInner_class -> {
                var test: Test = Test()
                test.test(testInterface = object : TestInterface {
                    override fun testInterface() {
                        println("我是匿名内部类")
                    }
                })
            }
            R.id.m_test_kotlin_button_setter -> {
                var setterForField: SetterForField = SetterForField()
                setterForField.name = "Setter 方法中设置值要使用field,因为默认 xx= 这样的设置属性值,默认会编译成setter方法,这样setter方法就会陷入死循环,直至内存溢出"
            }
            R.id.m_test_kotlin_button_extend -> {
                var student: Student = Student("张三", 18, "男", "007")
                student.fly()
                student.read("小碗熊")
                student.write("娃哈哈")
                student.money()
            }
            R.id.m_test_kotlin_button_decorate -> {
                // 扩展类函数
                var user: User = User("大佬")
                user.buy("辣条")

                // 扩展函数的函数
                var list = mutableListOf(1, 2, 3)
                println("原始数据：${list.toString()}")
                list.swap(0, 2)
                println("函数扩展交换位置函数结果：${list.toString()}")

                // 类的扩展方法调用：具体被调用的是哪一个函数，由调用函数的对象表达式决定，而不是动态的类型决定（比如：虽然传入的C的子类D，但是调用C中的方法就是C的函数，不会调用D中函数）
                printFoo(D())

                // 伴生对象
                MyClass.foo()

                // 一个类中扩展另外一个类
                var x: X = X()
                var y: Y = Y()
                y.caller(x)

                var doWithClass: DoWithClass = DoWithClass()
                doWithClass.testOne()
                DoWithClass.one()
            }
            R.id.m_test_kotlin_button_data -> {
                doDataUser(name = "oldJack", age = 81)
                var triple = Triple(1, 2, 3)
                println("triple: $triple")
                println("triple.roList : ${triple.toList()}")
                var pair = Pair("one", "two")
                println("pair : $pair ")
                println("pair.to : ${pair to "1000"}")
                println("pair.toList : ${pair.toList()}")
            }
            else ->
                m_test_textView_kotlin_result.text = "這是else"
        }
    }
}




