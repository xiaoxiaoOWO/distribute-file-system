package api;

/**
* api/NameNodeHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��14�� ���ڶ� ����01ʱ37��36�� CST
*/

public final class NameNodeHolder implements org.omg.CORBA.portable.Streamable
{
  public api.NameNode value = null;

  public NameNodeHolder ()
  {
  }

  public NameNodeHolder (api.NameNode initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.NameNodeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.NameNodeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.NameNodeHelper.type ();
  }

}
