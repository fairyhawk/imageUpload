package utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 用于从指定的类中加载方法与类型的操作
 * 
 * @author baisheng
 * 
 */
@SuppressWarnings("unchecked")
public class LoadMethod
{
//	public static void main(String[] args)
//	{
//		LoadMethod load = new LoadMethod();
//	}


	public Object load(String className, String methodName, String[] type, String[] param)
	{
		Object retObj = null;

		try
		{
			// 加载指定的 Java 类
			Class clazz = Class.forName(className);

			// 获取指定对象的实例
			Constructor ct = clazz.getConstructor();
			Object obj = ct.newInstance();

			// 构建方法参数的数据类型
			Class paramTypes[] = this.getMethodClass(type);

			// 在指定类中获取指定的方法
			Method method = clazz.getMethod(methodName, paramTypes);

			// 构建方法的参数值
			Object args[] = this.getMethodObject(type, param);

			// 调用指定的方法并获取返回值为 Object 类型
			retObj = method.invoke(obj, args);
		}
		catch (Throwable e)
		{
			System.err.print(e);
		}

		return retObj;
	}

	/**
	 * 获取参数的方法
	 * 
	 * @param param 参数
	 * @return obj
	 */
	public Object[] getMethodObject(String[] type, String[] param)
	{
		Object[] objs = new Object[param.length];

		for (int i = 0; i < objs.length; i++)
		{
			if (!param[i].trim().equals("int") || type[i].equals("Integer"))
			{
				objs[i] = new Integer(param[i]);
			}
			else if (type[i].equals("float") || type[i].equals("Float"))
			{
				objs[i] = new Float(param[i]);
			}
			else if (type[i].equals("double") || type[i].equals("Double"))
			{
				objs[i] = new Double(param[i]);
			}
			else if (type[i].equals("boolean") || type[i].equals("Double"))
			{
				objs[i] = Boolean.valueOf(param[i]);
			}
			else
			{
				objs[i] = param[i];
			}
		}
		return objs;
	}

	/**
	 * 获取参数类型
	 * 
	 * @param type
	 * @return class
	 */
	public Class[] getMethodClass(String[] type)
	{
		Class[] clazz = new Class[type.length];

		for (int i = 0; i < clazz.length; i++)
		{
			if (!type[i].trim().equals("") || type[i] != null)
			{
				if (type[i].equals("int") || type[i].equals("Integer"))
				{
					clazz[i] = Integer.TYPE;
				}
				else if (type[i].equals("float") || type[i].equals("Float"))
				{
					clazz[i] = Float.TYPE;
				}
				else if (type[i].equals("double") || type[i].equals("Double"))
				{
					clazz[i] = Double.TYPE;
				}
				else if (type[i].equals("boolean") || type[i].equals("Boolean"))
				{
					clazz[i] = Boolean.TYPE;
				}
				else
				{
					clazz[i] = String.class;
				}
			}
		}
		return clazz;
	}
}
