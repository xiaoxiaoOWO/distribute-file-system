package api;


/**
* api/NameNodeHelper.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��14�� ���ڶ� ����01ʱ37��36�� CST
*/

abstract public class NameNodeHelper
{
  private static String  _id = "IDL:api/NameNode:1.0";

  public static void insert (org.omg.CORBA.Any a, api.NameNode that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static api.NameNode extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (api.NameNodeHelper.id (), "NameNode");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static api.NameNode read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_NameNodeStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, api.NameNode value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static api.NameNode narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof api.NameNode)
      return (api.NameNode)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      api._NameNodeStub stub = new api._NameNodeStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static api.NameNode unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof api.NameNode)
      return (api.NameNode)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      api._NameNodeStub stub = new api._NameNodeStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
