// IMyComplexDataAidlInterface.aidl
package com.zsf.m_ipc;

// Declare any non-default types here with import statements
// 跨进程复杂数据传输
// 导入要传输的复杂数据载体
import com.zsf.m_ipc.data.Result;

interface IMyComplexDataAidlInterface {

    int add(int a, int b);

    // 返回复杂数据对象
    Result getResult(long a, long b);

    // 返回复杂数据集合
    List<Result> getListResult(long a, long b);

    // Map 里不支持泛型,aidl编译会提示编译失败
    // Map<Integer, Result>
    // 使用Map时, key或者value不能是非基础类型数据
    Map getMapResult(inout Map datas);

    // 将非基础类型数据作为入参,一定要加上定向 tag
    // 传参时除了Java基本类型以及String，CharSequence之外的类型
    // 都需要在前面加上定向tag，具体加什么量需而定
    Result putResult(inout Result result);

}
