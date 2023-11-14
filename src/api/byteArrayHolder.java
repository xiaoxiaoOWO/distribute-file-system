package api;


/**
* api/byteArrayHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��14�� ���ڶ� ����01ʱ37��36�� CST
*/

public final class byteArrayHolder implements org.omg.CORBA.portable.Streamable
{
  public byte value[] = null;

  public byteArrayHolder ()
  {
  }

  public byteArrayHolder (byte[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.byteArrayHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.byteArrayHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.byteArrayHelper.type ();
  }

}
